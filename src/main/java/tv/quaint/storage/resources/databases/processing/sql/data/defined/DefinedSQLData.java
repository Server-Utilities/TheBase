package tv.quaint.storage.resources.databases.processing.sql.data.defined;

import tv.quaint.storage.resources.databases.processing.mongo.MongoSchematic;
import tv.quaint.storage.resources.databases.processing.sql.SQLSchematic;
import tv.quaint.storage.resources.databases.processing.sql.data.AbstractSQLData;
import tv.quaint.storage.resources.databases.processing.sql.data.SQLDataLike;

import java.util.Date;
import java.util.Set;

/**
 * A class that contains more classes that define specific {@link SQLDataLike} data types that are specified by {@link SQLSchematic.SQLType} values.
 */
public class DefinedSQLData {
    /**
     * A function that returns an {@link AbstractSQLData} object that is defined by the {@link SQLSchematic.SQLType} value.
     * It uses classes defined in this parent class and instantiates them with the given value.
     * @param type The {@link SQLSchematic.SQLType} value that defines the type of data to return.
     * @param value The value of the data to return.
     * @return A {@link AbstractSQLData} object that is defined by the {@link MongoSchematic.MongoType} value.
     */
    public static AbstractSQLData<?> getFromType(SQLSchematic.SQLType type, Object value) {
        switch (type) {
            case VARCHAR:
                return new SQLVarcharData((String) value);
            case TEXT:
                return new SQLTextData((String) value);
            case INT:
                return new SQLIntData((Integer) value);
            case BIGINT:
                return new SQLBigIntData((Long) value);
            case FLOAT:
                return new SQLFloatData((Float) value);
            case DOUBLE:
                return new SQLDoubleData((Double) value);
            case DATE:
                return new SQLDateData((Date) value);
            case TIME:
                return new SQLTimeData((Date) value);
            case TIMESTAMP:
                return new SQLTimestampData((Date) value);
            case BLOB:
                return new SQLBlobData((byte[]) value);
            case ENUM:
                return new SQLEnumData((String) value);
            case SET:
                return new SQLSetData((Set<String>) value);
        }

        return null;
    }
    /**
     * A class that defines a {@link SQLDataLike} data type that is a {@link SQLSchematic.SQLType#VARCHAR}.
     */
    public static class SQLVarcharData extends AbstractSQLData<String> {
        private final String data;

        public SQLVarcharData(String data) {
            super(SQLSchematic.SQLType.VARCHAR);
            this.data = data;
        }

        @Override
        public String getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link SQLDataLike} data type that is a {@link SQLSchematic.SQLType#TEXT}.
     */
    public static class SQLTextData extends AbstractSQLData<String> {
        private final String data;

        public SQLTextData(String data) {
            super(SQLSchematic.SQLType.TEXT);
            this.data = data;
        }

        @Override
        public String getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link SQLDataLike} data type that is a {@link SQLSchematic.SQLType#INT}.
     */
    public static class SQLIntData extends AbstractSQLData<Integer> {
        private final int data;

        public SQLIntData(int data) {
            super(SQLSchematic.SQLType.INT);
            this.data = data;
        }

        @Override
        public Integer getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link SQLDataLike} data type that is a {@link SQLSchematic.SQLType#BIGINT}.
     */
    public static class SQLBigIntData extends AbstractSQLData<Long> {
        private final long data;

        public SQLBigIntData(long data) {
            super(SQLSchematic.SQLType.BIGINT);
            this.data = data;
        }

        @Override
        public Long getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link SQLDataLike} data type that is a {@link SQLSchematic.SQLType#FLOAT}.
     */
    public static class SQLFloatData extends AbstractSQLData<Float> {
        private final float data;

        public SQLFloatData(float data) {
            super(SQLSchematic.SQLType.DOUBLE);
            this.data = data;
        }

        @Override
        public Float getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link SQLDataLike} data type that is a {@link SQLSchematic.SQLType#DOUBLE}.
     */
    public static class SQLDoubleData extends AbstractSQLData<Double> {
        private final double data;

        public SQLDoubleData(double data) {
            super(SQLSchematic.SQLType.DOUBLE);
            this.data = data;
        }

        @Override
        public Double getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link SQLDataLike} data type that is a {@link SQLSchematic.SQLType#DATE}.
     */
    public static class SQLDateData extends AbstractSQLData<Date> {
        private final Date data;

        public SQLDateData(Date data) {
            super(SQLSchematic.SQLType.DATE);
            this.data = data;
        }

        @Override
        public Date getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link SQLDataLike} data type that is a {@link SQLSchematic.SQLType#TIMESTAMP}.
     */
    public static class SQLTimestampData extends AbstractSQLData<Date> {
        private final Date data;

        public SQLTimestampData(Date data) {
            super(SQLSchematic.SQLType.TIMESTAMP);
            this.data = data;
        }

        @Override
        public Date getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link SQLDataLike} data type that is a {@link SQLSchematic.SQLType#TIME}.
     */
    public static class SQLTimeData extends AbstractSQLData<Date> {
        private final Date data;

        public SQLTimeData(Date data) {
            super(SQLSchematic.SQLType.TIME);
            this.data = data;
        }

        @Override
        public Date getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link SQLDataLike} data type that is a {@link SQLSchematic.SQLType#CHAR}.
     */
    public static class SQLCharData extends AbstractSQLData<Character> {
        private final char data;

        public SQLCharData(char data) {
            super(SQLSchematic.SQLType.CHAR);
            this.data = data;
        }

        @Override
        public Character getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link SQLDataLike} data type that is a {@link SQLSchematic.SQLType#BLOB}.
     */
    public static class SQLBlobData extends AbstractSQLData<byte[]> {
        private final byte[] data;

        public SQLBlobData(byte[] data) {
            super(SQLSchematic.SQLType.BLOB);
            this.data = data;
        }

        @Override
        public byte[] getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link SQLDataLike} data type that is a {@link SQLSchematic.SQLType#ENUM}.
     */
    public static class SQLEnumData extends AbstractSQLData<String> {
        private final String data;

        public SQLEnumData(String data) {
            super(SQLSchematic.SQLType.ENUM);
            this.data = data;
        }

        @Override
        public String getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link SQLDataLike} data type that is a {@link SQLSchematic.SQLType#SET}.
     */
    public static class SQLSetData extends AbstractSQLData<Set<?>> {
        private final Set<?> data;

        public SQLSetData(Set<?> data) {
            super(SQLSchematic.SQLType.SET);
            this.data = data;
        }

        @Override
        public Set<?> getData() {
            return data;
        }
    }
}
