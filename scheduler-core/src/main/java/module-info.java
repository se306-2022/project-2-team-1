module com.team01.scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires google.options;
    requires org.slf4j;

    opens com.team01.scheduler to javafx.fxml;
    opens com.team01.scheduler.io to google.options;
    exports com.team01.scheduler;
    exports com.team01.scheduler.io;
    exports com.team01.scheduler.algorithm;
    exports com.team01.scheduler.prototype;
    exports com.team01.scheduler.graph.models;
}