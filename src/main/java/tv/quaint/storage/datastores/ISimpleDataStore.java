package tv.quaint.storage.datastores;

import tv.quaint.objects.MappableObject;
import tv.quaint.storage.documents.ISimpleDocument;

public interface ISimpleDataStore<O extends MappableObject> extends ISimpleDocument {
    public O getMappableObject();

    public void setMappableObject(O object);

    public void init(String identifier);

    public void save(String identifier);

    public void delete(String identifier);

    public boolean exists(String identifier);
}
