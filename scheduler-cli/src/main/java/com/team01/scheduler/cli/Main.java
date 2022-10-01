package com.team01.scheduler.cli;

import com.team01.scheduler.cli.io.InputController;

public class Main {

    public static void attachVisualisation(String[] args) {
        throw new UnsupportedOperationException("Visualisation is not implemented yet!");
    }

    public static void main(String[] args) {
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