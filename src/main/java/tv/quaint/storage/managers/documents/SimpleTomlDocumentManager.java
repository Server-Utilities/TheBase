package tv.quaint.storage.managers.documents;

import tv.quaint.storage.documents.SimpleTomlDocument;

import java.io.File;

public abstract class SimpleTomlDocumentManager extends SimpleDocumentManager<SimpleTomlDocument> {
    public SimpleTomlDocumentManager(File parentDirectory) {
        super(SimpleTomlDocument.class, parentDirectory);
    }
}
