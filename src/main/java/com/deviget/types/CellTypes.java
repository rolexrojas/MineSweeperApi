package com.deviget.types;

public enum CellTypes {
    NUMBER("Number"),
    EMPTY("Empty"),
    BOMB("Bomb");

    public final String label;

    private CellTypes(String label) {
        this.label = label;
    }
}
