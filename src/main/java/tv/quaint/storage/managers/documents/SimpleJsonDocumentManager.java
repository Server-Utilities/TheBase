package tv.quaint.storage.managers.documents;

import tv.quaint.storage.documents.SimpleJsonDocument;

import java.io.File;

public abstract class SimpleJsonDocumentManager extends SimpleDocumentManager<SimpleJsonDocument> {
    public SimpleJsonDocumentManager(File parentDirectory) {
        super(SimpleJsonDocument.class, parentDirectory);
    }
}
