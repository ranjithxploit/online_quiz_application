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

    public AttemptController() {
        try {
            MongoDatabase database = DatabaseConnection.getDatabase();
            this.attemptCollection = database.getCollection("attempts");
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
                .append("userId", attemptData.get("userId"))
                .append("correctAnswers", attemptData.get("correctAnswers"))
                .append("wrongAnswers", attemptData.get("wrongAnswers"))
                .append("skippedAnswers", attemptData.get("skippedAnswers"))
                .append("earnedPoints", attemptData.get("earnedPoints"))
                .append("totalPoints", attemptData.get("totalPoints"))
                .append("percentage", attemptData.get("percentage"))
                .append("completedAt", attemptData.get("completedAt"))
                .append("createdAt", new Date());

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
