package com.rest_assured.common;

import io.restassured.RestAssured;
	
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

import com.project.base.BaseClass;

public class SignUpUser extends BaseClass {

    public static Response signUpUser(String email, String password) {
        JSONObject payload = new JSONObject();
        payload.put("email", email);
        payload.put("password", password);

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(payload.toString())
                .when()
                .post("/signup");

        System.out.println("Signup Response: " + response.prettyPrint()); // optional for debug

        return response;
    }
}
