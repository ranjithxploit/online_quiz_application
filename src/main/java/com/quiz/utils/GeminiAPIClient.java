package com.quiz.utils;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.quiz.models.Option;
import com.quiz.models.QuizQuestion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeminiAPIClient {
    private static final String MODEL_NAME = "gemini-2.5-flash";
    private final Client client;
    private final Gson gson;

    public GeminiAPIClient() {
        String apiKey = ConfigLoader.getGeminiApiKey();
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your_gemini_api_key_here")) {
            throw new IllegalStateException("Gemini API key not configured. Please update .env file.");
        }
        
        // Initialize the Google GenAI Client with API key
        this.client = Client.builder().apiKey(apiKey).build();
        this.gson = new Gson();
    }

    public List<QuizQuestion> generateQuizQuestions(String topic, int numberOfQuestions, String difficulty) throws IOException {
        String prompt = createQuizPrompt(topic, numberOfQuestions, difficulty);
        String response = callGeminiAPI(prompt);
        return parseQuizResponse(response, difficulty, topic);
    }

    private String createQuizPrompt(String topic, int numberOfQuestions, String difficulty) {
        return String.format(
            "Generate exactly %d multiple-choice quiz questions about '%s' at %s difficulty level. " +
            "For each question, provide:\n" +
            "1. The question text\n" +
            "2. Exactly 4 options (A, B, C, D)\n" +
            "3. Mark the correct answer clearly\n\n" +
            "Format your response as a JSON array with this exact structure:\n" +
            "[\n" +
            "  {\n" +
            "    \"question\": \"Question text here?\",\n" +
            "    \"options\": [\n" +
            "      \"Option A text\",\n" +
            "      \"Option B text\",\n" +
            "      \"Option C text\",\n" +
            "      \"Option D text\"\n" +
            "    ],\n" +
            "    \"correctAnswer\": 0\n" +
            "  }\n" +
            "]\n\n" +
            "Note: correctAnswer should be the index (0-3) of the correct option.\n" +
            "Make sure questions are educational, clear, and appropriate for a quiz.\n" +
            "IMPORTANT: Return ONLY the JSON array, no additional text or markdown.",
            numberOfQuestions, topic, difficulty
        );
    }

    private String callGeminiAPI(String prompt) throws IOException {
        try {
            GenerateContentResponse response = client.models.generateContent(
                MODEL_NAME,
                prompt,
                null
            );
            
            String text = response.text();
            if (text == null || text.isEmpty()) {
                throw new IOException("Empty response from Gemini API");
            }
            
            return text;
        } catch (Exception e) {
            throw new IOException("Gemini API request failed: " + e.getMessage(), e);
        }
    }

    private List<QuizQuestion> parseQuizResponse(String response, String difficulty, String category) {
        List<QuizQuestion> questions = new ArrayList<>();
        
        try {
            // Extract JSON array from response
            String jsonString = extractJsonArray(response);
            JsonArray questionsArray = gson.fromJson(jsonString, JsonArray.class);
            
            int points = getPointsByDifficulty(difficulty);
            
            for (int i = 0; i < questionsArray.size(); i++) {
                JsonObject questionObj = questionsArray.get(i).getAsJsonObject();
                
                String questionText = questionObj.get("question").getAsString();
                JsonArray optionsArray = questionObj.getAsJsonArray("options");
                int correctAnswerIndex = questionObj.get("correctAnswer").getAsInt();
                
                List<Option> options = new ArrayList<>();
                for (int j = 0; j < optionsArray.size(); j++) {
                    String optionText = optionsArray.get(j).getAsString();
                    boolean isCorrect = (j == correctAnswerIndex);
                    options.add(new Option(optionText, isCorrect));
                }
                
                QuizQuestion question = new QuizQuestion(questionText, options, difficulty, category, points);
                questions.add(question);
            }
        } catch (Exception e) {
            System.err.println("Error parsing quiz response: " + e.getMessage());
            System.err.println("Raw response: " + response);
            // Fallback: create a sample question if parsing fails
            questions.add(createFallbackQuestion(category, difficulty));
        }
        
        return questions;
    }

    private String extractJsonArray(String text) {
        // Find the first [ and last ]
        int start = text.indexOf('[');
        int end = text.lastIndexOf(']');
        
        if (start != -1 && end != -1 && end > start) {
            return text.substring(start, end + 1);
        }
        
        return "[]";
    }

    private int getPointsByDifficulty(String difficulty) {
        switch (difficulty.toUpperCase()) {
            case "EASY":
                return 1;
            case "MEDIUM":
                return 2;
            case "HARD":
                return 3;
            default:
                return 1;
        }
    }

    private QuizQuestion createFallbackQuestion(String category, String difficulty) {
        List<Option> options = new ArrayList<>();
        options.add(new Option("Sample Option A", true));
        options.add(new Option("Sample Option B", false));
        options.add(new Option("Sample Option C", false));
        options.add(new Option("Sample Option D", false));
        
        return new QuizQuestion(
                "This is a sample question for " + category + ". What is the correct answer?",
                options,
                difficulty,
                category,
                getPointsByDifficulty(difficulty)
        );
    }

    public boolean testConnection() {
        try {
            GenerateContentResponse response = client.models.generateContent(
                MODEL_NAME,
                "Hello, respond with OK",
                null
            );
            return response.text() != null && !response.text().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
