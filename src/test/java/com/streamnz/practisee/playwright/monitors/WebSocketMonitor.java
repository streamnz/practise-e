package com.streamnz.practisee.playwright.monitors;

import com.microsoft.playwright.Page;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * WebSocket monitoring utility for Playwright tests
 * Demonstrates proper separation of concerns and thread safety
 */
public class WebSocketMonitor {
    
    private final List<String> messages = new ArrayList<>();
    private volatile boolean connected = false;
    private final CompletableFuture<Void> connectionFuture = new CompletableFuture<>();
    
    /**
     * Set up WebSocket monitoring on the page
     */
    public void setupMonitoring(Page page) {
        page.onWebSocket(webSocket -> {
            System.out.println("[WebSocket] Connected: " + webSocket.url());
            connected = true;
            connectionFuture.complete(null);
            
            // Monitor messages
            webSocket.onFrameReceived(frame -> {
                String message = frame.text();
                System.out.println("[WebSocket] Message received: " + message);
                synchronized (messages) {
                    messages.add(message);
                }
            });
            
            // Handle close events
            webSocket.onClose(ws -> {
                System.out.println("[WebSocket] Connection closed");
                connected = false;
            });
        });
    }
    
    /**
     * Wait for WebSocket connection with timeout
     */
    public boolean waitForConnection(int timeoutMs) {
        try {
            connectionFuture.get(timeoutMs, TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception e) {
            System.out.println("[WebSocket] Connection timeout: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if WebSocket is connected
     */
    public boolean isConnected() {
        return connected;
    }
    
    /**
     * Get all received messages
     */
    public List<String> getMessages() {
        synchronized (messages) {
            return new ArrayList<>(messages);
        }
    }
    
    /**
     * Get message count
     */
    public int getMessageCount() {
        synchronized (messages) {
            return messages.size();
        }
    }
    
    /**
     * Clear all messages
     */
    public void clearMessages() {
        synchronized (messages) {
            messages.clear();
        }
    }
}
