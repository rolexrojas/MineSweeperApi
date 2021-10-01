package com.deviget.types;

import com.deviget.domain.Cell;

import java.util.LinkedHashSet;
import java.util.Set;

public class BoardMoveResponse {

    private String duration;
    private Set<Cell> emptyCellList = new LinkedHashSet<>();
    private Set<Cell> numberCellList = new LinkedHashSet<>();
    private Set<Cell> bombCellList = new LinkedHashSet<>();

    private BoardMoveResponseType boardMoveResponseType;

    public BoardMoveResponseType getBoardMoveResponseType() {
        return boardMoveResponseType;
    }

    public void setBoardMoveResponseType(BoardMoveResponseType boardMoveResponseType) {
        this.boardMoveResponseType = boardMoveResponseType;
    }

    public Set<Cell> getEmptyCellList() {
        return emptyCellList;
    }

    public void setEmptyCellList(Set<Cell> emptyCellList) {
        this.emptyCellList = emptyCellList;
    }

    public Set<Cell> getNumberCellList() {
        return numberCellList;
    }

    public void setNumberCellList(Set<Cell> numberCellList) {
        this.numberCellList = numberCellList;
    }

    public Set<Cell> getBombCellList() {
        return bombCellList;
    }

    public void setBombCellList(Set<Cell> bombCellList) {
        this.bombCellList = bombCellList;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
