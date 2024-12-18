package com.xmacedo.MyOwnDontPad.service;

import com.xmacedo.MyOwnDontPad.model.Document;
import com.xmacedo.MyOwnDontPad.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    public Document saveText(Document document) {
        documentRepository.save(document);
        return document;
    }
}
