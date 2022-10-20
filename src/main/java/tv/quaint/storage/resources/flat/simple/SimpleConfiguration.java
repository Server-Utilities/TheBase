package tv.quaint.storage.resources.flat.simple;

import de.leonhard.storage.Config;
import tv.quaint.objects.handling.derived.IModifierEventable;
import tv.quaint.storage.resources.flat.FlatFileResource;

import java.io.File;

public abstract class SimpleConfiguration extends FlatFileResource<Config> {
    public SimpleConfiguration(String fileName, File parentDirectory, boolean selfContained) {
        super(Config.class, fileName, parentDirectory, selfContained);
        init();
    }

    public SimpleConfiguration(String fileName, IModifierEventable eventable, boolean selfContained) {
        this(fileName, eventable.getDataFolder(), selfContained);
    }

    public SimpleConfiguration(String fileName, File parentDirectory) {
        this(fileName, parentDirectory, true);
    }

    public SimpleConfiguration(String fileName, IModifierEventable eventable) {
        this(fileName, eventable, true);
    }

    public abstract void init();
}
