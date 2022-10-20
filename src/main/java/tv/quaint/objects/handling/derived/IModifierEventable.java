package tv.quaint.objects.handling.derived;

import tv.quaint.objects.handling.IEventable;

import java.io.File;

public interface IModifierEventable extends IEventable {
    enum ModifierType {
        PLUGIN,
        MOD,
        ;
    }

    ModifierType getModifierType();

    boolean isPlugin();

    boolean isMod();

    File getDataFolder();

    void initializeDataFolder();
}
