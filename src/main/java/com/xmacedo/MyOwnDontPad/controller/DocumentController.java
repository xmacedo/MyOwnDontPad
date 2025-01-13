package com.xmacedo.MyOwnDontPad.controller;

import com.xmacedo.MyOwnDontPad.model.Document;
import com.xmacedo.MyOwnDontPad.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Document send(Document message) {
        System.out.println("received message: "+message);
        return documentService.saveText(message);
    }

}
