package com.deviget.domain;

import com.google.gson.Gson;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "game_state")
public class GameState implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "game_id")
    private String gameId;

    @Column(name = "user_id")
    private String userId;

    @Lob
    @Column(name = "board_array")
    private String boardArray;

    public String getBoardArrayString() {
        return boardArray;
    }

    public void setBoardArrayString(String boardArray) {
        this.boardArray = boardArray;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String serializeBoardToJsonString(Cell[][] boardArray) {
        Gson gson = new Gson();
        String recordsSerialized = gson.toJson(boardArray);
        System.out.println("Serialize Version: " + recordsSerialized);
        return recordsSerialized;
    }

    public Cell[][] stringToBoardArray(String retrieveBoard) {
        Gson gson = new Gson();
        Cell retrievedArray[][] = gson.fromJson(retrieveBoard, Cell[][].class);
        //System.out.println("De - Serialize Version: " + retrievedArray);
        /*
        for(int x = 1; x < retrievedArray.length; x++){
            for(int y = 1; y < retrievedArray.length; y++){
                System.out.println("Element found: " + retrievedArray[x][y].getColumnIndex() + + retrievedArray[x][y].getRowElement());
            }
        } */
        return retrievedArray;
    }


}
