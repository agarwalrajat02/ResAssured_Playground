package com.rest_assured.common;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import com.rest_assured.constants.InputConstants;

public class TokenManager {

    private static String accessToken;

    public static String getToken() {
        if (accessToken == null) {
            Response response = given()
                .header("Content-Type", "application/json")
                .body("{\"email\":\"" + InputConstants.USERNAME + "\",\"password\":\"" + InputConstants.PASSWORD + "\"}")
                .post("/login");

            if (response.getStatusCode() == 200) {
                accessToken = response.jsonPath().getString("access_token");
            } else {
                throw new RuntimeException("Failed to fetch token. Status: " + response.getStatusCode());
            }
        }
        return accessToken;
    }

    public static void setToken(String token) {
        accessToken = token;
    }

    public static void clearToken() {
        accessToken = null;
    }
}
