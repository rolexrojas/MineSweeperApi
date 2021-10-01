package com.deviget.controller;

import com.deviget.component.JwtManager;
import com.deviget.component.MineSweeperComponent;
import com.deviget.config.ApplicationProperties;
import com.deviget.exception.DuplicateKeyException;
import com.deviget.exception.NoGameFoundException;
import com.deviget.types.BoardMove;
import com.deviget.types.BoardMoveResponse;
import com.deviget.types.TokenDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class MineSweeperController {

    private MineSweeperComponent mineSweeperComponent;

    private JwtManager jwtManager;

    private ApplicationProperties applicationProperties;

    @Autowired
    public void SetMineSweeperComponent(MineSweeperComponent mineSweeperComponent) {
        this.mineSweeperComponent = mineSweeperComponent;
    }

    @Autowired
    public void setJwtManager(JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    @Autowired
    public void setApplicationProperties(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @GetMapping(path = "/startNewGame")
    public ResponseEntity<String> GameSetup(@RequestHeader(required = true, name = "Authorization") String token) {

        if (!jwtManager.validateToken(token)) {
            ResponseEntity.status(400).body("{'Error': 'Invalid token'}");
        }
        //generate boardArray with no of cells
        System.out.println("GameSetup");

        try {
            mineSweeperComponent.GameSetup();
        } catch (Exception e) {
            return ResponseEntity.status(400).body("{'Error': 'Unexpected error'}");
        }


        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("New Game has been created");
    }

    @GetMapping(path = "/saveGame")
    public ResponseEntity saveGame(@RequestHeader(required = true, name = "Authorization") String token) {

        TokenDataDTO tokenDataDTO = jwtManager.parseTokenToModel(token);
        if (!jwtManager.validateToken(token)) {
            ResponseEntity.status(400).body("{'Error': 'Invalid token'}");
        }

        System.out.println("saveGame");
        try {
            mineSweeperComponent.saveCurrentGame();
        } catch (Exception e) {
            return ResponseEntity.status(400).body("{'Error': 'Unexpected error'}");
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("Game has been saved");
    }


    @GetMapping(path = "/resumeGame")
    public ResponseEntity resumeGame(@RequestHeader(required = false, name = "Authorization") String token) {

        TokenDataDTO tokenDataDTO = jwtManager.parseTokenToModel(token);
        if (!jwtManager.validateToken(token)) {
            ResponseEntity.status(400).body("{'Error': 'Invalid token'}");
        }

        System.out.println("resumeGame");
        try {
            mineSweeperComponent.resumeGame();
        } catch (Exception e) {
            return ResponseEntity.status(400).body("{'Error': 'Unexpected error'}");
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("Game resumed succesfully");
    }

    @GetMapping(path = "/makeMove")
    public ResponseEntity makeMove(@RequestBody BoardMove aboardMove, @RequestHeader(required = false, name = "Authorization") String token) {

        if (!jwtManager.validateToken(token)) {
            ResponseEntity.status(400).body("{'Error': 'Invalid token'}");
        }
        try {
            if (aboardMove.getColumnIndex() < applicationProperties.getBoardColumnSize() && aboardMove.getRowIndex() < applicationProperties.getBoardRowSize()
                    && aboardMove.getColumnIndex() > 0 && aboardMove.getRowIndex() > 0) {

                BoardMoveResponse boardMoveResponse = mineSweeperComponent.makeMoveAndGetResult(aboardMove.getColumnIndex(), aboardMove.getRowIndex());
                return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(boardMoveResponse);
            }
        } catch (NullPointerException e) {
            return ResponseEntity.status(400).body("{'Error': 'Unexpected error '}");
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(404).body("{'Illegal Move': 'This key has already been clicked'}");
        } catch (NoGameFoundException e) {
            return ResponseEntity.status(404).body("{'Error': 'No game found, make sure you start a new game'}");
        }

        return ResponseEntity.status(404).body("{'Error': 'No game found, make sure you start a new game'}");
    }
}
