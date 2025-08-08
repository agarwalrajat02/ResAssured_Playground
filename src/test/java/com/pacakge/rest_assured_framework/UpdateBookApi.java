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

@Epic("Bookstore API Tests")
@Feature("Update Book API Tests")
public class UpdateBookApi extends BaseClass {

    private static final String UPDATE_BOOK_ENDPOINT = "/books/{id}";

    @Test(priority = 1, groups = {"sanity", "positive"})
    @Description("Verify that a book can be updated successfully with valid data")
    @Severity(SeverityLevel.CRITICAL)
    @Story("PUT /books/{id} - Update a book entry")
    public void shouldUpdateBookSuccessfully() {
        String token = TokenManager.getToken();
        int existingBookId = 1; // TODO: Replace with a dynamically fetched ID

        JSONObject payload = new JSONObject();
        payload.put("name", RandomDataGenerator.generateRandomName());
        payload.put("author", RandomDataGenerator.generateRandomAuthor());
        payload.put("published_year", RandomDataGenerator.generateRandomPublishedYear());
        payload.put("book_summary", RandomDataGenerator.generateRandomSummary());
        payload.put("id", existingBookId);

        Response response = ApiUtils.putRequest(
                UPDATE_BOOK_ENDPOINT.replace("{id}", String.valueOf(existingBookId)),
                payload.toString(),
                token
        );

        Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch");
        Assert.assertEquals(response.jsonPath().getString("name"), payload.getString("name"), "Book name mismatch");
        Assert.assertEquals(response.jsonPath().getString("author"), payload.getString("author"), "Author mismatch");
    }

    @Test(priority = 2, groups = {"negative"})
    @Description("Verify that updating a book with invalid ID returns 404")
    @Severity(SeverityLevel.NORMAL)
    @Story("PUT /books/{id} - Invalid book ID")
    public void shouldFailForInvalidBookId() {
        String token = TokenManager.getToken();
        int invalidBookId = 99999;

        JSONObject payload = new JSONObject();
        payload.put("name", RandomDataGenerator.generateRandomName());
        payload.put("author", RandomDataGenerator.generateRandomAuthor());
        payload.put("published_year", RandomDataGenerator.generateRandomPublishedYear());
        payload.put("book_summary", RandomDataGenerator.generateRandomSummary());
        payload.put("id", invalidBookId);

        Response response = ApiUtils.putRequest(
                UPDATE_BOOK_ENDPOINT.replace("{id}", String.valueOf(invalidBookId)),
                payload.toString(),
                token
        );

        Assert.assertEquals(response.getStatusCode(), 404, "Expected 404 for invalid ID");
    }

    @Test(priority = 3, groups = {"negative"})
    @Description("Verify that updating a book without required field returns 400")
    @Severity(SeverityLevel.NORMAL)
    @Story("PUT /books/{id} - Missing required field")
    public void shouldFailWhenRequiredFieldMissing() {
        String token = TokenManager.getToken();
        int existingBookId = 1;

        JSONObject payload = new JSONObject();
        // Missing "name"
        payload.put("author", RandomDataGenerator.generateRandomAuthor());
        payload.put("published_year", RandomDataGenerator.generateRandomPublishedYear());
        payload.put("book_summary", RandomDataGenerator.generateRandomSummary());
        payload.put("id", existingBookId);

        Response response = ApiUtils.putRequest(
                UPDATE_BOOK_ENDPOINT.replace("{id}", String.valueOf(existingBookId)),
                payload.toString(),
                token
        );

        Assert.assertEquals(response.getStatusCode(), 400, "Expected 400 for missing required field");
    }

    @Test(priority = 4, groups = {"negative"})
    @Description("Verify that updating a book without token returns 401")
    @Severity(SeverityLevel.CRITICAL)
    @Story("PUT /books/{id} - No Authorization token")
    public void shouldFailWithoutToken() {
        int existingBookId = 1;

        JSONObject payload = new JSONObject();
        payload.put("name", RandomDataGenerator.generateRandomName());
        payload.put("author", RandomDataGenerator.generateRandomAuthor());
        payload.put("published_year", RandomDataGenerator.generateRandomPublishedYear());
        payload.put("book_summary", RandomDataGenerator.generateRandomSummary());
        payload.put("id", existingBookId);

        Response response = ApiUtils.putRequest(
                UPDATE_BOOK_ENDPOINT.replace("{id}", String.valueOf(existingBookId)),
                payload.toString(),
                "" // No token
        );

        Assert.assertEquals(response.getStatusCode(), 401, "Expected 401 for missing token");
    }

    @Test(priority = 5, groups = {"negative"})
    @Description("Verify that updating a book with invalid token returns 401")
    @Severity(SeverityLevel.CRITICAL)
    @Story("PUT /books/{id} - Invalid Authorization token")
    public void shouldFailWithInvalidToken() {
        int existingBookId = 1;

        JSONObject payload = new JSONObject();
        payload.put("name", RandomDataGenerator.generateRandomName());
        payload.put("author", RandomDataGenerator.generateRandomAuthor());
        payload.put("published_year", RandomDataGenerator.generateRandomPublishedYear());
        payload.put("book_summary", RandomDataGenerator.generateRandomSummary());
        payload.put("id", existingBookId);

        Response response = ApiUtils.putRequest(
                UPDATE_BOOK_ENDPOINT.replace("{id}", String.valueOf(existingBookId)),
                payload.toString(),
                "invalid_token"
        );

        Assert.assertEquals(response.getStatusCode(), 401, "Expected 401 for invalid token");
    }
}
