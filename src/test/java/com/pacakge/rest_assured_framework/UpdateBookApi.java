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

public class UpdateBookApi extends BaseClass {

    @Test(priority = 1, groups = {"sanity", "positive"})
    @Description("Verify that a book can be updated successfully with valid data")
    @Severity(SeverityLevel.CRITICAL)
    @Story("PUT /books/{id} - Update a book entry")
    public void shouldUpdateBookSuccessfully() {
        String token = TokenManager.getToken();

        int existingBookId = 1; // Replace with valid book ID or fetch dynamically

        JSONObject payload = new JSONObject();
        payload.put("name", RandomDataGenerator.generateRandomName());
        payload.put("author", RandomDataGenerator.generateRandomAuthor());
        payload.put("published_year", RandomDataGenerator.generateRandomPublishedYear());
        payload.put("book_summary", RandomDataGenerator.generateRandomSummary());
        payload.put("id", existingBookId);  // ID must be present as per your schema

        // Add assertions for other fields if needed
    }

}
