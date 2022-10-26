package tv.quaint.storage.resources.databases.processing.interfacing;

public interface DBDataLike<T> {
    /**
     * Returns the data of the column / name.
     * @return The data of the column / name.
     */
    T getData();

    /**
     * Returns the type of the data.
     * @return The type of the data.
     */
    DataLikeType getType();
}
