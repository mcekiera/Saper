package Game;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Board {
    private Cell[][] cells;
    private int cellID;
    private int side;
    private int limit;
    private int mines;
    Main main;

    public Board(int cellPerSide, int mines){

        side = cellPerSide;
        cells = new Cell[side][side];
        limit = side - 2;
        cellID = 0;
        this.mines = mines;
   }

    public JPanel setBoard(Main main){
        this.main = main;
        JPanel panel = addCells();

        plantMines();
        setCellValues();

        return panel;
    }

    public JPanel addCells(){
        JPanel panel = new JPanel(new GridLayout(side,side));
        for(int i = 0; i< side; i++){
            for(int j = 0; j<side; j++){
                cells[i][j] = new Cell(this);
                cells[i][j].setId(getID());
                panel.add(cells[i][j].getButton());
            }
        }
        return panel;
    }

    public void plantMines(){
        ArrayList<Integer> loc = generateMinesLocation(mines);
        for(int i : loc){
            getCell(i).setValue(-1);
        }
    }
    /*Choose rendom places for mines*/
    public ArrayList<Integer> generateMinesLocation(int q){
        ArrayList<Integer> loc = new ArrayList<Integer>();
        int random;
        for(int i = 0; i<q;){
            random = (int)(Math.random()* (side*side));
            if(!loc.contains(random)){
                loc.add(random);
                i++;
            }
        }
        return loc;
    }
    /*This method count number of mines around particular cell and set its value*/
    public void setCellValues(){
        for(int byLoc = 0; byLoc < side*side; byLoc++){
            for(int byID : getIDsFromArea(byLoc)){
                if(getCell(byID).isTheMine()) getCell(byLoc).incrementValue();
            }
        }
    }
    /*This method starts chain reaction. When user click on particular cell, if cell is empty (value = 0) this
    method look for other empty cells next to activated one. If finds one, it call checkCell and in effect,
    start next scan on its closest area.
     */
    public void scanForEmptyCells(){
        for(int i = 0; i<side; i++){
            for(int j = 0; j<side; j++){
                if(!cells[i][j].isNotChecked()){
                    //todo odkrywa tylko 0, a powinno też inne wartości
                    if(j>=1 && cells[i][j-1].isEmpty()) cells[i][j-1].checkCell();
                    if(j<= limit && cells[i][j+1].isEmpty()) cells[i][j+1].checkCell();
                    if(i>=1 && cells[i-1][j].isEmpty()) cells[i-1][j].checkCell();
                    if(i<= limit && cells[i+1][j].isEmpty()) cells[i+1][j].checkCell();
                    if(i>=1 && j>= 1 && cells[i-1][j-1].isEmpty()) cells[i-1][j-1].checkCell();
                    if(i<= limit && j<= limit && cells[i+1][j+1].isEmpty()) cells[i+1][j+1].checkCell();
                    if(i>=1 && j<= limit && cells[i-1][j+1].isEmpty()) cells[i-1][j+1].checkCell();
                    if(i<= limit && j>= 1 && cells[i+1][j-1].isEmpty()) cells[i+1][j-1].checkCell();
                }
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

    public void fail(){
        for(Cell[] a : cells){
            for(Cell b : a){
                b.reveal();
            }
        }
        main.timerStop();
    }
    public List<Integer> getIDsFromArea(int impulse){
        List<Integer> ids = new ArrayList<Integer>();
        System.out.println(ids.toString());
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
        System.out.println(ids.toString());
        return  ids;
    }

}
