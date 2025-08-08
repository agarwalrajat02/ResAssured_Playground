package com.pacakge.rest_assured_framework;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.project.base.BaseClass;
import com.rest_assured.common.ApiUtils;
import com.rest_assured.constants.InputConstants;

import io.qameta.allure.*;
import io.restassured.response.Response;

public class LoginApi extends BaseClass {

    // --- Positive Test Case ---

    @Test(priority = 1, groups = { "positive", "sanity" }, description = "Verify login succeeds with valid credentials.")
    @Story("POST /login - Success")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that valid email and password credentials return HTTP 200 and an access token in the response.")
    public void loginWithValidCredentials() {
        String requestBody = "{ \"email\": \"" + InputConstants.USERNAME + "\", \"password\": \"" + InputConstants.PASSWORD + "\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 OK for valid login");

        String token = response.jsonPath().getString("access_token");
        Assert.assertNotNull(token, "Token should not be null after successful login");

        System.out.println("Extracted token: " + token);
    }

    // --- Negative Test Cases ---

    @Test(priority = 2, groups = "negative", description = "Verify login fails with an unregistered email.")
    @Story("POST /login - Invalid Email")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks that using an email not present in the system returns HTTP 400 or appropriate error code.")
    public void loginWithInvalidEmail() {
        String requestBody = "{ \"email\": \"wrong.email@test.com\", \"password\": \"" + InputConstants.PASSWORD + "\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertEquals(response.getStatusCode(), 400, "Expected 400 for invalid email");
    }

    @Test(priority = 3, groups = "negative", description = "Verify login fails with incorrect password.")
    @Story("POST /login - Invalid Password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that valid email but wrong password returns HTTP 400 or appropriate authentication error.")
    public void loginWithInvalidPassword() {
        String requestBody = "{ \"email\": \"" + InputConstants.USERNAME + "\", \"password\": \"wrongPass\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertEquals(response.getStatusCode(), 400, "Expected 400 for invalid password");
    }

    @Test(priority = 4, groups = "negative", description = "Verify login fails when email field is blank.")
    @Story("POST /login - Blank Email")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks that an empty email field returns HTTP 400 or validation error.")
    public void loginWithBlankEmail() {
        String requestBody = "{ \"email\": \"\", \"password\": \"" + InputConstants.PASSWORD + "\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected 4xx for blank email");
    }

    @Test(priority = 5, groups = "negative", description = "Verify login fails when password field is blank.")
    @Story("POST /login - Blank Password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks that an empty password field returns HTTP 400 or validation error.")
    public void loginWithBlankPassword() {
        String requestBody = "{ \"email\": \"" + InputConstants.USERNAME + "\", \"password\": \"\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected 4xx for blank password");
    }

    @Test(priority = 6, groups = "negative", description = "Verify login fails when both email and password fields are blank.")
    @Story("POST /login - Blank Fields")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that empty email and password fields return HTTP 400 or validation error.")
    public void loginWithBlankFields() {
        String requestBody = "{ \"email\": \"\", \"password\": \"\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected 4xx for blank fields");
    }

    @Test(priority = 7, groups = "negative", description = "Verify login fails when email field is missing from payload.")
    @Story("POST /login - Missing Email Field")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks that missing 'email' field in the request body returns HTTP 400 or validation error.")
    public void loginWithMissingEmailField() {
        String requestBody = "{ \"password\": \"" + InputConstants.PASSWORD + "\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected 4xx for missing email field");
    }

    @Test(priority = 8, groups = "negative", description = "Verify login fails when password field is missing from payload.")
    @Story("POST /login - Missing Password Field")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks that missing 'password' field in the request body returns HTTP 400 or validation error.")
    public void loginWithMissingPasswordField() {
        String requestBody = "{ \"email\": \"" + InputConstants.USERNAME + "\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected 4xx for missing password field");
    }

    @Test(priority = 9, groups = "negative", description = "Verify login behavior when an unexpected extra field is sent.")
    @Story("POST /login - Extra Field in Payload")
    @Severity(SeverityLevel.MINOR)
    @Description("Validates that adding an extra field like 'role' in the payload either ignores it and logs in or returns a validation error.")
    public void loginWithExtraField() {
        String requestBody = "{ \"email\": \"" + InputConstants.USERNAME + "\", \"password\": \"" + InputConstants.PASSWORD + "\", \"role\": \"admin\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 400, "API should handle extra fields gracefully");
    }

    @Test(priority = 10, groups = "negative", description = "Verify login fails when request JSON is malformed.")
    @Story("POST /login - Malformed JSON")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks that sending improperly formatted JSON in the request body returns HTTP 400 or parsing error.")
    public void loginWithMalformedJson() {
        String malformedJson = "{ \"email\": \"" + InputConstants.USERNAME + "\", \"password\": \"" + InputConstants.PASSWORD + "\" "; // Missing closing brace
        Response response = ApiUtils.postRequest("/login", malformedJson);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected error for malformed JSON");
    }

    @Test(priority = 11, groups = "negative", description = "Verify login fails when SQL injection is attempted in the password field.")
    @Story("POST /login - SQL Injection Attempt")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensures that SQL injection strings in the password field are blocked and return HTTP 400 or appropriate error.")
    public void loginWithSqlInjectionInPassword() {
        String requestBody = "{ \"email\": \"" + InputConstants.USERNAME + "\", \"password\": \"' OR '1'='1\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected error for SQL injection attempt");
    }
}
