package org.example.demo4;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private TextField display;
    private double firstNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    @Override
    public void start(Stage primaryStage) {
        display = new TextField("0");
        display.setEditable(false);
        display.setStyle("-fx-font-size: 18;");

        String[][] buttons = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "-"},
                {"0", ".", "=", "+"}
        };

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));

        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                Button button = new Button(buttons[i][j]);
                button.setPrefSize(50, 50);
                button.setStyle("-fx-font-size: 14;");

                final String symbol = buttons[i][j];
                button.setOnAction(e -> handleButton(symbol));

                grid.add(button, j, i + 1);
            }
        }

        Button clearButton = new Button("C");
        clearButton.setPrefSize(50, 50);
        clearButton.setStyle("-fx-font-size: 14;");
        clearButton.setOnAction(e -> {
            display.setText("0");
            firstNumber = 0;
            operator = "";
            startNewNumber = true;
        });

        grid.add(display, 0, 0, 4, 1);
        grid.add(clearButton, 4, 0);

        VBox root = new VBox(10, grid);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Calculator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void handleButton(String symbol) {
        if ("0123456789.".contains(symbol)) {
            if (startNewNumber) {
                display.setText(symbol.equals(".") ? "0." : symbol);
                startNewNumber = false;
            } else {
                if (symbol.equals(".") && display.getText().contains(".")) {
                    return;
                }
                display.setText(display.getText() + symbol);
            }
        } else if ("+-*/".contains(symbol)) {
            if (!operator.isEmpty()) {
                calculate();
            }
            firstNumber = Double.parseDouble(display.getText());
            operator = symbol;
            startNewNumber = true;
        } else if ("=".equals(symbol)) {
            calculate();
            operator = "";
            startNewNumber = true;
        }
    }

    private void calculate() {
        if (operator.isEmpty()) return;

        double secondNumber = Double.parseDouble(display.getText());
        double result = 0;

        switch (operator) {
            case "+": result = firstNumber + secondNumber; break;
            case "-": result = firstNumber - secondNumber; break;
            case "*": result = firstNumber * secondNumber; break;
            case "/":
                if (secondNumber == 0) {
                    display.setText("Ошибка: деление на 0");
                    return;
                }
                result = firstNumber / secondNumber;
                break;
        }

        display.setText(String.valueOf(result));
        firstNumber = result;
    }
}