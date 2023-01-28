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

    public static <C, R extends DatabaseResource<C>> DatabaseSingle<C, R> pushToDatabase(String table, String discriminatorKey, String discriminator, CachedResource<?> resource, R database) {
        switch (database.getConfig().getType()) {
            case MYSQL:
                return (DatabaseSingle<C, R>) new MySQLSingle((MySQLResource) database, table, discriminatorKey, discriminator);
            case MONGO:
                return (DatabaseSingle<C, R>) new MongoSingle((MongoResource) database, table, discriminatorKey, discriminator);
            case SQLITE:
                return (DatabaseSingle<C, R>) new SQLiteSingle((SQLiteResource) database, table, discriminatorKey, discriminator);
            default:
                return null;
        }
    }

    public static MongoSingle pushToDatabase(String table, String discriminatorKey, String discriminator, CachedResource<?> resource, MongoResource database) {
        ConcurrentSkipListSet<DatabaseValue<?>> values = new ConcurrentSkipListSet<>();
        for (Map.Entry<String, Object> entry : resource.getCachedData().entrySet()) {
            values.add(new DatabaseValue<>(entry.getKey(), entry.getValue()));
        }

        database.create(table, discriminatorKey, values);
        if (database.exists(table, discriminatorKey, discriminator)) {
            ConcurrentSkipListMap<String, Object> map = new ConcurrentSkipListMap<>();
            for (DatabaseValue<?> value : values) {
                map.put(value.getKey(), value.getValue());
            }

            database.updateMultiple(table, discriminatorKey, discriminator, map);
        } else {
            database.insert(table, values);
        }
        return new MongoSingle(database, table, discriminatorKey, discriminator);
    }

    public static MySQLSingle pushToDatabase(String table, String discriminatorKey, String discriminator, CachedResource<?> resource, MySQLResource database) {
        ConcurrentSkipListSet<DatabaseValue<?>> values = new ConcurrentSkipListSet<>();
        for (Map.Entry<String, Object> entry : resource.getCachedData().entrySet()) {
            values.add(new DatabaseValue<>(entry.getKey(), entry.getValue()));
        }

        database.create(table, discriminatorKey, values);
        if (database.exists(table, discriminatorKey, discriminator)) {
            ConcurrentSkipListMap<String, Object> map = new ConcurrentSkipListMap<>();
            for (DatabaseValue<?> value : values) {
                map.put(value.getKey(), value.getValue());
            }

            database.updateMultiple(table, discriminatorKey, discriminator, map);
        } else {
            database.insert(table, values);
        }
        return new MySQLSingle(database, table, discriminatorKey, discriminator);
    }

    public static SQLiteSingle pushToDatabase(String table, String discriminatorKey, String discriminator, CachedResource<?> resource, SQLiteResource database) {
        ConcurrentSkipListSet<DatabaseValue<?>> values = new ConcurrentSkipListSet<>();
        for (Map.Entry<String, Object> entry : resource.getCachedData().entrySet()) {
            values.add(new DatabaseValue<>(entry.getKey(), entry.getValue()));
        }

        database.create(table, discriminatorKey, values);
        if (database.exists(table, discriminatorKey, discriminator)) {
            ConcurrentSkipListMap<String, Object> map = new ConcurrentSkipListMap<>();
            for (DatabaseValue<?> value : values) {
                map.put(value.getKey(), value.getValue());
            }

            database.updateMultiple(table, discriminatorKey, discriminator, map);
        } else {
            database.insert(table, values);
        }
        return new SQLiteSingle(database, table, discriminatorKey, discriminator);
    }

    public static ConcurrentSkipListSet<DatabaseValue<?>> getValues(CachedResource<?> resource) {
        ConcurrentSkipListSet<DatabaseValue<?>> values = new ConcurrentSkipListSet<>();
        for (Map.Entry<String, Object> entry : resource.getCachedData().entrySet()) {
            values.add(new DatabaseValue<>(entry.getKey(), entry.getValue()));
        }
        return values;
    }

    public static <C, R extends DatabaseResource<C>> void updateCache(String table, String discriminatorKey, String discriminator, CachedResource<?> resource, R database) {
        switch (database.getConfig().getType()) {
            case MYSQL:
                MySQLSingle mySQLSingle = new MySQLSingle((MySQLResource) database, table, discriminatorKey, discriminator);
                resource.getCachedData().forEach((s, o) -> {
                    resource.write(s, mySQLSingle.get(s, o.getClass()));
                });
                break;
            case MONGO:
                MongoSingle mongoSingle = new MongoSingle((MongoResource) database, table, discriminatorKey, discriminator);
                resource.getCachedData().forEach((s, o) -> {
                    resource.write(s, mongoSingle.get(s, o.getClass()));
                });
                break;
            case SQLITE:
                SQLiteSingle sqLiteSingle = new SQLiteSingle((SQLiteResource) database, table, discriminatorKey, discriminator);
                resource.getCachedData().forEach((s, o) -> {
                    resource.write(s, sqLiteSingle.get(s, o.getClass()));
                });
                break;
        }
    }
}
