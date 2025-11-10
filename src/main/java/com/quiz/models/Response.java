package com.quiz.models;

public class Response {
    private int questionIndex;
    private int selectedOptionIndex;
    private boolean isCorrect;
    private int pointsEarned;

    public Response() {
    }

    public Response(int questionIndex, int selectedOptionIndex) {
        this.questionIndex = questionIndex;
        this.selectedOptionIndex = selectedOptionIndex;
    }

    // Getters and Setters
    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    public int getSelectedOptionIndex() {
        return selectedOptionIndex;
    }

    public void setSelectedOptionIndex(int selectedOptionIndex) {
        this.selectedOptionIndex = selectedOptionIndex;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    @Override
    public String toString() {
        return "Response{" +
                "questionIndex=" + questionIndex +
                ", selectedOptionIndex=" + selectedOptionIndex +
                ", isCorrect=" + isCorrect +
                ", pointsEarned=" + pointsEarned +
                '}';
    }
}
