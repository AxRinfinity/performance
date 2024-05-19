package com.perf;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class PerformancePredictor extends Application {

    private Button startButton;
    private Button selectFileButton;
    private Label selectedFileLabel;
    private TextArea outputArea;
    private File selectedFile;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Performance Predictor");

        VBox root = new VBox();
        root.setSpacing(10);
        Scene scene = new Scene(root, 400, 300);

        selectFileButton = new Button("Select CSV File");
        selectFileButton.setOnAction(event -> selectFile());

        selectedFileLabel = new Label("No file selected.");

        startButton = new Button("Start");
        startButton.setOnAction(event -> startPrediction());
        startButton.setDisable(true); // Disable until a file is selected

        outputArea = new TextArea();
        outputArea.setEditable(false);

        root.getChildren().addAll(selectFileButton, selectedFileLabel, startButton, outputArea);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            selectedFileLabel.setText("Selected file: " + selectedFile.getName());
            startButton.setDisable(false);
        } else {
            selectedFileLabel.setText("No file selected.");
            startButton.setDisable(true);
        }
    }

    private void startPrediction() {
        if (selectedFile == null) {
            outputArea.appendText("No CSV file selected.\n");
            return;
        }

        outputArea.appendText("Starting prediction...\n");

        // Adjust the Python command and path as necessary
        ProcessBuilder processBuilder = new ProcessBuilder("python", "E:\\PerfPredictor\\prediction_script.py", selectedFile.getAbsolutePath(), "C:\\Users\\augus\\arima_model.pkl");
        try {
            Process process = processBuilder.start();

            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                outputArea.appendText(line + "\n");
            }
            reader.close();

            // Wait for the Python process to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                outputArea.appendText("Prediction successful.\n");
            } else {
                outputArea.appendText("Error executing prediction script. Exit code: " + exitCode + "\n");
            }
        } catch (IOException | InterruptedException e) {
            outputArea.appendText("Exception: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
