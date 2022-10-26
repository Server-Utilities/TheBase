package tv.quaint.storage.resources.databases.processing.sql.data;

import lombok.Getter;
import tv.quaint.storage.resources.databases.processing.interfacing.DBColumn;
import tv.quaint.storage.resources.databases.processing.sql.SQLSchematic;

/**
 * Used by {@link SQLSchematic} to represent a column in an SQL table.
 * Defined by an {@link SQLSchematic.SQLType} and a max.
 * @author Quaint
 */
public class SQLColumn implements Comparable<SQLColumn>, DBColumn {
    @Getter
    private final String name;
    @Getter
    private final SQLSchematic.SQLType type;
    @Getter
    private final long max;
    @Getter
    private final int index;

    public SQLColumn(String name, SQLSchematic.SQLType type, long max, int index) {
        this.name = name;
        this.type = type;
        this.max = max;
        this.index = index;
    }

    public SQLColumn(String name, SQLSchematic.SQLType type, int index) {
        this(name, type, type.getMax(), index);
    }

    @Override
    public String toString() {
        return "SQLColumn{" +
                "type=" + type +
                ", index=" + index +
                '}';
    }

    @Override
    public int compareTo(SQLColumn o) {
        return CharSequence.compare(getType().toString(), o.getType().toString());
    }
}
