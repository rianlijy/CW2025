package com.comp2042.ui;

import com.comp2042.game.ViewData;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HoldRenderer {

    private final VBox leftDummy;

    private GridPane holdGrid;
    private Rectangle[][] holdRects;

    private static final int HOLD_CELL = 12;

    public HoldRenderer(VBox leftDummy) {
        this.leftDummy = leftDummy;
        initializeHoldPanel();
    }

    private void initializeHoldPanel() {
        holdGrid = new GridPane();
        holdGrid.setHgap(1);
        holdGrid.setVgap(1);
        holdGrid.setAlignment(Pos.CENTER);

        holdRects = new Rectangle[4][4];

        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                Rectangle rect = new Rectangle(HOLD_CELL, HOLD_CELL);
                rect.setFill(Color.TRANSPARENT);
                rect.setVisible(false);
                holdGrid.add(rect, c, r);
                holdRects[r][c] = rect;
            }
        }

        leftDummy.getChildren().add(holdGrid);
    }

    public void updateHold(ViewData view) {
        int[][] mat = view.getHeldBrick();
        int[] b = getBounds(mat);

        for (int r = 0; r < 4; r++)
            for (int c = 0; c < 4; c++) {
                Rectangle rect = holdRects[r][c];

                if (r < b[0] || r > b[1] || c < b[2] || c > b[3] || mat[r][c] == 0) {
                    rect.setVisible(false);
                } else {
                    rect.setFill(ColorUtil.getFillColor(mat[r][c]));
                    rect.setVisible(true);
                }
            }
    }

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
}
