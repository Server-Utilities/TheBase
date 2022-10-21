package tv.quaint.storage.datastores;

import tv.quaint.storage.documents.ISimpleDocument;

public interface ISimpleDataStore extends ISimpleDocument {
    void init(String identifier);

    void save(String identifier);

    void delete(String identifier);

    boolean exists(String identifier);
}
