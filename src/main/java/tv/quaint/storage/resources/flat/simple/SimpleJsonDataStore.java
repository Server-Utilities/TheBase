package tv.quaint.storage.resources.flat.simple;

import de.leonhard.storage.Config;
import de.leonhard.storage.Json;
import tv.quaint.objects.handling.derived.IModifierEventable;
import tv.quaint.storage.resources.flat.FlatFileResource;

import java.io.File;

public abstract class SimpleJsonDataStore extends FlatFileResource<Json> {
    public SimpleJsonDataStore(String fileName, File parentDirectory, boolean selfContained) {
        super(Json.class, fileName, parentDirectory, selfContained);
        init();
    }

    public SimpleJsonDataStore(String fileName, IModifierEventable eventable, boolean selfContained) {
        this(fileName, eventable.getDataFolder(), selfContained);
    }

    public SimpleJsonDataStore(String fileName, File parentDirectory) {
        this(fileName, parentDirectory, false);
    }

    public SimpleJsonDataStore(String fileName, IModifierEventable eventable) {
        this(fileName, eventable, false);
    }

    public abstract void init();
}
