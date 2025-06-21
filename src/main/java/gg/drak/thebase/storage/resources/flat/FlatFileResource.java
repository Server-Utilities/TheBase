package gg.drak.thebase.storage.resources.flat;

import de.leonhard.storage.*;
import de.leonhard.storage.internal.FlatFile;
import lombok.Getter;
import lombok.Setter;
import gg.drak.thebase.storage.StorageUtils;
import gg.drak.thebase.storage.resources.StorageResource;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Getter @Setter
public class FlatFileResource<T extends FlatFile> extends StorageResource<T> {
    T resource;
    final String fileName;
    final File parentDirectory;
    final File selfFile;
    final boolean selfContained;

    public FlatFileResource(Class<T> resourceType, String fileName, File parentDirectory, boolean selfContained) {
        super(resourceType, "name", fileName);
        
        this.fileName = fileName;
        this.parentDirectory = parentDirectory;
        this.selfFile = new File(parentDirectory, fileName);
        this.selfContained = selfContained;

        reloadResource(true);
    }

    public T load(boolean selfContained) {
        if (selfContained) {
            return loadConfigFromSelf(this.selfFile, this.fileName);
        } else {
            return loadConfigNoDefault(this.selfFile);
        }
    }

    public void reload(boolean selfContained) {
        this.resource = load(selfContained);
    }

    public void syncMap() {
        for (String key : this.resource.keySet()) {
            Object obj = this.resource.get(key);
            if (obj == null) continue;
            this.getMap().put(key, obj);
        }
    }

    @Override
    public <O> O get(String key, Class<O> def) {
        try {
            O object = this.resource.get(key, def.newInstance());

            if (! def.isInstance(object)) return null;

            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void continueReloadResource() {
        reload(this.selfContained);
        syncMap();
    }

    @Override
    public <V> void write(String key, V value) {
        this.resource.set(key, value);
    }

    @Override
    public <O> O getOrSetDefault(String key, O value) {
        return this.resource.getOrSetDefault(key, value);
    }

    @Override
    public void push() {
//        this.resource.write();
    }

    public boolean exists() {
        return this.selfFile.exists();
    }

    public boolean empty() {
        return lineCount() <= 0;
    }

    public ConcurrentSkipListMap<Integer, String> lines() {
        try {
            Scanner reader = new Scanner(this.selfFile);

            ConcurrentSkipListMap<Integer, String> lines = new ConcurrentSkipListMap<>();
            while (reader.hasNext()) {
                String s = reader.nextLine();
                lines.put(lines.size() + 1, s);
            }
            return lines;
        } catch (Exception e) {
            e.printStackTrace();
            return new ConcurrentSkipListMap<>();
        }
    }

    public int lineCount() {
        return lines().size();
    }

    public T loadConfigFromSelf(File file, String fileString) {
        if (! file.exists()) {
            try {
                this.parentDirectory.mkdirs();
                try (InputStream in = getResourceAsStream(fileString)) {
                    assert in != null;
                    Files.copy(in, file.toPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (getResourceType().equals(Config.class)) {
            return (T) StorageUtils.fromFile(file).createConfig();
        }
        if (getResourceType().equals(Yaml.class)) {
            return (T) StorageUtils.fromFile(file).createYaml();
        }
        if (getResourceType().equals(Json.class)) {
            return (T) StorageUtils.fromFile(file).createJson();
        }
        if (getResourceType().equals(Toml.class)) {
            return (T) StorageUtils.fromFile(file).createToml();
        }
        return null;
    }

    public T loadConfigNoDefault(File file) {
        if (! file.exists()) {
            try {
                this.parentDirectory.mkdirs();
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (getResourceType().equals(Config.class)) {
            return (T) SimplixBuilder.fromFile(file).createConfig();
        }
        if (getResourceType().equals(Yaml.class)) {
            return (T) SimplixBuilder.fromFile(file).createYaml();
        }
        if (getResourceType().equals(Json.class)) {
            return (T) SimplixBuilder.fromFile(file).createJson();
        }
        if (getResourceType().equals(Toml.class)) {
            return (T) SimplixBuilder.fromFile(file).createToml();
        }
        return null;
    }

    @Override
    public void delete() {
        this.selfFile.delete();
    }

    @Override
    public ConcurrentSkipListSet<String> singleLayerKeySet() {
        return new ConcurrentSkipListSet<>(resource.singleLayerKeySet());
    }

    @Override
    public ConcurrentSkipListSet<String> singleLayerKeySet(String key) {
        return new ConcurrentSkipListSet<>(resource.singleLayerKeySet(key));
    }

    @Override
    public <V> void updateSingle(String key, V value) {
        write(key, value);
    }

    @Override
    public <V> void updateMultiple(ConcurrentSkipListMap<String, V> values) {
        values.forEach(this::updateSingle);
    }
}
