import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board {
    private Cell[][] cells;
    private static int cellID = 0;
    private final static int[] range = {11,10,9,1};
    int side;
    int x;
    int mines;

    public static void main(String[] args){
        Board board = new Board();
        board.setBoard();
    }
    public void setBoard(){
        JFrame frame = new JFrame();
        JPanel background = new JPanel();
        frame.add(background);

        side = 20;
        x = side-2;
        mines = 40;

        background.setLayout(new GridLayout(side,side));

        cells = new Cell[side][side];
        for(int i = 0; i< side; i++){
            for(int j = 0; j<side; j++){
                cells[i][j] = new Cell(this);
                cells[i][j].setId(getID());

                background.add(cells[i][j].getButton());
            }
        }

        addMines(mines);
        setValues();

        frame.setSize(550,550);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void addMines(int q){
        ArrayList<Integer> loc = getMinesLoc(q);
        for(int i : loc){
            getCell(i).setValue(-1);
            getCell(i).setText("-1");
        }
    }

    public ArrayList<Integer> getMinesLoc(int q){
        ArrayList<Integer> loc = new ArrayList<Integer>();
        int random;
        for(int i = 0; i<q;){
            random = (int)(Math.random()*mines*10);
            if(loc.contains(random)){
                continue;
            }else{
                System.out.println(random);
                loc.add(random);
                i++;
            }
        }
        return loc;
    }
    public void setValues(){
        for(int i = 0; i<side; i++){
            for(int j = 0; j<side; j++){
                 if(cells[i][j].getValue() != -1){
                     if(j>=1 && cells[i][j-1].getValue() == -1) cells[i][j].incrementValue();
                     if(j<=x && cells[i][j+1].getValue() == -1) cells[i][j].incrementValue();
                     if(i>=1 && cells[i-1][j].getValue() == -1) cells[i][j].incrementValue();
                     if(i<=x && cells[i+1][j].getValue() == -1) cells[i][j].incrementValue();
                     if(i>=1 && j>= 1 && cells[i-1][j-1].getValue() == -1) cells[i][j].incrementValue();
                     if(i<=x && j<= x && cells[i+1][j+1].getValue() == -1) cells[i][j].incrementValue();
                     if(i>=1 && j<= x && cells[i-1][j+1].getValue() == -1) cells[i][j].incrementValue();
                     if(i<=x && j>= 1 && cells[i+1][j-1].getValue() == -1) cells[i][j].incrementValue();
                 }
            }
        }
    }

    public void scanAreaForEmpty(){
        for(int i = 0; i<side; i++){
            for(int j = 0; j<side; j++){
                if(!cells[i][j].isNotChecked()){
                    if(j>=1 && cells[i][j-1].isEmpty()) cells[i][j-1].checkCell();
                    if(j<=x && cells[i][j+1].isEmpty()) cells[i][j+1].checkCell();
                    if(i>=1 && cells[i-1][j].isEmpty()) cells[i-1][j].checkCell();
                    if(i<=x && cells[i+1][j].isEmpty()) cells[i+1][j].checkCell();
                    if(i>=1 && j>= 1 && cells[i-1][j-1].isEmpty()) cells[i-1][j-1].checkCell();
                    if(i<=x && j<= x && cells[i+1][j+1].isEmpty()) cells[i+1][j+1].checkCell();
                    if(i>=1 && j<= x && cells[i-1][j+1].isEmpty()) cells[i-1][j+1].checkCell();
                    if(i<=x && j>= 1 && cells[i+1][j-1].isEmpty()) cells[i+1][j-1].checkCell();
                }
            }
        }
    }


    public static int getID(){
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
                b.setEnabled(false);
            }
        }
    }
}
