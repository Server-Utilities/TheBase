package tv.quaint.storage;

import de.leonhard.storage.Config;
import de.leonhard.storage.Json;
import de.leonhard.storage.Toml;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.FlatFile;
import lombok.Getter;
import org.bson.Document;
import tv.quaint.objects.handling.IEventable;
import tv.quaint.objects.handling.derived.IModifierEventable;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class StorageUtils {
    @Getter
    private static final File environmentFolder = new File(System.getProperty("user.dir"));

    public enum StorageType {
        YAML,
        JSON,
        TOML,
        MONGO,
        MYSQL,
        ;
    }
    public enum DatabaseType {
        MONGO,
        MYSQL,
        ;
    }

    public static File initializeModifierEventableFolder(IModifierEventable eventable) {
        File parentDir = getEnvironmentFolder();
        if (eventable.isMod()) parentDir = new File(getEnvironmentFolder(), "mods" + File.separator);
        if (eventable.isMod()) parentDir = new File(getEnvironmentFolder(), "plugins" + File.separator);
        return new File(parentDir, eventable.getIdentifier() + File.separator);
    }

    public static boolean copy(File updateFile, File file) {
        try {
            Files.copy(updateFile.toPath(), file.toPath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static StorageType getStorageTypeFromLeonhard(Class<? extends FlatFile> leonhard) {
        if (leonhard.equals(Yaml.class)) return StorageType.YAML;
        if (leonhard.equals(Config.class)) return StorageType.YAML;
        if (leonhard.equals(Json.class)) return StorageType.JSON;
        if (leonhard.equals(Toml.class)) return StorageType.TOML;

        return null;
    }

    public static StorageType getStorageType(Class<?> clazz) {
        if (clazz.equals(Yaml.class)) return StorageType.YAML;
        if (clazz.equals(Config.class)) return StorageType.YAML;
        if (clazz.equals(Json.class)) return StorageType.JSON;
        if (clazz.equals(Toml.class)) return StorageType.TOML;
        if (clazz.equals(Document.class)) return StorageType.MONGO;

        return null;
    }

    public static Document getWhere(String key, Object value) {
        return new Document(key, value);
    }

    public static String parseDotsMongo(String key) {
        return key
                .replace("-", "")
                .replace(".", "_")
                ;
    }

    public static void ensureFileFromSelf(ClassLoader loader, File parentDirectory, File toEnsure, String fileName) {
        if (! toEnsure.exists()) {
            try {
                parentDirectory.mkdirs();
                try (InputStream in = loader.getResourceAsStream(fileName)) {
                    assert in != null;
                    Files.copy(in, toEnsure.toPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void ensureFileNoDefault(File parentDirectory, File toEnsure) {
        if (! toEnsure.exists()) {
            try {
                parentDirectory.mkdirs();
                toEnsure.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
