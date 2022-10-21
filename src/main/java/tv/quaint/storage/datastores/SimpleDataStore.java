package tv.quaint.storage.datastores;

import de.leonhard.storage.internal.FlatFile;
import tv.quaint.objects.handling.derived.IModifierEventable;
import tv.quaint.storage.resources.flat.FlatFileResource;

import java.io.File;

public abstract class SimpleDataStore<T extends FlatFile> extends FlatFileResource<T> implements ISimpleDataStore {
    public SimpleDataStore(Class<T> resourceType, String fileName, File parentDirectory, boolean selfContained) {
        super(resourceType, fileName, parentDirectory, selfContained);
        init();
    }

    public SimpleDataStore(Class<T> resourceType, String fileName, IModifierEventable eventable, boolean selfContained) {
        this(resourceType, fileName, eventable.getDataFolder(), selfContained);
        init();
    }

    public SimpleDataStore(Class<T> resourceType, String fileName, File parentDirectory) {
        this(resourceType, fileName, parentDirectory, false);
        init();
    }

    public SimpleDataStore(Class<T> resourceType, String fileName, IModifierEventable eventable) {
        this(resourceType, fileName, eventable, false);
        init();
    }

    @Override
    public void init() {
        onInit();
    }

    public abstract void onInit();

    @Override
    public void save() {
        onSave();
    }

    public abstract void onSave();

    @Override
    public void delete() {
        onDelete();
    }

    public abstract void onDelete();

    @Override
    public boolean exists() {
        return onExists();
    }

    public abstract boolean onExists();

    @Override
    public void init(String identifer) {
        onInit();
    }

    public abstract void onInit(String identifer);

    @Override
    public void save(String identifer) {
        onSave();
    }

    public abstract void onSave(String identifer);

    @Override
    public void delete(String identifer) {
        onDelete();
    }

    public abstract void onDelete(String identifer);

    @Override
    public boolean exists(String identifer) {
        return onExists();
    }

    public abstract boolean onExists(String identifer);
}
