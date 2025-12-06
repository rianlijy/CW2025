package com.comp2042.ui;

import com.comp2042.game.ViewData;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders the preview of the next five Tetris pieces that will appear.
 * Displays mini representations of upcoming bricks in a vertical layout.
 */
public class PreviewRenderer {

    private final VBox previewBox;
    private final List<GridPane> previewGrids = new ArrayList<>();
    private final List<Rectangle[][]> previewRectangles = new ArrayList<>();
    private static final int PREVIEW_CELL = 12;

    public PreviewRenderer(VBox box) {
        this.previewBox = box;
        initializePreviewPanels();
    }

    /**
     * Initializes five preview panels, each with a 4x4 grid of transparent rectangles.
     */
    private void initializePreviewPanels() {
        previewBox.getChildren().removeIf(node -> node instanceof GridPane);
        previewGrids.clear();
        previewRectangles.clear();

        for (int i = 0; i < 5; i++) {
            GridPane mini = new GridPane();
            mini.setHgap(1);
            mini.setVgap(1);
            mini.setAlignment(Pos.CENTER);

            Rectangle[][] rects = new Rectangle[4][4];

            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    Rectangle rect = new Rectangle(PREVIEW_CELL, PREVIEW_CELL);
                    rect.setFill(Color.TRANSPARENT);
                    rect.setVisible(false);
                    mini.add(rect, c, r);
                    rects[r][c] = rect;
                }
            }

            previewBox.getChildren().add(mini);
            previewGrids.add(mini);
            previewRectangles.add(rects);
        }
    }

    /**
     * Calculates the bounding box of non-zero cells in the matrix.
     * Returns [minRow, maxRow, minCol, maxCol].
     */
    private int[] getBounds(int[][] mat) {
        int minR = 4, maxR = -1;
        int minC = 4, maxC = -1;

        for (int r = 0; r < 4; r++)
            for (int c = 0; c < 4; c++)
                if (mat[r][c] != 0) {
                    if (r < minR) minR = r;
                    if (r > maxR) maxR = r;
                    if (c < minC) minC = c;
                    if (c > maxC) maxC = c;
                }

        return new int[]{minR, maxR, minC, maxC};
    }

    /**
     * Updates the preview display with the next five pieces, showing only
     * visible cells within each piece's bounding box.
     */
    public void updatePreview(ViewData view) {
        var nextFive = view.getNextFive();

        for (int idx = 0; idx < previewRectangles.size(); idx++) {
            Rectangle[][] rects = previewRectangles.get(idx);
            int[][] mat = nextFive.get(idx);
            int[] b = getBounds(mat);

            for (int r = 0; r < 4; r++)
                for (int c = 0; c < 4; c++) {
                    Rectangle rect = rects[r][c];
                    if (r < b[0] || r > b[1] || c < b[2] || c > b[3] || mat[r][c] == 0) {
                        rect.setVisible(false);
                    } else {
                        rect.setFill(ColorUtil.getFillColor(mat[r][c]));
                        rect.setVisible(true);
                    }
                }
        }
    }
}

