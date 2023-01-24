package tv.quaint.storage.resources.cache;

import tv.quaint.storage.resources.databases.DatabaseResource;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;
import tv.quaint.storage.resources.databases.singled.DatabaseSingle;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

public class CachedResourceUtils {
    public static <T> void pushToDatabase(String table, CachedResource<T> resource, DatabaseResource<?> database) {
        ConcurrentSkipListSet<DatabaseValue<?>> values = new ConcurrentSkipListSet<>();
        for (Map.Entry<String, Object> entry : resource.getCachedData().entrySet()) {
            values.add(new DatabaseValue<>(entry.getKey(), entry.getValue()));
        }

        database.create(table, values);
    }

    public static <C, R extends DatabaseResource<C>> DatabaseSingle<C, R> pushToDatabase(String table, String discriminatorKey, String discriminator, CachedResource<?> resource, R database) {
        ConcurrentSkipListSet<DatabaseValue<?>> values = new ConcurrentSkipListSet<>();
        for (Map.Entry<String, Object> entry : resource.getCachedData().entrySet()) {
            values.add(new DatabaseValue<>(entry.getKey(), entry.getValue()));
        }

        database.create(table, values);
        return new DatabaseSingle<>(database, table, discriminatorKey, discriminator);
    }
}
