/*
 * This Page Object is a base page for all other pages.
 */
package org.example.UI.page_objects;

import org.openqa.selenium.WebDriver;

public class BasePage {
    protected WebDriver driver;
    protected final String base_url = "http://localhost:3000/admin/";
    protected String url;

    public BasePage(WebDriver driver, String url) {
        this.driver = driver;
        this.url = base_url + url;
        // Navigate to the relevant page.
        this.driver.get(this.url);
    }

}
