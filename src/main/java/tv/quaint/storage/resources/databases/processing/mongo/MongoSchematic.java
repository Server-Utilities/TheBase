package tv.quaint.storage.resources.databases.processing.mongo;

import lombok.Getter;
import lombok.Setter;
import tv.quaint.storage.resources.databases.processing.interfacing.DataLikeType;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLColumn;

import java.util.concurrent.ConcurrentSkipListSet;

public class MongoSchematic {
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
    ConcurrentSkipListSet<SQLColumn> columns = new ConcurrentSkipListSet<>();

    public MongoSchematic(String tableFullName, String tableBaseName, String tablePrefix) {
        this.tableFullName = tableFullName;
        this.tableBaseName = tableBaseName;
        this.tablePrefix = tablePrefix;
    }
}
