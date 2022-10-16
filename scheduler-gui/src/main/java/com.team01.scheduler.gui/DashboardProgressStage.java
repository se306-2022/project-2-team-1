package com.team01.scheduler.gui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;

public class DashboardProgressStage extends DashboardStage {
    private final DoubleProperty progress;

    public DashboardProgressStage(String title) {
        super(title);
        progress = new SimpleDoubleProperty(0.0f);
    }

    public void setProgress(double value) {
        progress.set(value);
    }

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }
}
