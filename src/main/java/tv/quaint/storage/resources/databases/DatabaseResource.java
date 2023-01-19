package tv.quaint.storage.resources.databases;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.resources.StorageResource;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;
import tv.quaint.utils.MathUtils;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public abstract class DatabaseResource<C> extends StorageResource<C> {
    @Getter @Setter
    private C cachedConnection;
    @Getter @Setter
    private Date lastConnectionCreation;

    public DatabaseResource(Class<C> resourceType, String discriminatorKey, Object discriminator) {
        super(resourceType, discriminatorKey, discriminator);
    }

    public abstract DatabaseConfig getConfig();

    protected abstract C connect();

    private C getConnection() {
        if (lastConnectionCreation == null || cachedConnection == null || MathUtils.isDateOlderThan(lastConnectionCreation, 5, ChronoUnit.MINUTES)) {
            this.cachedConnection = connect();
            this.lastConnectionCreation = new Date();
        }
        return cachedConnection;
    }

    public abstract void create();

    public void ensure() {
        if (! exists()) {
            create();
        }
    }
}
