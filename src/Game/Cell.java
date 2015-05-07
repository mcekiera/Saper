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
    public boolean isFlaged;

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
                        isFlaged = false;
                        button.addActionListener(Cell.this);
                        board.incrementCounter();
                    } else if (isNotChecked()) {
                        button.setText("!!!");
                        isFlaged = true;
                        button.removeActionListener(Cell.this);
                        board.decrementCounter();

                    }
                }
            }
        });
        button.addActionListener(this);
        button.setPreferredSize(new Dimension(20,20));
        button.setMargin(new Insets(0,0,0,0));

        isFlaged = false;
        notChecked = true;
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

    public void setValue(int value) {
        this.value = value;
    }
    public void declareVictory(){
        if(value ==-1){
            displayValue();
            button.setBackground(Color.GREEN);
        }
    }
    public void displayValue(){
        if(value==-1){
            button.setText("\u2600");
            button.setBackground(Color.RED);
        }else if(value==0){
            button.setText("");
        }else{
            button.setText(String.valueOf(value));
        }
    }

    public void checkCell(){
        if(isFlaged) return;
        button.setEnabled(false);
        displayValue();
        notChecked = false;
        if(value == 0) board.scanForEmptyCells(board.getIDsFromArea(getId()));
        if(value == -1) board.fail();
    }

    public void incrementValue(){
        value++;
    }

    public boolean isNotChecked(){
        return notChecked;
    }

    public void reveal(){
        displayValue();
        button.setEnabled(false);
        notChecked = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        checkCell();


    }

    public boolean isMine(){
        return value == -1;
    }
}
