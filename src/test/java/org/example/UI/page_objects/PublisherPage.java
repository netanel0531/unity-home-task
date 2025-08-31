package org.example.UI.page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class PublisherPage extends BasePage {
    private final By newPublisherButton = By.cssSelector("[data-testid='action-new']");
    private final By publisherNameInput = By.id("name");
    private final By publisherEmailInput = By.id("email");
    private final By saveButton = By.cssSelector("[data-testid='button-save']");

    public PublisherPage(WebDriver driver) {
        super(driver, "resources/Publisher");

        // Wait for the new publisher button to be visible
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(newPublisherButton));
    }

    /**
     * Create a publisher with only the mandatory field (email).
     * @param email
     */
    public void createPublisher(String email) {
        createPublisher(email, null);
    }

    /**
     * Create a new publisher with the given email and name.
     * @param email
     * @param name
     */
    public void createPublisher(String email, String name) {
        // Open the new publisher form
        driver.findElement(newPublisherButton).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(saveButton));

        // Fill the publisher name if given
        if (name != null && !name.isEmpty()) {
            driver.findElement(publisherNameInput).sendKeys(name);
        }

        // Fill the publisher email
        driver.findElement(publisherEmailInput).sendKeys(email);

        // Click the save button
        driver.findElement(saveButton).click();
    }
}
