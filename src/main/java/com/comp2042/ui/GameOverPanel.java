package com.comp2042.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * A panel that displays the "GAME OVER" message when the player loses.
 */
public class GameOverPanel extends BorderPane {

    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        setCenter(gameOverLabel);
    }

}
