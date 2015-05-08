package Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private Cell[][] cells;
    private int side;
    private int mines;
    private int flagCounter;
    private int checkCounter;
    private JTextField counter;
    private Main main;
    private boolean success;

    public Board(int cellPerSide, int mines){

        side = cellPerSide;
        cells = new Cell[side][side];
        flagCounter = mines;
        checkCounter = side*side-mines;
        this.mines = mines;
        success = true;
   }

    public JPanel setBoard(Main main){
        JPanel panel = addCells();
        panel.setBorder(BorderFactory.createBevelBorder(0));
        plantMines();
        setCellValues();
        this.main = main;
        return panel;
    }
    public JTextField getCounter(){
        counter = new JTextField(3);
        counter.setText(String.valueOf(mines));
        return counter;
    }
    public void incrementCounter(){
        counter.setText(String.valueOf(++flagCounter));
    }

    public void decrementCounter(){
        counter.setText(String.valueOf(--flagCounter));
    }
    public JPanel addCells(){
        JPanel panel = new JPanel(new GridLayout(side,side));
        int id = 0;
        for(int row = 0; row< side; row++){
            for(int col = 0; col<side; col++){
                cells[row][col] = new Cell(this);
                cells[row][col].setId(id);
                panel.add(cells[row][col].getButton());
                id++;
            }
        }
        return panel;
    }

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

    /*This method count number of mines around particular cell and set its value*/
    public void setCellValues(){
        for(int byLoc = 0; byLoc < side*side; byLoc++){
            for(int byID : getIDsFromArea(byLoc)){
                if(getCell(byID).isMine() && !getCell(byLoc).isMine()){
                    getCell(byLoc).incrementValue();
                }
            }
        }
    }
    /*This method starts chain reaction. When user click on particular cell, if cell is empty (value = 0) this
    method look for other empty cells next to activated one. If finds one, it call checkCell and in effect,
    start next scan on its closest area.
     */
    public void scanForEmptyCells(List<Integer> listID){
        for(int id : listID){
            Cell cell = getCell(id);
            if(cell.getValue()==0){
                for(int empty : getIDsFromArea(id)){
                    if(!getCell(empty).isChecked()) getCell(empty).checkCell();
                }
            }else if(cell.getValue()>0){
                cell.reveal();
            }
        }
    }

    public Cell getCell(int id){
        for(Cell[] a : cells){
            for(Cell b : a){
                if(b.getId() == id) return b;
            }
        }
        return null;
    }

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

    public void decreaseCounter(){
        --checkCounter;
        if(checkCounter==0 && success) finish(true);
    }
}
