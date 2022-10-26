package tv.quaint.storage.resources.databases.processing.mongo.data;

import tv.quaint.storage.resources.databases.processing.CachedDBColumnData;

public class MongoCachedColumnData<T, D extends MongoDataLike<T>> extends CachedDBColumnData<T, D> {
    public MongoCachedColumnData(MongoColumn column, D data) {
        super(column, data);
    }
}
