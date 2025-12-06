package com.comp2042.ui;

import com.comp2042.game.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class BoardRenderer {

    private static final int BRICK_SIZE = 20;

    private final GridPane gamePanel;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles;

    public BoardRenderer(GridPane gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void initBoard(int[][] boardMatrix, ViewData brick,
                          Rectangle[][] displayMatrixTarget,
                          Rectangle[][] rectanglesTarget,
                          Rectangle[][] ghostRectanglesTarget) {

        this.displayMatrix = displayMatrixTarget;
        this.rectangles = rectanglesTarget;
        this.ghostRectangles = ghostRectanglesTarget;

        final int TOTAL_ROWS = boardMatrix.length;
        final int VISIBLE_ROWS = TOTAL_ROWS - 2;
        final int TOTAL_COLS = boardMatrix[0].length;

        gamePanel.getColumnConstraints().clear();
        for (int c = 0; c < TOTAL_COLS; c++) {
            javafx.scene.layout.ColumnConstraints cc = new javafx.scene.layout.ColumnConstraints();
            cc.setMinWidth(BRICK_SIZE);
            cc.setPrefWidth(BRICK_SIZE);
            cc.setMaxWidth(BRICK_SIZE);
            gamePanel.getColumnConstraints().add(cc);
        }

        gamePanel.getRowConstraints().clear();
        for (int r = 0; r < VISIBLE_ROWS; r++) {
            javafx.scene.layout.RowConstraints rc = new javafx.scene.layout.RowConstraints();
            rc.setMinHeight(BRICK_SIZE);
            rc.setPrefHeight(BRICK_SIZE);
            rc.setMaxHeight(BRICK_SIZE);
            gamePanel.getRowConstraints().add(rc);
        }

        double prefW = TOTAL_COLS * BRICK_SIZE + Math.max(0, TOTAL_COLS - 1) * gamePanel.getHgap();
        double prefH = VISIBLE_ROWS * BRICK_SIZE + Math.max(0, VISIBLE_ROWS - 1) * gamePanel.getVgap();
        gamePanel.setPrefWidth(prefW);
        gamePanel.setPrefHeight(prefH);
        for (int i = 2; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < TOTAL_COLS; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.getStyleClass().add("gameCell");
                rectangle.setArcWidth(0);
                rectangle.setArcHeight(0);
                displayMatrix[i][j] = rectangle;

                int gridRow = i - 2;
                gamePanel.add(rectangle, j, gridRow);
            }
        }

        int[][] brickData = brick.getBrickData();
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = rectangles[i][j];

                int col = brick.getxPosition() + j;
                int row = brick.getyPosition() + i - 2;
                int addRow = Math.max(0, row);

                if (col >= 0 && col < TOTAL_COLS && addRow >= 0 && addRow < VISIBLE_ROWS) {
                    gamePanel.add(rectangle, col, addRow);
                } else {
                    int addCol = Math.max(0, Math.min(col, TOTAL_COLS - 1));
                    int addRowSafe = Math.max(0, Math.min(addRow, Math.max(0, VISIBLE_ROWS - 1)));
                    gamePanel.add(rectangle, addCol, addRowSafe);
                }
            }
        }

        int[][] ghostData = brick.getGhostBrick();
        for (int i = 0; i < ghostData.length; i++) {
            for (int j = 0; j < ghostData[i].length; j++) {
                Rectangle rectangle = ghostRectangles[i][j];

                int col = brick.getGhostX() + j;
                int row = brick.getGhostY() + i - 2;
                int addRow = Math.max(0, row);

                if (col >= 0 && col < TOTAL_COLS && addRow >= 0 && addRow < VISIBLE_ROWS) {
                    gamePanel.add(rectangle, col, addRow);
                } else {
                    int addCol = Math.max(0, Math.min(col, TOTAL_COLS - 1));
                    int addRowSafe = Math.max(0, Math.min(addRow, Math.max(0, VISIBLE_ROWS - 1)));
                    gamePanel.add(rectangle, addCol, addRowSafe);
                }
            }
        }
    }

    public void refreshBrick(ViewData brick) {
        final int VISIBLE_ROWS = gamePanel.getRowConstraints().size();
        final int TOTAL_COLS = gamePanel.getColumnConstraints().size();

        int[][] ghostData = brick.getGhostBrick();
        int ghostX = brick.getGhostX();
        int ghostY = brick.getGhostY();

        for (int i = 0; i < ghostData.length; i++) {
            for (int j = 0; j < ghostData[i].length; j++) {

                Rectangle r = ghostRectangles[i][j];
                boolean isGhostCell = ghostData[i][j] != 0;

                if (!isGhostCell) {
                    r.setVisible(false);
                    continue;
                }

                int col = ghostX + j;
                int row = ghostY + i - 2;

                if (row >= 0 &&
                        col >= 0 &&
                        col < TOTAL_COLS &&
                        row < VISIBLE_ROWS) {

                    GridPane.setColumnIndex(r, col);
                    GridPane.setRowIndex(r, row);
                    r.setVisible(true);
                } else {
                    r.setVisible(false);
                }
            }
        }


        int[][] bData = brick.getBrickData();
        for (int i = 0; i < bData.length; i++) {
            for (int j = 0; j < bData[i].length; j++) {
                Rectangle r = rectangles[i][j];

                int newCol = brick.getxPosition() + j;
                int newRow = brick.getyPosition() + i - 2;

                int clampedRow = Math.max(0, newRow);
                int clampedCol = Math.max(0, Math.min(newCol, TOTAL_COLS - 1));

                setRectangleColor(bData[i][j], r);

                GridPane.setColumnIndex(r, clampedCol);
                GridPane.setRowIndex(r, Math.min(clampedRow, VISIBLE_ROWS - 1));

                r.setVisible(bData[i][j] != 0 && newRow >= 0);
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleColor(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleColor(int color, Rectangle rectangle) {
        rectangle.setFill(ColorUtil.getFillColor(color));
        rectangle.setArcHeight(0);
        rectangle.setArcWidth(0);
    }
}

