package tv.quaint.storage.managers.datastores;

import tv.quaint.storage.datastores.SimpleFlatDatastore;
import tv.quaint.storage.managers.IStorageManager;

public interface IDataStoreManager<T extends SimpleFlatDatastore<?, ?>> extends IStorageManager<T> {
    /**
     * Saves all documents.
     */
    void saveAll();
}
