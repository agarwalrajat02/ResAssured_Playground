package com.pacakge.rest_assured_framework;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.project.base.BaseClass;
import com.rest_assured.common.ApiUtils;
import com.rest_assured.common.TokenManager;

import io.qameta.allure.*;
import io.restassured.response.Response;

@Epic("Bookstore API Tests")
@Feature("Get All Books")
public class GetAllBooksApi extends BaseClass {

    /* -------------------- POSITIVE TEST CASES -------------------- */

    @Test(priority = 1, groups = {"sanity", "positive"})
    @Description("Verify that the Get All Books API returns a valid list of books")
    @Severity(SeverityLevel.CRITICAL)
    @Story("GET /books - Fetch all available books")
    public void shouldReturnAllBooksSuccessfully() {
        Response response = ApiUtils.getRequest("/books", TokenManager.getToken());

        logResponse(response);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 OK");

        int bookCount = response.jsonPath().getList("$").size();
        Assert.assertTrue(bookCount > 0,
                "Books list should not be empty. Actual count: " + bookCount);
    }

    @Test(priority = 4, groups = {"sanity", "positive"})
    @Description("Verify structure of each book object returned by the API")
    @Severity(SeverityLevel.MINOR)
    @Story("GET /books - Validate book fields")
    public void shouldHaveExpectedFieldsInBooks() {
        Response response = ApiUtils.getRequest("/books", TokenManager.getToken());

        logResponse(response);
        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 OK");

        String firstTitle = response.jsonPath().getString("[0].name");
        String firstAuthor = response.jsonPath().getString("[0].author");

        Assert.assertNotNull(firstTitle, "Book name should not be null");
        Assert.assertNotNull(firstAuthor, "Book author should not be null");

        System.out.println("First Book Name: " + firstTitle);
        System.out.println("First Book Author: " + firstAuthor);
    }

    /* -------------------- NEGATIVE TEST CASES -------------------- */

    @Test(priority = 2, groups = {"negative"})
    @Description("Verify that accessing the Get All Books API without a token returns 403 Forbidden")
    @Severity(SeverityLevel.NORMAL)
    @Story("GET /books - Unauthorized access")
    public void shouldFailWithoutAuthToken() {
        Response response = ApiUtils.getRequest("/books");

        logResponse(response);
        Assert.assertEquals(response.getStatusCode(), 403,
                "Expected 403 Forbidden when token is missing");
    }

    @Test(priority = 3, groups = {"negative"})
    @Description("Verify that accessing the Get All Books API with an invalid token returns 403 Forbidden")
    @Severity(SeverityLevel.NORMAL)
    @Story("GET /books - Invalid token access")
    public void shouldFailWithInvalidToken() {
        String invalidToken = "InvalidOrExpiredToken123";
        Response response = ApiUtils.getRequest("/books", invalidToken);

        logResponse(response);
        Assert.assertEquals(response.getStatusCode(), 403,
                "Expected 403 Forbidden for invalid token");
    }

    @Test(priority = 5, groups = {"negative"})
    @Description("Verify 404 Not Found for invalid endpoint")
    @Severity(SeverityLevel.NORMAL)
    @Story("GET /invalid - Wrong endpoint test")
    public void shouldReturn404ForInvalidEndpoint() {
        Response response = ApiUtils.getRequest("/bookz", TokenManager.getToken()); // intentional typo

        logResponse(response);
        Assert.assertEquals(response.getStatusCode(), 404,
                "Expected 404 Not Found for invalid endpoint");
    }

    /* -------------------- UTILITY METHODS -------------------- */

    /**
     * Fetch the ID of the first book in the list.
     * Can be reused in update/delete operations.
     *
     * @return First book's ID
     */
    public static int getFirstBookId() {
        Response response = ApiUtils.getRequest("/books", TokenManager.getToken());
        if (response.getStatusCode() == 200) {
            return response.jsonPath().getInt("[0].id");
        }
        throw new RuntimeException(
                "Failed to fetch books for ID extraction. Status: " + response.getStatusCode()
        );
    }

    /**
     * Logs the status code and formatted response body.
     *
     * @param response RestAssured Response object
     */
    private void logResponse(Response response) {
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body:\n" + response.getBody().asPrettyString());
    }
}
