package com.pacakge.rest_assured_framework;

import com.rest_assured.common.SignUpUser;
import com.project.base.BaseClass;
import com.rest_assured.common.RandomDataGenerator;
import com.rest_assured.constants.InputConstants;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.*;

public class SignUpAPI extends BaseClass {

    // HTTP Status Codes
    private static final int STATUS_OK = 200;
    private static final int STATUS_BAD_REQUEST = 400;
    private static final int STATUS_CONFLICT = 409;
    private static final int STATUS_INTERNAL_SERVER_ERROR = 500;

    // Expected Messages
    private static final String MSG_SUCCESS = "user created successfully";
    private static final String MSG_ALREADY_EXISTS = "already";
    private static final String MSG_REQUIRED = "required";
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

        String message;
        if (response.getBody().asString().contains("message")) {
            message = response.jsonPath().getString("message");
        } else if (response.getBody().asString().contains("detail")) {
            try {
                message = response.jsonPath().getString("detail");
            } catch (Exception e) {
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

    @Test(priority = 1, description = "Verify that a new user can successfully sign up with valid email and password.")
    @Story("POST /signup - Success")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that providing a unique email and valid password returns HTTP 200 with 'user created successfully'.")
    public void testSignupSuccess() {
        logTestStart("testSignupSuccess");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        String password = InputConstants.Signup_Password;

        Response response = SignUpUser.signUpUser(email, password);
        assertResponse(response, STATUS_OK, MSG_SUCCESS);
    }

    @Test(priority = 2, description = "Verify signup works with a complex password containing special characters.")
    @Story("POST /signup - Success with complex password")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks that signup succeeds with a valid email and a complex password containing uppercase, lowercase, numbers, and special characters.")
    public void testSignupWithComplexPassword() {
        logTestStart("testSignupWithComplexPassword");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        String password = "P@ssw0rd!23";

        Response response = SignUpUser.signUpUser(email, password);
        assertResponse(response, STATUS_OK, MSG_SUCCESS);
    }

    // --- Negative Test Cases ---

    @Test(priority = 3, description = "Verify signup fails for an already registered email.")
    @Story("POST /signup - Duplicate Email")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that attempting to sign up with an already registered email returns HTTP 400 with 'Email already registered' message.")
    public void testSignupWithExistingEmail() {
        logTestStart("testSignupWithExistingEmail");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        String password = InputConstants.Signup_Password;

        SignUpUser.signUpUser(email, password);
        Response response = SignUpUser.signUpUser(email, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_ALREADY_EXISTS);
    }

    @Test(priority = 4, description = "Verify signup fails when email is empty.")
    @Story("POST /signup - Empty Email")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks that submitting signup request with an empty email returns HTTP 400 with an email validation error.")
    public void testSignupWithEmptyEmail() {
        logTestStart("testSignupWithEmptyEmail");
        String email = "";
        String password = InputConstants.Signup_Password;

        Response response = SignUpUser.signUpUser(email, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_INVALID_EMAIL);
    }

    @Test(priority = 5, description = "Verify signup fails for invalid email format.")
    @Story("POST /signup - Invalid Email Format")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that signup with an invalid email format returns HTTP 400 with an email validation error.")
    public void testSignupWithInvalidEmailFormat() {
        logTestStart("testSignupWithInvalidEmailFormat");
        String email = "invalidemail.com";
        String password = InputConstants.Signup_Password;

        Response response = SignUpUser.signUpUser(email, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_INVALID_EMAIL);
    }

    @Test(priority = 6, description = "Verify signup fails when password is empty.")
    @Story("POST /signup - Empty Password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validates that providing an empty password during signup returns HTTP 400 with 'required' field message.")
    public void testSignupWithEmptyPassword() {
        logTestStart("testSignupWithEmptyPassword");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        String password = "";

        Response response = SignUpUser.signUpUser(email, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_REQUIRED);
    }

    @Test(priority = 7, description = "Verify signup fails when password is null.")
    @Story("POST /signup - Null Password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that signup request without a password returns HTTP 400 with 'required' field message.")
    public void testSignupWithNullPassword() {
        logTestStart("testSignupWithNullPassword");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        String password = null;

        Response response = SignUpUser.signUpUser(email, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_REQUIRED);
    }

    @Test(priority = 8, description = "Verify signup fails when password is shorter than required length.")
    @Story("POST /signup - Short Password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks that signup with a password shorter than the minimum allowed length returns HTTP 400 with 'password is too short'.")
    public void testSignupWithShortPassword() {
        logTestStart("testSignupWithShortPassword");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        String shortPassword = "aB1!";

        Response response = SignUpUser.signUpUser(email, shortPassword);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_PASSWORD_TOO_SHORT);
    }

    @Test(priority = 9, description = "Verify signup fails with an empty request payload.")
    @Story("POST /signup - Empty Payload")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validates that sending an empty request body returns HTTP 400 with 'Input payload validation failed'.")
    public void testSignupWithEmptyPayload() {
        logTestStart("testSignupWithEmptyPayload");
        Response response = SignUpUser.signUpUser(null, null);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_INVALID_INPUT);
    }

    @Test(priority = 10, description = "Verify signup fails when email is missing.")
    @Story("POST /signup - Missing Email")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that signup without the email field returns HTTP 400 with 'required' field message.")
    public void testSignupWithoutEmail() {
        logTestStart("testSignupWithoutEmail");
        String password = InputConstants.Signup_Password;
        Response response = SignUpUser.signUpUser(null, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_REQUIRED);
    }

    @Test(priority = 11, description = "Verify signup fails when password is missing.")
    @Story("POST /signup - Missing Password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensures that signup without the password field returns HTTP 400 with 'required' field message.")
    public void testSignupWithoutPassword() {
        logTestStart("testSignupWithoutPassword");
        String email = RandomDataGenerator.generateUniqueEmail(InputConstants.SignUp_Name);
        Response response = SignUpUser.signUpUser(email, null);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_REQUIRED);
    }

    @Test(priority = 12, description = "Verify signup fails when email contains spaces.")
    @Story("POST /signup - Email With Spaces")
    @Severity(SeverityLevel.NORMAL)
    @Description("Checks that signup with an email containing spaces returns HTTP 400 with an email validation error.")
    public void testSignupWithEmailContainingSpaces() {
        logTestStart("testSignupWithEmailContainingSpaces");
        String emailWithSpaces = "test email@example.com";
        String password = InputConstants.Signup_Password;

        Response response = SignUpUser.signUpUser(emailWithSpaces, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_INVALID_EMAIL);
    }

    @Test(priority = 13, description = "Verify signup fails when email exceeds maximum allowed length.")
    @Story("POST /signup - Very Long Email")
    @Severity(SeverityLevel.MINOR)
    @Description("Ensures that signup with an excessively long email returns HTTP 400 with an email validation error.")
    public void testSignupWithVeryLongEmail() {
        logTestStart("testSignupWithVeryLongEmail");
        String longEmail = "a".repeat(250) + "@longdomain.com";
        String password = InputConstants.Signup_Password;

        Response response = SignUpUser.signUpUser(longEmail, password);
        assertResponse(response, STATUS_BAD_REQUEST, MSG_INVALID_EMAIL);
    }
}
