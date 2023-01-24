package tv.quaint.storage.resources.cache;

import tv.quaint.storage.resources.databases.DatabaseResource;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;

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
}
