package com.saucedemo.utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * Loads non-secret settings from config.properties and credentials from .env
 * (or real OS environment variables). Prefer env over properties for secrets.
 */
public final class ConfigReader {
    private static final Properties PROPERTIES = new Properties();
    private static final Dotenv DOTENV = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String envKey = toEnvKey(key);

        String fromSystem = System.getenv(envKey);
        if (isPresent(fromSystem)) {
            return fromSystem.trim();
        }

        String fromDotEnv = DOTENV.get(envKey);
        if (isPresent(fromDotEnv)) {
            return fromDotEnv.trim();
        }

        String fromProperties = PROPERTIES.getProperty(key);
        if (isPresent(fromProperties)) {
            return fromProperties.trim();
        }

        throw new IllegalArgumentException(
                "Missing config '" + key + "'. Set " + envKey + " in .env (see .env.example) "
                        + "or add it to config.properties.");
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    private static String toEnvKey(String key) {
        return key.trim().toUpperCase(Locale.ROOT).replace('.', '_');
    }

    private static boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }
}
