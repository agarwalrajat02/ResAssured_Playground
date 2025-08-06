package com.pacakge.rest_assured_framework;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.project.base.BaseClass;

@Epic("Bookstore API Tests")
@Feature("Health Endpoint Validations")
@Listeners({io.qameta.allure.testng.AllureTestNg.class})
public class HealthCheckAPI extends BaseClass {

    @Test(priority = 1, description = "Verify GET /health returns 200")
    @Story("GET /health - Success")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test checks if the /health endpoint returns HTTP status 200.")
    public void shouldReturnStatus200() {
        Response response = RestAssured.get("/health");
        Allure.step("Send GET request to /health");
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code 200");
    }

    @Test(priority = 2, description = "Verify /health JSON contains 'status' key")
    @Story("GET /health - JSON Schema")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test checks if the 'status' key is present in the JSON response.")
    public void shouldContainStatusKey() {
        Response response = RestAssured.get("/health");
        Allure.step("Send GET request to /health");
        boolean containsKey = response.jsonPath().getMap("").containsKey("status");
        Assert.assertTrue(containsKey, "Expected response to contain key: status");
    }

    @Test(priority = 3, description = "Verify status value is 'up'")
    @Story("GET /health - Status Value")
    @Severity(SeverityLevel.CRITICAL)
    @Description("This test checks if the 'status' value returned by /health is 'up'.")
    public void shouldReturnStatusValueUp() {
        Response response = RestAssured.get("/health");
        Allure.step("Send GET request to /health");
        String statusValue = response.jsonPath().getString("status");
        Assert.assertEquals(statusValue, "up", "Expected status value to be 'up'");
    }

    @Test(priority = 4, description = "Verify POST on /health returns 405")
    @Story("POST /health - Not Allowed")
    @Severity(SeverityLevel.MINOR)
    @Description("This test checks if POST requests to /health are not allowed (405).")
    public void shouldNotAllowPostMethod() {
        Response response = RestAssured.post("/health");
        Allure.step("Send POST request to /health");
        Assert.assertEquals(response.getStatusCode(), 405, "Expected status code 405 for POST method");
    }

    @Test(priority = 5, description = "Verify GET /healthz returns 404")
    @Story("GET /healthz - Invalid Endpoint")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test checks if an invalid health endpoint returns HTTP status 404.")
    public void shouldReturn404ForInvalidEndpoint() {
        Response response = RestAssured.get("/healthz");
        Allure.step("Send GET request to /healthz (invalid endpoint)");
        Assert.assertEquals(response.getStatusCode(), 404, "Expected status code 404 for invalid endpoint");
    }
}
