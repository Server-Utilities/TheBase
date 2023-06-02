package tv.quaint.savables.events;

import tv.quaint.savables.SavableResource;

public class DeleteSavableResourceEvent<T extends SavableResource> extends SavableEvent<T> {
    public DeleteSavableResourceEvent(T resource) {
        super(resource);
    }
}
