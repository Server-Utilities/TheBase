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
import java.lang.reflect.Array;
import java.time.temporal.ChronoUnit;
import java.util.*;
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

    public abstract void create(String table, String primaryKey, ConcurrentSkipListSet<DatabaseValue<?>> values);

    public abstract void insert(String table, ConcurrentSkipListSet<DatabaseValue<?>> values);

    public void ensure(String table, String discriminatorKey, String discriminator, String key, Object value) {
        if (! has(table, discriminatorKey, discriminator, key, value)) {
            updateSingle(table, discriminatorKey, discriminator, key, value);
        }
    }

    public void reloadResource() {
        reloadResource(false);
    }

    public abstract <O> O get(String table, String discriminatorKey, String discriminator, String key, Class<O> def);

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

    public abstract <O> O getOrSetDefault(String table, String discriminatorKey, String discriminator, String key, O value);

    public abstract void delete(String table, String discriminatorKey, String key);

    public abstract void delete(String table);

    public abstract boolean exists(String table, String discriminatorKey, String discriminator);

    public abstract boolean exists(String table);

    public <O> boolean has(String table, String discriminatorKey, String discriminator, String key, O value) {
        if (! exists(table, discriminatorKey, discriminator)) {
            return false;
        }
        Class<O> type = (Class<O>) value.getClass();
        O val = get(table, discriminatorKey, discriminator, key, type);
        return val.equals(value);
    }

    public abstract <V> void updateSingle(String table, String discriminatorKey, String discriminator, String key, V value);

    public abstract <V> void updateMultiple(String table, String discriminatorKey, String discriminator, ConcurrentSkipListMap<String, V> values);

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

    public static <V> DatabaseValue<?> fromCollectionOrArray(String key, V value) {
        DatabaseValue<?> databaseValue;
        if (value instanceof Collection) {
            Collection<?> collection = (Collection<?>) value;
            StringBuilder builder = new StringBuilder("{");

            int i = 0;
            for (Object o : collection) {
                builder.append(o.toString());
                if (i != collection.size() - 1) {
                    builder.append(",");
                }
                i++;
            }

            builder.append("}");

            databaseValue = new DatabaseValue<>(key, builder.toString());
        } else if (value.getClass().isArray()) {
            Object[] array = (Object[]) value;
            StringBuilder builder = new StringBuilder("[");

            int i = 0;
            for (Object o : array) {
                builder.append(o.toString());
                if (i != array.length - 1) {
                    builder.append(",");
                }
                i++;
            }

            builder.append("]");

            databaseValue = new DatabaseValue<>(key, builder.toString());
        } else {
            databaseValue = new DatabaseValue<>(key, value);
        }
        return databaseValue;
    }

    public static <V> V toCollectionOrArrayFromString(String s) {
        String[] split = s.substring(1, s.length() - 1).split(",");
        try {
            if (s.startsWith("{")) {
                Collection<String> collection = new HashSet<>(Arrays.asList(split));
                return (V) collection;
            } else if (s.startsWith("[")) {
                String[] array = new String[split.length];
                for (int i = 0; i < split.length; i++) {
                    array[i] = split[i];
                }
                return (V) array;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <V> Collection<V> getCollectionFromString(String s, Class<V> type) {
        Collection<String> strings = toCollectionOrArrayFromString(s);

        Collection<V> collection = new HashSet<>();

        if (strings == null) {
            return collection;
        }

        for (String string : strings) {
            if (type == String.class) {
                collection.add((V) string);
            } else if (type == Integer.class) {
                collection.add((V) Integer.valueOf(string));
            } else if (type == Double.class) {
                collection.add((V) Double.valueOf(string));
            } else if (type == Float.class) {
                collection.add((V) Float.valueOf(string));
            } else if (type == Long.class) {
                collection.add((V) Long.valueOf(string));
            } else if (type == Short.class) {
                collection.add((V) Short.valueOf(string));
            } else if (type == Byte.class) {
                collection.add((V) Byte.valueOf(string));
            } else if (type == Boolean.class) {
                collection.add((V) Boolean.valueOf(string));
            } else if (type == Character.class) {
                collection.add((V) Character.valueOf(string.charAt(0)));
            }
        }

        return collection;
    }

    public static <V> V[] getArrayFromString(String s, Class<V> type) {
        String[] strings = toCollectionOrArrayFromString(s);

        V[] array = (V[]) Array.newInstance(type, strings.length);

        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            if (type == String.class) {
                array[i] = (V) string;
            } else if (type == Integer.class) {
                array[i] = (V) Integer.valueOf(string);
            } else if (type == Double.class) {
                array[i] = (V) Double.valueOf(string);
            } else if (type == Float.class) {
                array[i] = (V) Float.valueOf(string);
            } else if (type == Long.class) {
                array[i] = (V) Long.valueOf(string);
            } else if (type == Short.class) {
                array[i] = (V) Short.valueOf(string);
            } else if (type == Byte.class) {
                array[i] = (V) Byte.valueOf(string);
            } else if (type == Boolean.class) {
                array[i] = (V) Boolean.valueOf(string);
            } else if (type == Character.class) {
                array[i] = (V) Character.valueOf(string.charAt(0));
            }
        }

        return array;
    }
}
