package tv.quaint.objects.handling.derived;

import lombok.Getter;
import tv.quaint.objects.handling.IEventable;
import tv.quaint.storage.StorageUtils;

import java.io.File;

public interface IPluginEventable extends IModifierEventable {
    @Override
    public default ModifierType getModifierType() {
        return ModifierType.PLUGIN;
    }
}
