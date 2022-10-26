package tv.quaint.storage.resources.databases.processing;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import tv.quaint.storage.resources.databases.processing.interfacing.DBColumn;
import tv.quaint.storage.resources.databases.processing.interfacing.DBDataLike;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLColumn;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLDataLike;

import java.util.Date;

/**
 * A class to represent a cached column in a SQL table.
 * Takes a {@link SQLColumn} to define the column.
 * Takes an {@link SQLDataLike} to define the data.
 * Has a {@link Date} to define when the data was last accessed.
 * @author Quaint
 */
public class CachedDBColumnData<T, D extends DBDataLike<T>> implements Comparable<CachedDBColumnData<?, ?>> {
    @Getter
    private final DBColumn column;
    @Getter
    private final D data;
    @Getter
    private Date lastAccessed;

    public CachedDBColumnData(DBColumn column, D data) {
        this.column = column;
        this.data = data;
        this.lastAccessed = new Date();
    }

    public void updateLastAccessed() {
        this.lastAccessed = new Date();
    }

    @Override
    public String toString() {
        return "CachedColumnData{" +
                "column=" + column +
                ", data=" + data +
                '}';
    }

    @Override
    public int compareTo(@NotNull CachedDBColumnData o) {
        return CharSequence.compare(getColumn().toString(), o.getColumn().toString());
    }
}
