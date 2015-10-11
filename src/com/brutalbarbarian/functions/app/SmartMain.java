package com.brutalbarbarian.functions.app;

import com.brutalbarbarian.functions.functions.smartbuilder.CSVScriptGenerator;
import com.brutalbarbarian.functions.functions.smartbuilder.TBOFittingFieldCreator;
import com.brutalbarbarian.functions.functions.smartbuilder.UpdateFittingLanguage;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Lu on 28/08/2015.
 */
public class SmartMain extends Main {
    public void init() throws Exception {
        super.init();

        FunctionCache.addFunction(new UpdateFittingLanguage());
        FunctionCache.addFunction(new TBOFittingFieldCreator());
        FunctionCache.addFunction(new CSVScriptGenerator());

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        super.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    protected void doPostInitialise(MainController mainController) {
        super.doPostInitialise(mainController);

//        mainController.tfSearch.setText("TBOFitting Field Creator");
//        try {
//            mainController.onAddFunction(null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
