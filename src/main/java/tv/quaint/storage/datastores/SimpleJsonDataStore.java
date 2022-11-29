package tv.quaint.storage.datastores;

import de.leonhard.storage.Json;
import tv.quaint.objects.MappableObject;
import tv.quaint.objects.handling.derived.IModifierEventable;

import java.io.File;

public abstract class SimpleJsonDataStore<O extends MappableObject> extends SimpleFlatDataStore<Json, O> {
    public SimpleJsonDataStore(O mappableObject, String fileName, File parentDirectory, boolean selfContained) {
        super(Json.class, mappableObject, fileName, parentDirectory, selfContained);
    }

    public SimpleJsonDataStore(O mappableObject, String fileName, IModifierEventable eventable, boolean selfContained) {
        super(Json.class, mappableObject, fileName, eventable, selfContained);
    }
}
