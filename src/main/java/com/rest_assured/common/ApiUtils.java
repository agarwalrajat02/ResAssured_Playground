package com.rest_assured.common;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;

public class ApiUtils {

    // GET Request
    public static Response getRequest(String endpoint) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        
        
    }
    
    //GET with Token 
    public static Response getRequest(String endpoint, String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    // POST Request
    public static Response postRequest(String endpoint, String requestBody) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }
    
    public static Response postRequest(String endpoint, String requestBody, String token) {
        return RestAssured
                .given()
                .relaxedHTTPSValidation()
                .redirects().follow(true)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }



    // POST Request with Authorization
    public static Response postRequestWithAuth(String endpoint, String requestBody, String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    // PUT Request
    public static Response putRequest(String endpoint, String requestBody, String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    // PATCH Request
    public static Response patchRequest(String endpoint, String requestBody, String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();
    }

    // DELETE Request
    public static Response deleteRequest(String endpoint, String token) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }
}
