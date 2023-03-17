package tv.quaint.storage.resources.databases.specific;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import tv.quaint.storage.resources.cache.CachedResource;
import tv.quaint.storage.resources.databases.DatabaseResource;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;
import tv.quaint.storage.resources.databases.events.MongoResourceStatementEvent;
import tv.quaint.storage.resources.databases.events.MongoStatementEvent;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;
import tv.quaint.utils.MathUtils;

import java.sql.Connection;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class MongoResource extends DatabaseResource<MongoClient> {
    public MongoResource(DatabaseConfig config) {
        super(MongoClient.class, config);
    }

    public MongoClientURI getMongoClientURI() {
        return new MongoClientURI(getConfig().getLink());
    }

    @Override
    protected MongoClient connect() {
        return new MongoClient(getMongoClientURI());
    }

    @Override
    public boolean testConnection() {
        return false;
    }

    @Override
    public ConcurrentSkipListSet<CachedResource<MongoClient>> listTable(String table) {
        ConcurrentSkipListSet<CachedResource<MongoClient>> cachedResources = new ConcurrentSkipListSet<>();
        String database = table;
        if (getMongoClientURI().getDatabase() != null) database = getMongoClientURI().getDatabase();
        if (! databaseExists(database)) {
            try {
                throw new RuntimeException("Database '" + database + "' does not exist! You need to specify a database in the Mongo Connection URI.");
            } catch (Exception e) {
                e.printStackTrace();
                return cachedResources;
            }
        }
        if (! exists(table)) {
            try {
                throw new RuntimeException("Collection '" + table + "' does not exist! You need to create it first.");
            } catch (Exception e) {
                e.printStackTrace();
                return cachedResources;
            }
        }
        for (Document document : getCollection(table).find()) {
            final CachedResource<MongoClient>[] cachedResource = new CachedResource[]{null};

            document.forEach((key, value) -> {
                if (cachedResource[0] == null) {
                    cachedResource[0] = new CachedResource<>(MongoClient.class, key, value);
                } else {
                    cachedResource[0].write(key, value);
                }
            });

            if (cachedResource[0] != null)
                cachedResources.add(cachedResource[0]);
        }
        setCompleted();
        return cachedResources;
    }

    @Override
    public void create(String table, String primaryKey, ConcurrentSkipListSet<DatabaseValue<?>> values) {
        if (exists(table)) return;
        String database = table;
        if (getMongoClientURI().getDatabase() != null) database = getMongoClientURI().getDatabase();
        // Below code might not need implementation.
//        if (! databaseExists(database)) {
//            try {
//                throw new RuntimeException("Database '" + database + "' does not exist! You need to specify a database in the Mongo Connection URI.");
//            } catch (Exception e) {
//                e.printStackTrace();
//                return;
//            }
//        }
        new MongoResourceStatementEvent(this, MongoStatementEvent.createCollection(table)).fire();
        getDatabase(database).createCollection(table);
        setCompleted();
    }

    public boolean databaseExists(String database) {
        boolean exists = databaseNames().contains(database);
        setCompleted();
        return exists;
    }

    public ConcurrentSkipListSet<String> databaseNames() {
        new MongoResourceStatementEvent(this, MongoStatementEvent.getCollectionNames()).fire();
        ConcurrentSkipListSet<String> databaseNames = getConnection().listDatabaseNames().into(new ConcurrentSkipListSet<>());
        setCompleted();
        return databaseNames;
    }

    @Override
    public void insert(String table, ConcurrentSkipListSet<DatabaseValue<?>> values) {
        ConcurrentSkipListMap<String, Object> map = new ConcurrentSkipListMap<>();
        for (DatabaseValue<?> value : values) {
            DatabaseValue<?> databaseValue = fromCollectionOrArray(value.getKey(), value.getValue());
            map.put(databaseValue.getKey(), databaseValue.getValue());
        }
        Document document = new Document(map);
        getCollection(table).insertOne(document);
        new MongoResourceStatementEvent(this, MongoStatementEvent.insert(table,
                document.toJson())).fire();
        setCompleted();
    }

    @Override
    public <O> O get(String table, String discriminatorKey, String discriminator, String key, Class<O> def) {
        O o = null;
        Document document = getCollection(table).find(new Document(discriminatorKey, discriminator)).first();
        if (document != null) {
            Object obj = document.get(key);
            if (obj != null) {
                if (def.isArray()) {
                    o = (O) getArrayFromString((String) obj, def);
                } else if (def.isAssignableFrom(Collection.class)) {
                    o = (O) getCollectionFromString((String) obj, def);
                } else {
                    o = (O) obj;
                }
            }
        }
        setCompleted();
        return o;
    }

    @Override
    public void onReload() {
        setCachedConnection(connect());
        setLastReload(new Date());
    }

    @Override
    public <O> O getOrSetDefault(String table, String discriminatorKey, String discriminator, String key, O value) {
        O o = get(table, discriminatorKey, discriminator, key, (Class<O>) value.getClass());
        if (o == null) {
            updateSingle(table, discriminatorKey, discriminator, key, value);
            return value;
        } else {
            return o;
        }
    }

    @Override
    public void delete(String table, String discriminatorKey, String discriminator) {
        DeleteResult result = getCollection(table).deleteOne(new Document(discriminatorKey, discriminator));
        new MongoResourceStatementEvent(this,
                MongoStatementEvent.remove(table, getDocument(table, discriminatorKey, discriminator).toJson())).fire();
        setCompleted();
    }

    public Document getDocument(String table, String discriminatorKey, String discriminator) {
        Document document = getCollection(table).find(new Document(discriminatorKey, discriminator)).first();
        setCompleted();
        return document;
    }

    @Override
    public void delete(String table) {
        getCollection(table).drop();
        new MongoResourceStatementEvent(this, MongoStatementEvent.dropCollection(table)).fire();
        setCompleted();
    }

    @Override
    public boolean exists(String table, String discriminatorKey, String discriminator) {
        boolean exists = getCollection(table).find(new Document(discriminatorKey, discriminator)).first() != null;
        setCompleted();
        return exists;
    }

    @Override
    public boolean exists(String table) {
        String database = table;
        if (getMongoClientURI().getDatabase() != null) database = getMongoClientURI().getDatabase();
        return getCollections(database).contains(table);
    }

    public ConcurrentSkipListSet<String> getCollections(String database) {
        ConcurrentSkipListSet<String> dbs = getDatabase(database).listCollectionNames().into(new ConcurrentSkipListSet<>());
        setCompleted();
        return dbs;
    }

    @Override
    public <V> void updateSingle(String table, String discriminatorKey, String discriminator, String key, V value) {
        DatabaseValue<?> databaseValue = fromCollectionOrArray(key, value);
        Document document = new Document(key, databaseValue.getValue());
        getCollection(table).updateOne(new Document(discriminatorKey, discriminator),
                new Document("$set", document));
        new MongoResourceStatementEvent(this, MongoStatementEvent.update(table, document.toJson())).fire();
        setCompleted();
    }

    @Override
    public <V> void updateMultiple(String table, String discriminatorKey, String discriminator, ConcurrentSkipListMap<String, V> values) {
        Document document = new Document();
        for (String key : values.keySet()) {
            DatabaseValue<?> databaseValue = fromCollectionOrArray(key, values.get(key));
            document.append(key, databaseValue.getValue());
        }
        getCollection(table).updateOne(new Document(discriminatorKey, discriminator),
                new Document("$set", document));
        new MongoResourceStatementEvent(this, MongoStatementEvent.update(table, document.toJson())).fire();
        setCompleted();
    }

    public MongoDatabase getDatabase(String database) {
        new MongoResourceStatementEvent(this, MongoStatementEvent.createDatabase(database)).fire();
        return getConnection().getDatabase(database);
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        String database = collectionName;
        if (getMongoClientURI().getDatabase() != null) database = getMongoClientURI().getDatabase();
        new MongoResourceStatementEvent(this, MongoStatementEvent.getCollection(collectionName)).fire();
        return getDatabase(database).getCollection(collectionName);
    }
}
