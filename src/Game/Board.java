package Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

/**
 * Board class creates Board object, which contains given number of Cell objects. It serves as a game board, and it
 * controls relations between Cells.
 */
public class Board {
    private LinkedHashMap<Integer,Cell> cellsMap;
    private Cell[][] cells;
    private int side;
    private int mines;
    private int flagCounter;
    private int checkCounter;
    private JTextField counter;
    private Main main;
    private boolean success;

    /**
     * It construct a Board object with Cells grid of given size, and given number of mines.
     * @param cellPerSide, determines number of Cells per side of board,
     * @param mines, determines number of mines,
     */
    public Board(int cellPerSide, int mines){

        side = cellPerSide;
        cells = new Cell[side][side];
        flagCounter = mines;
        checkCounter = side*side-mines;
        this.mines = mines;
        success = true;
   }

    /**
     * Method which build Board GUI ready for implementation to main user interface.
     * @param main, gives reference to user interface
     * @return JPanel, containing Board part of GUI.
     */
    public JPanel setBoard(Main main){
        JPanel panel = addCells();
        panel.setBorder(BorderFactory.createBevelBorder(0));
        plantMines();
        setCellValues();
        this.main = main;
        return panel;
    }

    /**
     * Provides counter for recognized/flagged mines.
     * @return JTextField counter, displaying number of not flagged mines.
     */
    public JTextField getCounter(){
        counter = new JTextField(3);
        counter.setText(String.valueOf(mines));
        return counter;
    }

    /**
     * Increment value displayed by counter by 1.
     */
    public void incrementCounter(){
        counter.setText(String.valueOf(++flagCounter));
    }

    /**
     * Determining result of a game.
     * @return succes, true if user wins, false if fails.
     */
    public boolean isSuccess(){
        return success;
    }

    /**
     * Decrement value displayed by counter by 1.
     */
    public void decrementCounter(){
        counter.setText(String.valueOf(--flagCounter));
    }

    /**
     * Generate Cell net.
     * @return JPanel with grid of buttons of generated Cell objects.
     */
    public JPanel addCells(){
        JPanel panel = new JPanel(new GridLayout(side,side));
        cellsMap = new LinkedHashMap<Integer, Cell>();
        int id = 0;
        for(int row = 0; row< side; row++){
            for(int col = 0; col<side; col++){
                Cell temp = new Cell(this);
                cells[row][col] = temp;
                cells[row][col].setId(id);
                cellsMap.put(id++,temp);
                panel.add(cells[row][col].getButton());
            }
        }
        return panel;
    }

    /**
     * Determines location of given number of mines.
     */
    public void plantMines(){
        Random random = new Random();
        for(int i =0; i<mines; i++){
            Cell cell = cells[random.nextInt(side)][random.nextInt(side)];
            if(!cell.isMine()){
                cell.setMine();
            }else{
                i--;
            }
        }
    }

    /**
     * This method count number of mines around particular cell and set its value
     * */
    public void setCellValues(){
        for(int byLoc = 0; byLoc < side*side; byLoc++){
            for(int byID : getIDsFromArea(byLoc)){
                if(getCell(byID).isMine() && !getCell(byLoc).isMine()){
                    getCell(byLoc).incrementValue();
                }
            }
        }
    }
    /**
     * tarts chain reaction. When user click on particular cell, if cell is empty (value = 0) this
     * method look for other empty cells next to activated one. If finds one, it call checkCell and in effect,
     * start next scan on its closest area.
     */
    public void scanForEmptyCells(List<Integer> listID){
        for(int id : listID){
            Cell cell = getCell(id);
            if(cell.getValue()==0){
                    if(!cell.isChecked()) cell.checkCell();
            }else if(cell.getValue()>0){
                cell.reveal();
            }
        }
    }

    /**
     * Search a single Cell.
     * @param id, individual number of particular Cell,
     * @return wanted Cell,
     */
    public Cell getCell(int id){
        return cellsMap.get(id);
    }

    /**
     * Provides list of locations around particular Cell on a game board.
     * @param impulse, id of Cell which button was clicked
     * @return list of locations around Cell which initialize method.
     */
    public List<Integer> getIDsFromArea(int impulse){
        List<Integer> ids = new ArrayList<Integer>();
        int limit = side - 2;
        for(int i = 0; i<side; i++){
            for(int j = 0; j<side; j++){
                if(cells[i][j].getId() == impulse){
                    if(j>=1) ids.add(cells[i][j - 1].getId());
                    if(j<= limit) ids.add(cells[i][j + 1].getId());
                    if(i>=1) ids.add(cells[i - 1][j].getId());
                    if(i<= limit) ids.add(cells[i + 1][j].getId());
                    if(i>=1 && j>= 1) ids.add(cells[i-1][j-1].getId());
                    if(i<= limit && j<= limit) ids.add(cells[i + 1][j + 1].getId());
                    if(i>=1 && j<= limit) ids.add(cells[i - 1][j + 1].getId());
                    if(i<= limit && j>= 1) ids.add(cells[i + 1][j - 1].getId());
                }
            }
        }
        return  ids;
    }

    /**
     * Called in the end of game. Depends on result, it reveal whole game board and stops timer.
     * @param success, determines result of game.
     */
    public void finish(boolean success){
        this.success = success;
        for(Cell[] a : cells){
            for(Cell b : a){
                b.reveal();
                b.disarm(success);
            }
        }
        main.timerStop();
    }

    /**
     * It update value of counter for revealed Cells, but not  mines. If all empty Cells are revealed, user wins game.
     */
    public void checkOutCell(){
        --checkCounter;
        if(checkCounter==0 && success) finish(true);
    }

    /**
     * Gives value of counter in number format.
     * @return Integer with counter value,
     */
    public int getCounterValue(){
        return Integer.parseInt(counter.getText());
    }
}
