package tv.quaint.savables.events;

import lombok.Getter;
import tv.quaint.events.components.BaseEvent;
import tv.quaint.savables.SavableResource;

public abstract class SavableEvent<T extends SavableResource> extends BaseEvent {
    @Getter
    private final T resource;

    public SavableEvent(T resource) {
        super();
        this.resource = resource;
    }
}
