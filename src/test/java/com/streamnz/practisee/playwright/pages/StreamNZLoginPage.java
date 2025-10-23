package com.streamnz.practisee.playwright.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Page Object Model for StreamNZ Login Page
 * This demonstrates good separation of concerns and reusability
 */
public class StreamNZLoginPage {
    
    private final Page page;
    
    // Page elements - using constants for maintainability
    private static final String LOGIN_BUTTON_SELECTOR = "text=Login";
    private static final String EMAIL_INPUT_SELECTOR = "input[placeholder='Enter your email']";
    private static final String PASSWORD_INPUT_SELECTOR = "input[placeholder='Enter your password']";
    private static final String SUBMIT_BUTTON_SELECTOR = "button:has-text('Login')";
    private static final String USER_MENU_SELECTOR = ".user-menu, .profile, .account";
    
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
            ".login-btn",
            "[data-testid='login-button']"
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
        // Wait for form to be visible
        page.waitForSelector(EMAIL_INPUT_SELECTOR, 
            new Page.WaitForSelectorOptions().setTimeout(10000));
        
        // Fill email
        page.fill(EMAIL_INPUT_SELECTOR, email);
        
        // Fill password
        page.fill(PASSWORD_INPUT_SELECTOR, password);
    }
    
    /**
     * Submit login form
     */
    public void submitLogin() {
        String[] submitSelectors = {
            SUBMIT_BUTTON_SELECTOR,
            "button[type='submit']",
            ".login-submit-btn"
        };
        
        for (String selector : submitSelectors) {
            try {
                if (page.isVisible(selector)) {
                    page.click(selector, new Page.ClickOptions().setForce(true));
                    return;
                }
            } catch (Exception e) {
                // Try next selector
            }
        }
        
        throw new RuntimeException("Could not submit login form");
    }
    
    /**
     * Check if login was successful
     */
    public boolean isLoginSuccessful() {
        // Check if login form disappears
        try {
            page.waitForSelector(EMAIL_INPUT_SELECTOR, 
                new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.DETACHED)
                    .setTimeout(5000));
            return true;
        } catch (PlaywrightException e) {
            // Form didn't disappear, check other indicators
        }
        
        // Check for user menu
        try {
            page.waitForSelector(USER_MENU_SELECTOR, 
                new Page.WaitForSelectorOptions().setTimeout(3000));
            return true;
        } catch (PlaywrightException e) {
            // No user menu found
        }
        
        // Check for success messages
        try {
            page.waitForSelector(".success, .alert-success, .login-success", 
                new Page.WaitForSelectorOptions().setTimeout(2000));
            return true;
        } catch (PlaywrightException e) {
            // No success message found
        }
        
        return false;
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
}
