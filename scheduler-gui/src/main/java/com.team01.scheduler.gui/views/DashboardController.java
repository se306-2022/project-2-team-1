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

import static com.team01.scheduler.algorithm.BranchAndBound.globalShortestPath;
import static com.team01.scheduler.algorithm.BranchAndBound.globalSolutionsFound;

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
    private Label shortestPath;
    @FXML
    private Label shortestPathLabel;
    @FXML
    private Label numberOfSolutions;
    @FXML
    private Label numberOfSolutionsLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        performRotationAnimation();

        //Updates display every time period stated in Keyframe(DURATION)
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), ev -> {
            displayStatistics();
            displayNumberOfSolutionsFound();
            displayShortestPath();

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

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
        //To stop display from jumping numbers when idle, set the lowest memory
        //identifier to be MB
        double info [] = computeMemory();
        switch ((int) info[1]) {
            case 0:
                memoryTypeLabel.setText("MB");
                info[0] /= (1024 ^ 2);
                break;

            case 1:
                memoryTypeLabel.setText("MB");
                info[0] /= 1024;
                break;

            case 2:
                memoryTypeLabel.setText("MB");
                break;

            case 3:
                memoryTypeLabel.setText("GB");
                break;

            default:
                memoryTypeLabel.setText("TOO MUCH MEM");
        }
        info[0] = twoDecimalRounding(info[0]);
        memoryNumberLabel.setText(String.valueOf(info[0]));
    }

    private double [] computeMemory() {

        double info[] = new double[2];

        double usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;

        double byteMultiplier = 0;

        while (usedMemory > 999) {
            usedMemory /= 1024;
            byteMultiplier++;
        }

        info[0] = usedMemory;
        info[1] = byteMultiplier;

        return info;
    }

    private double twoDecimalRounding(double number) {
        return Math.round(number * 100.0) / 100.0;
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

    private void displayShortestPath() {
        shortestPath.setText(String.valueOf(globalShortestPath));
    }

    private void displayNumberOfSolutionsFound() {
        numberOfSolutions.setText(String.valueOf(globalSolutionsFound));

    }

}
