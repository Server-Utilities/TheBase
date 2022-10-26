package tv.quaint.storage.resources.databases.processing.mongo.data;

import lombok.Getter;
import org.bson.Document;
import tv.quaint.storage.resources.databases.processing.interfacing.DBRow;

import java.util.Date;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents a row of data ({@link MongoColumn}s) in an SQL table.
 */
public class MongoRow implements Comparable<MongoRow>, DBRow<MongoColumn, MongoDataLike<?>> {
    @Getter
    private final Date accessed;
    @Getter
    private final ConcurrentSkipListMap<MongoColumn, MongoDataLike<?>> map = new ConcurrentSkipListMap<>();

    public MongoRow(MongoColumn[] columns, MongoDataLike<?>[] data) {
        accessed = new Date();
        for (int i = 0; i < columns.length; i++) {
            map.put(columns[i], data[i]);
        }
    }

    public MongoRow(ConcurrentSkipListMap<MongoColumn, MongoDataLike<?>> map) {
        accessed = new Date();
        this.map.putAll(map);
    }

    public Document asDocument() {
        Document document = new Document();
        getMap().forEach((column, data) -> document.append(column.getName(), data.getData()));
        return document;
    }

    @Override
    public MongoDataLike<?> getValue(MongoColumn column) {
        return getMap().get(column);
    }

    @Override
    public MongoDataLike<?> getValue(String columnName) {
        return getValue(getColumn(columnName));
    }

    @Override
    public MongoColumn getColumn(String columnName) {
        AtomicReference<MongoColumn> atomicReference = new AtomicReference<>();
        getMap().keySet().forEach(column -> {
            if (column.getName().equals(columnName)) {
                atomicReference.set(column);
            }
        });
        return atomicReference.get();
    }

    @Override
    public MongoColumn getColumn(int index) {
        return getColumns()[index];
    }

    @Override
    public MongoDataLike<?> getValue(int index) {
        return getValues()[index];
    }

    @Override
    public int size() {
        return getMap().size();
    }

    @Override
    public MongoColumn[] getColumns() {
        return getMap().keySet().toArray(new MongoColumn[0]);
    }

    @Override
    public MongoDataLike<?>[] getValues() {
        return getMap().values().toArray(new MongoDataLike<?>[0]);
    }

    @Override
    public String toString() {
        return "SQLRow{" +
                "map=" + map +
                '}';
    }

    @Override
    public int compareTo(MongoRow o) {
        return Long.compare(getAccessed().getTime(), o.getAccessed().getTime());
    }
}