package main;

import data.JSONConverter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Main class with the start method containing FXMLLoader, the error catching
 * method and data initialization of the JSON file.
 */

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * This method initialize the scene and related
     * .fxml file and shows content.
     * @param primaryStage principal stage
     * @throws IOException exception for FXMLLoader
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("main.fxml"))));

        primaryStage.setTitle("Chat Viewer");

        Image someImage = new Image(String.valueOf((getClass().getResource("/images/icon.png"))));;
        primaryStage.getIcons().add(someImage);

        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        Scene scene = new Scene(root);
        scene.setFill(Color.web("#1C1C2A"));
        primaryStage.setScene(scene);
        primaryStage.resizableProperty().setValue(false);
        primaryStage.show();
    }

    /**
     * This method shows related to the exception
     * message catching from the whole project.
     * @param option int for case
     */
    public static void showExceptionDialog(Integer option) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error detected");
        alert.setHeaderText(null);
        switch (option){
            case 1:
                alert.setContentText("File parsing error : the 'message' is corrupted or missing");
                break;
            case 2:
                alert.setContentText("File parsing error : the 'name' line is corrupted or missing");
                break;
            case 3:
                alert.setContentText("File parsing error : the 'time' line is corrupted or missing");
                break;
            case 4:
                alert.setContentText("The file format is incorrect or corrupted!");
                break;
            case 5:
                alert.setContentText("The file format is null!");
                break;
            case 6:
                alert.setContentText("Select the conversation before!");
        } alert.showAndWait();
    }

    /**
     * This methode call the method from
     * the ConvertJSON class and launch
     * main method of the class.
     * @param args args
     */
    public static void main(String[] args) {
        JSONConverter.main();
        Application.launch(Main.class, args);

    }
}
