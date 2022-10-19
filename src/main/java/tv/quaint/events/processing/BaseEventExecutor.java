package tv.quaint.events.processing;

import org.jetbrains.annotations.NotNull;
import tv.quaint.events.BaseEventListener;
import tv.quaint.events.components.BaseEvent;
import tv.quaint.events.processing.exception.BaseEventException;

/**
 * Interface which defines the class for event call backs to plugins
 */
public interface BaseEventExecutor {
    public void execute(@NotNull BaseEventListener listener, @NotNull BaseEvent event) throws BaseEventException;
}
