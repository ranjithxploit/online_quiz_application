package com.quiz.models;

import org.bson.types.ObjectId;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Attempt {
    private ObjectId id;
    private ObjectId studentId;
    private ObjectId quizId;
    private List<Response> responses;
    private int score;
    private int totalPoints;
    private double percentage;
    private Date startedAt;
    private Date completedAt;
    private int timeTaken; // in seconds
    private boolean isCompleted;

    public Attempt() {
        this.responses = new ArrayList<>();
        this.startedAt = new Date();
        this.isCompleted = false;
    }

    public Attempt(ObjectId studentId, ObjectId quizId) {
        this.studentId = studentId;
        this.quizId = quizId;
        this.responses = new ArrayList<>();
        this.startedAt = new Date();
        this.isCompleted = false;
    }

    public void calculateScore() {
        this.score = responses.stream()
                .mapToInt(Response::getPointsEarned)
                .sum();
        if (this.totalPoints > 0) {
            this.percentage = ((double) this.score / this.totalPoints) * 100;
        }
    }

    public void completeAttempt() {
        this.completedAt = new Date();
        this.isCompleted = true;
        if (this.startedAt != null && this.completedAt != null) {
            this.timeTaken = (int) ((this.completedAt.getTime() - this.startedAt.getTime()) / 1000);
        }
        calculateScore();
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getStudentId() {
        return studentId;
    }

    public void setStudentId(ObjectId studentId) {
        this.studentId = studentId;
    }

    public ObjectId getQuizId() {
        return quizId;
    }

    public void setQuizId(ObjectId quizId) {
        this.quizId = quizId;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    public void addResponse(Response response) {
        if (this.responses == null) {
            this.responses = new ArrayList<>();
        }
        this.responses.add(response);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        return "Attempt{" +
                "studentId=" + studentId +
                ", quizId=" + quizId +
                ", score=" + score +
                ", totalPoints=" + totalPoints +
                ", percentage=" + String.format("%.2f", percentage) +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
