package tv.quaint.storage.resources;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import tv.quaint.objects.Classifiable;
import tv.quaint.storage.StorageUtils;
import tv.quaint.utils.MathUtils;

import java.io.InputStream;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public abstract class StorageResource<T> implements Comparable<StorageResource<?>>, Classifiable<T> {
    @Getter
    private final Date initializeDate;
    @Getter @Setter
    private StorageUtils.SupportedStorageType type;
    @Getter @Setter
    private Class<T> resourceType;
    @Getter @Setter
    private String discriminatorKey;
    @Getter @Setter
    private Object discriminator;
    @Getter @Setter
    private int hangingMillis;
    @Getter @Setter
    private Date lastReload;
    @Getter @Setter
    private ConcurrentSkipListMap<String, Object> map;

    public StorageResource(Class<T> resourceType, String discriminatorKey, Object discriminator) {
        initializeDate = new Date();
        this.resourceType = resourceType;
        this.discriminatorKey = discriminatorKey;
        this.discriminator = discriminator;
        this.type = StorageUtils.getStorageType(resourceType);
        this.hangingMillis = 5000;
        this.map = new ConcurrentSkipListMap<>();
    }

    public void reloadResource() {
        reloadResource(false);
    }

    public abstract <O> O get(String key, Class<O> def);

    public void reloadResource(boolean force) {
        if (! force) {
            if (this.lastReload != null) {
                if (! MathUtils.isDateOlderThan(this.lastReload, this.hangingMillis, ChronoUnit.MILLIS)) {
                    return;
                }
            }
        }

        this.continueReloadResource();
        this.lastReload = new Date();
    }

    public abstract void continueReloadResource();

    public abstract <V> void write(String key, V value);

    public abstract <O> O getOrSetDefault(String key, O value);

    public void sync() {
        this.push();
        this.reloadResource();
    }

    public void sync(boolean force) {
        this.push();
        this.reloadResource(force);
    }

    public abstract void push();

    public abstract void delete();

    public abstract boolean exists();

    public ConcurrentSkipListSet<String> singleLayerKeySet() {
        return singleLayerKeySet("");
    }

    public ConcurrentSkipListSet<String> singleLayerKeySet(String section) {
        ConcurrentSkipListSet<String> r = new ConcurrentSkipListSet<>();

        this.map.keySet().forEach(a -> {
            if (a.startsWith(section)) {
                int start = a.substring(section.length()).lastIndexOf(".") + 1;
                String k = a.substring(start);
                int end = k.indexOf(".");
                if (end == -1) end = k.length();
                k = k.substring(0, end);
                r.add(k);
            }
        });

        return r;
    }

    public abstract <V> void updateSingle(String key, V value);

    public abstract <V> void updateMultiple(ConcurrentSkipListMap<String, V> values);

    public boolean isEmpty() {
        sync(true);

        return getMap().isEmpty();
    }

    public InputStream getResourceAsStream(String filename) {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }

    @Override
    public int compareTo(@NotNull StorageResource<?> other) {
        return Long.compare(getInitializeDate().getTime(), other.getInitializeDate().getTime());
    }

    @Override
    public Class<T> getClassifier() {
        return getResourceType();
    }
}
