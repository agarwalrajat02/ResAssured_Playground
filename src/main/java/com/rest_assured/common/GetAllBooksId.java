package com.rest_assured.common;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;

public class GetAllBooksId {

	
	public static int getFirstBookId() {
	    Response response = ApiUtils.getRequest("/books", TokenManager.getToken());
	    if (response.getStatusCode() == 200) {
	        // Assuming response body is a JSON array of book objects
	        return response.jsonPath().getInt("[0].id");
	    } else {
	        throw new RuntimeException("Failed to fetch books for ID extraction. Status: " + response.getStatusCode());
	    }
	}
}
