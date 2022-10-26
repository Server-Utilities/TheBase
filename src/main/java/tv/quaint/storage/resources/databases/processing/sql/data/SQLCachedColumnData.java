package tv.quaint.storage.resources.databases.processing.sql.data;

import tv.quaint.storage.resources.databases.processing.CachedDBColumnData;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLColumn;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLDataLike;

public class SQLCachedColumnData<T, D extends SQLDataLike<T>> extends CachedDBColumnData<T, D> {
    public SQLCachedColumnData(SQLColumn column, D data) {
        super(column, data);
    }
}
