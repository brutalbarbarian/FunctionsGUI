package com.brutalbarbarian.utils;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Lu on 3/11/14.
 */
public class Console extends OutputStream{
    private TextArea output;

    public Console(TextArea ta) {
        this.output = ta;
    }

    public void clear() {
        if (Platform.isFxApplicationThread()) {
            output.clear();
        } else {
            Platform.runLater(() -> {
                output.clear();
            });
        }
    }

    @Override
    public void write(final int i) throws IOException {
        if (Platform.isFxApplicationThread()) {
            output.appendText(String.valueOf((char) i));
        } else {
            Platform.runLater(() -> {
                output.appendText(String.valueOf((char) i));
            });
        }
    }
}
