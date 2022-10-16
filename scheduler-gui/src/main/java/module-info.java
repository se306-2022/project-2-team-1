module com.team01.scheduler.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires google.options;
    requires org.slf4j;
    requires com.team01.scheduler;

    opens com.team01.scheduler.gui to javafx.fxml;
    opens com.team01.scheduler.gui.views to javafx.fxml;

    exports com.team01.scheduler.gui;
    exports com.team01.scheduler.gui.views;
}