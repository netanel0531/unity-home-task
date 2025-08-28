package org.example.UI.page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ShowPostPage extends BasePage {
    private final By editButton = By.cssSelector("[data-testid='action-edit']");
    private String postId;

    public ShowPostPage(WebDriver driver, String postId) {
        super(driver, String.format("resources/Post/records/%s/show", postId));
        this.postId = postId;
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(editButton));
    }

    public EditPostPage editPost() {
        driver.findElement(editButton).click();
        return new EditPostPage(driver, postId);

    }
}
