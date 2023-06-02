package tv.quaint.objects;

import org.jetbrains.annotations.NotNull;

/**
 * An interface for objects that have an identifier.
 */
public interface Identifiable extends Comparable<Identifiable> {
    /**
     * Get the identifier of this object.
     * @return The identifier of this object.
     */
    public String getIdentifier();

    /**
     * Set the identifier of this object.
     * @param identifier The identifier to set.
     */
    public void setIdentifier(String identifier);

    /**
     * Compare two {@link Identifiable} objects.
     * @param o the {@link Identifiable} object to compare to.
     * @return the result of the comparison.
     */
    @Override
    default int compareTo(@NotNull Identifiable o) {
        return getIdentifier().compareTo(o.getIdentifier());
    }
}
