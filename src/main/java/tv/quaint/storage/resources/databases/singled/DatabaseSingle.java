package tv.quaint.storage.resources.databases.singled;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import tv.quaint.storage.resources.cache.CachedResource;
import tv.quaint.storage.resources.databases.DatabaseResource;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;

import java.util.concurrent.ConcurrentSkipListSet;

public class DatabaseSingle<C, R extends DatabaseResource<C>> implements Comparable<DatabaseSingle<?, ?>> {
    @Getter @Setter
    R database;
    @Getter @Setter
    String table;
    @Getter @Setter
    String discriminatorKey;
    @Getter @Setter
    String discriminator;
    @Getter @Setter
    CachedResource<?> cachedResource;

    public DatabaseSingle(R database, String table, String discriminatorKey, String discriminator, CachedResource<?> cachedResource) {
        this.database = database;
        this.table = table;
        this.discriminatorKey = discriminatorKey;
        this.discriminator = discriminator;
        this.cachedResource = cachedResource;
    }

    public <V> void update(DatabaseValue<V> value) {
        database.updateSingle(table, discriminatorKey, discriminator, value.getKey(), value);
    }

    public <V> void update(String key, V value) {
        database.updateSingle(table, discriminatorKey, discriminator, key, new DatabaseValue<>(key, value));
    }

    public void delete() {
        database.delete(table, discriminatorKey, discriminator);
    }

    public boolean exists() {
        return database.exists(table, discriminatorKey, discriminator);
    }

    public <V> V get(String key, Class<V> def) {
        return database.get(table, discriminatorKey, discriminator, key, def);
    }

    public <V> V getOrSetDefault(String key, V def) {
        return database.getOrSetDefault(table, discriminatorKey, discriminator, key, def);
    }

    public void push() {
        ConcurrentSkipListSet<DatabaseValue<?>> values = new ConcurrentSkipListSet<>();

        cachedResource.getCachedData().forEach((key, value) -> values.add(new DatabaseValue<>(key, value)));

        if (! database.exists(table)) database.create(table, discriminatorKey, values);
        if (exists()) database.updateMultiple(table, discriminatorKey, discriminator, cachedResource.getCachedData());
        else {
            database.insert(table, values);
        }
    }

    public void get() {
        cachedResource.getCachedData().forEach((key, value) -> {
            Object o = database.get(table, discriminatorKey, discriminator, key, value.getClass());
            if (o != null) cachedResource.getCachedData().put(key, o);
        });
    }

    @Override
    public int compareTo(@NotNull DatabaseSingle<?, ?> o) {
        return CharSequence.compare(discriminator, o.discriminator);
    }
}
