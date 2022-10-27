package tv.quaint.storage.resources.databases.processing.mongo;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.resources.databases.processing.DBSchematic;
import tv.quaint.storage.resources.databases.processing.interfacing.DataLikeType;
import tv.quaint.storage.resources.databases.processing.mongo.data.MongoColumn;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLColumn;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class MongoSchematic implements DBSchematic<MongoColumn> {
    public enum MongoType implements DataLikeType {
        STRING,
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        BOOLEAN,
        DATE,
        OBJECT,
        ARRAY,
        BINARY,
        NULL,
        UNDEFINED,
        REGEX,
        DB_POINTER,
        JAVASCRIPT,
        SYMBOL,
        JAVASCRIPT_WITH_SCOPE,
        TIMESTAMP,
        MIN_KEY,
        MAX_KEY,
        ;

        public static MongoType fromObject(Object o) {
            if (o instanceof String) return STRING;
            if (o instanceof Integer) return INT;
            if (o instanceof Long) return LONG;
            if (o instanceof Float) return FLOAT;
            if (o instanceof Double) return DOUBLE;
            if (o instanceof Boolean) return BOOLEAN;
            if (o instanceof java.util.Date) return DATE;
            if (o instanceof org.bson.Document) return OBJECT;
            if (o instanceof org.bson.types.Binary) return BINARY;
            if (o instanceof org.bson.types.ObjectId) return DB_POINTER;
            if (o instanceof org.bson.types.Symbol) return SYMBOL;
            if (o instanceof org.bson.types.BSONTimestamp) return TIMESTAMP;
            if (o instanceof org.bson.types.MinKey) return MIN_KEY;
            if (o instanceof org.bson.types.MaxKey) return MAX_KEY;
            if (o instanceof org.bson.types.Code) return JAVASCRIPT;
            if (o instanceof org.bson.types.CodeWithScope) return JAVASCRIPT_WITH_SCOPE;

            return null;
        }
    }

    @Getter
    private final String tableFullName;
    @Getter
    private final String tableBaseName;
    @Getter
    private final String tablePrefix;

    @Getter @Setter
    ConcurrentSkipListSet<MongoColumn> columns = new ConcurrentSkipListSet<>();

    public MongoSchematic(String tableFullName, String tableBaseName, String tablePrefix) {
        this.tableFullName = tableFullName;
        this.tableBaseName = tableBaseName;
        this.tablePrefix = tablePrefix;
    }

    public MongoSchematic(String tableFullName) {
        this(tableFullName, "");
    }

    public MongoSchematic(String tableBaseName, String tablePrefix) {
        this(tablePrefix + tableBaseName, tableBaseName, tablePrefix);
    }

    public void addColumn(MongoColumn column) {
        columns.add(column);
    }

    public void addColumn(String columnName, MongoType columnType) {
        addColumn(new MongoColumn(columnName, columnType));
    }

    public void removeColumn(MongoColumn column) {
        columns.remove(column);
    }

    public void removeColumn(String columnName) {
        columns.removeIf(column -> column.getName().equals(columnName));
    }

    public MongoColumn getColumn(String columnName) {
        for (MongoColumn column : columns) {
            if (column.getName().equals(columnName)) return column;
        }
        return null;
    }

    public MongoColumn getColumn(int columnIndex) {
        int i = 0;
        for (MongoColumn column : columns) {
            if (i == columnIndex) return column;
            i++;
        }
        return null;
    }

    public int getColumnIndex(String columnName) {
        int i = 0;
        for (MongoColumn column : columns) {
            if (column.getName().equals(columnName)) return i;
            i++;
        }
        return -1;
    }

    public int getColumnIndex(MongoColumn column) {
        int i = 0;
        for (MongoColumn column1 : columns) {
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

    public boolean hasColumn(MongoColumn column) {
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

    public boolean hasColumns(MongoColumn... columns) {
        for (MongoColumn column : columns) {
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

    public ConcurrentSkipListMap<String, MongoColumn> getColumnsByName() {
        ConcurrentSkipListMap<String, MongoColumn> map = new ConcurrentSkipListMap<>();
        for (MongoColumn column : columns) {
            map.put(column.getName(), column);
        }
        return map;
    }

    public ConcurrentSkipListMap<Integer, MongoColumn> getColumnsByIndex() {
        ConcurrentSkipListMap<Integer, MongoColumn> map = new ConcurrentSkipListMap<>();
        int i = 0;
        for (MongoColumn column : columns) {
            map.put(i, column);
            i++;
        }
        return map;
    }
}
