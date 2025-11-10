package com.quiz;

import com.quiz.utils.DatabaseConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Main entry point for the Online Quiz Application
 */
public class MainApplication extends Application {
    
    private TextArea outputArea;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        // Create UI components
        Label titleLabel = new Label("Online Quiz Application");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        Label statusLabel = new Label("Application Status");
        statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        statusLabel.setStyle("-fx-text-fill: #34495e;");
        
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(400);
        outputArea.setFont(Font.font("Consolas", 14));
        outputArea.setStyle("-fx-control-inner-background: #ecf0f1;");
        
        // Layout
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #ffffff;");
        root.getChildren().addAll(titleLabel, statusLabel, outputArea);
        
        // Scene and Stage
        Scene scene = new Scene(root, 700, 550);
        primaryStage.setTitle("Online Quiz Application");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            DatabaseConnection.close();
            Platform.exit();
        });
        primaryStage.show();
        
        // Initialize application in background thread
        new Thread(this::initializeApplication).start();
    }
    
    private void initializeApplication() {
        try {
            appendOutput("==============================================");
            appendOutput("  Online Quiz Application - Starting...      ");
            appendOutput("==============================================\n");
            
            // Configuration is auto-loaded by ConfigLoader static block
            appendOutput("✓ Configuration loaded successfully");
            
            // Test database connection
            DatabaseConnection.connect();
            if (DatabaseConnection.isConnected()) {
                appendOutput("✓ Database connection established");
            } else {
                appendOutput("✗ Failed to connect to database");
            }
            
            appendOutput("\n==============================================");
            appendOutput("  Application started successfully!          ");
            appendOutput("==============================================");
            appendOutput("\nReady for quiz operations...");
            
        } catch (Exception e) {
            appendOutput("\n✗ Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void appendOutput(String text) {
        Platform.runLater(() -> {
            outputArea.appendText(text + "\n");
        });
    }
}
