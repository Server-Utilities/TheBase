package tv.quaint.storage.managers.datastores;

import tv.quaint.storage.datastores.SimpleTomlDataStore;

import java.io.File;

public abstract class SimpleTomlDataStoreManager extends SimpleDataStoreManager<SimpleTomlDataStore> {
    public SimpleTomlDataStoreManager(File parentDirectory) {
        super(SimpleTomlDataStore.class, parentDirectory);
    }
}
