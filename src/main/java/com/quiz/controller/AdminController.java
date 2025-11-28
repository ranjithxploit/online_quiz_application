package com.quiz.controller;

import com.quiz.models.Quiz;
import com.quiz.models.QuizQuestion;
import com.quiz.models.User;
import com.quiz.service.QuizService;
import com.quiz.service.UserService;
import com.quiz.utils.GeminiAPIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Admin operations
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;

    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");

            User user = userService.login(email, password);

            // Check if user is admin
            if (!user.isAdmin()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Access denied. Admin privileges required.");
                return ResponseEntity.status(403).body(error);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Admin login successful");
            response.put("user", user);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> adminRegister(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            String fullName = request.get("fullName");
            String adminSecret = request.get("adminSecret");

            // Simple admin secret validation (you can make this more secure)
            if (!"ADMIN_SECRET_2024".equals(adminSecret)) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Invalid admin secret key");
                return ResponseEntity.status(403).body(error);
            }

            User user = userService.registerAdmin(email, password, fullName);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Admin registration successful");
            response.put("user", user);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/generate-quiz")
    public ResponseEntity<?> generateQuiz(@RequestBody Map<String, Object> request) {
        try {
            String userId = (String) request.get("userId");
            String title = (String) request.get("title");
            String description = (String) request.get("description");
            String topic = (String) request.get("topic");
            String category = (String) request.get("category");
            int numberOfQuestions = (int) request.get("numberOfQuestions");
            String difficulty = (String) request.get("difficulty");
            int timeLimit = (int) request.get("timeLimit");

            // Verify admin user
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.isAdmin()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Access denied. Admin privileges required.");
                return ResponseEntity.status(403).body(error);
            }

            // Generate quiz questions using Gemini API
            GeminiAPIClient geminiClient = new GeminiAPIClient();
            List<QuizQuestion> questions = geminiClient.generateQuizQuestions(topic, numberOfQuestions, difficulty);

            // Create quiz
            Quiz quiz = new Quiz(title, description, category, questions, timeLimit);
            quiz.setCreatedBy(userId);
            quiz = quizService.createQuiz(quiz);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Quiz generated successfully");
            response.put("quiz", quiz);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Failed to generate quiz: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/quizzes")
    public ResponseEntity<?> getAllQuizzesForAdmin(@RequestParam String userId) {
        try {
            // Verify admin user
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.isAdmin()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Access denied. Admin privileges required.");
                return ResponseEntity.status(403).body(error);
            }

            List<Quiz> quizzes = quizService.getAllQuizzes();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("quizzes", quizzes);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/quiz/{quizId}")
    public ResponseEntity<?> deleteQuiz(@PathVariable String quizId, @RequestParam String userId) {
        try {
            // Verify admin user
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.isAdmin()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Access denied. Admin privileges required.");
                return ResponseEntity.status(403).body(error);
            }

            quizService.deleteQuiz(quizId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Quiz deleted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
