package tv.quaint.storage.resources.databases;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.resources.StorageResource;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;
import tv.quaint.storage.resources.databases.processing.AbstractDatabaseValue;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public abstract class DatabaseResource<P> extends StorageResource<P> {
    @Getter @Setter
    private String table;
    @Getter @Setter
    private DatabaseConfig config;

    public DatabaseResource(Class<P> resourceType, String discriminatorKey, String discriminator, String table, DatabaseConfig config) {
        super(resourceType, discriminatorKey, discriminator);
        this.table = table;
        this.config = config;
    }

    @Override
    public String getDiscriminator() {
        return (String) super.getDiscriminator();
    }

    public abstract P getProvider();

    public abstract void createTable();

    public abstract void ensureTableExists();

    public abstract void insert();

    public abstract void update();

    @Override
    public void push() {
        if (! this.exists()) {
            this.ensureTableExists();
            this.insert();
        } else {
            this.update();
        }
    }
}
