package tv.quaint.storage.managers.datastores;

import tv.quaint.storage.datastores.SimpleJsonDataStore;

import java.io.File;

public abstract class SimpleJsonDataStoreManager extends SimpleDataStoreManager<SimpleJsonDataStore> {
    public SimpleJsonDataStoreManager(File parentDirectory) {
        super(SimpleJsonDataStore.class, parentDirectory);
    }
}
