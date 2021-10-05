package com.deviget.component;

import com.deviget.config.ApplicationProperties;
import com.deviget.domain.Cell;
import com.deviget.domain.GameState;
import com.deviget.exception.DuplicateKeyException;
import com.deviget.exception.NoGameFoundException;
import com.deviget.repository.GameStateRepository;
import com.deviget.types.BoardMoveResponse;
import com.deviget.types.BoardMoveResponseType;
import com.deviget.types.CellTypes;
import com.deviget.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class MineSweeperComponent {
    private Cell[][] boardArray;
    private Cell[][] minesArray;
    private GameStateRepository gameStateRepository;
    private long startTime = 0L;
    private final int ZERO = 0;
    private ApplicationProperties applicationProperties;

    @Autowired
    public void setGameStateRepository(GameStateRepository gameStateRepository) {
        this.gameStateRepository = gameStateRepository;
    }

    @Autowired
    public void setApplicationProperties(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

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

    public Cell[][] getBoardArray() {
        return boardArray;
    }

    private Set<Cell> getMinesOnBoard() {
        return minesOnBoard;
    }

    private long getStartTime() {
        return startTime;
    }

    private void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Cell[][] getMinesArray() {
        return minesArray;
    }



    public void clearGameData() {
        setBoardArray(null);
        setMinesArray(null);
    }

    /**
     * saveCurrentGame
     * @param username username used to save the current game to db
     * @throws NoGameFoundException when no game is found in order to save
     */
    public void saveCurrentGame(String username) throws NoGameFoundException {

        if (this.getBoardArray() == null) {
            throw new NoGameFoundException("No game was found");
        }

        GameState gameState = new GameState();
        gameState.setBoardArrayString(gameState.serializeBoardToJsonString(this.getBoardArray()));
        gameState.setGameId(username);
        gameState.setUserId(username);
        gameStateRepository.save(gameState);
    }



    /**
     * resumeGame
     * @param username username used to retrieve the sved game from db
     * @throws NoGameFoundException when no game is found in order to save
     */
    public void resumeGame(String username) throws NoGameFoundException {
        try {
            clearGameData();
            GameState gameState = gameStateRepository.findFirstByGameIdEquals(username);
            System.out.println("Game found");
            System.out.println(gameState.getUserId());
            Cell[][] retrievedBoard = gameState.stringToBoardArray(gameState.getBoardArrayString());
            hasWinGame(retrievedBoard);
            this.setBoardArray(retrievedBoard);
            System.out.println("Game found finish display");
        } catch (Exception e) {
            throw new NoGameFoundException("No game was found");
        }
    }

    /**
     * GameSetup
     * This method initialize a new game
     */
    public void GameSetup() {
        //generate boardArray with no of cells
        Cell[][] boardArray = new Cell[applicationProperties.getBoardColumnSize()][applicationProperties.getBoardRowSize()];

        //populate two dimensional array with empty fields
        populateBoardArray(boardArray);

        //generate random Mines
        Cell[][] minesPlace = dynamicMineGenerator();

        //Place Mine in board
        placeMinesInBoard(boardArray, minesPlace);
        this.setMinesArray(minesPlace);
        this.setBoardArray(setNumbersInBoard(boardArray));

        displayBoardState(boardArray);

        setStartTime(System.currentTimeMillis());
    }

    /**
     * populateBoardArray
     * @param boardArray receives a 2d array representing an empty board
     */
    public void populateBoardArray(Cell[][] boardArray) {

        for (int x = 1; x < applicationProperties.getBoardColumnSize(); x++) {
            for (int y = 1; y < applicationProperties.getBoardRowSize(); y++) {
                boardArray[x][y] = new Cell(x, y, 0, "Empty");
            }
        }
    }

    /**
     * dynamicMineGenerator
     * This methods create randomly generated mines and placed in an empty board
     *  that clones the size of the original board
     * @return 2D array representing the minesweeper board
     */
    public Cell[][] dynamicMineGenerator() {

        Cell[][] minePlace = new Cell[applicationProperties.getBoardColumnSize()][applicationProperties.getBoardRowSize()];
        for (int x = 1; x <= applicationProperties.getMineTotal(); x++) {
            Cell bomb1 = new Cell((int) (Math.random() * (applicationProperties.getBoardColumnSize() - 1) + 1), (int) (Math.random() * (applicationProperties.getBoardRowSize() - 1) + 1), 100, "Bomb");
            System.out.println("Mine => " + bomb1.getColumnIndex() + bomb1.getRowElement());
            minePlace[bomb1.getColumnIndex()][bomb1.getRowElement()] = bomb1;
        }

        return minePlace;
    }

    /**
     * placeMinesInBoard
     *
     * This method takes a board with same size of game board and place the mines in the current game board
     *
     * @param boardArray 2d game representation of game cells
     * @param mines 2d game representation of mines positions
     */
    public void placeMinesInBoard(Cell[][] boardArray, Cell[][] mines) {

        Set<Cell> minesOnBoard = new LinkedHashSet<>();
        for (int x = 1; x < applicationProperties.getBoardColumnSize(); x++) {
            for (int y = 1; y < applicationProperties.getBoardRowSize(); y++) {

                try {
                    if (mines[x][y] != null) {
                        if (boardArray[x][y].getColumnIndex() == mines[x][y].getColumnIndex() && boardArray[x][y].getRowElement() == mines[x][y].getRowElement()) {
                            boardArray[x][y] = mines[x][y];
                            minesOnBoard.add(mines[x][y]);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Do Nothing");
                }
            }
        }
        setMinesOnBoard(minesOnBoard);
    }

    /**
     * setNumbersInBoard
     * This methods place the numbers in the minesweeper board
     * @param boardArray 2d array that represents the current game
     * @return 2d array
     */
    public Cell[][] setNumbersInBoard(Cell[][] boardArray) {

        System.out.println("boardArraySize: " + boardArray.length);
        for (int x = 1; x < applicationProperties.getBoardColumnSize(); x++) {
            for (int y = 1; y < applicationProperties.getBoardRowSize(); y++) {
                if (boardArray[x][y].getGameRole().equalsIgnoreCase("Bomb")) {

                    //next element same row to the right
                    if (y + 1 < applicationProperties.getBoardRowSize() && boardArray[x][y + 1].getRowValue() != 100) {
                        boardArray[x][y + 1].setRowValue(boardArray[x][y + 1].getRowValue() + 1);
                        boardArray[x][y + 1].setGameRole("Number");
                    }

                    //next element same row to the left
                    if (y - 1 > ZERO && boardArray[x][y - 1].getRowValue() != 100) {
                        boardArray[x][y - 1].setRowValue(boardArray[x][y - 1].getRowValue() + 1);
                        boardArray[x][y - 1].setGameRole("Number");
                    }

                    //element right above
                    if (x - 1 > ZERO && boardArray[x - 1][y].getRowValue() != 100) {
                        boardArray[x - 1][y].setRowValue(boardArray[x - 1][y].getRowValue() + 1);
                        boardArray[x - 1][y].setGameRole("Number");
                    }

                    //element right above and to the left
                    if (x - 1 > ZERO && y - 1 > ZERO && boardArray[x - 1][y - 1].getRowValue() != 100) {
                        boardArray[x - 1][y - 1].setRowValue(boardArray[x - 1][y - 1].getRowValue() + 1);
                        boardArray[x - 1][y - 1].setGameRole("Number");
                    }

                    //element right above and to the right
                    if (x - 1 > ZERO && y + 1 < applicationProperties.getBoardRowSize() && boardArray[x - 1][y + 1].getRowValue() != 100) {
                        boardArray[x - 1][y + 1].setRowValue(boardArray[x - 1][y + 1].getRowValue() + 1);
                        boardArray[x - 1][y + 1].setGameRole("Number");
                    }

                    //Below ZOne
                    //element right below
                    if (x + 1 < applicationProperties.getBoardColumnSize() && boardArray[x + 1][y].getRowValue() != 100) {
                        boardArray[x + 1][y].setRowValue(boardArray[x + 1][y].getRowValue() + 1);
                        boardArray[x + 1][y].setGameRole("Number");
                    }

                    //element right below and to the left
                    if (x + 1 < applicationProperties.getBoardColumnSize() && y - 1 > ZERO && boardArray[x + 1][y - 1].getRowValue() != 100) {
                        boardArray[x + 1][y - 1].setRowValue(boardArray[x + 1][y - 1].getRowValue() + 1);
                        boardArray[x + 1][y - 1].setGameRole("Number");
                    }

                    //element right below and to the right
                    if (x + 1 < applicationProperties.getBoardColumnSize() && y + 1 < applicationProperties.getBoardRowSize() && boardArray[x + 1][y + 1].getRowValue() != 100) {
                        boardArray[x + 1][y + 1].setRowValue(boardArray[x + 1][y + 1].getRowValue() + 1);
                        boardArray[x + 1][y + 1].setGameRole("Number");
                    }
                }
            }
        }

        return boardArray;
    }

    /**
     * displayBoardState: receives and print each cell and its properties to console output
     * @param boardArray contains full list of items that are being evaluated in the matching line iteration
     */
    private void displayBoardState(Cell[][] boardArray) {
        System.out.println("Internal Game Created");
        for (int x = 1; x < applicationProperties.getBoardColumnSize(); x++) {
            System.out.println();
            for (int y = 1; y < applicationProperties.getBoardRowSize(); y++) {
                if (boardArray[x][y] != null) {
                    System.out.print(boardArray[x][y].getColumnIndex() + "" + boardArray[x][y].getRowElement() + "[" + boardArray[x][y].getGameRole() + " || Seen? " + boardArray[x][y].isSeen() + "]");
                }
            }

        }
    }

    /**
     * makeMoveAndGetResult: This methods receive a board position and extract the cell and its value
     * @param columnIndex Positive value pointing to column position of 2d game board array
     * @param rowIndex Positive value pointing to column position of 2d game board array
     * @return BoardMoveResponse object containing details about the element/cell found
     * @throws DuplicateKeyException when the board position has already been revealed
     * @throws NoGameFoundException when there is not a current game found
     */
    public BoardMoveResponse makeMoveAndGetResult(int columnIndex, int rowIndex) throws DuplicateKeyException, NoGameFoundException {

        Cell cell = new Cell(columnIndex, rowIndex, 0, "Empty");
        System.out.println("Clicked Cell=> " + cell.getColumnIndex() + " | " + cell.getRowElement());

        if (this.getBoardArray() == null) {
            throw new NoGameFoundException("No game was found");
        }

        Cell cellBoard = this.getBoardArray()[cell.getColumnIndex()][cell.getRowElement()];

        if (cellBoard.isSeen()) {
            throw new DuplicateKeyException("This board key has already been revealed");
        }
        //marked as revealed
        cellBoard.setSeen(true);
        BoardMoveResponse boardMoveResponse = new BoardMoveResponse();
        LinkedHashSet<Cell> numberCell = new LinkedHashSet<>();
        LinkedHashSet<Cell> emptyCell = new LinkedHashSet<>();

        int secs = Utilities.timerCount(getStartTime(), System.currentTimeMillis());
        String duration = String.format("Time Elapsed: %s Seconds", secs);

        if (cellBoard.getGameRole().equalsIgnoreCase(CellTypes.EMPTY.label)) {
            System.out.println("Clicked a Empty => " + cellBoard.getColumnIndex() + cellBoard.getRowElement() + cellBoard.getGameRole());
            boardMoveResponse = fillEmptyAdjacentPositions(this.getBoardArray(), cellBoard);
            boardMoveResponse.setDuration(duration);
        }

        if (cellBoard.getGameRole().equalsIgnoreCase(CellTypes.NUMBER.label)) {
            System.out.println("Clicked a Number => " + cellBoard.getColumnIndex() + cellBoard.getRowElement() + cellBoard.getGameRole());
            boardMoveResponse.setBoardMoveResponseType(BoardMoveResponseType.GAME_INFO);
            numberCell.add(cellBoard);
            boardMoveResponse.setEmptyCellList(emptyCell);
            boardMoveResponse.setNumberCellList(numberCell);
            boardMoveResponse.setDuration(duration);
        }

        if (cellBoard.getGameRole().equalsIgnoreCase(CellTypes.BOMB.label)) {
            System.out.println("[GAME OVER ] - Clicked a Bomb => " + cellBoard.getColumnIndex() + cellBoard.getRowElement() + cellBoard.getGameRole());

            boardMoveResponse.setBoardMoveResponseType(BoardMoveResponseType.GAME_OVER);
            boardMoveResponse.setEmptyCellList(emptyCell);
            boardMoveResponse.setNumberCellList(numberCell);
            boardMoveResponse.setBombCellList(getMinesOnBoard());
            boardMoveResponse.setDuration(duration);
            //End Of game and return all bombs
            clearGameData();

            return boardMoveResponse;
        }

        if (hasWinGame(this.getBoardArray())) {
            boardMoveResponse.setBoardMoveResponseType(BoardMoveResponseType.GAME_WON);
            int x = 0;
            System.out.println();
            while (x < 30) {
                System.out.print("\t\t[GAME WON ] - !!Victory!!");
                if (x == 10) System.out.println();
                if (x == 20) System.out.println();
                x++;
            }
            clearGameData();
        }


        return boardMoveResponse;
    }

    /**
     * hasWinGame: This methods count over the cells (empty cells and number) that are unrevealed in the board
     * when thecout matches cero it lauches the game win flow
     * @param boardArray 2d representation of the minesweeper game board.
     */
    public boolean hasWinGame(Cell[][] boardArray) {

        System.out.println("GAME VIEW REPRESENTATION");
        int totalToWin = ((applicationProperties.getBoardColumnSize() - 1) * (applicationProperties.getBoardRowSize() - 1) - applicationProperties.getMineTotal());
        boolean resp = false;
        int leftCounter = 0;
        for (int x = 1; x < applicationProperties.getBoardColumnSize(); x++) {
            System.out.println();
            for (int y = 1; y < applicationProperties.getBoardRowSize(); y++) {
                if (boardArray[x][y] != null) {

                    String viewStatus = boardArray[x][y].isSeen() ? "[---" + boardArray[x][y].getRowValue() + "--]" : "[Hidden]";
                    // boardArray[x][y].isSeen() ? "Seen" : "Hidden";
                    System.out.print("\t" + viewStatus + "   ||   ");
                    if (boardArray[x][y].isSeen()) {
                        leftCounter++;
                    }
                }
            }
        }

        if (leftCounter == totalToWin) {
            resp = true;
        }

        return resp;
    }


    /**
     * fillEmptyAdjacentPositions: misleading name for this one
     * This methods separate the numbers and cells and prepare the response object
     * @param boardArray 2d bi-dimensional array representation of minesweeper game board
     * @param cell specific cell location received from the user
     * @return boardMoveResponse object containing list of numbers cells,  empty cells found adjacent to the given
     * number.
     */
    public BoardMoveResponse fillEmptyAdjacentPositions(Cell[][] boardArray, Cell cell) {
        Set<Cell> listOfElements = new LinkedHashSet<>();
        Set<Cell> justNumbersList = new LinkedHashSet<>();
        Set<Cell> justEmptyList = new LinkedHashSet<>();
        Set<Cell> elementsToEvaluate;

        listOfElements.add(cell);

        /**
         * This iteration is to make sure the loop doesn't finish until evaluation size has checked all elements
         * */
        for (int x = 0; x < listOfElements.size(); x++) {
            //System.out.println("listOfElement size=> " + listOfElements.size());
            //System.out.println("Iteration Counter=> " + x);
            elementsToEvaluate = new LinkedHashSet<>(findElementsToEvaluate(boardArray, listOfElements));
            listOfElements.remove(cell);
            //System.out.println("Consumed elements list");
            for (Cell comboCell : elementsToEvaluate) {
                if (comboCell.getGameRole().equalsIgnoreCase("Number")) {
                    justNumbersList.add(comboCell);
                    listOfElements.remove(comboCell);
                }

                if (comboCell.getGameRole().equalsIgnoreCase("Empty")) {
                    justEmptyList.add(comboCell);
                    boolean resp = listOfElements.add(comboCell);
                    if (!resp) {
                        //System.out.println("found duplicated element: " + comboCell.getColumnIndex() + comboCell.getRowElement());
                        listOfElements.remove(comboCell);
                    }
                }
            }

        }

        BoardMoveResponse boardMoveResponse = new BoardMoveResponse();
        boardMoveResponse.setNumberCellList(justNumbersList);
        boardMoveResponse.setEmptyCellList(justEmptyList);
        boardMoveResponse.setBoardMoveResponseType(BoardMoveResponseType.GAME_INFO);

        return boardMoveResponse;
    }

    /**
     * findElementsToEvaluate
     * @param boardArray 2d bi-dimensional array representation of minesweeper game board
     * @param evaluationList list of method chosen to find its next adjacent elements
     * @return Set collections containing the cell elements found
     */
    private Set<Cell> findElementsToEvaluate(Cell[][] boardArray, Set<Cell> evaluationList) {
        Set<Cell> elementFound = new LinkedHashSet<>();
        //System.out.println("Eval List size= " + evaluationList.size());
        //This iteration make sure to run trough all elements of evaluation list
        Iterator<Cell> itr = evaluationList.iterator();
        while (itr.hasNext()) {
            Cell currentElement = itr.next();
            //System.out.println("celda a evaluar " + currentElement.getColumnIndex() + currentElement.getRowElement() + currentElement.getGameRole());
            //sino se ha evaluado se buscan los elementos adjacentes
            //if(!currentElement.checked){
            Set<Cell> cells = findAdjacentPositionsBasedOnGivenCell(boardArray, currentElement);
            elementFound.addAll(cells);
        }

        return elementFound;
    }

    /**
     * fillEmptyAdjacentPositions
     * @param boardArray 2d bi-dimensional array representation of minesweeper game board
     * @param cell specific cell location received from the user
     * @return Set collection containing all cells found adjacent to the given number.
     */
    public Set<Cell> findAdjacentPositionsBasedOnGivenCell(Cell[][] boardArray, Cell cell) {
        Set<Cell> contigousEmptyCellsFound = new LinkedHashSet<>();
        boolean up = false, down = false, right = false, left = false, upRight = false, upLeft = false, downLeft = false, downRight = false;

        for (int x = 1; x < applicationProperties.getBoardColumnSize(); x++) {  //BOARD_COLUMNS - ROWVALUE should have the returning positions to the edge
            //next element same row to the right
            if (!right && cell.getRowElement() + x < applicationProperties.getBoardColumnSize() && boardArray[cell.getColumnIndex()][cell.getRowElement() + x].getRowValue() != 100) {

                //SI EL ELEMENTO DE LA DERECHA ESTA VACIO SIMPLEMENTE LO AGREGO AL ARREGLO
                if (boardArray[cell.getColumnIndex()][cell.getRowElement() + x].getGameRole().equalsIgnoreCase("Empty")) {
                    boardArray[cell.getColumnIndex()][cell.getRowElement() + x].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex()][cell.getRowElement() + x]);
                    right = true;
                }

                if (boardArray[cell.getColumnIndex()][cell.getRowElement() + x].getGameRole().equalsIgnoreCase("Number")) {
                    boardArray[cell.getColumnIndex()][cell.getRowElement() + x].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex()][cell.getRowElement() + x]);
                    right = true;
                }
            }

            if (!left && cell.getRowElement() - x > ZERO && boardArray[cell.getColumnIndex()][cell.getRowElement() - x].getRowValue() != 100) {

                //SI EL ELEMENTO DE LA IZQUIERDA ESTA VACIO SIMPLEMENTE LO AGREGO AL ARREGLO
                if (boardArray[cell.getColumnIndex()][cell.getRowElement() - x].getGameRole().equalsIgnoreCase("Empty")) {
                    boardArray[cell.getColumnIndex()][cell.getRowElement() - x].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex()][cell.getRowElement() - x]);
                    left = true;
                }

                //SI ES UN NUMERO LO AGREGO AL ARREGLO Y TERMINO EL CICLO PARA EVITAR MAS NUMEROS AGREGADOS
                if (boardArray[cell.getColumnIndex()][cell.getRowElement() - x].getGameRole().equalsIgnoreCase("Number")) {
                    boardArray[cell.getColumnIndex()][cell.getRowElement() - x].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex()][cell.getRowElement() - x]);
                    left = true;
                }
            }

            //AGREGANDO ELEMENTO SUPERIOR
            if (!up && cell.getColumnIndex() - x > ZERO && boardArray[cell.getColumnIndex() - x][cell.getRowElement()].getRowValue() != 100) {

                //SI EL ELEMENTO DE LA DERECHA ESTA VACIO SIMPLEMENTE LO AGREGO AL ARREGLO
                if (boardArray[cell.getColumnIndex() - x][cell.getRowElement()].getGameRole().equalsIgnoreCase("Empty")) {
                    boardArray[cell.getColumnIndex() - x][cell.getRowElement()].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex() - x][cell.getRowElement()]);
                    up = true;
                }

                if (boardArray[cell.getColumnIndex() - x][cell.getRowElement()].getGameRole().equalsIgnoreCase("Number")) {
                    boardArray[cell.getColumnIndex() - x][cell.getRowElement()].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex() - x][cell.getRowElement()]);
                    up = true;
                }
            }
            //agregando elemento debajo
            if (!down && cell.getColumnIndex() + x < applicationProperties.getBoardColumnSize() && boardArray[cell.getColumnIndex() + x][cell.getRowElement()].getRowValue() != 100) {

                //SI EL ELEMENTO DEBAJO ESTA VACIO SIMPLEMENTE LO AGREGO AL ARREGLO
                if (boardArray[cell.getColumnIndex() + x][cell.getRowElement()].getGameRole().equalsIgnoreCase("Empty")) {
                    boardArray[cell.getColumnIndex() + x][cell.getRowElement()].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex() + x][cell.getRowElement()]);
                    down = true;
                }

                if (boardArray[cell.getColumnIndex() + x][cell.getRowElement()].getGameRole().equalsIgnoreCase("Number")) {
                    boardArray[cell.getColumnIndex() + x][cell.getRowElement()].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex() + x][cell.getRowElement()]);
                    down = true;
                }
            }

            //AGREGANDO ELEMENTO DEBAJO Y A LA DERECHA
            //agregando elemento debajo
            if (!downRight && cell.getColumnIndex() + x < applicationProperties.getBoardColumnSize() && cell.getRowElement() + x < applicationProperties.getBoardColumnSize() && boardArray[cell.getColumnIndex() + x][cell.getRowElement() + x].getRowValue() != 100) {

                //SI EL ELEMENTO DEBAJO ESTA VACIO SIMPLEMENTE LO AGREGO AL ARREGLO
                if (boardArray[cell.getColumnIndex() + x][cell.getRowElement() + x].getGameRole().equalsIgnoreCase("Empty")) {
                    boardArray[cell.getColumnIndex() + x][cell.getRowElement() + x].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex() + x][cell.getRowElement() + x]);
                    downRight = true;
                }

                if (boardArray[cell.getColumnIndex() + x][cell.getRowElement() + x].getGameRole().equalsIgnoreCase("Number")) {
                    boardArray[cell.getColumnIndex() + x][cell.getRowElement() + x].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex() + x][cell.getRowElement() + x]);
                    downRight = true;
                }
            }

            //AGREGANDO ELEMENTO DEBAJO Y A LA IZQUIERDA
            //agregando elemento debajo
            if (!downLeft && cell.getColumnIndex() + x < applicationProperties.getBoardColumnSize() && cell.getRowElement() - x > ZERO && boardArray[cell.getColumnIndex() + x][cell.getRowElement() - x].getRowValue() != 100) {

                //SI EL ELEMENTO DEBAJO ESTA VACIO SIMPLEMENTE LO AGREGO AL ARREGLO
                if (boardArray[cell.getColumnIndex() + x][cell.getRowElement() - x].getGameRole().equalsIgnoreCase("Empty")) {
                    boardArray[cell.getColumnIndex() + x][cell.getRowElement() - x].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex() + x][cell.getRowElement() - x]);
                    downLeft = true;
                }

                if (!downLeft && boardArray[cell.getColumnIndex() + x][cell.getRowElement() - x].getGameRole().equalsIgnoreCase("Number")) {
                    boardArray[cell.getColumnIndex() + x][cell.getRowElement() - x].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex() + x][cell.getRowElement() - x]);
                    downLeft = true;
                }
            }

            //AGREGANDO ELEMENTO ARRIBA Y A LA IZQUIERDA
            //agregando elemento debajo
            if (!upLeft && cell.getColumnIndex() - x > ZERO && cell.getRowElement() - x > ZERO && boardArray[cell.getColumnIndex() - x][cell.getRowElement() - x].getRowValue() != 100) {

                //SI EL ELEMENTO DEBAJO ESTA VACIO SIMPLEMENTE LO AGREGO AL ARREGLO
                if (boardArray[cell.getColumnIndex() - x][cell.getRowElement() - x].getGameRole().equalsIgnoreCase("Empty")) {
                    boardArray[cell.getColumnIndex() - x][cell.getRowElement() - x].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex() - x][cell.getRowElement() - x]);
                    upLeft = true;
                }

                if (boardArray[cell.getColumnIndex() - x][cell.getRowElement() - x].getGameRole().equalsIgnoreCase("Number")) {
                    boardArray[cell.getColumnIndex() - x][cell.getRowElement() - x].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex() - x][cell.getRowElement() - x]);
                    upLeft = true;
                }
            }
            //AGREGANDO ELEMENTO ARRIBA Y A LA DERECHA
            //agregando elemento debajo
            if (!upRight && cell.getColumnIndex() - x > ZERO && cell.getRowElement() + x < applicationProperties.getBoardColumnSize() && boardArray[cell.getColumnIndex() - x][cell.getRowElement() + x].getRowValue() != 100) {

                //SI EL ELEMENTO DEBAJO ESTA VACIO SIMPLEMENTE LO AGREGO AL ARREGLO
                if (boardArray[cell.getColumnIndex() - x][cell.getRowElement() + x].getGameRole().equalsIgnoreCase("Empty")) {
                    boardArray[cell.getColumnIndex() - x][cell.getRowElement() + x].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex() - x][cell.getRowElement() + x]);
                    upRight = true;
                }

                if (boardArray[cell.getColumnIndex() - x][cell.getRowElement() + x].getGameRole().equalsIgnoreCase("Number")) {
                    boardArray[cell.getColumnIndex() - x][cell.getRowElement() + x].setSeen(true);
                    contigousEmptyCellsFound.add(boardArray[cell.getColumnIndex() - x][cell.getRowElement() + x]);
                    upRight = true;
                }
            }
        }

        return contigousEmptyCellsFound;
    }
}
