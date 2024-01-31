package com.example.javafx_btl;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlayHistoryEntry {
    private final StringProperty formattedTime;
    private final StringProperty timeUsage;
    private final StringProperty lastQuestion;
    private final StringProperty competitiveMode;

    public PlayHistoryEntry(String formattedTime, String timeUsage, String lastQuestion, String competitiveMode) {
        this.formattedTime = new SimpleStringProperty(formattedTime);
        this.timeUsage = new SimpleStringProperty(timeUsage);
        this.lastQuestion = new SimpleStringProperty(lastQuestion);
        this.competitiveMode = new SimpleStringProperty(competitiveMode);
    }

    public String getFormattedTime() {
        return formattedTime.get();
    }

    public StringProperty formattedTimeProperty() {
        return formattedTime;
    }

    public String getTimeUsage() {
        return timeUsage.get();
    }

    public StringProperty timeUsageProperty() {
        return timeUsage;
    }

    public String getLastQuestion() {
        return lastQuestion.get();
    }

    public StringProperty lastQuestionProperty() {
        return lastQuestion;
    }

    public String getCompetitiveMode() {
        return competitiveMode.get();
    }

    public StringProperty competitiveModeProperty() {
        return competitiveMode;
    }
}