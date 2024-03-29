package com.team01.scheduler.gui.views;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Custom JavaFX control which can act as a destination for standard
 * output and error streams.
 */
public class Console extends TextArea {

    // Loosely adapted from this web page:
    // https://stackoverflow.com/questions/43821965/redirecting-sop-console-messages-to-jtextarea-and-also-should-not-flicker-and-sh

    /**
     * Create a new OutputStream implementation which writes to
     * the textarea.
     *
     */
    private OutputStream outputStream = new OutputStream() {
        @Override
        public void write(int i) throws IOException {
            // Append character
            appendText(String.valueOf((char) i));
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            appendText(new String(b, off, len));
        }
    };

    /**
     * Get the Console control's output stream.
     *
     * @return OutputStream for this console
     */
    public OutputStream getConsoleOutputStream() {
        return outputStream;
    }
}
