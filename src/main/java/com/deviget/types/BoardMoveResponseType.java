package com.deviget.types;

public enum BoardMoveResponseType {
    GAME_OVER("Game Over"),
    GAME_WON("Game Won"),
    GAME_INFO("Game Info");

    public final String label;

    BoardMoveResponseType(String label) {
        this.label = label;
    }
}
