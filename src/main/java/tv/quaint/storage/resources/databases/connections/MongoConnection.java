package tv.quaint.storage.resources.databases.connections;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;
import tv.quaint.storage.resources.databases.differentiating.MongoSpecific;
import tv.quaint.storage.resources.databases.processing.interfacing.DBDataLike;
import tv.quaint.storage.resources.databases.processing.mongo.MongoSchematic;
import tv.quaint.storage.resources.databases.processing.mongo.data.MongoColumn;
import tv.quaint.storage.resources.databases.processing.mongo.data.MongoDataLike;
import tv.quaint.storage.resources.databases.processing.mongo.data.MongoRow;
import tv.quaint.storage.resources.databases.processing.mongo.data.defined.DefinedMongoData;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLDataLike;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MongoConnection implements MongoSpecific {
    @Getter @Setter
    private DatabaseConfig config;

    public MongoConnection(DatabaseConfig config) {
        this.config = config;
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return createConnection().getDatabase(config.getDatabase()).getCollection(collectionName);
    }

    @Override
    public void replace(MongoCollection<Document> collection, String discriminatorKey, String discriminator, Document replacement) {
        collection.replaceOne(new Document(discriminatorKey, discriminator), replacement);
    }

    @Override
    public void replace(String collectionName, String discriminatorKey, String discriminator, Document replacement) {
        replace(getCollection(collectionName), discriminatorKey, discriminator, replacement);
    }

    @Override
    public void delete(MongoCollection<Document> collection, String discriminatorKey, String discriminator) {
        collection.deleteOne(new Document(discriminatorKey, discriminator));
    }

    @Override
    public void delete(String collectionName, String discriminatorKey, String discriminator) {
        delete(getCollection(collectionName), discriminatorKey, discriminator);
    }

    @Override
    public boolean exists(MongoCollection<Document> collection, String discriminatorKey, String discriminator) {
        return collection.find(new Document(discriminatorKey, discriminator)).first() != null;
    }

    @Override
    public boolean exists(String collectionName, String discriminatorKey, String discriminator) {
        return exists(getCollection(collectionName), discriminatorKey, discriminator);
    }

    @Override
    public Document pull(MongoCollection<Document> collection, String discriminatorKey, String discriminator) {
        return collection.find(new Document(discriminatorKey, discriminator)).first();
    }

    @Override
    public Document pull(String collectionName, String discriminatorKey, String discriminator) {
        return pull(getCollection(collectionName), discriminatorKey, discriminator);
    }

    @Override
    public MongoRow getRow(MongoCollection<Document> collection, String discriminatorKey, String discriminator) {
        return getRow(pull(collection, discriminatorKey, discriminator));
    }

    @Override
    public MongoRow getRow(String collectionName, String discriminatorKey, String discriminator) {
        return getRow(getCollection(collectionName), discriminatorKey, discriminator);
    }

    @Override
    public MongoRow createRow(String table, String discriminatorKey, String discriminator, ConcurrentSkipListMap<String, MongoDataLike<?>> data) {
        Document document = new Document(discriminatorKey, discriminator);
        document.putAll(data);
        getCollection(table).insertOne(document);

        return getRow(table, discriminatorKey, discriminator);
    }

    @Override
    public MongoRow createRow(String table, String discriminatorKey, String discriminator, MongoRow row) {
        ConcurrentSkipListMap<String, MongoDataLike<?>> r = new ConcurrentSkipListMap<>();

        row.getMap().forEach((k, v) -> r.put(k.getName(), v));

        return createRow(table, discriminatorKey, discriminator, r);
    }

    @Override
    public MongoRow getRow(Document document) {
        ConcurrentSkipListMap<MongoColumn, MongoDataLike<?>> columns = new ConcurrentSkipListMap<>();
        document.forEach((key, value) -> {
            MongoSchematic.MongoType type = MongoSchematic.MongoType.fromObject(value);
            columns.put(new MongoColumn(key, type), DefinedMongoData.getFromType(type, value));
        });
        MongoColumn[] columnArray = new MongoColumn[columns.size()];
        MongoDataLike<?>[] dataArray = new MongoDataLike<?>[columns.size()];
        AtomicInteger atomicInteger = new AtomicInteger(0);
        columns.forEach((column, data) -> {
            int i = atomicInteger.get();
            columnArray[i] = column;
            dataArray[i] = data;
            atomicInteger.incrementAndGet();
        });

        return new MongoRow(columnArray, dataArray);
    }

    @Override
    public void createCollection(MongoSchematic schematic) {
        createConnection().getDatabase(config.getDatabase()).createCollection(schematic.getTableFullName());
    }

    @Override
    public MongoClient createConnection() {
        String uri = "mongodb://{{user}}:{{pass}}@{{host}}:{{port}}{{options}}";

        uri = uri.replace("{{user}}", config.getUsername());
        uri = uri.replace("{{pass}}", config.getPassword());
        uri = uri.replace("{{host}}", config.getHost());
        uri = uri.replace("{{port}}", String.valueOf(config.getPort()));
        uri = uri.replace("{{options}}", config.getOptions());
        uri = uri.replace("{{database}}", config.getDatabase());
        uri = uri.replace("{{table_prefix}}", config.getTablePrefix());

        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        return new MongoClient(mongoClientURI);
    }

    @Override
    public void replace(String table, String discriminatorKey, String discriminator, String key, MongoDataLike<?> to) {
        Document document = getRow(table, discriminatorKey, discriminator).asDocument();
        document.replace(key, to.getData());
        replace(table, discriminatorKey, discriminator, document);
    }

    @Override
    public MongoDataLike<?> get(String table, String discriminatorKey, String discriminator, String key) {
        try {
            return getRow(table, discriminatorKey, discriminator).getValue(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean exists(String table, String discriminatorKey, String discriminator, String key) {
        return getRow(table, discriminatorKey, discriminator).getColumn(key) != null;
    }
}
