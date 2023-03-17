package tv.quaint.storage.resources.cache;

import tv.quaint.storage.resources.databases.DatabaseResource;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;
import tv.quaint.storage.resources.databases.singled.*;
import tv.quaint.storage.resources.databases.specific.MongoResource;
import tv.quaint.storage.resources.databases.specific.MySQLResource;
import tv.quaint.storage.resources.databases.specific.SQLiteResource;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class CachedResourceUtils {
    public static <T> void pushToDatabase(String table, CachedResource<T> resource, DatabaseResource<?> database) {
        ConcurrentSkipListSet<DatabaseValue<?>> values = new ConcurrentSkipListSet<>();
        for (Map.Entry<String, Object> entry : resource.getCachedData().entrySet()) {
            values.add(new DatabaseValue<>(entry.getKey(), entry.getValue()));
        }

        database.create(table, resource.getDiscriminatorKey(), values);
        if (database.exists(table, resource.getDiscriminatorKey(), resource.getDiscriminatorAsString())) {
            ConcurrentSkipListMap<String, Object> map = new ConcurrentSkipListMap<>();
            for (DatabaseValue<?> value : values) {
                map.put(value.getKey(), value.getValue());
            }

            database.updateMultiple(table, resource.getDiscriminatorKey(), resource.getDiscriminatorAsString(), map);
        } else {
            database.insert(table, values);
        }
    }

    public static <C, R extends DatabaseResource<C>> void pushToDatabase(String table, String discriminatorKey, String discriminator, CachedResource<?> resource, R database) {
        DatabaseSingle<?, ?> single;
        switch (database.getConfig().getType()) {
            case MYSQL:
                single = new MySQLSingle((MySQLResource) database, table, discriminatorKey, discriminator, resource);
                break;
            case MONGO:
                single = new MongoSingle((MongoResource) database, table, discriminatorKey, discriminator, resource);
                break;
            case SQLITE:
                single = new SQLiteSingle((SQLiteResource) database, table, discriminatorKey, discriminator, resource);
                break;
            default:
                return;
        }

        single.push();
    }

    public static MongoSingle pushToDatabase(String table, String discriminatorKey, String discriminator, CachedResource<?> resource, MongoResource database) {
        MongoSingle single = new MongoSingle(database, table, discriminatorKey, discriminator, resource);

        single.push();

        return single;
    }

    public static MySQLSingle pushToDatabase(String table, String discriminatorKey, String discriminator, CachedResource<?> resource, MySQLResource database) {
        MySQLSingle single = new MySQLSingle(database, table, discriminatorKey, discriminator, resource);

        single.push();

        return single;
    }

    public static SQLiteSingle pushToDatabase(String table, String discriminatorKey, String discriminator, CachedResource<?> resource, SQLiteResource database) {
        SQLiteSingle single = new SQLiteSingle(database, table, discriminatorKey, discriminator, resource);

        single.push();

        return single;
    }

    public static ConcurrentSkipListSet<DatabaseValue<?>> getValues(CachedResource<?> resource) {
        ConcurrentSkipListSet<DatabaseValue<?>> values = new ConcurrentSkipListSet<>();
        for (Map.Entry<String, Object> entry : resource.getCachedData().entrySet()) {
            values.add(new DatabaseValue<>(entry.getKey(), entry.getValue()));
        }
        return values;
    }

    public static <C, R extends DatabaseResource<C>> void updateCache(String table, String discriminatorKey, String discriminator, CachedResource<?> resource, R database) {
        DatabaseSingle<?, ?> single;
        switch (database.getConfig().getType()) {
            case MYSQL:
                single = new MySQLSingle((MySQLResource) database, table, discriminatorKey, discriminator, resource);
                break;
            case MONGO:
                single = new MongoSingle((MongoResource) database, table, discriminatorKey, discriminator, resource);
                break;
            case SQLITE:
                single = new SQLiteSingle((SQLiteResource) database, table, discriminatorKey, discriminator, resource);
                break;
            default:
                return;
        }

        single.get();

        resource.getCachedData().forEach((s, o) -> {
            resource.write(s, single.getCachedResource().getCachedData().get(s));
        });
    }
}
