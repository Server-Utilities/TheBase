package tv.quaint.storage.resources.databases.singled;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.resources.databases.DatabaseResource;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;

public class DatabaseSingle<C> {
    @Getter @Setter
    DatabaseResource<C> database;
    @Getter @Setter
    String table;
    @Getter @Setter
    String discriminatorKey;
    @Getter @Setter
    String discriminator;

    public DatabaseSingle(DatabaseResource<C> database, String table, String discriminatorKey, String discriminator) {
        this.database = database;
        this.table = table;
        this.discriminatorKey = discriminatorKey;
        this.discriminator = discriminator;
    }

    public <V> void update(DatabaseValue<V> value) {
        database.updateSingle(table, discriminatorKey, discriminator, value);
    }

    public <V> void update(String key, V value) {
        database.updateSingle(table, discriminatorKey, discriminator, new DatabaseValue<>(key, value));
    }

    public void delete() {
        database.delete(table, discriminatorKey, discriminator);
    }

    public boolean exists() {
        return database.exists(table, discriminatorKey, discriminator);
    }

    public <V> V get(Class<V> def) {
        return database.get(table, discriminatorKey, discriminator, def);
    }

    public <V> V getOrSetDefault(V def) {
        return database.getOrSetDefault(table, discriminatorKey, discriminator, def);
    }
}
