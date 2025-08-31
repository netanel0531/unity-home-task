package org.example.UI.page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {
    private final By loginButton = By.cssSelector("button[color=\"primary\"]");
    private final By usernameInput = By.name("email");
    private final By passwordInput = By.name("password");

    public LoginPage(WebDriver driver) {
        super(driver, "login");
        // Wait for the Login button to be visible
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(loginButton));
    }

    /**
     * Perform a login using a given username and password.
     * @param username
     * @param password
     */
    public void login(String username, String password) {
        // Clear and fill the username
        driver.findElement(usernameInput).clear();
        driver.findElement(usernameInput).sendKeys(username);

        // Clear and fill the password
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(password);

        // Click Login
        driver.findElement(loginButton).click();

        // Wait and assert successfully login.
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlToBe("http://localhost:3000/admin"));
    }
}
