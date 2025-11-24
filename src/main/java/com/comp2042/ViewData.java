package com.comp2042;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final List<int[][]> nextFiveBricks;

    private final int[][] ghostBrick;
    private final int ghostX;
    private final int ghostY;

    public ViewData(int[][] brickData, int xPosition, int yPosition,
                    List<int[][]> nextFiveBricks,
                    int[][] ghostBrick, int ghostX, int ghostY) {
        this.brickData = MatrixOperations.copy(brickData);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        // deep copy the provided matrices to be safe
        this.nextFiveBricks = nextFiveBricks == null ? new ArrayList<>() :
                nextFiveBricks.stream().map(MatrixOperations::copy).collect(Collectors.toList());
        this.ghostBrick = MatrixOperations.copy(ghostBrick);
        this.ghostX = ghostX;
        this.ghostY = ghostY;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public List<int[][]> getNextFive() {
        return nextFiveBricks.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

    public int[][] getGhostBrick() { return MatrixOperations.copy(ghostBrick); }

    public int getGhostX() { return ghostX; }

    public int getGhostY() { return ghostY; }
}