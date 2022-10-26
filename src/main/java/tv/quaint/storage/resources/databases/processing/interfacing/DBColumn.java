package tv.quaint.storage.resources.databases.processing.interfacing;

public interface DBColumn {
    /**
     * Gets the name of the column.
     * @return The name of the column
     */
    String getName();

    /**
     * Gets the type of the column.
     * @return The type of the column
     */
    DataLikeType getType();
}
