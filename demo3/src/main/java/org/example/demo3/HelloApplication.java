package org.example.demo3;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class HelloApplication extends Application {
    private Map<String, Double> prices = new HashMap<>();
    private Map<String, Spinner<Integer>> quantitySpinners = new HashMap<>();
    private Map<String, CheckBox> checkBoxes = new HashMap<>();
    private TextArea receiptArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        prices.put("Суп", 250.0);
        prices.put("Салат", 180.0);
        prices.put("Стейк", 650.0);
        prices.put("Рыба", 450.0);
        prices.put("Десерт", 200.0);

        VBox menuBox = new VBox(10);

        for (String dish : prices.keySet()) {
            CheckBox checkBox = new CheckBox(dish);
            Spinner<Integer> spinner = new Spinner<>(0, 10, 0);
            spinner.setDisable(true);
            Label priceLabel = new Label(prices.get(dish) + " руб.");

            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                spinner.setDisable(!newVal);
                if (newVal && spinner.getValue() == 0) {
                    spinner.getValueFactory().setValue(1);
                }
                updateReceipt();
            });

            spinner.valueProperty().addListener((obs, oldVal, newVal) -> {
                updateReceipt();
            });

            HBox dishBox = new HBox(10, checkBox, spinner, priceLabel);
            menuBox.getChildren().add(dishBox);

            quantitySpinners.put(dish, spinner);
            checkBoxes.put(dish, checkBox);
        }

        receiptArea.setEditable(false);
        receiptArea.setPrefHeight(200);

        VBox root = new VBox(20, new Label("Меню:"), menuBox,
                new Label("Чек:"), receiptArea);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("Restaurant Order");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateReceipt() {
        StringBuilder receipt = new StringBuilder();
        double total = 0;

        for (String dish : prices.keySet()) {
            if (checkBoxes.get(dish).isSelected()) {
                int quantity = quantitySpinners.get(dish).getValue();
                if (quantity > 0) {
                    double price = prices.get(dish);
                    double dishTotal = price * quantity;
                    receipt.append(String.format("%s: %d x %.2f = %.2f руб.\n",
                            dish, quantity, price, dishTotal));
                    total += dishTotal;
                }
            }
        }

        receipt.append(String.format("\nИтого: %.2f руб.", total));
        receiptArea.setText(receipt.toString());
    }
}