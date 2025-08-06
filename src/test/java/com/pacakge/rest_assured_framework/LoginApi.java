package com.pacakge.rest_assured_framework;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.project.base.BaseClass;
import com.rest_assured.common.ApiUtils;
import com.rest_assured.common.TokenManager;
import com.rest_assured.constants.InputConstants;

import io.restassured.response.Response;

public class LoginApi extends BaseClass {

    /**
     * Positive test: Login with valid credentials
     */
	  @Test(priority = 1, groups = { "positive", "sanity" })
	    public void loginWithValidCredentials() {
	        String requestBody = "{ \"email\": \"" + InputConstants.USERNAME + "\", \"password\": \"" + InputConstants.PASSWORD + "\" }";
	        Response response = ApiUtils.postRequest("/login", requestBody);

	        System.out.println("Status Code: " + response.getStatusCode());
	        System.out.println("Response Body: " + response.getBody().asString());

	        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 OK for valid login");

	        String token = response.jsonPath().getString("access_token");
	        Assert.assertNotNull(token, "Token should not be null after successful login");

	        System.out.println("Extracted token: " + token);
	        // No need to call TokenManager.setToken(token) anymore
	    }

    /**
     * Negative test: Invalid email
     */
    @Test(priority = 2, groups = "negative")
    public void loginWithInvalidEmail() {
        String requestBody = "{ \"email\": \"wrong.email@test.com\", \"password\": \"" + InputConstants.PASSWORD + "\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertEquals(response.getStatusCode(), 400, "Expected 400 for invalid email");
    }

    /**
     * Negative test: Invalid password
     */
    @Test(priority = 3, groups = "negative")
    public void loginWithInvalidPassword() {
        String requestBody = "{ \"email\": \"" + InputConstants.USERNAME + "\", \"password\": \"wrongPass\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertEquals(response.getStatusCode(), 400, "Expected 400 for invalid password");
    }

    /**
     * Negative test: Blank email
     */
    @Test(priority = 4, groups = "negative")
    public void loginWithBlankEmail() {
        String requestBody = "{ \"email\": \"\", \"password\": \"" + InputConstants.PASSWORD + "\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected 4xx for blank email");
    }

    /**
     * Negative test: Blank password
     */
    @Test(priority = 5, groups = "negative")
    public void loginWithBlankPassword() {
        String requestBody = "{ \"email\": \"" + InputConstants.USERNAME + "\", \"password\": \"\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected 4xx for blank password");
    }

    /**
     * Negative test: Both fields blank
     */
    @Test(priority = 6, groups = "negative")
    public void loginWithBlankFields() {
        String requestBody = "{ \"email\": \"\", \"password\": \"\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected 4xx for blank fields");
    }

    /**
     * Negative test: Missing email field
     */
    @Test(priority = 7, groups = "negative")
    public void loginWithMissingEmailField() {
        String requestBody = "{ \"password\": \"" + InputConstants.PASSWORD + "\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected 4xx for missing email field");
    }

    /**
     * Negative test: Missing password field
     */
    @Test(priority = 8, groups = "negative")
    public void loginWithMissingPasswordField() {
        String requestBody = "{ \"email\": \"" + InputConstants.USERNAME + "\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected 4xx for missing password field");
    }

    /**
     * Negative test: Extra unexpected field
     */
    @Test(priority = 9, groups = "negative")
    public void loginWithExtraField() {
        String requestBody = "{ \"email\": \"" + InputConstants.USERNAME + "\", \"password\": \"" + InputConstants.PASSWORD + "\", \"role\": \"admin\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 400, "API should handle extra fields gracefully");
    }

    /**
     * Negative test: Malformed JSON
     */
    @Test(priority = 10, groups = "negative")
    public void loginWithMalformedJson() {
        String malformedJson = "{ \"email\": \"" + InputConstants.USERNAME + "\", \"password\": \"" + InputConstants.PASSWORD + "\" "; // missing closing brace
        Response response = ApiUtils.postRequest("/login", malformedJson);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected error for malformed JSON");
    }

    /**
     * Negative test: SQL Injection attempt
     */
    @Test(priority = 11, groups = "negative")
    public void loginWithSqlInjectionInPassword() {
        String requestBody = "{ \"email\": \"" + InputConstants.USERNAME + "\", \"password\": \"' OR '1'='1\" }";
        Response response = ApiUtils.postRequest("/login", requestBody);
        Assert.assertTrue(response.getStatusCode() >= 400, "Expected error for SQL injection attempt");
    }
}
