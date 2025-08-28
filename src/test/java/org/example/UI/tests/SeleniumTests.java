package org.example.UI.tests;

import org.example.UI.page_objects.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SeleniumTests {
    private WebDriver driver;
    private final String publisherName = "Test Publisher " + System.currentTimeMillis();
    private final String postTitle = "Test Post " + System.currentTimeMillis();
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
    public void createPublisherTest() {
        System.out.println("Login as an admin");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(admin_username, admin_password);

        // Step 1: Create a Publisher
        System.out.println("Step 1: Creating a Publisher...");
        PublisherPage publisherPage = new PublisherPage(driver);
        publisherPage.createPublisher(publisherName);
        System.out.println("Publisher created: " + publisherName);

        // Create mew post and link it to the new publisher
        PostsPage postsPage = new PostsPage(driver);
        String newPostTitle = "new_title " + System.currentTimeMillis();
        postsPage.createPost(newPostTitle, "ACTIVE", true, publisherName);

        // edit that post
        EditPostPage editPostPage = postsPage.openShowPostPage(newPostTitle).editPost();
        editPostPage.updateStatus("REMOVED"); //todo use constants for those values


    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
