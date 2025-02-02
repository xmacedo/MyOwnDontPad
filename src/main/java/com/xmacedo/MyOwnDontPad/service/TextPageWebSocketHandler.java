package com.xmacedo.MyOwnDontPad.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class TextPageWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, String> pages = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, String> sessionPageMap = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, ScheduledFuture<?>> tasksToUpdate = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionPageMap.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, String> payload = mapper.readValue(message.getPayload(), new TypeReference<>() {
        });

        String pageName = payload.get("pageName");
        String content = payload.get("content");

        if (pageName == null) {
            return;
        }
        sessionPageMap.put(session, pageName);

        if (content != null) {
            // Save or update the page content
            pages.put(pageName, content);
            waitUntilUpdate(pageName, content);
        } else {
            // Retrieve the page content
            String retrievedContent = pages.getOrDefault(pageName, "");
            session.sendMessage(new TextMessage(mapper.writeValueAsString(Map.of("content", retrievedContent))));
        }
    }

    private void waitUntilUpdate(String pageName, String content) {
        ScheduledFuture<?> existingTask = tasksToUpdate.get(pageName);
        if (existingTask != null && !existingTask.isDone()) {
            existingTask.cancel(false); // Cancela a tarefa anterior se ainda não foi executada
        }

        ScheduledFuture<?> newTask = scheduler.schedule(() -> {
            broadcastUpdate(pageName, content);
        }, 500, TimeUnit.MILLISECONDS); // Delay de 500ms

        tasksToUpdate.put(pageName, newTask);
    }

    private void broadcastUpdate(String pageName, String content) {

        String updateMessage;
        try {
            updateMessage = mapper.writeValueAsString(Map.of("content", content));
        } catch (Exception e) {
            throw new RuntimeException("Error serializing update message", e);
        }

        for (Map.Entry<WebSocketSession, String> entry : sessionPageMap.entrySet()) {
            WebSocketSession session = entry.getKey();
            String sessionPage = entry.getValue();

            if (session.isOpen() && sessionPage.equals(pageName)) {
                try {
                    session.sendMessage(new TextMessage(updateMessage));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
