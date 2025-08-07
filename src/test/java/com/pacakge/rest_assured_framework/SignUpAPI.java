package com.pacakge.rest_assured_framework;

import com.rest_assured.common.SignUpUser;
import com.project.base.BaseClass;
import com.rest_assured.common.RandomDataGenerator;
import com.rest_assured.constants.InputConstants;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class SignUpAPI extends BaseClass {

    // HTTP Status Codes
    private static final int STATUS_OK = 200;
    private static final int STATUS_BAD_REQUEST = 400;
    private static final int STATUS_CONFLICT = 409;
    private static final int STATUS_INTERNAL_SERVER_ERROR = 500;

    // Expected Messages
    private static final String MSG_SUCCESS = "user created successfully";
    private static final String MSG_ALREADY_EXISTS = "already"; // matches "Email already registered"
    private static final String MSG_REQUIRED = "required"; // matches "required" or "field required"
    private static final String MSG_INVALID_EMAIL = "email";
    private static final String MSG_INTERNAL_SERVER_ERROR = "Internal Server Error";
    private static final String MSG_PASSWORD_TOO_SHORT = "password is too short";
    private static final String MSG_INVALID_PASSWORD = "invalid password";
    private static final String MSG_INVALID_CREDENTIALS = "Invalid username/password";
    private static final String MSG_INVALID_INPUT = "Input payload validation failed";

    private void logTestStart(String testName) {
        System.out.println("\n==============================");
        System.out.println(">> Running Test: " + testName);
        System.out.println("==============================");
    }

    private void assertResponse(Response response, int expectedStatus, String expectedMessageContains) {
        System.out.println("====== API Response ======");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body:\n" + response.asPrettyString());

        Assert.assertEquals(response.getStatusCode(), expectedStatus,
                "Unexpected status code. Response: " + response.asPrettyString());

        String message = null;
        if (response.getBody().asString().contains("message")) {
            message = response.jsonPath().getString("message");
        } else if (response.getBody().asString().contains("detail")) {
            // Check if 'detail' is a simple string or a JSON object
            try {
                message = response.jsonPath().getString("detail");
            } catch (Exception e) {
                // If it's a list or map, convert it to a string for assertion
                message = response.jsonPath().get("detail").toString();
            }
        } else {
            message = response.getBody().asString();
        }

        System.out.println("Extracted Message: " + message);

        Assert.assertNotNull(message, "Message field is null in response: " + response.asPrettyString());
        Assert.assertTrue(message.toLowerCase().contains(expectedMessageContains.toLowerCase()),
                "Expected message to contain '" + expectedMessageContains + "' but got: " + message);
    }

    // --- Positive Test Cases ---

    @Test(priority = 1)
    public void testSignupSuccess() {
        logTestStart("testSignupSuccess");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        String password = InputConstants.Signup_Password;

        Response response = SignUpUser.signUpUser(email, password);
        assertResponse(response, STATUS_OK, MSG_SUCCESS);
    }

    @Test(priority = 2)
    public void testSignupWithComplexPassword() {
        logTestStart("testSignupWithComplexPassword");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        // Using a password with special characters, numbers, and both cases
        String password = "P@ssw0rd!23";

        Response response = SignUpUser.signUpUser(email, password);
        assertResponse(response, STATUS_OK, MSG_SUCCESS);
    }


    // --- Negative Test Cases ---

    @Test(priority = 3)
    public void testSignupWithExistingEmail() {
        logTestStart("testSignupWithExistingEmail");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        String password = InputConstants.Signup_Password;

        SignUpUser.signUpUser(email, password); // First signup
        Response response = SignUpUser.signUpUser(email, password); // Second signup
        assertResponse(response, STATUS_BAD_REQUEST, MSG_ALREADY_EXISTS);
    }

    @Test(priority = 4)
    public void testSignupWithEmptyEmail() {
        logTestStart("testSignupWithEmptyEmail");
        String email = "";
        String password = InputConstants.Signup_Password;

        Response response = SignUpUser.signUpUser(email, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_INVALID_EMAIL);
    }

    @Test(priority = 5)
    public void testSignupWithInvalidEmailFormat() {
        logTestStart("testSignupWithInvalidEmailFormat");
        String email = "invalidemail.com";
        String password = InputConstants.Signup_Password;

        Response response = SignUpUser.signUpUser(email, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_INVALID_EMAIL);
    }

    @Test(priority = 6)
    public void testSignupWithEmptyPassword() {
        logTestStart("testSignupWithEmptyPassword");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        String password = "";

        Response response = SignUpUser.signUpUser(email, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_REQUIRED);
    }

    @Test(priority = 7)
    public void testSignupWithNullPassword() {
        logTestStart("testSignupWithNullPassword");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        String password = null;

        Response response = SignUpUser.signUpUser(email, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_REQUIRED);
    }

    @Test(priority = 8)
    public void testSignupWithShortPassword() {
        logTestStart("testSignupWithShortPassword");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        String shortPassword = "aB1!"; // Assuming min length is > 4

        Response response = SignUpUser.signUpUser(email, shortPassword);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_PASSWORD_TOO_SHORT);
    }

    @Test(priority = 9)
    public void testSignupWithEmptyPayload() {
        logTestStart("testSignupWithEmptyPayload");
        Response response = SignUpUser.signUpUser(null, null); // Pass nulls to simulate an empty payload
        assertResponse(response, STATUS_BAD_REQUEST, MSG_INVALID_INPUT);
    }

    @Test(priority = 10)
    public void testSignupWithoutEmail() {
        logTestStart("testSignupWithoutEmail");
        String password = InputConstants.Signup_Password;
        Response response = SignUpUser.signUpUser(null, password); // Omit email
        assertResponse(response, STATUS_BAD_REQUEST, MSG_REQUIRED);
    }

    @Test(priority = 11)
    public void testSignupWithoutPassword() {
        logTestStart("testSignupWithoutPassword");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        Response response = SignUpUser.signUpUser(email, null); // Omit password
        assertResponse(response, STATUS_BAD_REQUEST, MSG_REQUIRED);
    }

    @Test(priority = 12)
    public void testSignupWithEmailContainingSpaces() {
        logTestStart("testSignupWithEmailContainingSpaces");
        String emailWithSpaces = "test email@example.com";
        String password = InputConstants.Signup_Password;

        Response response = SignUpUser.signUpUser(emailWithSpaces, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_INVALID_EMAIL);
    }

    @Test(priority = 13)
    public void testSignupWithVeryLongEmail() {
        logTestStart("testSignupWithVeryLongEmail");
        String longEmail = "a".repeat(250) + "@longdomain.com";
        String password = InputConstants.Signup_Password;

        Response response = SignUpUser.signUpUser(longEmail, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_INVALID_EMAIL);
    }
}
