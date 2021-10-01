package com.deviget.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BoardMove {
    @JsonProperty("columnIndex")
    private int columnIndex;

    @JsonProperty("rowIndex")
    private int rowIndex;

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
