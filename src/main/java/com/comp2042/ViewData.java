package com.comp2042;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;

    private final int[][] ghostBrick;
    private final int ghostX;
    private final int ghostY;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int[][] ghostBrick, int ghostX, int ghostY) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostBrick = ghostBrick;
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

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    public int[][] getGhostBrick() { return MatrixOperations.copy(ghostBrick); }

    public int getGhostX() { return ghostX; }

    public int getGhostY() { return ghostY; }
}
