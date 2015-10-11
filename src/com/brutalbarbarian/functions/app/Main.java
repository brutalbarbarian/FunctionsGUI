package com.brutalbarbarian.functions.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Main extends Application {
    private static final String cachePackagePath = "com.brutalbarbarian.functions.functions";
    private static Stage stage;
    private static Main appInstance;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Computation Problem Solver");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();

        appInstance = this;
        FunctionCache.initialiseCache(cachePackagePath);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Window getStage() {
        return stage;
    }

    protected void doPostInitialise(MainController mainController) {
        // Override to do things
    }

    public static void postInitialise(MainController mainController) {
        appInstance.doPostInitialise(mainController);
    }
}
