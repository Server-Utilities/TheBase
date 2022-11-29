package tv.quaint.storage.resources.databases.processing;

public interface IDatabaseValue<V> extends Comparable<IDatabaseValue<V>> {
    /**
     * Gets the value represented by the given key.
     * @return The value represented by the given key.
     */
    public V getValue();

    /**
     * Sets the value represented by the given key.
     * @param value The value to set.
     */
    public void setValue(V value);

    /**
     * Gets the key represented by this pair.
     * @return The key represented by this pair.
     */
    public String getKey();
}
