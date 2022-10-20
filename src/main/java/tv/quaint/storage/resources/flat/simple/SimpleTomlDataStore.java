package tv.quaint.storage.resources.flat.simple;

import de.leonhard.storage.Json;
import de.leonhard.storage.Toml;
import tv.quaint.objects.handling.derived.IModifierEventable;
import tv.quaint.storage.resources.flat.FlatFileResource;

import java.io.File;

public abstract class SimpleTomlDataStore extends FlatFileResource<Toml> {
    public SimpleTomlDataStore(String fileName, File parentDirectory, boolean selfContained) {
        super(Toml.class, fileName, parentDirectory, selfContained);
        init();
    }

    public SimpleTomlDataStore(String fileName, IModifierEventable eventable, boolean selfContained) {
        this(fileName, eventable.getDataFolder(), selfContained);
    }

    public SimpleTomlDataStore(String fileName, File parentDirectory) {
        this(fileName, parentDirectory, false);
    }

    public SimpleTomlDataStore(String fileName, IModifierEventable eventable) {
        this(fileName, eventable, false);
    }

    public abstract void init();
}
