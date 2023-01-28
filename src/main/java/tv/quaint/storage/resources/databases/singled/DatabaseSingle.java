package tv.quaint.storage.resources.databases.singled;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import tv.quaint.storage.resources.databases.DatabaseResource;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;

public class DatabaseSingle<C, R extends DatabaseResource<C>> implements Comparable<DatabaseSingle<?, ?>> {
    @Getter @Setter
    R database;
    @Getter @Setter
    String table;
    @Getter @Setter
    String discriminatorKey;
    @Getter @Setter
    String discriminator;

    public DatabaseSingle(R database, String table, String discriminatorKey, String discriminator) {
        this.database = database;
        this.table = table;
        this.discriminatorKey = discriminatorKey;
        this.discriminator = discriminator;
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

    @Override
    public int compareTo(@NotNull DatabaseSingle<?, ?> o) {
        return CharSequence.compare(discriminator, o.discriminator);
    }
}
