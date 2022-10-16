package com.team01.scheduler.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class DashboardStage {
    private final String title;
    private final BooleanProperty finished;

    public DashboardStage(String title) {
        this.title = title;
        this.finished = new SimpleBooleanProperty(false);
    }

    public String getTitle() {
        return title;
    }

    public BooleanProperty finishedProperty() {
        return finished;
    }

    public void setFinished(boolean isFinished) {
        finished.set(isFinished);
    }

    public boolean getFinished() {
        return finished.get();
    }
}
