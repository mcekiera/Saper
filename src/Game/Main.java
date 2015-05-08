package Game;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class Main implements Runnable{

    private Board board;
    private JFrame frame;
    private JPanel panel;
    private Timer timer;
    private int min;
    private int sec;
    private JFormattedTextField time;
    private JTextField minesLeft;


    public static void main(String[] args){
        Main main = new Main();
        main.buildGUI();
    }
    public void buildGUI(){
        frame = new JFrame();
        frame.setJMenuBar(buildMenu());

        min = sec = 0;

        createDisplay();

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        new Thread(this).start();
    }
    public void createDisplay(){
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(BorderLayout.CENTER, buildBoard());
        panel.add(BorderLayout.NORTH, buildDisplay());
        frame.getContentPane().add(BorderLayout.CENTER,panel);
    }
    public JMenuBar buildMenu(){
        JMenuBar menu = new JMenuBar();
        JMenu options = new JMenu("Options");
        JMenu help = new JMenu("Help");
        menu.add(options);
        menu.add(help);
        return menu;
    }

    public JPanel buildDisplay(){
        JPanel panel = new JPanel(new GridLayout(1,3,2,2));
        minesLeft = board.getCounter();
        minesLeft.setEnabled(false);

        time = new JFormattedTextField(createFormatter("##:##"));
        time.setEnabled(true);
        ImageIcon icon = new ImageIcon(Main.class.getResource("/Game/BUTTON.png"));
        JButton restart = new JButton();
        restart.setBackground(Color.white);
        restart.setIcon(icon);
        restart.setFont(new Font("Arial",Font.PLAIN,34));
        restart.setMargin(new Insets(0, 0, 0, 0));
        restart.addActionListener(new RestartAction());
        panel.add(minesLeft);
        panel.add(restart);
        panel.add(time);

        panel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        return panel;
    }

    public JPanel buildBoard(){
        board = new Board(8,5);
        return  board.setBoard(this);
    }
    public MaskFormatter createFormatter(String s){
        MaskFormatter formatter = null;
        try{
            formatter = new MaskFormatter(s);
            formatter.setPlaceholderCharacter('0');
        }catch (ParseException ex){
            ex.printStackTrace();
        }
        return formatter;
    }

    public void resetDisplay(){
        minesLeft.setText("0");
    }
    public void timerStop(){
        timer.stop();
    }

    @Override
    public void run() {
        timer = new Timer(1000,new TimerAction());
        timer.setRepeats(true);
        timer.start();
    }

    public class RestartAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.remove(panel);
            createDisplay();
            frame.revalidate();
        }
    }
    public class TimerAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            sec++;
            min += sec/60;
            sec = sec%60;

            String minStr = (min<10)? "0"+min : String.valueOf(min);
            String secStr = (sec<10)? "0"+sec : String.valueOf(sec);
            time.setText(minStr+":"+secStr);
            //minesLeft.setText(String.valueOf(board.getCounter()));
        }
    }
}
