package com.geom.voronoi.state;

public class GameState {
    private int redPlayerRounds;
    private int bluePlayerRounds;

    private boolean isRedPlayer;

    public GameState(int redPlayerRounds, int bluePlayerRounds) {
        this.redPlayerRounds = redPlayerRounds;
        this.bluePlayerRounds = bluePlayerRounds;
    }

    public GameState() {

    }

    public int getRedPlayerRounds() {
        return redPlayerRounds;
    }

    public void setRedPlayerRounds(int redPlayerRounds) {
        this.redPlayerRounds = redPlayerRounds;
    }

    public int getBluePlayerRounds() {
        return bluePlayerRounds;
    }

    public void setBluePlayerRounds(int bluePlayerRounds) {
        this.bluePlayerRounds = bluePlayerRounds;
    }

    public boolean isRedPlayer() {
        return isRedPlayer;
    }

    public void setRedPlayer(boolean redPlayer) {
        isRedPlayer = redPlayer;
    }
}
