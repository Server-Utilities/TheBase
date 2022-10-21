package tv.quaint.storage.documents;

import de.leonhard.storage.Toml;
import tv.quaint.objects.handling.derived.IModifierEventable;

import java.io.File;

public abstract class SimpleTomlDocument extends SimpleDocument<Toml> {
    public SimpleTomlDocument(String fileName, File parentDirectory, boolean selfContained) {
        super(Toml.class, fileName, parentDirectory, selfContained);
    }

    public SimpleTomlDocument(String fileName, IModifierEventable eventable, boolean selfContained) {
        super(Toml.class, fileName, eventable.getDataFolder(), selfContained);
    }

    public SimpleTomlDocument(String fileName, File parentDirectory) {
        super(Toml.class, fileName, parentDirectory, false);
    }

    public SimpleTomlDocument(String fileName, IModifierEventable eventable) {
        super(Toml.class, fileName, eventable, false);
    }
}
