package com.deviget.component;

import com.deviget.domain.Cell;

import java.util.LinkedHashSet;
import java.util.Set;

public class MineSweeperComponent {
    private Cell[][] boardArray;
    private Cell[][] minesArray;

    private Set<Cell> minesOnBoard = new LinkedHashSet<>();
    private void setMinesOnBoard(Set<Cell> minesOnBoard) {
        this.minesOnBoard = minesOnBoard;
    }

    public void setMinesArray(Cell[][] minesArray) {
        this.minesArray = minesArray;
    }

    public void setBoardArray(Cell[][] boardArray) {
        this.boardArray = boardArray;
    }

    private final int ZERO = 0;

    public void GameSetup(){
        //generate boardArray with no of cells
        Cell[][] boardArray = new Cell[7][7];

        //populate two dimensional array with empty fields
        populateBoardArray(boardArray);

        //generate random Mines
        Cell[][] minesPlace = dynamicMineGenerator();

        //Place Mine in board
        placeMinesInBoard(boardArray, minesPlace);
        this.setMinesArray(minesPlace);
        this.setBoardArray(setNumbersInBoard(boardArray));

        displayBoardState(boardArray);
    }

    public void populateBoardArray(Cell[][] boardArray){

        for(int x = 1; x < 7; x++){
            for(int y = 1; y < 7; y++){
                boardArray[x][y] = new Cell(x, y, 0, "Empty");
            }
        }
    }

    public Cell[][] dynamicMineGenerator(){

        Cell[][] minePlace = new Cell[7][7];
        for(int x = 1; x <= 7; x++) {
            Cell bomb1 = new Cell((int) (Math.random() * (7 - 1) + 1), (int) (Math.random() * (7 - 1) + 1), 100, "Bomb");
            System.out.println("Mine => " + bomb1.getColumnIndex() + bomb1.getRowElement());
            minePlace[bomb1.getColumnIndex()][bomb1.getRowElement()] = bomb1;
        }

        return minePlace;
    }

    public void placeMinesInBoard(Cell[][] boardArray, Cell[][] mines){

        Set<Cell> minesOnBoard = new LinkedHashSet<>();
        for(int x = 1; x < 7; x++){
            for(int y = 1; y < 7; y++){

                try {
                    if(mines[x][y] != null) {
                        if (boardArray[x][y].getColumnIndex() == mines[x][y].getColumnIndex() && boardArray[x][y].getRowElement() == mines[x][y].getRowElement()) {
                            boardArray[x][y] = mines[x][y];
                            minesOnBoard.add(mines[x][y]);
                        }
                    }
                }catch (Exception e){
                    System.out.println("Do Nothing");
                }
            }
        }
        setMinesOnBoard(minesOnBoard);
    }


    public Cell[][] setNumbersInBoard(Cell[][] boardArray){

        System.out.println("boardArraySize: " + boardArray.length);
        for(int x = 1; x < 7; x++){
            for(int y = 1; y < 7; y++){
                if(boardArray[x][y].getGameRole().equalsIgnoreCase("Bomb")){

                    //next element same row to the right
                    if( y + 1 < 7  && boardArray[x][y + 1].getRowValue() != 100){
                        boardArray[x][y + 1].setRowValue(boardArray[x][y + 1].getRowValue() + 1);
                        boardArray[x][y + 1].setGameRole("Number");
                    }

                    //next element same row to the left
                    if( y - 1 > ZERO && boardArray[x][y - 1].getRowValue() != 100){
                        boardArray[x][y - 1].setRowValue(boardArray[x][y - 1].getRowValue() + 1);
                        boardArray[x][y - 1].setGameRole("Number");
                    }

                    //element right above
                    if(x - 1 > ZERO && boardArray[x - 1][y].getRowValue() != 100){
                        boardArray[x - 1][y].setRowValue(boardArray[x - 1][y].getRowValue() + 1);
                        boardArray[x - 1][y].setGameRole("Number");
                    }

                    //element right above and to the left
                    if(x - 1 > ZERO  && y - 1 > ZERO && boardArray[x - 1][y - 1].getRowValue() != 100){
                        boardArray[x - 1][y - 1].setRowValue(boardArray[x - 1][y - 1].getRowValue() + 1);
                        boardArray[x - 1][y - 1].setGameRole("Number");
                    }

                    //element right above and to the right
                    if( x - 1 > ZERO  && y + 1 < 7 && boardArray[x - 1][y + 1].getRowValue() != 100){
                        boardArray[x - 1][y + 1].setRowValue(boardArray[x - 1][y + 1].getRowValue() + 1);
                        boardArray[x - 1][y + 1].setGameRole("Number");
                    }

                    //Below ZOne
                    //element right below
                    if( x + 1 < 7 && boardArray[x + 1][y].getRowValue() != 100){
                        boardArray[x + 1][y].setRowValue(boardArray[x + 1][y].getRowValue() + 1);
                        boardArray[x + 1][y].setGameRole("Number");
                    }

                    //element right below and to the left
                    if(x + 1 < 7 && y - 1 > ZERO && boardArray[x + 1][y - 1].getRowValue() != 100){
                        boardArray[x + 1][y - 1].setRowValue(boardArray[x + 1][y - 1].getRowValue() + 1);
                        boardArray[x + 1][y - 1].setGameRole("Number");
                    }

                    //element right below and to the right
                    if(x + 1 < 7 && y + 1 < 7  && boardArray[x + 1][y + 1].getRowValue() != 100){
                        boardArray[x + 1][y + 1].setRowValue(boardArray[x + 1][y + 1].getRowValue() + 1);
                        boardArray[x + 1][y + 1].setGameRole("Number");
                    }
                }
            }
        }

        return boardArray;
    }

    private void displayBoardState(Cell[][] boardArray){
        System.out.println("Internal Game Created");
        for(int x = 1; x < 7; x++){
            System.out.println();
            for(int y = 1; y < 7; y++) {
                if(boardArray[x][y] != null){
                    System.out.print(boardArray[x][y].getColumnIndex() + "" + boardArray[x][y].getRowElement() + "[" + boardArray[x][y].getGameRole() + " || Seen? " + boardArray[x][y].isSeen() + "]");
                }
            }

        }
    }

    public static void main(String[] args) {
       // MineSweeperComponent mineSweeperComponent = new MineSweeperComponent();
       // mineSweeperComponent.GameSetup();
    }
}
