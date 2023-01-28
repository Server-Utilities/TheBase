package tv.quaint.storage.resources.databases.specific;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tv.quaint.storage.resources.databases.DatabaseResource;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;

import java.util.Collection;
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
    public void create(String table, String primaryKey, ConcurrentSkipListSet<DatabaseValue<?>> values) {
        getDatabase().createCollection(table);
    }

    @Override
    public void insert(String table, ConcurrentSkipListSet<DatabaseValue<?>> values) {
        ConcurrentSkipListMap<String, Object> map = new ConcurrentSkipListMap<>();
        for (DatabaseValue<?> value : values) {
            DatabaseValue<?> databaseValue = fromCollectionOrArray(value.getKey(), value.getValue());
            map.put(databaseValue.getKey(), databaseValue.getValue());
        }
        getDatabase().getCollection(table).insertOne(new Document(map));
    }

    @Override
    public <O> O get(String table, String discriminatorKey, String discriminator, String key, Class<O> def) {
        Document document = getDatabase().getCollection(table).find(new Document(discriminatorKey, discriminator)).first();
        if (document != null) {
            Object obj = document.get(key);
            if (def.isArray()) {
                return (O) getArrayFromString((String) obj, def);
            } else if (def.isAssignableFrom(Collection.class)) {
                return (O) getCollectionFromString((String) obj, def);
            } else {
                return (O) obj;
            }
        } else {
            return null;
        }
    }

    @Override
    public void onReload() {

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
        getDatabase().getCollection(table).deleteOne(new Document(discriminatorKey, discriminator));
    }

    @Override
    public void delete(String table) {
        getDatabase().getCollection(table).drop();
    }

    @Override
    public boolean exists(String table, String discriminatorKey, String discriminator) {
        return getDatabase().getCollection(table).find(new Document(discriminatorKey, discriminator)).first() != null;
    }

    @Override
    public boolean exists(String table) {
        return getDatabase().getCollection(table).count() > 0;
    }

    @Override
    public <V> void updateSingle(String table, String discriminatorKey, String discriminator, String key, V value) {
        DatabaseValue<?> databaseValue = fromCollectionOrArray(key, value);
        getDatabase().getCollection(table).updateOne(new Document(discriminatorKey, discriminator),
                new Document("$set", new Document(key, databaseValue.getValue())));
    }

    @Override
    public <V> void updateMultiple(String table, String discriminatorKey, String discriminator, ConcurrentSkipListMap<String, V> values) {
        values.forEach((k, v) -> updateSingle(table, discriminatorKey, discriminator, k, v));
    }

    public MongoDatabase getDatabase() {
        return getCachedConnection().getDatabase(getMongoClientURI().getDatabase());
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }
}
