import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Cell implements ActionListener{
    private JButton button;
    private int value;
    private int id;
    private Board board;
    private boolean notChecked;

    public Cell(Board board){
        button = new JButton();
        button.addActionListener(this);

        value = 0;
        this.board = board;
        notChecked = true;
    }

    public JButton getButton() {
        return button;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void displayValue(){
        if(value==-1){
            button.setText("\u2600");
        }else if(value!=0){
            button.setText(String.valueOf(value));
        }
    }

    public void checkCell(){
        button.setEnabled(false);
        displayValue();
        notChecked = false;
        if(value == 0) board.scanAreaForEmpty();
        if(value == -1) board.fail();
    }

    public boolean isNotChecked(){
        return notChecked;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        checkCell();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setText(String str){
        button.setText(str);
    }

    public void incrementValue(){
        value++;
    }

    public boolean isEmpty(){
        return isNotChecked() && value==0;
    }

    public void setEnabled(boolean enabled){
        button.setEnabled(enabled);
    }
}
