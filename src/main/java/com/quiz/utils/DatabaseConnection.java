package com.quiz.utils;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DatabaseConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static void connect() {
        try {
            String uri = ConfigLoader.getMongoDbUri();
            String dbName = ConfigLoader.getDatabaseName();

            if (uri == null || uri.equals("your_mongodb_atlas_connection_string_here")) {
                throw new IllegalStateException("MongoDB URI not configured. Please update .env file with your MongoDB Atlas connection string.");
            }

            // Configure POJO codec registry for automatic POJO mapping
            CodecRegistry pojoCodecRegistry = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(uri))
                    .codecRegistry(pojoCodecRegistry)
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(dbName);
            
            // Test connection
            database.listCollectionNames().first();
            
            System.out.println("Connected to MongoDB Atlas successfully!");
        } catch (Exception e) {
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            connect();
        }
        return database;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }

    public static boolean isConnected() {
        try {
            if (database != null) {
                database.listCollectionNames().first();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
