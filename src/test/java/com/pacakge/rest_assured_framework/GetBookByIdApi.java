package com.pacakge.rest_assured_framework;

import com.project.base.BaseClass;
import com.rest_assured.common.ApiUtils;
import com.rest_assured.common.GetAllBooksId;
import com.rest_assured.common.TokenManager;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Bookstore API Tests")
@Feature("Get Book By ID")
public class GetBookByIdApi extends BaseClass {

    @Test(priority = 1, groups = {"sanity", "positive"})
    @Description("Verify that a book can be fetched successfully by valid ID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("GET /books/{id} - Positive")
    public void shouldGetBookByIdSuccessfully() {
        String token = TokenManager.getToken();
        int bookId = GetAllBooksId.getFirstBookId();

        Response response = ApiUtils.getRequest("/books/" + bookId, token);

        Assert.assertEquals(response.getStatusCode(), 200, "Expected HTTP 200");
        Assert.assertEquals(response.jsonPath().getInt("id"), bookId, "Book ID mismatch");
        Assert.assertNotNull(response.jsonPath().getString("name"), "Book name should not be null");
        Assert.assertNotNull(response.jsonPath().getString("author"), "Book author should not be null");
    }

    @Test(priority = 2, groups = {"negative"})
    @Description("Verify fetching book with an invalid ID returns 404")
    @Severity(SeverityLevel.NORMAL)
    @Story("GET /books/{id} - Invalid ID")
    public void shouldReturnNotFoundForInvalidId() {
        String token = TokenManager.getToken();
        int invalidBookId = 999999; // assuming this ID doesn't exist

        Response response = ApiUtils.getRequest("/books/" + invalidBookId, token);

        Assert.assertEquals(response.getStatusCode(), 404, "Expected HTTP 404");
        Assert.assertTrue(response.asString().contains("not found"), "Error message should indicate 'not found'");
    }

    @Test(priority = 3, groups = {"negative"})
    @Description("Verify fetching book with a non-numeric ID returns 400")
    @Severity(SeverityLevel.NORMAL)
    @Story("GET /books/{id} - Non-numeric ID")
    public void shouldReturnBadRequestForNonNumericId() {
        String token = TokenManager.getToken();

        Response response = ApiUtils.getRequest("/books/abc", token);

        Assert.assertEquals(response.getStatusCode(), 400, "Expected HTTP 400");
        Assert.assertTrue(response.asString().toLowerCase().contains("invalid"), "Response should mention 'invalid'");
    }

    @Test(priority = 4, groups = {"negative"})
    @Description("Verify fetching book without token returns 401")
    @Severity(SeverityLevel.CRITICAL)
    @Story("GET /books/{id} - Missing token")
    public void shouldReturnUnauthorizedWhenTokenIsMissing() {
        int bookId = GetAllBooksId.getFirstBookId();

        Response response = ApiUtils.getRequest("/books/" + bookId, "");

        Assert.assertEquals(response.getStatusCode(), 401, "Expected HTTP 401");
        Assert.assertTrue(response.asString().toLowerCase().contains("unauthorized"), "Should indicate 'unauthorized'");
    }
}
