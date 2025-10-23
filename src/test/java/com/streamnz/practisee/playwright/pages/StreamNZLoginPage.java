package com.streamnz.practisee.playwright.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Page Object Model for StreamNZ Login Page
 * This demonstrates good separation of concerns and reusability
 */
public class StreamNZLoginPage {
    
    private final Page page;
    
    // Page elements
    private static final String LOGIN_BUTTON_SELECTOR = "text=Login";
    
    public StreamNZLoginPage(Page page) {
        this.page = page;
    }
    
    /**
     * Navigate to the home page
     */
    public void navigateToHomePage() {
        page.navigate("https://streamnz.com");
        page.waitForTimeout(2000); // Wait for page to load
    }
    
    /**
     * Open login modal
     */
    public void openLoginModal() {
        String[] loginSelectors = {
            LOGIN_BUTTON_SELECTOR,
            "button:has-text('Login')",
            ".navbar-login-btn",
            ".navbar-btn.navbar-login-btn"
        };
        
        for (String selector : loginSelectors) {
            try {
                if (page.isVisible(selector)) {
                    page.click(selector);
                    return;
                }
            } catch (Exception e) {
                // Try next selector
            }
        }
        
        throw new RuntimeException("Could not find login button");
    }
    
    /**
     * Fill login credentials
     */
    public void fillCredentials(String email, String password) {
        page.fill("input[placeholder='Enter your email']", email);
        page.fill("input[placeholder='Enter your password']", password);
    }
    
    /**
     * Submit login form
     */
    public void submitLogin() {
        page.click("button[type='submit']");
    }
    
    /**
     * Check if login was successful
     */
    public boolean isLoginSuccessful() {
        page.waitForTimeout(2000);
        return !page.url().contains("login");
    }
    
    /**
     * Get current page title
     */
    public String getPageTitle() {
        return page.title();
    }
    
    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return page.url();
    }
    
    /**
     * Wait for login to complete
     */
    public void waitForLoginCompletion() {
        page.waitForTimeout(3000);
    }
    
    
    /**
     * Click Enter Game button
     */
    public void clickEnterGame() {
        page.click("text=Enter Game");
    }
    
    /**
     * Wait for game page to load
     */
    public void waitForGamePageLoad() {
        page.waitForTimeout(2000);
    }
    
    /**
     * Select "Play as White" button to enter the game board
     */
    public void selectPlayAsWhite() {
        page.click("button:has-text('Play as White')");
    }
    
    /**
     * Select "Play as Black" button to enter the game board
     */
    public void selectPlayAsBlack() {
        page.click("button:has-text('Play as Black')");
    }
    
    /**
     * Wait for game board to load
     */
    public void waitForGameBoard() {
        page.waitForTimeout(3000);
    }
}
