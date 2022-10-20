package tv.quaint.objects.handling.derived;

import lombok.Getter;
import tv.quaint.storage.StorageUtils;

import java.io.File;

public abstract class ModEventable implements IModifierEventable {
    @Getter
    final String identifier;
    @Getter
    final ModifierType modifierType = ModifierType.PLUGIN;
    @Getter
    final File dataFolder;

    public ModEventable(String identifier, boolean initDataFolder) {
        this.identifier = identifier;
        this.dataFolder = StorageUtils.initializeModifierEventableFolder(this);

        if (initDataFolder) initializeDataFolder();
    }

    public ModEventable(String identifier) {
        this(identifier, true);
    }

    @Override
    public boolean isPlugin() {
        return getModifierType().equals(ModifierType.PLUGIN);
    }

    @Override
    public boolean isMod() {
        return getModifierType().equals(ModifierType.MOD);
    }

    @Override
    public void initializeDataFolder() {
        getDataFolder().mkdirs();
    }
}
