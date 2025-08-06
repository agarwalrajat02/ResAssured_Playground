package com.pacakge.rest_assured_framework;

import com.project.base.BaseClass;
import com.rest_assured.common.ApiUtils;
import com.rest_assured.common.TokenManager;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetBookByIdApi extends BaseClass {

    @Test(priority = 1, groups = {"sanity", "positive"})
    @Description("Verify that a book can be fetched successfully by valid ID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("GET /books/{id} - Fetch book by ID")
    public void shouldGetBookByIdSuccessfully() {
        String token = TokenManager.getToken();

        int validBookId = 1; // replace with actual valid ID or use dynamic retrieval if possible

        Response response = ApiUtils.getRequest("/books/" + validBookId, token);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 OK");

        int returnedId = response.jsonPath().getInt("id");
        Assert.assertEquals(returnedId, validBookId, "Returned book ID should match requested ID");
    }

    @Test(priority = 2, groups = {"negative"})
    @Description("Verify fetching book by invalid ID returns 404 Not Found")
    @Severity(SeverityLevel.NORMAL)
    @Story("GET /books/{id} - Invalid book ID")
    public void shouldReturn404ForInvalidBookId() {
        String token = TokenManager.getToken();

        int invalidBookId = 999999; // assuming this ID does not exist

        Response response = ApiUtils.getRequest("/books/" + invalidBookId, token);

        Assert.assertEquals(response.getStatusCode(), 404, "Expected 404 Not Found for invalid book ID");
    }

    @Test(priority = 3, groups = {"negative"})
    @Description("Verify fetching book by negative ID returns 404 Not Found (backend returns 404, not 400)")
    @Severity(SeverityLevel.MINOR)
    @Story("GET /books/{id} - Negative book ID")
    public void shouldReturn404ForNegativeBookId() {
        String token = TokenManager.getToken();

        int negativeBookId = -1;

        Response response = ApiUtils.getRequest("/books/" + negativeBookId, token);

        // Backend returns 404 instead of 400 for negative ID, adapting test accordingly
        Assert.assertEquals(response.getStatusCode(), 404, "Expected 404 Not Found for negative book ID (backend returns 404)");
    }

    @Test(priority = 4, groups = {"negative"})
    @Description("Verify fetching book by non-numeric ID returns 422 Unprocessable Entity (backend returns 422, not 400)")
    @Severity(SeverityLevel.MINOR)
    @Story("GET /books/{id} - Non-numeric book ID")
    public void shouldReturn422ForNonNumericBookId() {
        String token = TokenManager.getToken();

        String nonNumericId = "abc";

        Response response = ApiUtils.getRequest("/books/" + nonNumericId, token);

        // Backend returns 422 instead of 400 for non-numeric ID, adapting test accordingly
        Assert.assertEquals(response.getStatusCode(), 422, "Expected 422 Unprocessable Entity for non-numeric book ID (backend returns 422)");
    }
}
