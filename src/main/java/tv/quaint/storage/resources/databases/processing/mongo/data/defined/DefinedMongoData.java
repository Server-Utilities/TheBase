package tv.quaint.storage.resources.databases.processing.mongo.data.defined;

import org.bson.Document;
import org.bson.types.Binary;
import tv.quaint.storage.resources.databases.processing.mongo.MongoSchematic;
import tv.quaint.storage.resources.databases.processing.mongo.data.AbstractMongoData;
import tv.quaint.storage.resources.databases.processing.mongo.data.MongoDataLike;

import java.util.Date;
import java.util.Set;

/**
 * A class that contains more classes that define specific {@link MongoDataLike} data types that are specified by {@link MongoSchematic.MongoType} values.
 */
public class DefinedMongoData {
    /**
     * A function that returns an {@link AbstractMongoData} object that is defined by the {@link MongoSchematic.MongoType} value.
     * It uses classes defined in this parent class and instantiates them with the given value.
     * @param type The {@link MongoSchematic.MongoType} value that defines the type of data to return.
     * @param value The value of the data to return.
     * @return A {@link AbstractMongoData} object that is defined by the {@link MongoSchematic.MongoType} value.
     */
    public static AbstractMongoData<?> getFromType(MongoSchematic.MongoType type, Object value) {
        switch (type) {
            case STRING:
                return new MongoStringData((String) value);
            case INT:
                return new MongoIntData((Integer) value);
            case LONG:
                return new MongoLongData((Long) value);
            case FLOAT:
                return new MongoFloatData((Float) value);
            case DOUBLE:
                return new MongoDoubleData((Double) value);
            case BOOLEAN:
                return new MongoBooleanData((Boolean) value);
            case DATE:
                return new MongoDateData((Date) value);
            case OBJECT:
                return new MongoObjectData(value);
            case ARRAY:
                return new MongoArrayData((Document) value);
            case BINARY:
                return new MongoBinaryData((Binary) value);
            case NULL:
                return new MongoNullData();
        }

        return null;
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#STRING}.
     */
    public static class MongoStringData extends AbstractMongoData<String> {
        private final String data;

        public MongoStringData(String data) {
            super(MongoSchematic.MongoType.STRING);
            this.data = data;
        }

        @Override
        public String getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#INT}.
     */
    public static class MongoIntData extends AbstractMongoData<Integer> {
        private final int data;

        public MongoIntData(int data) {
            super(MongoSchematic.MongoType.INT);
            this.data = data;
        }

        @Override
        public Integer getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#LONG}.
     */
    public static class MongoLongData extends AbstractMongoData<Long> {
        private final long data;

        public MongoLongData(long data) {
            super(MongoSchematic.MongoType.LONG);
            this.data = data;
        }

        @Override
        public Long getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#FLOAT}.
     */
    public static class MongoFloatData extends AbstractMongoData<Float> {
        private final float data;

        public MongoFloatData(float data) {
            super(MongoSchematic.MongoType.FLOAT);
            this.data = data;
        }

        @Override
        public Float getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#DOUBLE}.
     */
    public static class MongoDoubleData extends AbstractMongoData<Double> {
        private final double data;

        public MongoDoubleData(double data) {
            super(MongoSchematic.MongoType.DOUBLE);
            this.data = data;
        }

        @Override
        public Double getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#BOOLEAN}.
     */
    public static class MongoBooleanData extends AbstractMongoData<Boolean> {
        private final boolean data;

        public MongoBooleanData(boolean data) {
            super(MongoSchematic.MongoType.BOOLEAN);
            this.data = data;
        }

        @Override
        public Boolean getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#DATE}.
     */
    public static class MongoDateData extends AbstractMongoData<Date> {
        private final Date data;

        public MongoDateData(Date data) {
            super(MongoSchematic.MongoType.DATE);
            this.data = data;
        }

        @Override
        public Date getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#OBJECT}.
     */
    public static class MongoObjectData extends AbstractMongoData<Object> {
        private final Object data;

        public MongoObjectData(Object data) {
            super(MongoSchematic.MongoType.OBJECT);
            this.data = data;
        }

        @Override
        public Object getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#ARRAY}.
     */
    public static class MongoArrayData extends AbstractMongoData<Document> {
        private final Document data;

        public MongoArrayData(Document data) {
            super(MongoSchematic.MongoType.ARRAY);
            this.data = data;
        }

        @Override
        public Document getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#BINARY}.
     */
    public static class MongoBinaryData extends AbstractMongoData<Binary> {
        private final Binary data;

        public MongoBinaryData(Binary data) {
            super(MongoSchematic.MongoType.BINARY);
            this.data = data;
        }

        @Override
        public Binary getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#NULL}.
     */
    public static class MongoNullData extends AbstractMongoData<Void> {
        public MongoNullData() {
            super(MongoSchematic.MongoType.NULL);
        }

        @Override
        public Void getData() {
            return null;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#UNDEFINED}.
     */
    public static class MongoUndefinedData extends AbstractMongoData<Void> {
        public MongoUndefinedData() {
            super(MongoSchematic.MongoType.UNDEFINED);
        }

        @Override
        public Void getData() {
            return null;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#REGEX}.
     */
    public static class MongoRegexData extends AbstractMongoData<String> {
        private final String data;

        public MongoRegexData(String data) {
            super(MongoSchematic.MongoType.REGEX);
            this.data = data;
        }

        @Override
        public String getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#DB_POINTER}.
     */
    public static class MongoDbPointerData extends AbstractMongoData<String> {
        private final String data;

        public MongoDbPointerData(String data) {
            super(MongoSchematic.MongoType.DB_POINTER);
            this.data = data;
        }

        @Override
        public String getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#JAVASCRIPT}.
     */
    public static class MongoJavaScriptData extends AbstractMongoData<String> {
        private final String data;

        public MongoJavaScriptData(String data) {
            super(MongoSchematic.MongoType.JAVASCRIPT);
            this.data = data;
        }

        @Override
        public String getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#JAVASCRIPT_WITH_SCOPE}.
     */
    public static class MongoJavaScriptWithScopeData extends AbstractMongoData<String> {
        private final String data;

        public MongoJavaScriptWithScopeData(String data) {
            super(MongoSchematic.MongoType.JAVASCRIPT_WITH_SCOPE);
            this.data = data;
        }

        @Override
        public String getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#SYMBOL}.
     */
    public static class MongoSymbolData extends AbstractMongoData<String> {
        private final String data;

        public MongoSymbolData(String data) {
            super(MongoSchematic.MongoType.SYMBOL);
            this.data = data;
        }

        @Override
        public String getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#TIMESTAMP}.
     */
    public static class MongoTimestampData extends AbstractMongoData<Long> {
        private final long data;

        public MongoTimestampData(long data) {
            super(MongoSchematic.MongoType.TIMESTAMP);
            this.data = data;
        }

        @Override
        public Long getData() {
            return data;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#MIN_KEY}.
     */
    public static class MongoMinKeyData extends AbstractMongoData<Void> {
        public MongoMinKeyData() {
            super(MongoSchematic.MongoType.MIN_KEY);
        }

        @Override
        public Void getData() {
            return null;
        }
    }

    /**
     * A class that defines a {@link MongoDataLike} data type that is a {@link MongoSchematic.MongoType#MAX_KEY}.
     */
    public static class MongoMaxKeyData extends AbstractMongoData<Void> {
        public MongoMaxKeyData() {
            super(MongoSchematic.MongoType.MAX_KEY);
        }

        @Override
        public Void getData() {
            return null;
        }
    }
}
