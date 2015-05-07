package Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    private Cell[][] cells;
    private int cellID;
    private int side;
    private int limit;
    private int mines;
    private int flagCounter;
    private int checkCounter;
    private boolean success;
    private JTextField counter;
    private Main main;

    public Board(int cellPerSide, int mines){

        side = cellPerSide;
        cells = new Cell[side][side];
        limit = side - 2;
        cellID = 0;
        flagCounter = mines;
        checkCounter = side*side-mines;
        System.out.println(checkCounter);
        this.mines = mines;
        success = true;
   }

    public JPanel setBoard(Main main){
        this.main = main;
        JPanel panel = addCells();
        panel.setBorder(BorderFactory.createBevelBorder(0));
        plantMines();
        setCellValues();

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
        for(int row = 0; row< side; row++){
            for(int col = 0; col<side; col++){
                cells[row][col] = new Cell(this);
                cells[row][col].setId(getID());
                panel.add(cells[row][col].getButton());
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
    /*Choose rendom places for mines*/

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
                    if(getCell(empty).isNotChecked()) getCell(empty).checkCell();
                }
            }else if(cell.getValue()>0){
                cell.reveal();
            }
        }
    }

    public int getID(){
        int id = cellID;
        cellID++;
        return id;
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
    public void fail(){
        for(Cell[] a : cells){
            for(Cell b : a){
                b.setFail();
                b.reveal();
                System.out.println(b.getId());
            }
        }
        main.timerStop();
    }
    public void declareVictory(){
            for(Cell[] a : cells){
                for(Cell b : a){
                    b.reveal();
                    b.disarm();
                }
            }
            main.timerStop();
    }


    public void decreaseCounter(){
        --checkCounter;
        System.out.println(checkCounter);
        if(checkCounter==0) declareVictory();
    }
}
