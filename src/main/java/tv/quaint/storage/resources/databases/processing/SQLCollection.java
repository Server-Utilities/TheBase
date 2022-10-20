package tv.quaint.storage.resources.databases.processing;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentSkipListMap;

public class SQLCollection {
    @Getter @Setter
    String collectionName;
    @Getter @Setter
    ConcurrentSkipListMap<String, Object> document;
    @Getter @Setter
    String discriminatorKey;
    @Getter @Setter
    Object discriminator;

    public SQLCollection(String collectionName, ConcurrentSkipListMap<String, Object> document, String discriminatorKey, Object discriminator) {
        this.collectionName = collectionName;
        this.document = document;
        this.discriminatorKey = discriminatorKey;
        this.discriminator = discriminator;
        document.put(discriminatorKey, discriminator);
    }

    public SQLCollection(String collectionName, String discriminatorKey, Object discriminator) {
        this(collectionName, new ConcurrentSkipListMap<>(), discriminatorKey, discriminator);
    }

    public void putSet(String key, Object value) {
        this.document.put(key, value);
    }

    public <O> O get(String key, Class<O> def) {
        try {
            O object = (O) this.document.get(key);

            if (! def.isInstance(object)) return null;

            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <O> O getOrSetDefault(String key, O value) {
        try {
            O object = (O) this.document.get(key);
            if (object == null) this.putSet(key, value);

            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
