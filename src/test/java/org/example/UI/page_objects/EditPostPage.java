package org.example.UI.page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EditPostPage extends BasePage {
    private String postId;
    private final By postStatusDropdown = By.id("react-select-2-input");
    private final By allDropdowns = By.xpath("//input[contains(@id, 'react-select')]"); //todo use this to extract the react-select-X for click and later options
    private final String reactSelectStatusOptionByText = "//div[contains(@id, 'react-select-2-option') and text()='%s']";
    private final By saveButton = By.cssSelector("[data-testid='button-save']");


    public EditPostPage(WebDriver driver, String postId) {
        super(driver, String.format("resources/Post/records/%s/edit", postId));
        this.postId = postId;
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(saveButton));
    }

    public void updateStatus(String newStatus) {

        driver.findElement(postStatusDropdown).click();
        // Construct the XPath to find the option with the desired text
        By statusOptionLocator = By.xpath(String.format(reactSelectStatusOptionByText, newStatus));

        // Wait for the option to be visible and then click it
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(statusOptionLocator))
                .click();

        driver.findElement(saveButton).click();
    }
}
