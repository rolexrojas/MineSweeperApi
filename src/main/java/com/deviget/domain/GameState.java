package com.deviget.domain;

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



}
