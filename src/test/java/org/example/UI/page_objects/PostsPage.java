package org.example.UI.page_objects;
/*
 * This Page Object represents the Post creation and editing page.
 * It contains methods for creating, linking, editing, and verifying a post.
 */

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class PostsPage extends BasePage {
    private final By newPostButton = By.cssSelector("[data-testid='action-new']");
    private final By postTitleInput = By.id("title");
    private final By postContentInput = By.id("content");
    private final By postStatusDropdown = By.id("react-select-5-input");
    // New locator for the react-select options list
    // This XPath finds a div with role="option" and a specific text content.
    // This is a robust way to handle dynamic IDs.
    private final String reactSelectStatusOptionByText = "//div[contains(@id, 'react-select-5-option') and text()='%s']";
    //    'data-testid="property-edit-status"';
//    private final By postStatusDropdown = By.id("postStatus");

    private final By publisherDropdown = By.id("react-select-6-input");
    private final String reactSelectPublisherOptionByText = "//div[contains(@id, 'react-select-6-option') and text()='%s']";


    private final By publishedCheckBox = By.xpath("//input[@id='published']/following-sibling::a");

    private final By saveButton = By.cssSelector("[data-testid='button-save']");
    private final By postStatusText = By.id("postStatusText");

    private final By postsTable = By.cssSelector("[data-css='Post-table-body']");
    private final By postsTableRow = By.cssSelector("[data-css='Post-table-row']");
    private final String postsTableTitleByText = "//td[data-css='Post-list-title'] and text()='%s'";
    private final String postIdByTitle = "//section[@data-testid='property-list-title' and text()='%s']/../following-sibling::td[@data-property-name='id']";

    public PostsPage(WebDriver driver) {
        super(driver, "resources/Post");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(newPostButton));
    }

    public void createPost(String title, String status, boolean published, String publisherName) {
        driver.findElement(newPostButton).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(postTitleInput));

        driver.findElement(postTitleInput).sendKeys(title);

        driver.findElement(postStatusDropdown).click();
        // Construct the XPath to find the option with the desired text
        By statusOptionLocator = By.xpath(String.format(reactSelectStatusOptionByText, status));

        // Wait for the option to be visible and then click it
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(statusOptionLocator))
                .click();

        if (published) driver.findElement(publishedCheckBox).click();

        driver.findElement(publisherDropdown).click();
        // Construct the XPath to find the option with the desired text
        By publisherOptionLocator = By.xpath(String.format(reactSelectPublisherOptionByText, publisherName));

        // Wait for the option to be visible and then click it
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(publisherOptionLocator))
                .click();

        driver.findElement(saveButton).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(newPostButton));
    }


    public ShowPostPage openShowPostPage(String title) {
        //todo navigate through the pages until finding the relevant post.
        // Navigates to the post's edit page
        By postRowLocator = By.xpath(String.format(postIdByTitle, title));
        WebElement postIdTableCell = driver.findElement(postRowLocator);
        String postId = postIdTableCell.getText();
        postIdTableCell.click();
        return new ShowPostPage(driver, postId);
    }

    public void savePost() {
        driver.findElement(saveButton).click();
    }

    public String getPostStatus() {
        // Navigates to the post's view page to get the status
        // TODO: Replace with the actual URL of the post's view page
        // For example: driver.get("http://your-app-url.com/post/view/123");

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(postStatusText));

        return driver.findElement(postStatusText).getText();
    }
}