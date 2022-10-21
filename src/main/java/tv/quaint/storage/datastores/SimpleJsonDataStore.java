package tv.quaint.storage.datastores;

import de.leonhard.storage.Json;
import tv.quaint.objects.handling.derived.IModifierEventable;

import java.io.File;

public abstract class SimpleJsonDataStore extends SimpleDataStore<Json> {
    public SimpleJsonDataStore(String fileName, File parentDirectory, boolean selfContained) {
        super(Json.class, fileName, parentDirectory, selfContained);
    }

    public SimpleJsonDataStore(String fileName, IModifierEventable eventable, boolean selfContained) {
        super(Json.class, fileName, eventable.getDataFolder(), selfContained);
    }

    public SimpleJsonDataStore(String fileName, File parentDirectory) {
        super(Json.class, fileName, parentDirectory, false);
    }

    public SimpleJsonDataStore(String fileName, IModifierEventable eventable) {
        super(Json.class, fileName, eventable, false);
    }
}
