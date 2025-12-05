package com.comp2042.game;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.SevenBagGenerator;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private final Deque<com.comp2042.logic.bricks.Brick> nextFive = new ArrayDeque<>();
    private Brick heldBrick = null;
    private boolean holdUsed = false;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new SevenBagGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
        for (int i = 0; i < 5; i++) {
            nextFive.add(brickGenerator.getBrick());
        }
    }

    public List<int[][]> getNextFiveMatrices() {
        return nextFive.stream()
                .map(b -> b.getShapeMatrix().get(0))
                .map(MatrixOperations::copy)
                .collect(Collectors.toList());
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }


    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }
    
    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        int[][] rotated = nextShape.getShape();
        int x = currentOffset.x;
        int y = currentOffset.y;
        int[][] kicks = {
                {0, 0},
                {1, 0},
                {-1, 0},
                {0, -1},
                {2, 0},
                {-2, 0}
        };
        for (int[] k : kicks) {
            int newX = x + k[0];
            int newY = y + k[1];
            boolean conflict =
                    MatrixOperations.intersect(currentMatrix, rotated, newX, newY);
            if (!conflict) {
                brickRotator.setCurrentShape(nextShape.getPosition());
                currentOffset.move(newX, newY);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean createNewBrick() {
        com.comp2042.logic.bricks.Brick currentBrick = nextFive.poll();
        nextFive.add(brickGenerator.getBrick());

        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(3, 0);
        holdUsed = false;
        return MatrixOperations.intersect(currentGameMatrix,
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        Point ghostPoint = computeGhostPosition();
        List<int[][]> nextFiveMatrices = getNextFiveMatrices();
        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                nextFiveMatrices,
                brickRotator.getCurrentShape(),
                ghostPoint.x,
                ghostPoint.y,
                getHeldMatrix()
        );
    }

    private Point computeGhostPosition() {
        int[][] matrix = MatrixOperations.copy(currentGameMatrix);
        int[][] shape = brickRotator.getCurrentShape();
        Point p = new Point(currentOffset);

        while (!MatrixOperations.intersect(matrix, shape, p.x, p.y + 1)) {
            p.translate(0, 1);
        }
        return p;
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        heldBrick = null;
        holdUsed = false;
        nextFive.clear();
        for (int i = 0; i < 5; i++) {
            nextFive.add(brickGenerator.getBrick());
        }
        createNewBrick();
    }

    public ViewData holdPiece() {
        if (holdUsed) {
            return getViewData();
        }

        holdUsed = true;
        com.comp2042.logic.bricks.Brick current = brickRotator.getBrick();

        if (current == null) {
            return getViewData();
        }

        if (heldBrick == null) {
            heldBrick = current;
            createNewBrick();
        } else {
            com.comp2042.logic.bricks.Brick tmp = heldBrick;
            heldBrick = current;
            brickRotator.setBrick(tmp);
            currentOffset = new Point(3, 0);
        }
        return getViewData();
    }
    public int[][] getHeldMatrix() {
        if (heldBrick == null) {
            return new int[4][4];
        }
        return MatrixOperations.copy(heldBrick.getShapeMatrix().get(0));
    }

    public void addGarbageRow() {
        for (int row = 0; row < width - 1; row++) {
            currentGameMatrix[row] = currentGameMatrix[row + 1];
        }
        int[] garbageRow = new int[height];

        int hole = (int)(Math.random() * height);

        for (int col = 0; col < height; col++) {
            garbageRow[col] = (col == hole) ? 0 : 8;
        }
        currentGameMatrix[width - 1] = garbageRow;
    }
}
