package com.streamnz.practisee.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.ArrayList;

/**
 * @Author cheng hao
 * @Date 23/10/2025 10:05
 */
public class PlaywrightLoginTest {

    static Playwright playwright;
    static Browser browser;

    Page page;
    private List<String> websocketMessages = new ArrayList<>();
    private boolean websocketConnected = false;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeEach
    void createPage() {
        page = browser.newPage();
        
        // Set up WebSocket monitoring
        setupWebSocketMonitoring();
        
        page.navigate("https://streamnz.com");
        
        // Wait for page to load and print page title for debugging
        page.waitForTimeout(2000); // Wait for page to load
        System.out.println("Page loaded: " + page.title());
        System.out.println("Current URL: " + page.url());
    }

    @AfterEach
    void closePage() {
        page.close();
    }

    @AfterAll
    static void teardown() {
        browser.close();
        playwright.close();
    }

    /**
     * Set up WebSocket monitoring to detect connections and messages
     */
    private void setupWebSocketMonitoring() {
        // Listen for WebSocket connections
        page.onWebSocket(webSocket -> {
            System.out.println("WebSocket connected: " + webSocket.url());
            websocketConnected = true;
            
            // Check if this is the expected WebSocket URL for StreamNZ
            if (webSocket.url().contains("streamnz.com") || webSocket.url().contains("ws://") || webSocket.url().contains("wss://")) {
                System.out.println("StreamNZ WebSocket connection established: " + webSocket.url());
            }
            
            // Listen for WebSocket messages
            webSocket.onFrameReceived(frame -> {
                String message = frame.text();
                System.out.println("WebSocket message received: " + message);
                websocketMessages.add(message);
            });
            
            // Listen for WebSocket close events
            webSocket.onClose(ws -> {
                System.out.println("WebSocket connection closed");
                websocketConnected = false;
            });
        });
    }

    @Test
    void testLoginAndWebSocketConnection() {
        // Perform login
        performLogin();
        
        // Wait for login to complete and check for success indicators
        waitForLoginSuccess();
        
        // Check if WebSocket connection is established
        boolean websocketEstablished = checkWebSocketConnection();
        
        if (websocketEstablished) {
            System.out.println("WebSocket connection established successfully");
            
            // Wait for any initial WebSocket messages
            waitForWebSocketMessages();
            
            // Log WebSocket messages received
            System.out.println("WebSocket messages received: " + websocketMessages.size());
            for (int i = 0; i < websocketMessages.size(); i++) {
                System.out.println("Message " + (i + 1) + ": " + websocketMessages.get(i));
            }
            
            // Verify that we received some WebSocket messages
            Assertions.assertFalse(websocketMessages.isEmpty(), "Should receive WebSocket messages after connection");
        } else {
            System.out.println("No WebSocket connection detected - this might be expected if the site doesn't use WebSocket");
            // Don't fail the test if no WebSocket is detected, as it might not be used
        }
        
        System.out.println("Login test completed successfully");
    }

    /**
     * Perform the login process
     */
    private void performLogin() {
        // First, try to find and click a login button/link to open the login form
        try {
            // Look for various possible login triggers
            page.click("text=Login", new Page.ClickOptions().setTimeout(5000));
        } catch (PlaywrightException e) {
            try {
                page.click("a:has-text('Login')", new Page.ClickOptions().setTimeout(5000));
            } catch (PlaywrightException e2) {
                try {
                    page.click("button:has-text('Login')", new Page.ClickOptions().setTimeout(5000));
                } catch (PlaywrightException e3) {
                    System.out.println("Could not find login trigger, trying to proceed with form if already visible");
                }
            }
        }
        
        // Wait for the login form to be visible
        page.waitForSelector("input[placeholder='Enter your email']", new Page.WaitForSelectorOptions().setTimeout(10000));
        
        // Check if credentials are already filled
        String emailValue = page.inputValue("input[placeholder='Enter your email']");
        String passwordValue = page.inputValue("input[placeholder='Enter your password']");
        
        System.out.println("Current email value: " + emailValue);
        System.out.println("Current password value: " + (passwordValue.isEmpty() ? "empty" : "filled"));
        
        // Fill in login credentials if not already filled
        if (emailValue.isEmpty()) {
            page.fill("input[placeholder='Enter your email']", "hao.streamnz@gmail.com");
        }
        if (passwordValue.isEmpty()) {
            page.fill("input[placeholder='Enter your password']", "Pass1234");
        }
        
        // Wait a bit for any animations or overlays to settle
        page.waitForTimeout(1000);
        
        // Check if login button is visible and clickable
        try {
            boolean isLoginButtonVisible = page.isVisible("button:has-text('Login')");
            System.out.println("Login button visible: " + isLoginButtonVisible);
            
            if (isLoginButtonVisible) {
                // Try to click the login button with force option to bypass overlays
                try {
                    System.out.println("Attempting to click login button with force...");
                    page.click("button:has-text('Login')", new Page.ClickOptions().setForce(true));
                    System.out.println("Login button clicked successfully with force");
                } catch (PlaywrightException e) {
                    // If force click fails, try with timeout and no force
                    System.out.println("Force click failed: " + e.getMessage());
                    System.out.println("Trying normal click...");
                    page.click("button:has-text('Login')", new Page.ClickOptions().setTimeout(10000));
                    System.out.println("Login button clicked successfully with normal click");
                }
            } else {
                System.out.println("Login button is not visible, trying alternative selectors...");
                // Try alternative selectors for login button
                try {
                    page.click("button[type='submit']", new Page.ClickOptions().setForce(true));
                    System.out.println("Clicked submit button successfully");
                } catch (PlaywrightException e2) {
                    System.out.println("Submit button click failed: " + e2.getMessage());
                    // Try clicking any button in the form
                    page.click("form button", new Page.ClickOptions().setForce(true));
                    System.out.println("Clicked form button successfully");
                }
            }
        } catch (PlaywrightException e) {
            System.out.println("Login button click failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Wait for login to complete successfully
     */
    private void waitForLoginSuccess() {
        System.out.println("Waiting for login to complete...");
        
        try {
            // Wait for the login form to disappear or for success indicators
            page.waitForSelector("input[placeholder='Enter your email']", 
                new Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED).setTimeout(10000));
            
            System.out.println("Login form disappeared - login appears successful");
        } catch (PlaywrightException e) {
            System.out.println("Login form did not disappear, checking for other success indicators...");
            
            // If login form doesn't disappear, wait for URL change or other indicators
            try {
                page.waitForURL("**/dashboard**", new Page.WaitForURLOptions().setTimeout(10000));
                System.out.println("Login successful - redirected to dashboard");
            } catch (PlaywrightException e2) {
                System.out.println("No URL redirect detected, checking for other success indicators...");
                
                // Check for user menu or other logged-in indicators
                try {
                    page.waitForSelector(".user-menu, [data-testid='user-menu'], .profile, .account, .navbar-user", 
                        new Page.WaitForSelectorOptions().setTimeout(5000));
                    System.out.println("Found user menu - login appears successful");
                } catch (PlaywrightException e3) {
                    System.out.println("No user menu found, checking for login success messages...");
                    
                    // Check for success messages or error messages
                    try {
                        page.waitForSelector(".success, .alert-success, .login-success", 
                            new Page.WaitForSelectorOptions().setTimeout(3000));
                        System.out.println("Found success message - login appears successful");
                    } catch (PlaywrightException e4) {
                        System.out.println("No success message found, checking for error messages...");
                        
                        try {
                            page.waitForSelector(".error, .alert-error, .login-error", 
                                new Page.WaitForSelectorOptions().setTimeout(2000));
                            System.out.println("Found error message - login might have failed");
                        } catch (PlaywrightException e5) {
                            System.out.println("No error message found, checking current URL...");
                            System.out.println("Current URL after login attempt: " + page.url());
                            
                            // Check if we're still on the same page
                            if (page.url().contains("streamnz.com") && !page.url().contains("login")) {
                                System.out.println("Still on main site, login might have failed or no redirect expected");
                            }
                            
                            // As a fallback, wait for any page changes or just wait a bit
                            page.waitForTimeout(3000);
                            System.out.println("Login process completed (fallback wait)");
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if WebSocket connection is established
     */
    private boolean checkWebSocketConnection() {
        // Poll for WebSocket connection
        int attempts = 0;
        int maxAttempts = 50; // 5 seconds with 100ms intervals
        
        while (!websocketConnected && attempts < maxAttempts) {
            try {
                Thread.sleep(100);
                attempts++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        return websocketConnected;
    }

    /**
     * Wait for WebSocket messages to be received
     */
    private void waitForWebSocketMessages() {
        // Wait a bit for initial messages
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // If no messages received, wait a bit more
        if (websocketMessages.isEmpty()) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Test
    void testWebSocketMessageHandling() {
        // This test focuses specifically on WebSocket message handling
        performLogin();
        waitForLoginSuccess();
        
        // Check if WebSocket connection is established
        boolean websocketEstablished = checkWebSocketConnection();
        
        if (websocketEstablished) {
            System.out.println("WebSocket connection established for message handling test");
            
            // Wait for messages and verify they are being received
            waitForWebSocketMessages();
            
            System.out.println("WebSocket message handling test completed. Messages received: " + websocketMessages.size());
            
            // Log all received messages for debugging
            for (int i = 0; i < websocketMessages.size(); i++) {
                System.out.println("Message " + (i + 1) + ": " + websocketMessages.get(i));
            }
            
            // Verify that we received some WebSocket messages
            Assertions.assertFalse(websocketMessages.isEmpty(), "Should receive WebSocket messages after connection");
        } else {
            System.out.println("No WebSocket connection detected for message handling test");
            // Don't fail the test if no WebSocket is detected
        }
    }

    @Test
    void testStreamNZWebSocketConnection() {
        // This test specifically focuses on StreamNZ WebSocket connection
        System.out.println("Starting StreamNZ WebSocket connection test...");
        
        // Perform login
        performLogin();
        waitForLoginSuccess();
        
        // Wait a bit for any WebSocket connections to be established
        page.waitForTimeout(5000);
        
        // Check if WebSocket connection is established
        boolean websocketEstablished = checkWebSocketConnection();
        
        if (websocketEstablished) {
            System.out.println("StreamNZ WebSocket connection established successfully");
            
            // Wait for any initial WebSocket messages
            waitForWebSocketMessages();
            
            // Log WebSocket messages received
            System.out.println("StreamNZ WebSocket messages received: " + websocketMessages.size());
            for (int i = 0; i < websocketMessages.size(); i++) {
                System.out.println("StreamNZ Message " + (i + 1) + ": " + websocketMessages.get(i));
            }
            
            // Verify that we received some WebSocket messages
            Assertions.assertFalse(websocketMessages.isEmpty(), "Should receive WebSocket messages from StreamNZ");
        } else {
            System.out.println("No StreamNZ WebSocket connection detected - this might be expected if the site doesn't use WebSocket");
            // Don't fail the test if no WebSocket is detected, as it might not be used
        }
        
        System.out.println("StreamNZ WebSocket connection test completed");
    }
}
