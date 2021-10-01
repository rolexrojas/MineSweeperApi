package com.deviget.CustomMineSweeper;
import com.deviget.component.MineSweeperComponent;
import com.deviget.config.ApplicationProperties;
import com.deviget.domain.Cell;
import com.deviget.exception.DuplicateKeyException;
import com.deviget.exception.NoGameFoundException;
import com.deviget.types.BoardMoveResponse;
import com.deviget.types.BoardMoveResponseType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomMineSweeperApplicationTests {

	private ApplicationProperties applicationProperties;

	private MineSweeperComponent aMineSweeperManager,  anotherMineSweeperManager, fixedMineSweeperGame;

	public void setaMineSweeperManager(MineSweeperComponent aMineSweeperManager) {
		this.aMineSweeperManager = aMineSweeperManager;
	}

	public void setAnotherMineSweeperManager(MineSweeperComponent anotherMineSweeperManager) {
		this.anotherMineSweeperManager = anotherMineSweeperManager;
	}

	public void setFixedMineSweeperGame(MineSweeperComponent fixedMineSweeperGame) {
		this.fixedMineSweeperGame = fixedMineSweeperGame;
	}

	@BeforeEach
	void initUseCase(){

		//GAME ONE SETUP SMALL BOARD SIZE
		MineSweeperComponent aMineSweeperManager = new MineSweeperComponent();
		applicationProperties = new ApplicationProperties();
		applicationProperties.setBoardColumnSize(6);
		applicationProperties.setBoardRowSize(6);
		applicationProperties.setMineTotal(2);
		aMineSweeperManager.setApplicationProperties(applicationProperties);
		setaMineSweeperManager(aMineSweeperManager);

		//GAME TWO SETUP BIG BOARD SIZE
		MineSweeperComponent anotherMineSweeperManager = new MineSweeperComponent();
		applicationProperties = new ApplicationProperties();
		applicationProperties.setBoardColumnSize(10);
		applicationProperties.setBoardRowSize(10);
		applicationProperties.setMineTotal(20);
		anotherMineSweeperManager.setApplicationProperties(applicationProperties);
		setAnotherMineSweeperManager(anotherMineSweeperManager);

		//GAME WITH FIXED MINES
		MineSweeperComponent fixedMineSweeperGame = new MineSweeperComponent();
		applicationProperties = new ApplicationProperties();
		applicationProperties.setBoardColumnSize(7);
		applicationProperties.setBoardRowSize(7);
		applicationProperties.setMineTotal(2);
		fixedMineSweeperGame.setApplicationProperties(applicationProperties);
		setFixedMineSweeperGame(fixedMineSweeperGame);

	}

	@Test
	void givenRepeatedMoveWhenGameStartedWillThrowDuplicateKeyException () {
		DuplicateKeyException duplicateKeyException = assertThrows(DuplicateKeyException.class, this::repeatedMoveWhenGameHasStartedSetUp);
		assertEquals("This board key has already been revealed", duplicateKeyException.getMessage());

	}

	@Test
	void givenMoveWhenNotGameStartedWillThrowNoGameFoundException() {
		NoGameFoundException noGameFoundException = assertThrows(NoGameFoundException.class, this::givenMoveWhenGameNotStartedSetUp);
		assertEquals("No game was found", noGameFoundException.getMessage());

	}

	@Test
	void givenFixedGameWhenGoodMovesAreGivenGameIsWon() throws NoGameFoundException, DuplicateKeyException {
		Cell[][] emptyGameBoard = new Cell[7][7];
		Cell[][] minePlace = new Cell[7][7];
		//fixedBombDeclaration
		Cell bomb1 = new Cell( 3, 3, 100, "Bomb");
		Cell bomb2 = new Cell( 4, 3, 100, "Bomb");
		minePlace[bomb1.getColumnIndex()][bomb1.getRowElement()] = bomb1;
		minePlace[bomb2.getColumnIndex()][bomb2.getRowElement()] = bomb2;
		//fixedMineSweeperGame.GameSetup();
		//fixedMineSweeperGame.clearGameData();
		fixedMineSweeperGame.populateBoardArray(emptyGameBoard);
		fixedMineSweeperGame.placeMinesInBoard(emptyGameBoard, minePlace);
		fixedMineSweeperGame.setBoardArray(fixedMineSweeperGame.setNumbersInBoard(emptyGameBoard));
		fixedMineSweeperGame.hasWinGame(emptyGameBoard);

		fixedMineSweeperGame.makeMoveAndGetResult(1, 1);
		//fixedMineSweeperGame.makeMoveAndGetResult(3, 2);
		BoardMoveResponse boardMoveResponse = fixedMineSweeperGame.makeMoveAndGetResult(4, 6);
		assertEquals(BoardMoveResponseType.GAME_WON, boardMoveResponse.getBoardMoveResponseType());
	}


	@Test
	void givenFixedGameWhenBombIsClickedGivenGameIsLost() throws NoGameFoundException, DuplicateKeyException {
		Cell[][] emptyGameBoard = new Cell[7][7];
		Cell[][] minePlace = new Cell[7][7];
		//fixedBombDeclaration
		Cell bomb1 = new Cell( 3, 3, 100, "Bomb");
		Cell bomb2 = new Cell( 4, 3, 100, "Bomb");
		minePlace[bomb1.getColumnIndex()][bomb1.getRowElement()] = bomb1;
		minePlace[bomb2.getColumnIndex()][bomb2.getRowElement()] = bomb2;
		//fixedMineSweeperGame.GameSetup();
		//fixedMineSweeperGame.clearGameData();
		fixedMineSweeperGame.populateBoardArray(emptyGameBoard);
		fixedMineSweeperGame.placeMinesInBoard(emptyGameBoard, minePlace);
		fixedMineSweeperGame.setBoardArray(fixedMineSweeperGame.setNumbersInBoard(emptyGameBoard));
		fixedMineSweeperGame.hasWinGame(emptyGameBoard);

		BoardMoveResponse boardMoveResponseInfo = fixedMineSweeperGame.makeMoveAndGetResult(1, 1);
		assertEquals(BoardMoveResponseType.GAME_INFO, boardMoveResponseInfo.getBoardMoveResponseType());

		BoardMoveResponse boardMoveResponse = fixedMineSweeperGame.makeMoveAndGetResult(4, 3);
		assertEquals(BoardMoveResponseType.GAME_OVER, boardMoveResponse.getBoardMoveResponseType());
	}

	@Test
	void sameGameSetupFailsAtCreatingIdenticalBoards () {
		aMineSweeperManager.GameSetup();;
		anotherMineSweeperManager.GameSetup();;
		assertNotNull(aMineSweeperManager.getBoardArray());
		assertNotNull(anotherMineSweeperManager.getBoardArray());
		assertNotEquals(aMineSweeperManager.getBoardArray(), anotherMineSweeperManager.getBoardArray());

	}

	@Test
	void differentGameSetupFailCreatingMinesIdentical () {

		aMineSweeperManager.GameSetup();
		anotherMineSweeperManager.GameSetup();
		assertNotNull(aMineSweeperManager.getMinesArray());
		assertNotNull(anotherMineSweeperManager.getMinesArray());
		assertTrue(anotherMineSweeperManager.getMinesArray().length > aMineSweeperManager.getMinesArray().length);

	}

	void repeatedMoveWhenGameHasStartedSetUp() throws NoGameFoundException, DuplicateKeyException {
		aMineSweeperManager.GameSetup();
		aMineSweeperManager.makeMoveAndGetResult(1, 2);
		aMineSweeperManager.makeMoveAndGetResult(1, 2);
	}

	void givenMoveWhenGameNotStartedSetUp() throws NoGameFoundException, DuplicateKeyException {
		aMineSweeperManager.makeMoveAndGetResult(1, 2);
	}
}

