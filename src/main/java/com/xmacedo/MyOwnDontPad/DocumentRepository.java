package com.xmacedo.MyOwnDontPad;

public class DocumentRepository {
    private  Database database = new Database();

    public void save(Document document) {
        //todo check if exists
        database.getDatabase().put(document.getUuid(), document);
    }

    public Document getDocumentById(String idDocument) {
        return database.getDatabase().get(idDocument);
    }
}
