package tv.quaint.storage.resources.databases.processing.sql.data;

import lombok.Getter;
import tv.quaint.storage.resources.databases.processing.sql.SQLSchematic;

public abstract class AbstractSQLData<T> implements SQLDataLike<T> {
    @Getter
    private final SQLSchematic.SQLType type;

    public AbstractSQLData(SQLSchematic.SQLType type) {
        this.type = type;
    }
}
