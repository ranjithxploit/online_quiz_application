package com.quiz.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "quizzes")
public class Quiz {
    @Id
    private String id;
    private String title;
    private String description;
    private String category;
    private List<QuizQuestion> questions;
    private int totalPoints;
    private int timeLimit; // in minutes
    private Date createdAt;
    private String createdBy; // Admin ID
    private boolean isActive;

    public Quiz() {
        this.questions = new ArrayList<>();
        this.createdAt = new Date();
        this.isActive = true;
    }

    public Quiz(String title, String description, String category, List<QuizQuestion> questions, int timeLimit) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.questions = questions;
        this.timeLimit = timeLimit;
        this.createdAt = new Date();
        this.isActive = true;
        calculateTotalPoints();
    }

    public void calculateTotalPoints() {
        this.totalPoints = questions.stream()
                .mapToInt(QuizQuestion::getPoints)
                .sum();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestion> questions) {
        this.questions = questions;
        calculateTotalPoints();
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", totalPoints=" + totalPoints +
                ", questionsCount=" + (questions != null ? questions.size() : 0) +
                '}';
    }
}
