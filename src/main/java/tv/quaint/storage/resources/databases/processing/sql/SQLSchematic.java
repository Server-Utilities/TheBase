package tv.quaint.storage.resources.databases.processing.sql;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.resources.databases.processing.DBSchematic;
import tv.quaint.storage.resources.databases.processing.interfacing.DataLikeType;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLColumn;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A schematic for creating an SQL table. Used to help an SQLPage.
 * @author Quaint
 */
public class SQLSchematic implements DBSchematic<SQLColumn> {
    public enum SQLType implements DataLikeType {
        VARCHAR(255),
        TEXT(65535),
        INT(11),
        BIGINT(20),
        FLOAT(12),
        DOUBLE(22),
        DATE(10),
        TIMESTAMP(19),
        TIME(8),
        CHAR(1),
        BLOB(65535),
        ENUM(65535),
        SET(64);

        @Getter
        private final long max;

        SQLType(long max) {
            this.max = max;
        }

        public static SQLType fromObject(Object o) {
            if (o instanceof String) return VARCHAR;
            if (o instanceof Integer) return INT;
            if (o instanceof Long) return BIGINT;
            if (o instanceof Float) return FLOAT;
            if (o instanceof Double) return DOUBLE;
            if (o instanceof java.util.Date) return DATE;
            if (o instanceof java.sql.Date) return DATE;
            if (o instanceof java.sql.Time) return TIME;
            if (o instanceof java.sql.Timestamp) return TIMESTAMP;
            if (o instanceof java.sql.Blob) return BLOB;
            if (o instanceof java.sql.Clob) return TEXT;
            if (o instanceof java.sql.Array) return ENUM;
            if (o instanceof java.sql.Ref) return SET;
            if (o instanceof java.sql.Struct) return SET;
            if (o instanceof java.sql.RowId) return VARCHAR;
            if (o instanceof java.sql.NClob) return TEXT;
            if (o instanceof java.sql.SQLXML) return TEXT;
            if (o instanceof java.sql.SQLData) return SET;
            if (o instanceof java.sql.SQLInput) return SET;
            if (o instanceof java.sql.SQLOutput) return SET;
            if (o instanceof java.sql.NClob) return TEXT;
            if (o instanceof java.sql.SQLXML) return TEXT;
            if (o instanceof java.sql.SQLData) return SET;
            if (o instanceof java.sql.SQLInput) return SET;
            if (o instanceof java.sql.SQLOutput) return SET;
            if (o instanceof java.sql.NClob) return TEXT;
            if (o instanceof java.sql.SQLXML) return TEXT;
            if (o instanceof java.sql.SQLData) return SET;
            if (o instanceof java.sql.SQLInput) return SET;
            if (o instanceof java.sql.SQLOutput) return SET;
            if (o instanceof java.sql.NClob) return TEXT;
            if (o instanceof java.sql.SQLXML) return TEXT;
            if (o instanceof java.sql.SQLData) return SET;
            if (o instanceof java.sql.SQLInput) return SET;
            if (o instanceof java.sql.SQLOutput) return SET;
            if (o instanceof java.sql.NClob) return TEXT;
            if (o instanceof java.sql.SQLXML) return TEXT;
            if (o instanceof java.sql.SQLData) return SET;

            return null;
        }
    }

    @Getter
    final String tableFullName;
    @Getter
    final String tableBaseName;
    @Getter
    final String tablePrefix;

    @Getter @Setter
    ConcurrentSkipListSet<SQLColumn> columns = new ConcurrentSkipListSet<>();

    public SQLSchematic(String tableBaseName, String tablePrefix) {
        this.tableBaseName = tableBaseName;
        this.tablePrefix = tablePrefix;
        this.tableFullName = tablePrefix + tableBaseName;
    }

    public SQLSchematic(String tableBaseName) {
        this(tableBaseName, "");
    }

    public void addColumn(String columnName, SQLType type, long max, int index) {
        this.columns.add(new SQLColumn(columnName, type, max, index));
    }

    public void addColumn(String columnName, SQLType type, int index) {
        this.columns.add(new SQLColumn(columnName, type, index));
    }

    public void addColumn(SQLColumn column) {
        this.columns.add(column);
    }

    public void addColumn(String columnName, int index) {
        this.addColumn(columnName, SQLType.VARCHAR, index);
    }

    public void removeColumn(String columnName) {
        columns.forEach(column -> {
            if (column.getType().toString().equals(columnName)) {
                columns.remove(column);
            }
        });
    }

    public SQLColumn getColumn(String columnName) {
        AtomicReference<SQLColumn> atomicReference = new AtomicReference<>(null);
        columns.forEach(column -> {
            if (column.getType().toString().equals(columnName)) {
                atomicReference.set(column);
            }
        });
        return atomicReference.get();
    }

    public int getColumnIndex(String columnName) {
        int i = 0;
        for (SQLColumn column : columns) {
            if (column.getName().equals(columnName)) return i;
            i++;
        }
        return -1;
    }

    public int getColumnIndex(SQLColumn column) {
        int i = 0;
        for (SQLColumn column1 : columns) {
            if (column1.equals(column)) return i;
            i++;
        }
        return -1;
    }

    public int getColumnCount() {
        return columns.size();
    }

    public boolean hasColumn(String columnName) {
        return getColumnIndex(columnName) != -1;
    }

    public boolean hasColumn(SQLColumn column) {
        return getColumnIndex(column) != -1;
    }

    public boolean hasColumn(int columnIndex) {
        return columnIndex >= 0 && columnIndex < getColumnCount();
    }

    public boolean hasColumns(String... columnNames) {
        for (String columnName : columnNames) {
            if (!hasColumn(columnName)) return false;
        }
        return true;
    }

    public boolean hasColumns(SQLColumn... columns) {
        for (SQLColumn column : columns) {
            if (!hasColumn(column)) return false;
        }
        return true;
    }

    public boolean hasColumns(int... columnIndices) {
        for (int columnIndex : columnIndices) {
            if (!hasColumn(columnIndex)) return false;
        }
        return true;
    }

    public String getCreateTableQuery() {
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableFullName + " (");

        columns.forEach(column -> {
            query.append(column.getName()).append(" ").append(column.getType().name());
            if (column.getType().getMax() != column.getType().getMax()) {
                query.append("(").append(column.getMax()).append(")");
            }
            query.append(", ");
        });

        query.delete(query.length() - 2, query.length());
        query.append(");");
        return query.toString();
    }

    /**
     * A {@link ConcurrentSkipListMap} of columns defined by their column name.
     */
    public ConcurrentSkipListMap<String, SQLColumn> getColumnsByName() {
        ConcurrentSkipListMap<String, SQLColumn> map = new ConcurrentSkipListMap<>();
        columns.forEach(column -> map.put(column.getName(), column));
        return map;
    }

    /**
     * A {@link ConcurrentSkipListMap} of columns defined by their column index.
     */
    public ConcurrentSkipListMap<Integer, SQLColumn> getColumnsByIndex() {
        ConcurrentSkipListMap<Integer, SQLColumn> map = new ConcurrentSkipListMap<>();
        columns.forEach(column -> map.put(column.getIndex(), column));
        return map;
    }
}
