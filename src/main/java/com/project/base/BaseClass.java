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

            // Get env from system property > env var > config file
            String env = System.getProperty("env");
            if (env == null || env.isEmpty()) {
                env = System.getenv("TARGET_ENV");
            }
            if (env == null || env.isEmpty()) {
                env = prop.getProperty("env", "dev");
            }

            env = env.toLowerCase(); // Normalize

            switch (env) {
                case "qa":
                    baseUrl = prop.getProperty("qa.url");
                    break;
                case "stage":
                    baseUrl = prop.getProperty("stage.url");
                    break;
                case "prod":
                    baseUrl = prop.getProperty("prod.url");
                    break;
                case "dev":
                default:
                    baseUrl = prop.getProperty("dev.url");
                    break;
            }

            System.out.println("Running tests against: " + baseUrl);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage());
        }

        RestAssured.baseURI = baseUrl;
    }
}
