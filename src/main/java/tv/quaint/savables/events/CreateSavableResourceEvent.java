package tv.quaint.savables.events;

import tv.quaint.savables.SavableResource;

public class CreateSavableResourceEvent<T extends SavableResource> extends SavableEvent<T> {
    public CreateSavableResourceEvent(T resource) {
        super(resource);
    }
}
