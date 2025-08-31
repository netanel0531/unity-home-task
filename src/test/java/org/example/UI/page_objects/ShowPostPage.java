package org.example.UI.page_objects;

import org.example.UI.enums.PostStatus;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class ShowPostPage extends BasePage {
    private String postId;
    private final By editButton = By.cssSelector("[data-testid='action-edit']");
    private final By statusBy = By.xpath("//section[@data-testid='property-show-status']/.//following-sibling::span");


    public ShowPostPage(WebDriver driver, String postId) {
        super(driver, String.format("resources/Post/records/%s/show", postId));
        this.postId = postId;
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(editButton));
    }

    public String getPostId() {
        return this.postId;
    }

    /**
     * Open the edit page of the current post.
     * @return an instance represents the edit page of the post.
     */
    public EditPostPage editPost() {
        driver.findElement(editButton).click();
        return new EditPostPage(driver, postId);
    }

    /**
     * Validate the status of the current post against an expected status.
     * @param expectedStatus the expected status.
     */
    public void validateStatus(PostStatus expectedStatus) {
        Assert.assertEquals(driver.findElement(statusBy).getText(), expectedStatus.getText(), "Post status doesn't match expected");
    }
}
