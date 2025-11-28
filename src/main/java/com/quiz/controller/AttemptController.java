package com.quiz.controller;

import com.quiz.models.Attempt;
import com.quiz.repository.QuizRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.quiz.utils.DatabaseConnection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/attempts")
@CrossOrigin(origins = "*")
public class AttemptController {

    private MongoCollection<Document> attemptCollection;
    private MongoCollection<Document> userCollection;
    private MongoCollection<Document> quizCollection;

    public AttemptController() {
        try {
            MongoDatabase database = DatabaseConnection.getDatabase();
            this.attemptCollection = database.getCollection("attempts");
            this.userCollection = database.getCollection("users");
            this.quizCollection = database.getCollection("quizzes");
        } catch (Exception e) {
            System.err.println("Error connecting to database for attempts: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> saveAttempt(@RequestBody Map<String, Object> attemptData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Document attemptDoc = new Document()
                .append("quizId", attemptData.get("quizId"))
                .append("quizTitle", attemptData.get("quizTitle"))
                .append("userId", attemptData.get("userId"))
                .append("userName", attemptData.get("userName"))
                .append("userEmail", attemptData.get("userEmail"))
                .append("correctAnswers", attemptData.get("correctAnswers"))
                .append("wrongAnswers", attemptData.get("wrongAnswers"))
                .append("skippedAnswers", attemptData.get("skippedAnswers"))
                .append("earnedPoints", attemptData.get("earnedPoints"))
                .append("totalPoints", attemptData.get("totalPoints"))
                .append("percentage", attemptData.get("percentage"))
                .append("timeTaken", attemptData.get("timeTaken"))
                .append("completedAt", new Date());

            attemptCollection.insertOne(attemptDoc);

            response.put("success", true);
            response.put("message", "Attempt saved successfully");
            response.put("attemptId", attemptDoc.getObjectId("_id").toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error saving attempt: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllAttempts() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (attemptCollection == null) {
                MongoDatabase database = DatabaseConnection.getDatabase();
                this.attemptCollection = database.getCollection("attempts");
            }
            
            List<Document> attempts = attemptCollection
                .find()
                .sort(new Document("completedAt", -1))
                .into(new ArrayList<>());

            System.out.println("Found " + attempts.size() + " attempts");

            List<Map<String, Object>> attemptList = new ArrayList<>();
            for (Document doc : attempts) {
                Map<String, Object> attempt = new HashMap<>();
                attempt.put("id", doc.getObjectId("_id").toString());
                attempt.put("quizId", doc.get("quizId"));
                attempt.put("quizTitle", doc.get("quizTitle"));
                attempt.put("userId", doc.get("userId"));
                attempt.put("userName", doc.get("userName"));
                attempt.put("userEmail", doc.get("userEmail"));
                attempt.put("correctAnswers", doc.get("correctAnswers"));
                attempt.put("wrongAnswers", doc.get("wrongAnswers"));
                attempt.put("skippedAnswers", doc.get("skippedAnswers"));
                attempt.put("earnedPoints", doc.get("earnedPoints"));
                attempt.put("totalPoints", doc.get("totalPoints"));
                attempt.put("percentage", doc.get("percentage"));
                attempt.put("timeTaken", doc.get("timeTaken"));
                attempt.put("completedAt", doc.getDate("completedAt"));
                attemptList.add(attempt);
            }

            response.put("success", true);
            response.put("attempts", attemptList);
            response.put("count", attemptList.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error fetching attempts: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Check if user has already attempted a specific quiz
    @GetMapping("/check/{quizId}/{userId}")
    public ResponseEntity<Map<String, Object>> checkAttempt(@PathVariable String quizId, @PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (attemptCollection == null) {
                MongoDatabase database = DatabaseConnection.getDatabase();
                this.attemptCollection = database.getCollection("attempts");
            }
            
            Document query = new Document("quizId", quizId).append("userId", userId);
            Document existingAttempt = attemptCollection.find(query).first();
            
            if (existingAttempt != null) {
                response.put("hasAttempted", true);
                response.put("attempted", true);
                response.put("percentage", existingAttempt.get("percentage"));
                response.put("earnedPoints", existingAttempt.get("earnedPoints"));
                response.put("totalPoints", existingAttempt.get("totalPoints"));
                response.put("completedAt", existingAttempt.getDate("completedAt"));
            } else {
                response.put("hasAttempted", false);
                response.put("attempted", false);
            }
            
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error checking attempt: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserAttempts(@PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Document> attempts = attemptCollection
                .find(new Document("userId", userId))
                .sort(new Document("createdAt", -1))
                .into(new ArrayList<>());

            List<Map<String, Object>> attemptList = new ArrayList<>();
            for (Document doc : attempts) {
                Map<String, Object> attempt = new HashMap<>();
                attempt.put("id", doc.getObjectId("_id").toString());
                attempt.put("quizId", doc.getString("quizId"));
                attempt.put("correctAnswers", doc.get("correctAnswers"));
                attempt.put("wrongAnswers", doc.get("wrongAnswers"));
                attempt.put("skippedAnswers", doc.get("skippedAnswers"));
                attempt.put("earnedPoints", doc.get("earnedPoints"));
                attempt.put("totalPoints", doc.get("totalPoints"));
                attempt.put("percentage", doc.get("percentage"));
                attempt.put("completedAt", doc.get("completedAt"));
                attemptList.add(attempt);
            }

            response.put("success", true);
            response.put("attempts", attemptList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching attempts: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<Map<String, Object>> getQuizAttempts(@PathVariable String quizId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Document> attempts = attemptCollection
                .find(new Document("quizId", quizId))
                .sort(new Document("createdAt", -1))
                .into(new ArrayList<>());

            List<Map<String, Object>> attemptList = new ArrayList<>();
            for (Document doc : attempts) {
                Map<String, Object> attempt = new HashMap<>();
                attempt.put("id", doc.getObjectId("_id").toString());
                attempt.put("userId", doc.getString("userId"));
                attempt.put("correctAnswers", doc.get("correctAnswers"));
                attempt.put("wrongAnswers", doc.get("wrongAnswers"));
                attempt.put("skippedAnswers", doc.get("skippedAnswers"));
                attempt.put("earnedPoints", doc.get("earnedPoints"));
                attempt.put("totalPoints", doc.get("totalPoints"));
                attempt.put("percentage", doc.get("percentage"));
                attempt.put("completedAt", doc.get("completedAt"));
                attemptList.add(attempt);
            }

            response.put("success", true);
            response.put("attempts", attemptList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching quiz attempts: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
