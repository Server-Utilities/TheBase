package tv.quaint.storage.resources.databases.processing.mongo.data;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import tv.quaint.storage.resources.databases.processing.interfacing.DBColumn;
import tv.quaint.storage.resources.databases.processing.mongo.MongoSchematic;

public class MongoColumn implements Comparable<MongoColumn>, DBColumn {

    @Getter
    private final String name;
    @Getter
    private final MongoSchematic.MongoType type;

    public MongoColumn(String name, MongoSchematic.MongoType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "MongoColumn{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public int compareTo(@NotNull MongoColumn o) {
        return CharSequence.compare(getName(), o.getName());
    }
}
