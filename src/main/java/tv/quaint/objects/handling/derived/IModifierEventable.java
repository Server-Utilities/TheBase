package tv.quaint.objects.handling.derived;

import tv.quaint.objects.handling.IEventable;

import java.io.File;

public interface IModifierEventable extends IEventable {
    enum ModifierType {
        PLUGIN,
        MOD,
        STREAMLINE,
        ;
    }

    ModifierType getModifierType();

    File getDataFolder();

    public default boolean isPlugin() {
        return getModifierType().equals(ModifierType.PLUGIN);
    }

    public default boolean isMod() {
        return getModifierType().equals(ModifierType.MOD);
    }

    public default boolean isStreamline() {
        return getModifierType().equals(ModifierType.STREAMLINE);
    }

    public default void initializeDataFolder() {
        getDataFolder().mkdirs();
    }
}
