package org.example.UI.page_objects;

import org.example.UI.enums.PostStatus;
import org.example.UI.exceptions.PostNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PostsPage extends BasePage {
    private final By newPostButton = By.cssSelector("[data-testid='action-new']");
    private final By nextPostsPage = By.cssSelector("[data-testid='next']");
    private final String postIdByTitle = "//section[@data-testid='property-list-title' and text()='%s']/../following-sibling::td[@data-property-name='id']";

    // New post locators
    private final By postTitleInput = By.id("title");

    private final By postStatusDropdown = By.id("react-select-5-input");
    private final String reactSelectStatusOptionByText = "//div[contains(@id, 'react-select-5-option') and text()='%s']";

    private final By publisherDropdown = By.id("react-select-6-input");
    private final String reactSelectPublisherOptionByText = "//div[contains(@id, 'react-select-6-option') and text()='%s']";

    private final By publishedCheckBox = By.xpath("//input[@id='published']/following-sibling::a");
    private final By saveButton = By.cssSelector("[data-testid='button-save']");


    public PostsPage(WebDriver driver) {
        super(driver, "resources/Post");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(newPostButton));
    }

    /**
     * Create a new post using the mandatory fields.
     * @param title
     * @param status
     * @param published
     * @param publisherName
     */
    public void createPost(String title, PostStatus status, boolean published, String publisherName) {
        driver.findElement(newPostButton).click();

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(postTitleInput));

        driver.findElement(postTitleInput).sendKeys(title);

        driver.findElement(postStatusDropdown).click();
        // Construct the XPath to find the option with the desired text
        By statusOptionLocator = By.xpath(String.format(reactSelectStatusOptionByText, status.getText()));

        // Wait for the option to be visible and then click it
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(statusOptionLocator))
                .click();

        if (published) driver.findElement(publishedCheckBox).click();

        driver.findElement(publisherDropdown).click();
        // Construct the XPath to find the option with the desired text
        By publisherOptionLocator = By.xpath(String.format(reactSelectPublisherOptionByText, publisherName));

        // Wait for the option to be visible and then click it
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(publisherOptionLocator))
                .click();

        driver.findElement(saveButton).click();

        // Wait to get back to finish the creation by verifying we at the Posts Page
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(newPostButton));
    }

    /**
     * Open the post page (show post) by the desired post's title.
     * @param title of the desired post.
     * @return new ShowPostPage of the relevant post.
     */
    public ShowPostPage openShowPostPage(String title) {
        // Construct the XPath to find the option with the desired text
        By postRowLocator = By.xpath(String.format(postIdByTitle, title));
        WebElement postIdTableCell = null;
        // Find the post in the Posts Page list, iterate over the pages until found or throw an PostNotFoundException.
        do {
            try {
                postIdTableCell = driver.findElement(postRowLocator);
            } catch (NoSuchElementException e) {
                try {
                    driver.findElement(nextPostsPage).click();
                } catch (NoSuchElementException noNextPage) {
                    System.out.println(String.format("No next page and post title %s haven't found", title));
                    throw new PostNotFoundException(noNextPage.getMessage());
                }
            }
        } while (postIdTableCell == null);

        // if the post found in the list, open it and return the relevant ShowPostPage.
        String postId = postIdTableCell.getText();
        postIdTableCell.click();
        return new ShowPostPage(driver, postId);
    }
}