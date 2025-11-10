package com.quiz.models;

public class Option {
    private String optionText;
    private boolean isCorrect;

    public Option() {
    }

    public Option(String optionText, boolean isCorrect) {
        this.optionText = optionText;
        this.isCorrect = isCorrect;
    }

    // Getters and Setters
    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return "Option{" +
                "optionText='" + optionText + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
