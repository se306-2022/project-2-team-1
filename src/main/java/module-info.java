module com.team01.scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires google.options;
    requires org.slf4j;


    opens com.team01.scheduler to javafx.fxml;
    exports com.team01.scheduler;
}