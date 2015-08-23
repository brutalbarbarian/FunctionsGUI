package com.brutalbarbarian.functions.app;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.exceptions.UnknownResultType;
import com.brutalbarbarian.functions.interfaces.Function;
import com.brutalbarbarian.utils.Console;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by Lu on 3/9/14.
 */
public class FunctionContentController implements Initializable {
    @FXML
    public TextField tfDescription, tfURL;
    public TextField tfExternalID;
    public TextField tfDuration;
    public GridPane gpInputParams;
    public Button btnRequestStop;
    @FXML
    TextArea taInput, taOutput;
    @FXML
    SplitPane spInput;
    @FXML
    Button btnCompute;
    @FXML
    BorderPane bpInputPane;

    private Function function;
    private long startTime;
    private Console console;

    @FXML
    public void onCompute(ActionEvent event) {
        // Compute using multi-threading later...
        // Keep it simple for now
        final String inputText = taInput.getText();
        final HashMap<Parameter, String> inputParams = new HashMap<>();
        if (function.requiresParameters()) {
            for (Node n : gpInputParams.getChildren()) {
                if (n instanceof TextField) {
                    inputParams.put((Parameter) ((TextField) n).getUserData(),
                            ((TextField) n).getText());
                }
            }
        }

        // Disable input controls before we start running
        btnCompute.setDisable(true);
        btnRequestStop.setDisable(false);
//        tvInputParams.setDisable(true);
        gpInputParams.setDisable(true);
        taInput.setDisable(true);

        if (console != null) {
            console.clear();
        }

        Thread runner = new Thread(() -> {
            try {
                // Record start time for tracking how long the function took
                startTime = System.currentTimeMillis();
                final Object outputText = function.computeResult(inputParams, inputText);
                // Assuming it got up to this point... should be error-less
                // Set the results
                Platform.runLater(()-> {
                    if (outputText == null) {
                        taOutput.setText("null");
                    } else {
                        switch (function.getResultType()) {
                            case String :
                                taOutput.setText(outputText.toString());
                                break;
                            default :
                                throw new UnknownResultType();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                final long duration = System.currentTimeMillis() - startTime;
                // Re-enable the controls
                Platform.runLater(()-> {
                    btnCompute.setDisable(false);
                    btnRequestStop.setDisable(true);
//                    tvInputParams.setDisable(false);
                    gpInputParams.setDisable(false);
                    taInput.setDisable(false);
                    tfDuration.setText(Long.toString(duration));
                });
            }
        });
        runner.start();;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setConsole(Console console) {
        this.console = console;
    }

    public void setFunction(Function function) {
        this.function = function;

        // Gotta remove instead of simply hiding otherwise splitter still remains
        if (!(function.requiresText() | function.requiresParameters())) {
            bpInputPane.setCenter(null);
            bpInputPane.setMaxHeight(Region.USE_PREF_SIZE);
        } else {
            if (!function.requiresText()) {
                spInput.getItems().remove(taInput);
            }
            if (!function.requiresParameters()) {
                spInput.getItems().remove(gpInputParams);
            }
        }

        tfDescription.setText(function.getDescription());
        tfURL.setText(function.getURL());
        tfExternalID.setText(function.getExternalID());

        if (function.requiresParameters()) {
            int row = 0;
            for (Parameter param : function.getParameters()) {
                Label lName = new Label(param.toString());
                TextField tfValue = new TextField();
                GridPane.setHgrow(lName, Priority.NEVER);
                GridPane.setHgrow(tfValue, Priority.ALWAYS);
                tfValue.setUserData(param); // For reference

                Object defValue = function.getDefaultParamValue(param);
                if (defValue != null) {
                    tfValue.setText(defValue.toString());
                }

                gpInputParams.addRow(row, lName, tfValue);
                row++;
            }
        }
        if (function.requiresText()) {
            String text = function.getDefaultText();
            if (text != null) {
                taInput.setText(text);;
            }
        }
    }

    public void onRequestStop(ActionEvent actionEvent) {
        if (function != null) {
            btnRequestStop.setDisable(true); // User has already requested once...
            function.requestStop();
        }
    }
}
