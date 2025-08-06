package com.project.base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BaseClass {

    @BeforeClass
    public void setup() {
        Properties prop = new Properties();
        String baseUrl = null;

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }

            prop.load(input);
            String env = prop.getProperty("env", "dev").toLowerCase();

            if (env.equals("qa")) {
                baseUrl = prop.getProperty("qa.url");
            } else if (env.equals("stage")) {
                baseUrl = prop.getProperty("stage.url");
            } else if (env.equals("prod")) {
                baseUrl = prop.getProperty("prod.url");
            } else {
                baseUrl = prop.getProperty("dev.url");
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage());
        }

        RestAssured.baseURI = baseUrl;
    }
}
