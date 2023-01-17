package tv.quaint.storage.managers.documents;

import tv.quaint.storage.documents.SimpleFlatDocument;
import tv.quaint.storage.managers.IStorageManager;

public interface IDocumentManager<T extends SimpleFlatDocument<?>> extends IStorageManager<T> {
    /**
     * Saves all documents.
     */
    void saveAll();
}
