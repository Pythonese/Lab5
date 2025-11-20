package org.example.demo6;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelloApplication extends Application {
    private Canvas canvas;
    private List<Figure> figures = new ArrayList<>();
    private Random random = new Random();
    private Figure draggedFigure = null;
    private double lastX, lastY;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        canvas = new Canvas(800, 600);
        root.setCenter(canvas);

        HBox buttons = new HBox(10);
        Button circleBtn = new Button("Добавить круг");
        Button rectBtn = new Button("Добавить прямоугольник");

        circleBtn.setOnAction(e -> addRandomCircle());
        rectBtn.setOnAction(e -> addRandomRectangle());

        buttons.getChildren().addAll(circleBtn, rectBtn);
        root.setTop(buttons);

        setupMouseHandlers();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Geometry2D App");
        primaryStage.setScene(scene);
        primaryStage.show();

        drawAll();
    }

    private void addRandomCircle() {
        double radius = 20 + random.nextDouble() * 50;
        double x = random.nextDouble() * (canvas.getWidth() - 2 * radius);
        double y = random.nextDouble() * (canvas.getHeight() - 2 * radius);
        Color color = randomColor();

        figures.add(new Circle(x + radius, y + radius, radius, color));
        drawAll();
    }

    private void addRandomRectangle() {
        double width = 40 + random.nextDouble() * 80;
        double height = 40 + random.nextDouble() * 80;
        double x = random.nextDouble() * (canvas.getWidth() - width);
        double y = random.nextDouble() * (canvas.getHeight() - height);
        Color color = randomColor();

        figures.add(new Rectangle(x, y, width, height, color));
        drawAll();
    }

    private Color randomColor() {
        return Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    private void setupMouseHandlers() {
        canvas.setOnMousePressed(e -> {
            double x = e.getX();
            double y = e.getY();

            if (e.isPrimaryButtonDown()) {
                for (int i = figures.size() - 1; i >= 0; i--) {
                    Figure figure = figures.get(i);
                    if (figure.contains(x, y)) {
                        draggedFigure = figure;
                        lastX = x;
                        lastY = y;

                        figures.remove(i);
                        figures.add(figure);
                        break;
                    }
                }
            } else if (e.isSecondaryButtonDown()) {
                for (int i = figures.size() - 1; i >= 0; i--) {
                    Figure figure = figures.get(i);
                    if (figure.contains(x, y)) {
                        figure.setColor(randomColor());
                        drawAll();
                        break;
                    }
                }
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (draggedFigure != null && e.isPrimaryButtonDown()) {
                double x = e.getX();
                double y = e.getY();
                double dx = x - lastX;
                double dy = y - lastY;

                draggedFigure.move(dx, dy);
                lastX = x;
                lastY = y;
                drawAll();
            }
        });

        canvas.setOnMouseReleased(e -> {
            draggedFigure = null;
        });
    }

    private void drawAll() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Figure figure : figures) {
            figure.draw(gc);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}