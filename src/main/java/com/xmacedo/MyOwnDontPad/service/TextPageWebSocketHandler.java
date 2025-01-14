package com.xmacedo.MyOwnDontPad.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TextPageWebSocketHandler  extends TextWebSocketHandler {
    private final Map<String, String> pages = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> payload = mapper.readValue(message.getPayload(), new TypeReference<>() {});

        String pageName = payload.get("pageName");
        String content = payload.get("content");

        if (content != null) {
            // Save or update the page content
            pages.put(pageName, content);
        } else {
            // Retrieve the page content
            String retrievedContent = pages.getOrDefault(pageName, "");
            session.sendMessage(new TextMessage(mapper.writeValueAsString(Map.of("content", retrievedContent))));
        }
    }
}
