package tv.quaint.storage.resources.databases;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import tv.quaint.objects.Classifiable;
import tv.quaint.storage.resources.StorageResource;
import tv.quaint.storage.resources.databases.configurations.DatabaseConfig;
import tv.quaint.storage.resources.databases.processing.DatabaseValue;
import tv.quaint.utils.MathUtils;

import java.io.InputStream;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public abstract class DatabaseResource<C> implements Comparable<DatabaseResource<?>>, Classifiable<C> {
    @Getter
    private final Date initializeDate;
    @Getter @Setter
    private Class<C> resourceType;
    @Getter @Setter
    private DatabaseConfig config;
    @Getter @Setter
    private C cachedConnection;
    @Getter @Setter
    private Date lastConnectionCreation;
    @Getter @Setter
    private int hangingMillis;
    @Getter @Setter
    private Date lastReload;

    public DatabaseResource(Class<C> resourceType, DatabaseConfig config) {
        this.initializeDate = new Date();
        this.resourceType = resourceType;
        this.config = config;
    }

    protected abstract C connect();

    private C getConnection() {
        if (lastConnectionCreation == null || cachedConnection == null || MathUtils.isDateOlderThan(lastConnectionCreation, 5, ChronoUnit.MINUTES)) {
            this.cachedConnection = connect();
            this.lastConnectionCreation = new Date();
        }
        return cachedConnection;
    }

    public abstract void create(String table, ConcurrentSkipListSet<DatabaseValue<?>> values);

    public void ensure(String table, String keyKey, String key, Object value) {
        if (! has(table, keyKey, key, value)) {
            updateSingle(table, keyKey, key, value);
        }
    }

    public void reloadResource() {
        reloadResource(false);
    }

    public abstract <O> O get(String table, String keyKey, String key, Class<O> def);

    public void reloadResource(boolean force) {
        if (! force) {
            if (this.lastReload != null) {
                if (! MathUtils.isDateOlderThan(this.lastReload, this.hangingMillis, ChronoUnit.MILLIS)) {
                    return;
                }
            }
        }

        this.onReload();
        this.lastReload = new Date();
    }

    public abstract void onReload();

    public abstract <O> O getOrSetDefault(String table, String keyKey, String key, O value);

    public abstract void delete(String table, String keyKey, String key);

    public abstract void delete(String table);

    public abstract boolean exists(String table, String keyKey, String key);

    public abstract boolean exists(String table);

    public <O> boolean has(String table, String keyKey, String key, O value) {
        if (! exists(table, keyKey, key)) {
            return false;
        }
        Class<O> type = (Class<O>) value.getClass();
        O val = get(table, keyKey, key, type);
        return val.equals(value);
    }

    public abstract <V> void updateSingle(String table, String keyKey, String key, V value);

    public abstract <V> void updateMultiple(String table, String keyKey, String key, ConcurrentSkipListMap<String, V> values);

    public InputStream getResourceAsStream(String filename) {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }

    @Override
    public int compareTo(@NotNull DatabaseResource<?> other) {
        return Long.compare(getInitializeDate().getTime(), other.getInitializeDate().getTime());
    }

    @Override
    public Class<C> getClassifier() {
        return getResourceType();
    }

    public static Map<String, DatabaseValue<?>> mapOf(Map<String, ?> values) {
        Map<String, DatabaseValue<?>> map = new ConcurrentSkipListMap<>();
        for (DatabaseValue<?> value : collectionOf(values)) {
            map.put(value.getKey(), value);
        }
        return map;
    }

    public static Collection<DatabaseValue<?>> collectionOf(Map<String, ?> values) {
        Collection<DatabaseValue<?>> col = new ConcurrentSkipListSet<>();
        for (Map.Entry<String, ?> entry : values.entrySet()) {
            col.add(DatabaseValue.of(entry.getKey(), entry.getValue()));
        }
        return col;
    }
}
