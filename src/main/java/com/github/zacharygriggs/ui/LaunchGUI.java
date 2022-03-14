package com.github.zacharygriggs.ui;

import com.github.zacharygriggs.chess.core.ChessDisplay;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public final class LaunchGUI extends Application {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Pane root = new FlowPane();
        root.setMinWidth(WIDTH);
        root.setMaxWidth(WIDTH);
        root.setPrefWidth(WIDTH);
        root.setMaxHeight(HEIGHT);
        root.setMinHeight(HEIGHT);
        root.setPrefHeight(HEIGHT);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Chess");
        primaryStage.setScene(scene);
        primaryStage.show();

        ComboBox<String> difficulties = new ComboBox<>();
        difficulties.getItems().addAll("Max", "Random", "Low");
        difficulties.getSelectionModel().selectFirst();

        Button restart = new Button("Restart Game");
        Button engineTakeover = new Button("Engine vs Engine");

        Controller controller = new Controller();
        ChessDisplay display = new ChessDisplay(WIDTH, HEIGHT-100);

        root.getChildren().addAll(display, new Label("Engine Difficulty: "), difficulties, restart, engineTakeover);
        difficulties.setOnAction(controller::changeDifficulty);
        restart.setOnAction(controller::restart);
        engineTakeover.setOnAction(controller::engineTakeOver);

        controller.setup(display, difficulties);

        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
