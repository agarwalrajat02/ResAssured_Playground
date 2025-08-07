package com.pacakge.rest_assured_framework;

import com.project.base.BaseClass;
import com.rest_assured.common.ApiUtils;
import com.rest_assured.common.TokenManager;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetBookByIdApi extends BaseClass {

    @Test(priority = 1, groups = {"sanity", "positive"})
    @Description("Verify that a book can be fetched successfully by valid ID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("GET /books/{id} - Fetch book by ID")
    public void shouldGetBookByIdSuccessfully() {
        String token = TokenManager.getToken();

        

    }
}