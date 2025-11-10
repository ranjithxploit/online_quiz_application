package com.quiz.models;

import java.util.List;
import java.util.ArrayList;

public class QuizQuestion {
    private String questionText;
    private List<Option> options;
    private String difficulty; // "EASY", "MEDIUM", "HARD"
    private String category;
    private int points;

    public QuizQuestion() {
        this.options = new ArrayList<>();
        this.points = 1;
    }

    public QuizQuestion(String questionText, List<Option> options, String difficulty, String category, int points) {
        this.questionText = questionText;
        this.options = options;
        this.difficulty = difficulty;
        this.category = category;
        this.points = points;
    }

    // Getters and Setters
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCorrectOptionIndex() {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).isCorrect()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return "QuizQuestion{" +
                "questionText='" + questionText + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", category='" + category + '\'' +
                ", points=" + points +
                '}';
    }
}
