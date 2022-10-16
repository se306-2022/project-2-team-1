module com.team01.scheduler.cli {
    requires google.options;
    requires org.slf4j;
    requires com.team01.scheduler;
    requires com.team01.scheduler.gui;
    requires javafx.base;
    requires javafx.graphics;

    opens com.team01.scheduler.cli.io to google.options;
}