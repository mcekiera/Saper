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

    private boolean notChecked;
    public boolean isFlagged;
    private boolean isMine;
    private boolean fail;

    public Cell(Board b){
        this.board = b;
        button = new JButton();
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
                    } else if (isNotChecked()) {
                        button.setText("!!!");
                        isFlagged = true;
                        button.removeActionListener(Cell.this);
                        board.decrementCounter();

                    }
                }
            }
        });
        button.addActionListener(this);
        button.setPreferredSize(new Dimension(20,20));
        button.setMargin(new Insets(0,0,0,0));

        fail = false;
        isMine = false;
        isFlagged = false;
        notChecked = true;
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

    public int getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFail(){
        fail = true;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public void disarm(){
        if(isMine){
            displayValue();
            button.setBackground(Color.GREEN);
        }
    }
    public void displayValue(){
        if(isMine){
            button.setText("\u2600");
            button.setBackground(Color.RED);
        }else if(value==0){
            button.setText("");
        }else{
            button.setText(String.valueOf(value));
        }
        if(isNotChecked()) board.decreaseCounter();
        button.removeActionListener(Cell.this);
    }

    public void checkCell(){
        if(isFlagged) return;
        if(isMine){
            System.out.println(fail);
            fail = true;
            System.out.println(fail);
            board.fail();
            System.out.println(fail);
        }
        button.setEnabled(false);
        displayValue();
        notChecked = false;
        if(value == 0) board.scanForEmptyCells(board.getIDsFromArea(getId()));

    }

    public void incrementValue(){
        value++;
    }

    public boolean isNotChecked(){
        return notChecked;
    }

    public void reveal(){
        if(isFlagged && (!fail)){
            System.out.println(isFlagged + "" + fail);
            return;
        }
        displayValue();
        button.setEnabled(false);
        notChecked = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        checkCell();
    }


}
