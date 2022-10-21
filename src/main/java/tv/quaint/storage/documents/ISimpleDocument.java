package tv.quaint.storage.documents;

public interface ISimpleDocument {
    void init();

    void save();

    void delete();

    boolean exists();
}
