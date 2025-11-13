package org.example.demo5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private ToggleGroup group1 = new ToggleGroup();
    private ToggleGroup group2 = new ToggleGroup();
    private ToggleGroup group3 = new ToggleGroup();

    @Override
    public void start(Stage primaryStage) {
        VBox stripe1 = createStripe("Первая полоса", group1);
        VBox stripe2 = createStripe("Вторая полоса", group2);
        VBox stripe3 = createStripe("Третья полоса", group3);

        Button drawButton = new Button("Нарисовать");
        Label resultLabel = new Label();

        drawButton.setOnAction(e -> {
            String color1 = getSelectedColor(group1);
            String color2 = getSelectedColor(group2);
            String color3 = getSelectedColor(group3);

            if (color1 != null && color2 != null && color3 != null) {
                resultLabel.setText(color1 + ", " + color2 + ", " + color3);
            } else {
                resultLabel.setText("Выберите цвета для всех полос!");
            }
        });

        VBox root = new VBox(20, stripe1, stripe2, stripe3, drawButton, resultLabel);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 300, 400);
        primaryStage.setTitle("Text Flag");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private VBox createStripe(String title, ToggleGroup group) {
        Label label = new Label(title);
        RadioButton red = new RadioButton("Красный");
        RadioButton blue = new RadioButton("Синий");
        RadioButton green = new RadioButton("Зеленый");
        RadioButton white = new RadioButton("Белый");
        RadioButton yellow = new RadioButton("Желтый");

        red.setToggleGroup(group);
        blue.setToggleGroup(group);
        green.setToggleGroup(group);
        white.setToggleGroup(group);
        yellow.setToggleGroup(group);

        HBox colorBox = new HBox(10, red, blue, green, white, yellow);
        return new VBox(10, label, colorBox);
    }

    private String getSelectedColor(ToggleGroup group) {
        RadioButton selected = (RadioButton) group.getSelectedToggle();
        return selected != null ? selected.getText() : null;
    }
}