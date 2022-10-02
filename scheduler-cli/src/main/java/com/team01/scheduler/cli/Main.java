package com.team01.scheduler.cli;

import com.team01.scheduler.cli.io.InputController;

public class Main {

    public static void attachVisualisation(String[] args) {
        throw new UnsupportedOperationException("Visualisation is not implemented yet!");
    }

    /**
     * Different run configuration depending on how the application was called.
     * @param args command line input
     */
    public static void main(String[] args) {
        // initialise inputcontroller to hand input arguments
        InputController ic = new InputController(args);
        switch (ic.getInvocationType())
        {
            case VISUALIZATION:
                attachVisualisation(args);
                ic.runScheduler();
                break;

            case HEADLESS:
                ic.runScheduler();
                break;
        }
    }
}