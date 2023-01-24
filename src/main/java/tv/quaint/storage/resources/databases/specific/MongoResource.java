package tv.quaint.storage.resources.databases.specific;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tv.quaint.storage.resources.databases.DatabaseResource;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class MongoResource extends DatabaseResource<MongoClient> {
    public MongoResource(DatabaseConfig config) {
        super(MongoClient.class, config);
    }

    public MongoClientURI getMongoClientURI() {
        return new MongoClientURI(getConfig().getLink());
    }

    public MongoClient connect() {
        return new MongoClient(getMongoClientURI());
    }

    @Override
    public void create(String table, ConcurrentSkipListSet<DatabaseValue<?>> values) {
        ConcurrentSkipListMap<String, Object> map = new ConcurrentSkipListMap<>();
        for (DatabaseValue<?> value : values) {
            map.put(value.getKey(), value.getValue());
        }
        getDatabase().getCollection(table).insertOne(new Document(map));
    }

    @Override
    public <O> O get(String table, String keyKey, String key, Class<O> def) {
        Document document = getDatabase().getCollection(table).find(new Document(keyKey, key)).first();
        if (document != null) {
            return document.get(keyKey, def);
        } else {
            return null;
        }
    }

    @Override
    public void onReload() {

    }

    @Override
    public <O> O getOrSetDefault(String table, String keyKey, String key, O value) {
        O o = get(table, keyKey, key, (Class<O>) value.getClass());
        if (o == null) {
            updateSingle(table, keyKey, key, value);
            return value;
        } else {
            return o;
        }
    }

    @Override
    public void delete(String table, String keyKey, String key) {
        getDatabase().getCollection(table).deleteOne(new Document(keyKey, key));
    }

    @Override
    public void delete(String table) {
        getDatabase().getCollection(table).drop();
    }

    @Override
    public boolean exists(String table, String keyKey, String key) {
        return getDatabase().getCollection(table).find(new Document(keyKey, key)).first() != null;
    }

    @Override
    public boolean exists(String table) {
        return getDatabase().getCollection(table).count() > 0;
    }

    @Override
    public <V> void updateSingle(String table, String keyKey, String key, V value) {
        getDatabase().getCollection(table).updateOne(new Document(keyKey, key), new Document("$set", new Document(keyKey, value)));
    }

    @Override
    public <V> void updateMultiple(String table, String keyKey, String key, ConcurrentSkipListMap<String, V> values) {
        values.forEach((k, v) -> updateSingle(table, keyKey, key, v));
    }

    public MongoDatabase getDatabase() {
        return getCachedConnection().getDatabase(getMongoClientURI().getDatabase());
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }
}
