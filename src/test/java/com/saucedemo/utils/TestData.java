package com.saucedemo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads JSON fixtures from {@code src/test/resources/testdata/}
 * (Selenium equivalent of Cypress {@code cy.fixture()}).
 */
public final class TestData {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Map<String, JsonNode> CACHE = new ConcurrentHashMap<>();

    private TestData() {
    }

    public static JsonNode load(String fileName) {
        return CACHE.computeIfAbsent(fileName, TestData::readFile);
    }

    public static String getString(String fileName, String... path) {
        return resolve(fileName, path).asText();
    }

    public static int getInt(String fileName, String... path) {
        return resolve(fileName, path).asInt();
    }

    private static JsonNode resolve(String fileName, String... path) {
        JsonNode node = load(fileName);
        for (String key : path) {
            node = node.path(key);
        }
        if (node.isMissingNode() || node.isNull()) {
            throw new IllegalArgumentException(
                    "Missing testdata path in " + fileName + ": " + String.join(".", path));
        }
        return node;
    }

    private static JsonNode readFile(String fileName) {
        String resourcePath = "testdata/" + fileName;
        try (InputStream input = TestData.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalStateException(resourcePath + " not found on classpath");
            }
            return MAPPER.readTree(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + resourcePath, e);
        }
    }
}
