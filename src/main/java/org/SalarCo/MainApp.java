package org.SalarCo;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Callable;


public class MainApp extends Application {
    public static Stage stage;
    public static File inputFile, outFile, jarFile;
    public static Checkers checkers;
    public static String sysArgs;
    public static String className;
    public static BotRunner botRunner;
    @Override
    public void start(Stage stage) throws Exception {
        VBox mainBox = new VBox(15);
        stage.initStyle(StageStyle.DECORATED);
        mainBox.setAlignment(Pos.CENTER);
        Label title = new Label("Checkers Graphics");
        Label credits = new Label("By AmirSalar Safaei");
        credits.setStyle("-fx-font-size: 15");
        FileChooser inputOutputFileChooser = new FileChooser();
        inputOutputFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        DirectoryChooser jarMainFileChooser = new DirectoryChooser();
           Button InputFileChooserButton = new Button("Input File Location");
        Button OutputFileChooserButton = new Button("Output File Location");
        Button JarFileChooserButton = new Button("Jar File Location");
        TextArea systemArgumentsTextArea = new TextArea("System Arguments for running java file");
        systemArgumentsTextArea.setStyle("-fx-border-radius: 20px;-fx-font-family: 'Comic Sans MS';-fx-padding: 20px;" +
                "-fx-background-insets: 20px;");
        systemArgumentsTextArea.setPrefRowCount(5);
        TextField classNameTextField = new TextField("Enter class name");
        ChoiceBox<Turn> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(Turn.Black, Turn.White);
        HBox turnAndLabel = new HBox(15);
        turnAndLabel.getChildren().addAll(new Label("Turn : "), choiceBox);
        turnAndLabel.setAlignment(Pos.CENTER);

        Button startGame = new Button("Start");
        startGame.setOnAction(event -> {
//            if (inputFile == null || outFile == null || jarFile == null || choiceBox.getValue() == null) {
//                return;
//            }
            botRunner = new BotRunner();
            sysArgs = systemArgumentsTextArea.getText();
            className = classNameTextField.getText();
            checkers = new Checkers(botRunner.getAnsFromBot());
            checkers.makeTable(stage);
            checkers.reloadTable();

        });
        InputFileChooserButton.setOnAction(event -> {
            inputFile = inputOutputFileChooser.showOpenDialog(stage);
        });
        OutputFileChooserButton.setOnAction(event -> {
            outFile = inputOutputFileChooser.showOpenDialog(stage);
        });
        JarFileChooserButton.setOnAction(event -> {
            jarFile = jarMainFileChooser.showDialog(stage);
        });
        mainBox.getChildren().addAll(title, credits, InputFileChooserButton, OutputFileChooserButton, JarFileChooserButton,
                systemArgumentsTextArea,classNameTextField,turnAndLabel, startGame);
        mainBox.setStyle("-fx-background-color: #ffffff");
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Scene scene = new Scene(mainBox,screenBounds.getWidth() / 2 , screenBounds.getHeight() / 2);
        scene.getStylesheets().add("styles/main.css");
        stage.setTitle("Checkers Graphics");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}