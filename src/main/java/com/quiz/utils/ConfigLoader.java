package com.quiz.utils;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigLoader {
    private static Dotenv dotenv;
    private static Properties fallbackProps;

    static {
        // Try dotenv first
        try {
            String workDir = System.getProperty("user.dir");
            System.out.println("[ConfigLoader] Working directory: " + workDir);
            
            File envFile = new File(workDir, ".env");
            System.out.println("[ConfigLoader] Looking for .env at: " + envFile.getAbsolutePath());
            System.out.println("[ConfigLoader] .env file exists: " + envFile.exists());
            
            if (envFile.exists()) {
                dotenv = Dotenv.configure()
                        .directory(workDir)
                        .ignoreIfMissing()
                        .load();
                
                String apiKey = dotenv.get("GEMINI_API_KEY");
                System.out.println("[ConfigLoader] Dotenv loaded. GEMINI_API_KEY present: " + (apiKey != null && !apiKey.isEmpty()));
            }
        } catch (Exception e) {
            System.err.println("[ConfigLoader] Dotenv error: " + e.getMessage());
            dotenv = null;
        }
        
        // Fallback: manually read .env as properties
        if (dotenv == null || get("GEMINI_API_KEY") == null) {
            try {
                File envFile = new File(System.getProperty("user.dir"), ".env");
                if (envFile.exists()) {
                    fallbackProps = new Properties();
                    try (FileInputStream fis = new FileInputStream(envFile)) {
                        fallbackProps.load(fis);
                    }
                    System.out.println("[ConfigLoader] Fallback properties loaded");
                }
            } catch (Exception e) {
                System.err.println("[ConfigLoader] Fallback load error: " + e.getMessage());
            }
        }
    }

    public static String get(String key) {
        // Try dotenv first
        if (dotenv != null) {
            String value = dotenv.get(key);
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        
        // Try fallback properties
        if (fallbackProps != null) {
            String value = fallbackProps.getProperty(key);
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        
        // Try system environment
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
