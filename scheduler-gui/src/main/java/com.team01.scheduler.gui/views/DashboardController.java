package com.team01.scheduler.gui.views;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DashboardController implements Initializable {

    @FXML
    private Circle c1;
    @FXML
    private Circle c2;
    @FXML
    private Circle c3;
    @FXML
    private Circle c4;
    @FXML
    private Circle c5;
    @FXML
    private Circle c6;
    @FXML
    private Circle c7;
    @FXML
    private Circle c8;
    @FXML
    private Circle c9;
    @FXML
    private Label memoryNumberLabel;
    @FXML
    private Label memoryTypeLabel;
    @FXML
    private Label FPS;
    @FXML
    private Label FPSLabel;
    @FXML
    private Label shortestCostLabel;
    @FXML
    private Label costLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        performRotationAnimation();
        displayStatistics();
    }

    private void performRotationAnimation() {
        setRotate(c1, true, 360, 5);
        setRotate(c2, true, 180, 10);
        setRotate(c3, true, 145, 15);

        setRotate(c4, true, 360, 6);
        setRotate(c5, true, 180, 12);
        setRotate(c6, true, 145, 18);

        setRotate(c7, true, 360, 7);
        setRotate(c8, true, 180, 14);
        setRotate(c9, true, 145, 21);
    }

    private void displayStatistics() {

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
            double info [] = computeMemory();

            memoryNumberLabel.setText(String.valueOf(info[0]));

            switch ((int) info[1]) {
                case 0:
                    memoryTypeLabel.setText("B");
                    break;

                case 1:
                    memoryTypeLabel.setText("KB");
                    break;

                case 2:
                    memoryTypeLabel.setText("MB");
                    break;

                case 3:
                    memoryTypeLabel.setText("GB");
                    break;

            }

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private double [] computeMemory() {

        double info [] = new double [2];

        double usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;

        double byteMultiplier = 0;

        while (usedMemory > 999) {
            usedMemory /= 1024;
            byteMultiplier++;
        }

        info[0] = Math.round(usedMemory*100.0)/100.0;
        info[1] = byteMultiplier;

        return info;
    }


    private void setRotate(Circle c, boolean reverse, int angle, double duration) {

        RotateTransition rt = new RotateTransition(Duration.seconds(duration), c);

        rt.setAutoReverse(reverse);

        rt.setByAngle(angle);

        rt.setDelay(Duration.seconds(0));

        rt.setRate(3);

        rt.setCycleCount(Animation.INDEFINITE);

        rt.play();

    }
}
