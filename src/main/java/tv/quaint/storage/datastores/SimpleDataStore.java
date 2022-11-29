package tv.quaint.storage.datastores;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.objects.MappableObject;
import tv.quaint.objects.handling.IEventable;
import tv.quaint.objects.handling.derived.IModifierEventable;
import tv.quaint.storage.resources.StorageResource;

public abstract class SimpleDataStore<T, O extends MappableObject> extends StorageResource<T> implements ISimpleDataStore<O> {
    @Getter @Setter
    private O mappableObject;

    public SimpleDataStore(Class<T> resourceType, O mappableObject) {
        super(resourceType, mappableObject.getDiscriminatorKey(), mappableObject.getDiscriminatorValue());
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
    public void init(String identifier) {
        onInit(identifier);
    }

    public abstract void onInit(String identifier);

    @Override
    public void save(String identifier) {
        onSave(identifier);
    }

    public abstract void onSave(String identifier);

    @Override
    public void delete(String identifier) {
        onDelete(identifier);
    }

    public abstract void onDelete(String identifier);

    @Override
    public boolean exists(String identifier) {
        return onExists(identifier);
    }

    public abstract boolean onExists(String identifier);
}
