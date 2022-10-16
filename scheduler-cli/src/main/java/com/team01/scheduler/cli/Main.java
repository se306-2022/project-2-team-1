package com.team01.scheduler.cli;

import com.team01.scheduler.cli.io.InputController;

public class Main {

    public static void attachVisualisation(String[] args) {
        throw new UnsupportedOperationException("Visualisation is not implemented yet!");
    }

    /**
     *  This is the entry point to the scheduler-cli subproject, the arguments invoked
     *  from the InputController class specify whether the cli should be run with the
     *  visualization, adding overhead, or without.
     *
     *  This method invokes the method that runs the scheduler in both instances.
     * @param args command line input
     */
    public static void main(String[] args) {
        // initialise inputcontroller to hand input arguments
        try {
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
        catch (Exception e) {
            System.err.println("Unable to run scheduler: " + e.getMessage());
        }
    }
}