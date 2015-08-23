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

        FunctionCache.initialiseCache(cachePackagePath);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Window getStage() {
        return stage;
    }
}
