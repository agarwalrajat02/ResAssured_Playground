package com.rest_assured.common;

import java.util.Random;
import java.util.UUID;

public class RandomDataGenerator {

    private static final Random random = new Random();

    public static String generateUniqueEmail(String baseName) {
        return baseName + "+" + System.currentTimeMillis() + "@gmail.com";
    }

    public static String generateRandomName() {
        return "Book_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generateRandomAuthor() {
        return "Author_" + UUID.randomUUID().toString().substring(0, 6);
    }

    public static String generateRandomSummary() {
        return "This is a summary of book " + generateRandomName();
    }

    public static int generateRandomId() {
        return 1000 + random.nextInt(9000); // Random ID between 1000 and 9999
    }

    public static int generateRandomPublishedYear() {
        return 1990 + random.nextInt(35); // Random year between 1990 and 2024
    }
}
