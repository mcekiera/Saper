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
    private int mines;
    private int side;
    private JFormattedTextField time;


    public static void main(String[] args){
        Main main = new Main();
        main.buildGUI();
    }

    public Board getBoard(){
        return board;
    }
    public void buildGUI(){
        frame = new JFrame();
        frame.setJMenuBar(buildMenu());

        min = sec = 0;
        mines = 10;
        side = 8;

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
        JMenuItem easy = new JMenuItem("Easy");
        JMenuItem medium = new JMenuItem("Medium");
        JMenuItem hard = new JMenuItem(("Hard"));
        JMenuItem extreme = new JMenuItem("Extreme");
        options.add(easy);
        easy.addActionListener(new LevelListener());
        easy.setActionCommand(Level.EASY.name());
        options.add(medium);
        medium.addActionListener(new LevelListener());
        medium.setActionCommand(Level.MEDIUM.name());
        options.add(hard);
        hard.addActionListener(new LevelListener());
        hard.setActionCommand(Level.HARD.name());
        options.add(extreme);
        extreme.addActionListener(new LevelListener());
        extreme.setActionCommand(Level.EXTREME.name());
        JMenu help = new JMenu("Help");
        menu.add(options);
        menu.add(help);
        return menu;
    }

    public JPanel buildDisplay(){
        JPanel panel = new JPanel(new GridLayout(1,3,2,2));
        JTextField minesLeft = board.getCounter();
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

        board = new Board(side,mines);
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

    public void timerStop(){
        timer.stop();
    }
    public void reset(){
        frame.remove(panel);
        createDisplay();
        frame.revalidate();
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
            reset();
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

        }
    }

    private class LevelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Level level = Level.valueOf(e.getActionCommand());

            switch(level){
                case EASY:
                    mines = 10;
                    side = 8;
                    break;
                case MEDIUM:
                    mines = 40;
                    side = 16;
                    break;
                case HARD:
                    mines = 100;
                    side = 24;
                    break;
                case EXTREME:
                    mines = 150;
                    side = 32;
                    break;
            }
            min = sec = 0;
            reset();
            frame.pack();
        }
    }
}
