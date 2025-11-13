package org.example.demo2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Пример текста");
        ProgressBar progressBar = new ProgressBar(0.5);
        Slider slider = new Slider(0, 100, 50);

        CheckBox checkBox1 = new CheckBox("Показать текст");
        CheckBox checkBox2 = new CheckBox("Показать прогресс");
        CheckBox checkBox3 = new CheckBox("Показать слайдер");

        checkBox1.setSelected(true);
        checkBox2.setSelected(true);
        checkBox3.setSelected(true);

        checkBox1.selectedProperty().addListener((obs, oldVal, newVal) ->
                label.setVisible(newVal));

        checkBox2.selectedProperty().addListener((obs, oldVal, newVal) ->
                progressBar.setVisible(newVal));

        checkBox3.selectedProperty().addListener((obs, oldVal, newVal) ->
                slider.setVisible(newVal));

        VBox widgetsBox = new VBox(10, label, progressBar, slider);
        VBox checkBoxes = new VBox(10, checkBox1, checkBox2, checkBox3);

        VBox root = new VBox(20, widgetsBox, checkBoxes);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 300, 300);
        primaryStage.setTitle("Widget Hider");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}