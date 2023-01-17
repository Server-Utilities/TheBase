package tv.quaint.storage.datastores;

import de.leonhard.storage.Json;
import de.leonhard.storage.internal.FlatFile;
import tv.quaint.objects.handling.derived.IModifierEventable;
import tv.quaint.storage.documents.SimpleFlatDocument;

import java.io.File;

public abstract class SimpleJsonDatastore<T> extends SimpleFlatDatastore<Json, T> {
    public SimpleJsonDatastore(String fileName, File parentDirectory, boolean selfContained) {
        super(Json.class, fileName, parentDirectory, selfContained);
    }

    public SimpleJsonDatastore(String fileName, IModifierEventable eventable, boolean selfContained) {
        super(Json.class, fileName, eventable, selfContained);
    }

    public SimpleJsonDatastore(String fileName, File parentDirectory) {
        super(Json.class, fileName, parentDirectory);
    }

    public SimpleJsonDatastore(String fileName, IModifierEventable eventable) {
        super(Json.class, fileName, eventable);
    }
}
