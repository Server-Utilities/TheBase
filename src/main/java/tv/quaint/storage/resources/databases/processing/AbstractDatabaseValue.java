package tv.quaint.storage.resources.databases.processing;

import lombok.Getter;
import lombok.Setter;

public class AbstractDatabaseValue<V> implements IDatabaseValue<V> {
    @Getter
    private final String key;
    @Getter @Setter
    private V value;

    public AbstractDatabaseValue(String key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int compareTo(IDatabaseValue o) {
        return getKey().compareTo(o.getKey());
    }
}
