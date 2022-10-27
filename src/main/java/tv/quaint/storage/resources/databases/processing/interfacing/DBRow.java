package tv.quaint.storage.resources.databases.processing.interfacing;

import java.util.concurrent.ConcurrentSkipListMap;

public interface DBRow<C extends DBColumn, V extends DBDataLike<?>> {
    /**
     * Gets the name of the table this row is in.
     */
    String getTableName();

    /**
     * Gets the value of the column.
     *
     * @param column The column to get the value of.
     * @return The value of the column.
     */
    V getValue(C column);

    /**
     * Gets the value of the column.
     *
     * @param columnName The name of the column to get the value of.
     * @return The value of the column.
     */
    V getValue(String columnName);

    /**
     * Gets the value at the given index.
     * @param index The index to get the value at.
     * @return The value at the given index.
     */
    V getValue(int index);

    /**
     * Gets a column by its name.
     *
     * @param columnName The name of the column to get.
     * @return The column.
     */
    C getColumn(String columnName);

    /**
     * Gets a column by its index.
     *
     * @param index The index of the column to get.
     * @return The column.
     */
    C getColumn(int index);

    /**
     * Gets the size of the row.
     *
     * @return The size of the row.
     */
    int size();

    /**
     * Gets the columns of the row.
     *
     * @return The columns of the row.
     */
    C[] getColumns();

    /**
     * Gets the values of the row.
     *
     * @return The values of the row.
     */
    V[] getValues();

    /**
     * Gets the map of the columns with their values.
     *
     * @return A {@link ConcurrentSkipListMap} of the columns with their values.
     */
    ConcurrentSkipListMap<C, V> getMap();

    /**
     * Returns this object as a {@link String}.
     * @return This object as a {@link String}.
     */
    String toString();
}
