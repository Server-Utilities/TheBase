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

    public <V> CachedResource(Class<T> resourceType, String discriminatorKey, V discriminator) {
        super(resourceType, discriminatorKey, discriminator);
        getCachedData().put(discriminatorKey, discriminator);
    }

    @Override
    public <O> O get(String key, Class<O> def) {
        return (O) cachedData.get(fixSyntax(key));
    }

    public <O> O get(String key) {
        return (O) cachedData.get(fixSyntax(key));
    }

    public Object getGeneric(String key) {
        return cachedData.get(fixSyntax(key));
    }

    public boolean containsKey(String key) {
        return cachedData.containsKey(fixSyntax(key));
    }

    @Override
    public void continueReloadResource() {

    }

    @Override
    public <V> void write(String key, V value) {
        if (key == null) return;
        if (value == null) return;
        if (key.isEmpty()) return;
        if (key.isBlank()) return;

        if (cachedData == null) cachedData = new ConcurrentSkipListMap<>();

        String fixedKey = fixSyntax(key);
        if (fixedKey.isEmpty() || fixedKey.isBlank()) return;

        cachedData.put(fixedKey, value);
    }

    @Override
    public <O> O getOrSetDefault(String key, O value) {
        if (key == null) return value;
        if (value == null) return value;
        if (key.isEmpty()) return value;
        if (key.isBlank()) return value;

        if (cachedData == null) cachedData = new ConcurrentSkipListMap<>();

        String fixedKey = fixSyntax(key);
        if (fixedKey.isEmpty() || fixedKey.isBlank()) return value;

        if (cachedData.containsKey(fixSyntax(key))) {
            return (O) cachedData.get(fixedKey);
        } else {
            cachedData.put(fixedKey, value);
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
