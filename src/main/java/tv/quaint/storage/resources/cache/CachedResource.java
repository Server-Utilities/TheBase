package tv.quaint.storage.resources.cache;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.resources.StorageResource;

import java.util.concurrent.ConcurrentSkipListMap;

public class CachedResource<T> extends StorageResource<T> {
    @Getter @Setter
    ConcurrentSkipListMap<String, Object> cachedData = new ConcurrentSkipListMap<>();
    @Getter @Setter
    boolean deleted = false;

    public CachedResource(Class<T> resourceType, String discriminatorKey, String discriminator) {
        super(resourceType, discriminatorKey, discriminator);
        getCachedData().put(discriminatorKey, discriminator);
    }

    @Override
    public <O> O get(String key, Class<O> def) {
        return (O) cachedData.get(fixSyntax(key));
    }

    @Override
    public void continueReloadResource() {

    }

    @Override
    public <V> void write(String key, V value) {
        cachedData.put(fixSyntax(key), value);
    }

    @Override
    public <O> O getOrSetDefault(String key, O value) {
        if (cachedData.containsKey(fixSyntax(key))) {
            return (O) cachedData.get(fixSyntax(key));
        } else {
            cachedData.put(fixSyntax(key), value);
            return value;
        }
    }

    @Override
    public void push() {

    }

    @Override
    public void delete() {
        cachedData.clear();
        deleted = true;
    }

    @Override
    public boolean exists() {
        return ! deleted;
    }

    @Override
    public <V> void updateSingle(String key, V value) {
        cachedData.put(fixSyntax(key), value);
    }

    @Override
    public <V> void updateMultiple(ConcurrentSkipListMap<String, V> values) {
        values.forEach((key, value) -> cachedData.put(fixSyntax(key), value));
    }

    public static String fixSyntax(String key) {
        return key
                .replace("-", "_")
                .replace('.', '_')
                .replace("+", "_")
                .replace(" ", "_")
                .replace(":", "_")
                .replace(";", "_")
                .replace("=", "_")
                .replace("?", "_")
                .replace("!", "_")
                .replace("@", "_")
                .replace("#", "_")
                .replace("$", "_")
                .replace("%", "_")
                .replace("^", "_")
                .replace("&", "_")
                .replace("*", "_")
                .replace("(", "_")
                .replace(")", "_")
                .replace("[", "_")
                .replace("]", "_")
                .replace("{", "_")
                .replace("}", "_")
                .replace("|", "_")
                .replace("\\", "_")
                .replace("/", "_")
                .replace("`", "_")
                .replace("~", "_")
                .replace("'", "_")
                .replace("\"", "_")
                .replace(",", "_")
                .replace("<", "_")
                .replace(">", "_")
                ;
    }
}
