package tv.quaint.storage.resources.databases.processing;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Date;

public class DatabaseValue<V> implements Comparable<DatabaseValue<?>> {
    @Getter @Setter
    String key;
    @Getter @Setter
    V value;

    public DatabaseValue(String key, V value) {
        this.key = key;
        this.value = value;
    }

    public boolean isString() {
        return value instanceof String;
    }

    public boolean isInteger() {
        return value instanceof Integer;
    }

    public boolean isLong() {
        return value instanceof Long;
    }

    public boolean isDouble() {
        return value instanceof Double;
    }

    public boolean isFloat() {
        return value instanceof Float;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public boolean isByte() {
        return value instanceof Byte;
    }

    public boolean isShort() {
        return value instanceof Short;
    }

    public boolean isCharacter() {
        return value instanceof Character;
    }

    public boolean isNumber() {
        return value instanceof Number;
    }

    public boolean isNull() {
        return value == null;
    }

    public boolean isNotNull() {
        return value != null;
    }

    public boolean isDate() {
        return value instanceof Date;
    }

    public boolean isStringArray() {
        return value instanceof String[];
    }

    public boolean isIntegerArray() {
        return value instanceof Integer[];
    }

    public boolean isLongArray() {
        return value instanceof Long[];
    }

    public boolean isDoubleArray() {
        return value instanceof Double[];
    }

    public boolean isFloatArray() {
        return value instanceof Float[];
    }

    public boolean isBooleanArray() {
        return value instanceof Boolean[];
    }

    public boolean isByteArray() {
        return value instanceof Byte[];
    }

    public boolean isShortArray() {
        return value instanceof Short[];
    }

    public boolean isCharacterArray() {
        return value instanceof Character[];
    }

    public boolean isNumberArray() {
        return value instanceof Number[];
    }

    public boolean isDateArray() {
        return value instanceof Date[];
    }

    public boolean isStringCollection() {
        return value instanceof Collection && ((Collection<?>) value).stream().allMatch(o -> o instanceof String);
    }

    public boolean isIntegerCollection() {
        return value instanceof Collection && ((Collection<?>) value).stream().allMatch(o -> o instanceof Integer);
    }

    public boolean isLongCollection() {
        return value instanceof Collection && ((Collection<?>) value).stream().allMatch(o -> o instanceof Long);
    }

    public boolean isDoubleCollection() {
        return value instanceof Collection && ((Collection<?>) value).stream().allMatch(o -> o instanceof Double);
    }

    public boolean isFloatCollection() {
        return value instanceof Collection && ((Collection<?>) value).stream().allMatch(o -> o instanceof Float);
    }

    public boolean isBooleanCollection() {
        return value instanceof Collection && ((Collection<?>) value).stream().allMatch(o -> o instanceof Boolean);
    }

    public boolean isByteCollection() {
        return value instanceof Collection && ((Collection<?>) value).stream().allMatch(o -> o instanceof Byte);
    }

    public boolean isShortCollection() {
        return value instanceof Collection && ((Collection<?>) value).stream().allMatch(o -> o instanceof Short);
    }

    public boolean isCharacterCollection() {
        return value instanceof Collection && ((Collection<?>) value).stream().allMatch(o -> o instanceof Character);
    }

    public boolean isNumberCollection() {
        return value instanceof Collection && ((Collection<?>) value).stream().allMatch(o -> o instanceof Number);
    }

    public boolean isDateCollection() {
        return value instanceof Collection && ((Collection<?>) value).stream().allMatch(o -> o instanceof Date);
    }

    public String getSQLType() {
        if (isString()) {
            return "VARCHAR";
        } else if (isInteger()) {
            return "INTEGER";
        } else if (isLong()) {
            return "BIGINT";
        } else if (isDouble()) {
            return "DOUBLE";
        } else if (isFloat()) {
            return "FLOAT";
        } else if (isBoolean()) {
            return "BOOLEAN";
        } else if (isByte()) {
            return "TINYINT";
        } else if (isShort()) {
            return "SMALLINT";
        } else if (isCharacter()) {
            return "CHAR";
        } else if (isDate()) {
            return "TIMESTAMP";
        } else {
            return "VARCHAR";
        }
    }

    @Override
    public int compareTo(@NotNull DatabaseValue<?> o) {
        return CharSequence.compare(key, o.key);
    }

    public static <O> DatabaseValue<O> of(String key, O value) {
        return new DatabaseValue<>(key, value);
    }
}
