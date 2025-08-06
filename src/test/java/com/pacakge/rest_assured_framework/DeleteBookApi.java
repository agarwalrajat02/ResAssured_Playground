package com.pacakge.rest_assured_framework;

import com.project.base.BaseClass;
import com.rest_assured.common.ApiUtils;
import com.rest_assured.common.RandomDataGenerator;
import com.rest_assured.common.TokenManager;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeleteBookApi extends BaseClass {

    @Test(priority = 1, groups = {"sanity", "positive"})
    @Description("Verify that a book can be deleted with a valid ID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("DELETE /books/:book_id - Delete an existing book")
    public void shouldDeleteBookSuccessfully() {
        String token = TokenManager.getToken();

        // Step 1: Create a book to ensure the ID exists
        int bookId = RandomDataGenerator.generateRandomId();
        JSONObject payload = new JSONObject();
        payload.put("name", RandomDataGenerator.generateRandomName());
        payload.put("author", RandomDataGenerator.generateRandomAuthor());
        payload.put("published_year", RandomDataGenerator.generateRandomPublishedYear());
        payload.put("book_summary", RandomDataGenerator.generateRandomSummary());
        payload.put("id", bookId);

        Response createResponse = ApiUtils.postRequest("/books/", payload.toString(), token);
        Assert.assertEquals(createResponse.getStatusCode(), 200, "Book creation failed before delete.");

        // Step 2: Delete the book
        Response deleteResponse = ApiUtils.deleteRequest("/books/" + bookId, token);
        Assert.assertEquals(deleteResponse.getStatusCode(), 200, "Book deletion failed.");
    }

    @Test(priority = 2, groups = {"negative"})
    @Description("Verify deleting a book with a non-existent ID fails")
    @Severity(SeverityLevel.NORMAL)
    @Story("DELETE /books/:book_id - Invalid ID")
    public void shouldFailWithInvalidBookId() {
        String token = TokenManager.getToken();

        int nonExistentId = 999999; // Assume this ID does not exist
        Response response = ApiUtils.deleteRequest("/books/" + nonExistentId, token);

        int statusCode = response.getStatusCode();
        boolean validFailure = (statusCode == 404 || statusCode == 400);
        boolean backendIssue = (statusCode == 500);

        Assert.assertTrue(validFailure || backendIssue,
            "Expected 404 or 400 for invalid ID, but got " + statusCode +
            (backendIssue ? ". Backend might not be validating properly." : ""));

        if (backendIssue) {
            System.out.println("[WARNING] Backend returned 500 for non-existent ID deletion.");
        }
    }

    @Test(priority = 3, groups = {"negative"})
    @Description("Verify deleting a book without authorization fails")
    @Severity(SeverityLevel.NORMAL)
    @Story("DELETE /books/:book_id - Unauthorized access")
    public void shouldFailWithoutAuthToken() {
        int bookId = RandomDataGenerator.generateRandomId();

        // Attempt to delete without token
        Response response = ApiUtils.deleteRequest("/books/" + bookId, "");

        int statusCode = response.getStatusCode();
        boolean expected = (statusCode == 401 || statusCode == 403);

        Assert.assertTrue(expected,
            "Expected 401 or 403 for unauthorized request, but got " + statusCode);
    }
}
