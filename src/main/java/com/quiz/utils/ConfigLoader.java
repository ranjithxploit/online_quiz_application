package com.quiz.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigLoader {
    private static Dotenv dotenv;

    static {
        try {
            dotenv = Dotenv.configure()
                    .directory(".")
                    .ignoreIfMissing()
                    .load();
        } catch (Exception e) {
            System.err.println("Error loading .env file: " + e.getMessage());
            dotenv = null;
        }
    }

    public static String get(String key) {
        if (dotenv != null) {
            return dotenv.get(key);
        }
        return System.getenv(key);
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    public static String getMongoDbUri() {
        return get("MONGODB_URI", "mongodb://localhost:27017");
    }

    public static String getDatabaseName() {
        return get("DATABASE_NAME", "online_quiz_db");
    }

    public static String getGeminiApiKey() {
        return get("GEMINI_API_KEY");
    }

    public static String getAdminUsername() {
        return get("ADMIN_USERNAME", "admin");
    }

    public static String getAdminPassword() {
        return get("ADMIN_PASSWORD", "admin123");
    }
}
