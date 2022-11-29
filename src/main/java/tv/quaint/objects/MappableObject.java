package tv.quaint.objects;

import java.util.concurrent.ConcurrentSkipListMap;

public interface MappableObject {
    /**
     * This method is used to get the map of the object.
     * @return The mapped data of the object.
     */
    public ConcurrentSkipListMap<String, ?> getMappedData();

    /**
     * Gets the discriminator key of the object.
     * @return The discriminator key of the object.
     */
    public String getDiscriminatorKey();

    /**
     * Gets the discriminator of the object.
     * @return The discriminator of the object.
     */
    public String getDiscriminatorValue();
}
