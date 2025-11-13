package org.example.demo1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private boolean isForward = true;

    @Override
    public void start(Stage primaryStage) {
        TextField field1 = new TextField();
        TextField field2 = new TextField();

        Button switchButton = new Button("→");
        switchButton.setFont(Font.font(20));

        switchButton.setOnAction(e -> {
            if (isForward) {
                field2.setText(field1.getText());
                field1.clear();
                switchButton.setText("←");
            } else {
                field1.setText(field2.getText());
                field2.clear();
                switchButton.setText("→");
            }
            isForward = !isForward;
        });

        HBox root = new HBox(10, field1, switchButton, field2);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(root, 400, 100);
        primaryStage.setTitle("Word Switcher");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}