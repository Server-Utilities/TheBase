package tv.quaint.storage.managers.documents;

import tv.quaint.storage.documents.SimpleDocument;
import tv.quaint.storage.managers.IStorageManager;

public interface IDocumentManager<T extends SimpleDocument<?>> extends IStorageManager<T> {
    /**
     * Saves all documents.
     */
    void saveAll();
}
