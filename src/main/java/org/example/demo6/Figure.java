package org.example.demo6;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public interface Figure {
    void draw(GraphicsContext gc);
    boolean contains(double x, double y);
    void move(double dx, double dy);
    void setColor(Color color);
    Color getColor();
}