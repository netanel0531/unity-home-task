package org.example.UI.tests;

import org.example.UI.enums.PostStatus;
import org.example.UI.exceptions.PostNotFoundException;
import org.example.UI.page_objects.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SeleniumTests {
    private WebDriver driver;
    private final String admin_username = "admin@example.com";
    private final String admin_password = "password";

    @BeforeTest
    public void setup() {
        // Set up the ChromeDriver for the tests
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void unityAssignmentTest() {
        // Login to the system using the given username and password
        System.out.println("Login as an admin");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(admin_username, admin_password);

        // Create a new Publisher
        System.out.println("Creating a Publisher...");
        String publisherName = "Test Publisher " + System.currentTimeMillis();
        PublisherPage publisherPage = new PublisherPage(driver);
        publisherPage.createPublisher(publisherName);
        System.out.println("Publisher created: " + publisherName);

        // Create a new post and link it to the new publisher, it's Active and Published=True
        PostsPage postsPage = new PostsPage(driver);
        String newPostTitle = "new_title " + System.currentTimeMillis();
        postsPage.createPost(newPostTitle, PostStatus.ACTIVE, true, publisherName);

        // Edit that post, change its status to "REMOVED" and save it.
        // a BUG found - a post doesn't being saved if the post doesn't have a JSON, which is not a mandatory to include.
        EditPostPage editPostPage = postsPage.openShowPostPage(newPostTitle).editPost();
        editPostPage.updateStatus(PostStatus.REMOVED);

        // Validate the status update.
        try {
            // Trying to find the relevant post in the posts list, and validate the status.
            // If the post haven't been found an PostNotFoundException is thrown.
            postsPage = new PostsPage(driver);
            postsPage.openShowPostPage(newPostTitle).validateStatus(PostStatus.REMOVED);
        } catch (PostNotFoundException e) {
            // Post wasn't found, failing the test.
            Assert.fail("The post haven't been found at the Posts page.");
        }

    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
