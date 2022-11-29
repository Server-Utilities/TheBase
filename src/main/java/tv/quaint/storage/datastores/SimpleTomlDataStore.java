package tv.quaint.storage.datastores;

import de.leonhard.storage.Json;
import de.leonhard.storage.Toml;
import tv.quaint.objects.MappableObject;
import tv.quaint.objects.handling.derived.IModifierEventable;

import java.io.File;

public abstract class SimpleTomlDataStore<O extends MappableObject> extends SimpleFlatDataStore<Toml, O> {
    public SimpleTomlDataStore(Class<Toml> resourceType, O mappableObject, String fileName, File parentDirectory, boolean selfContained) {
        super(resourceType, mappableObject, fileName, parentDirectory, selfContained);
    }

    public SimpleTomlDataStore(Class<Toml> resourceType, O mappableObject, String fileName, IModifierEventable eventable, boolean selfContained) {
        super(resourceType, mappableObject, fileName, eventable, selfContained);
    }
}
