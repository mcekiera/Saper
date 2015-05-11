package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class responsible for behaviour and state of particular Cell on game board. It has a button, a part of GUI, by which
 * user can change internal state of single Cell, which is controlled by three boolean fields: isChecked, isFlagged and
 * is Mine. Every cell has value variable, determining number of mines around it; individual id number for searching
 * purposes; isChecked, isFlagged and isMine booleans determining Cell status.
 */
public class Cell implements ActionListener{
    private JButton button;
    private Board board;
    private int value;
    private int id;

    private boolean isChecked;
    public boolean isFlagged;
    private boolean isMine;

    /**
     * Basic constructor, creates Cell object containing a button and 3 false booleans: isMine, isFlagged, isChecked.
     * @param board; get a reference to game board.
     */
    public Cell(Board board){
        this.board = board;
        button = new JButton();
        button.addActionListener(this);
        addMouseAdapter();
        button.setPreferredSize(new Dimension(20,20));
        button.setMargin(new Insets(0,0,0,0));

        isMine = false;
        isFlagged = false;
        isChecked = false;
    }

    /**
     * Determines if a particular Cell is a mine.
     * @return isMine; true if Cell is marked as mine.
     */
    public boolean isMine(){
        return isMine;
    }

    /**
     * Mark Cell as a mine.
     */
    public void setMine(){
        isMine = true;
    }

    /**
     * Provide Cells button to game board.
     * @return JButton, representing particular Cell.
     */
    public JButton getButton() {
        return button;
    }

    /**
     * Setter method for value field. Set a value, which determines how many mines are around Cell.
     * @param value, is a variable passed by Board class method, which count mines around Cells.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Provide value field value.
     * @return value, number which stand for a quantity of mines around Cell.
     */
    public int getValue() {
        return value;
    }

    /**
     * Provide ID number of individual Cell. id variable is used in algorithm scanning area around the Cell.
     * @return id number,
     */
    public int getId() {
        return id;
    }

    /**
     * Set id number of particular Cell.
     * @param id, number passed by method which creates game board.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Provide information, if Cell was check, and therefore if it is uncovered.
     * @return isChecked, true if Cell is checked.
     */
    public boolean isChecked(){
        return isChecked;
    }

    /**
     * Method called in the end of game. If user wins, it display mines in green color. If user fails it colored mines
     * on red color.
     * @param success, boolean which stands for win/failure (true/false).
     */
    public void disarm(boolean success){
        if(isMine){
            displayValue();
            button.setBackground((success) ? Color.GREEN : Color.RED);
        }
    }

    /**
     * Method increment value field, every time when method scanning area around Cell find a min.
     */
    public void incrementValue(){
        value++;
    }

    /**
     * Method add a Mouse Adapter to button of the Cell. On a right-click if mark a Cell as a flagged, and disabled
     * it unless right-clicked again. The flanged state is symbolised by "!"  sign.
     */
    public void addMouseAdapter(){
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (button.getText().equals("!")) {
                        button.setBackground(new JButton().getBackground());
                        button.setText("");
                        isFlagged = false;
                        button.addActionListener(Cell.this);
                        board.incrementCounter();
                    } else if (!isChecked()) {
                        button.setText("!");
                        isFlagged = true;
                        button.removeActionListener(Cell.this);
                        board.decrementCounter();

                    }
                }
            }
        });
    }

    /**
     * Display value of particular cell. If Cell is a mine, it display mine symbol(Unicode U2600), else if Cell has value 0, it is displayed
     * as empty. Every other value is printed as number.
     */
    public void displayValue(){
        if(isMine){
            button.setText("\u2600");
        }else if(value==0){
            button.setText("");
        }else{
            button.setText(String.valueOf(value));
        }
        if(!isChecked()) board.checkOutCell();
        button.removeActionListener(Cell.this);
    }

    /**
     * Methods check a content of Cell, if it is mine, it ends the game, if flagged it ignore action, in another case,
     * it reveal content and if value is 0, it fire a chaining reaction scanning for 0 valued Cells.
     */
    public void checkCell(){
        if(isMine){
            board.finish(false);
            return;
        }
        if(isFlagged) return;
        reveal();
        if(value == 0) board.scanForEmptyCells(board.getIDsFromArea(getId()));

    }

    /**
     * Method call displayValue() method, and then disable Cells button and confirm isChecked variable.
     */
    public void reveal(){

        displayValue();
        button.setEnabled(false);
        isChecked = true;
    }


    /**
     * Action Listener for Cells button, it call checkCell() method.
     * @param e, button event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        checkCell();
    }


}
