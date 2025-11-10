package com.quiz.models;

import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private String fullName;
    private String studentId;
    private int totalScore;
    private int quizzesAttempted;
    private List<ObjectId> attemptIds;

    public Student() {
        super();
        this.setRole("STUDENT");
        this.attemptIds = new ArrayList<>();
        this.totalScore = 0;
        this.quizzesAttempted = 0;
    }

    public Student(String username, String password, String email, String fullName, String studentId) {
        super(username, password, email, "STUDENT");
        this.fullName = fullName;
        this.studentId = studentId;
        this.attemptIds = new ArrayList<>();
        this.totalScore = 0;
        this.quizzesAttempted = 0;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getQuizzesAttempted() {
        return quizzesAttempted;
    }

    public void setQuizzesAttempted(int quizzesAttempted) {
        this.quizzesAttempted = quizzesAttempted;
    }

    public List<ObjectId> getAttemptIds() {
        return attemptIds;
    }

    public void setAttemptIds(List<ObjectId> attemptIds) {
        this.attemptIds = attemptIds;
    }

    public void addAttempt(ObjectId attemptId) {
        if (this.attemptIds == null) {
            this.attemptIds = new ArrayList<>();
        }
        this.attemptIds.add(attemptId);
    }

    public double getAverageScore() {
        if (quizzesAttempted == 0) return 0.0;
        return (double) totalScore / quizzesAttempted;
    }

    @Override
    public String toString() {
        return "Student{" +
                "fullName='" + fullName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", username='" + getUsername() + '\'' +
                ", totalScore=" + totalScore +
                ", quizzesAttempted=" + quizzesAttempted +
                '}';
    }
}
