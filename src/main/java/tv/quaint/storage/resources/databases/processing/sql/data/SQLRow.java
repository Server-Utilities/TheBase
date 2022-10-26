package tv.quaint.storage.resources.databases.processing.sql.data;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import tv.quaint.storage.resources.databases.processing.interfacing.DBRow;
import tv.quaint.storage.resources.databases.processing.sql.SQLSchematic;

import java.util.Date;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents a row of data ({@link SQLColumn}s) in an SQL table.
 */
public class SQLRow implements Comparable<SQLRow>, DBRow<SQLColumn, SQLDataLike<?>> {
    @Getter
    private final Date accessed;
    @Getter
    private final ConcurrentSkipListMap<SQLColumn, SQLDataLike<?>> map = new ConcurrentSkipListMap<>();

    public SQLRow(SQLColumn[] columns, SQLDataLike<?>[] data) {
        accessed = new Date();
        for (int i = 0; i < columns.length; i++) {
            map.put(columns[i], data[i]);
        }
    }

    public SQLRow(ConcurrentSkipListMap<SQLColumn, SQLDataLike<?>> map) {
        accessed = new Date();
        this.map.putAll(map);
    }

    @Override
    public SQLDataLike<?> getValue(SQLColumn column) {
        return getMap().get(column);
    }

    @Override
    public SQLDataLike<?> getValue(String columnName) {
        return getValue(getColumn(columnName));
    }

    @Override
    public SQLColumn getColumn(String columnName) {
        AtomicReference<SQLColumn> atomicReference = new AtomicReference<>();
        getMap().keySet().forEach(column -> {
            if (column.getName().equals(columnName)) {
                atomicReference.set(column);
            }
        });
        return atomicReference.get();
    }

    public SQLColumn getColumn(int index) {
        return getColumns()[index];
    }

    public SQLDataLike<?> getValue(int index) {
        return getValues()[index];
    }

    public int size() {
        return getMap().size();
    }

    @Override
    public SQLColumn[] getColumns() {
        return getMap().keySet().toArray(new SQLColumn[0]);
    }

    @Override
    public SQLDataLike<?>[] getValues() {
        return getMap().values().toArray(new SQLDataLike<?>[0]);
    }

    public String toSqlStringForReplace() {
        StringBuilder builder = new StringBuilder();

        for (SQLColumn column : getMap().keySet()) {
            builder.append(column.getName()).append(" = ");
            if (column.getType().equals(SQLSchematic.SQLType.VARCHAR)) {
                builder.append("'").append(getValue(column).getData()).append("'");
            } else {
                builder.append(getValue(column).getData());
            }
            builder.append(", ");
        }

        return builder.substring(0, builder.length() - 2);
    }

    @Override
    public String toString() {
        return "SQLRow{" +
                "map=" + map +
                '}';
    }

    @Override
    public int compareTo(@NotNull SQLRow o) {
        return Long.compare(getAccessed().getTime(), o.getAccessed().getTime());
    }
}
