package org.example.API.tests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.nio.charset.StandardCharsets;

public class ApiTests {
    private final String BASE_URL = "http://localhost:3000";
    private final String API_BASE = BASE_URL + "/admin/api/resources";
    private final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            // Use a CookieManager to handle session cookies automatically
            .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
            .build();

    @Test
    public void apiTest() {
        login();
        int publisherId = createPublisher();
        Assert.assertTrue(publisherId > 0);
        int postId = createPost(publisherId);
        Assert.assertTrue(postId > 0);
        editPostStatus(postId);
        validatePostStatus(postId);
    }

    // --- Step 1: Login ---
    public void login() {
        System.out.println("--- Step 1: Logging in ---");
        String email = "admin@example.com";
        String password = "password";
        String loginUrl = BASE_URL + "/admin/login";
        String body = String.format("email=%s&password=%s", email, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(loginUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(body))
                .build();
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if ((response.statusCode() == 200) || (response.statusCode() == 302)) {
                System.out.println("Login successful.");
                return;
            } else {
                System.out.println("Login failed with status code: " + response.statusCode());
                Assert.fail("Login failed with status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Login request failed: " + e.getMessage());
            Assert.fail("Login request failed: " + e.getMessage());
        }
    }

    // --- Step 2: Create a Publisher ---
    public int createPublisher() {
        System.out.println("\n--- Step 2: Creating a Publisher ---");
        String publisherUrl = API_BASE + "/Publisher/actions/new";
        String publisherEmail = "api publisher " + System.currentTimeMillis();
        String boundary = "----" + UUID.randomUUID();

        Map<Object, Object> data = new HashMap<>();
        data.put("email", publisherEmail);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(publisherUrl))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(ofMimeMultipartData(data, boundary))
                .build();
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                // Simple JSON parsing to get the ID, you may need a library like Gson or Jackson for production code
                int id = Integer.parseInt(responseBody.split("\"id\":")[1].split(",")[0]);
                System.out.println("Publisher created with ID: " + id);
                return id;
            } else {
                System.out.println("Failed to create publisher with status: " + response.statusCode());
                System.out.println("Response: " + response.body());
                return -1;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Create publisher request failed: " + e.getMessage());
            return -1;
        }
    }

    // --- Step 3: Create and Link a Post ---
    public int createPost(int publisherId) {
        System.out.println("\n--- Step 3: Creating a Post and Linking it to the Publisher ---");
        String postUrl = API_BASE + "/Post/actions/new";
        String postTitle = "Post Title " + System.currentTimeMillis();
        String boundary = "----" + UUID.randomUUID();

        Map<Object, Object> data = new HashMap<>();
        data.put("title", postTitle);
        data.put("content", "This post was created via the API.");
        data.put("publisher", publisherId);
        data.put("status", "ACTIVE");
        data.put("isPublished", "true");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(postUrl))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(ofMimeMultipartData(data, boundary))
                .build();

        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                int id = Integer.parseInt(responseBody.split("\"id\":")[1].split(",")[0]);
                System.out.println("Post created with ID: " + id);
                return id;
            } else {
                System.out.println("Failed to create post with status: " + response.statusCode());
                System.out.println("Response: " + response.body());
                return -1;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Create post request failed: " + e.getMessage());
            return -1;
        }
    }

    // --- Step 4: Edit the Post Status to "remove" ---
    public void editPostStatus(int postId) {
        System.out.printf("\n--- Step 4: Changing Post %d status to 'REMOVED' ---%n", postId);
        String editUrl = API_BASE + "/Post/records/" + postId +"/edit";
        String boundary = "----" + UUID.randomUUID();

        Map<Object, Object> data = new HashMap<>();
        data.put("id", postId);
        data.put("status", "REMOVED");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(editUrl))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(ofMimeMultipartData(data, boundary))
                .build();

        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("Post status updated to 'REMOVED' successfully.");
                return;
            } else {
                System.out.println("Failed to edit post status with status: " + response.statusCode());
                System.out.println("Response: " + response.body());
                Assert.fail("Failed to edit post status with status: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Edit post status request failed: " + e.getMessage());
            Assert.fail("Edit post status request failed: " + e.getMessage());
        }
    }

    // --- Step 5: Validate the Post Status ---
    public void validatePostStatus(int postId) {
        System.out.printf("\n--- Step 5: Validating Post %s status ---%n", postId);
        String validationUrl = API_BASE + "/Post/records/" + postId + "/show";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(validationUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                String currentStatus = responseBody.split("\"status\":\"")[1].split("\"")[0];

                Assert.assertEquals(currentStatus, "REMOVED");
            } else {
                System.out.println("Validation request failed with status: " + response.statusCode());
                System.out.println("Response: " + response.body());
                Assert.fail("Validation request failed with status: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Validation request failed: " + e.getMessage());
            Assert.fail("Validation request failed: " + e.getMessage());
        }
    }

    // Helper method to build multipart/form-data body
    public static HttpRequest.BodyPublisher ofMimeMultipartData(Map<Object, Object> data, String boundary) {
        var byteArrays = new StringBuilder();
        String CRLF = "\r\n";

        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.append("--").append(boundary).append(CRLF);
            byteArrays.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"").append(CRLF);
            byteArrays.append(CRLF);
            byteArrays.append(entry.getValue()).append(CRLF);
        }
        byteArrays.append("--").append(boundary).append("--").append(CRLF);

        return BodyPublishers.ofString(byteArrays.toString(), StandardCharsets.UTF_8);
    }
}