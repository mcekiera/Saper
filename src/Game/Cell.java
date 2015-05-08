package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Cell implements ActionListener{
    private JButton button;
    private Board board;
    private int value;
    private int id;

    private boolean isChecked;
    public boolean isFlagged;
    private boolean isMine;

    public Cell(Board b){
        this.board = b;
        button = new JButton();
        addMouseAdapter();
        button.addActionListener(this);

        button.setPreferredSize(new Dimension(20,20));
        button.setMargin(new Insets(0,0,0,0));

        isMine = false;
        isFlagged = false;
        isChecked = false;
    }

    public boolean isMine(){
        return isMine;
    }

    public void setMine(){
        isMine = true;
    }

    public JButton getButton() {
        return button;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked(){
        return isChecked;
    }

    public void disarm(boolean success){
        if(isMine){
            displayValue();
            button.setBackground((success) ? Color.GREEN : Color.RED);
        }
    }
    public void incrementValue(){
        value++;
    }

    public void addMouseAdapter(){
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (button.getText().equals("!!!")) {
                        button.setBackground(new JButton().getBackground());
                        button.setText("");
                        isFlagged = false;
                        button.addActionListener(Cell.this);
                        board.incrementCounter();
                    } else if (!isChecked()) {
                        button.setText("!!!");
                        isFlagged = true;
                        button.removeActionListener(Cell.this);
                        board.decrementCounter();

                    }
                }
            }
        });
    }

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

    public void checkCell(){
        if(isMine){
            board.finish(false);
            return;
        }
        if(isFlagged) return;
        reveal();
        if(value == 0) board.scanForEmptyCells(board.getIDsFromArea(getId()));

    }

    public void reveal(){

        displayValue();
        button.setEnabled(false);
        isChecked = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        checkCell();
    }


}
