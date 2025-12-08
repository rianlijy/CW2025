package com.comp2042.ui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Utility class for mapping brick type IDs to their corresponding colors.
 * Provides a static method to retrieve the fill color for each Tetris piece type.
 */
public class ColorUtil {

    public static Paint getFillColor(int i) {
        return switch (i) {
            case 1 -> Color.web("#00AFFF"); // I
            case 2 -> Color.web("#B044FF"); // J
            case 3 -> Color.web("#FF2DAE"); // L
            case 4 -> Color.web("#FFF65B"); // O
            case 5 -> Color.web("#63FFA7"); // S
            case 6 -> Color.web("#FF4E3D"); // Z
            case 7 -> Color.web("#FF6BFA"); // T
            case 8 -> Color.web("#777777");  // garbage
            default -> Color.BLACK;
        };
    }
}

