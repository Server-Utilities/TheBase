package tv.quaint.storage.resources.databases.processing.mongo.data;

import lombok.Getter;
import tv.quaint.storage.resources.databases.processing.mongo.MongoSchematic;

public abstract class AbstractMongoData<T> implements MongoDataLike<T> {
    @Getter
    private final MongoSchematic.MongoType type;

    public AbstractMongoData(MongoSchematic.MongoType type) {
        this.type = type;
    }
}
