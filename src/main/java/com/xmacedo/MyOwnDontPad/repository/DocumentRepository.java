package com.xmacedo.MyOwnDontPad.repository;

import com.xmacedo.MyOwnDontPad.model.Database;
import com.xmacedo.MyOwnDontPad.model.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentRepository {
    private Database database = new Database();

    public void save(Document document) {
        //todo check if exists
        if (getDocumentById(document.getUuid()) == null) {
            database.getDatabase().put(document.getUuid(), document);
        } else {
            getDocumentById(document.getUuid()).setText(document.getText());
        }
    }

    public Document getDocumentById(String idDocument) {
        return database.getDatabase().get(idDocument);
    }
}
