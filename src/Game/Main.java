package Game;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

/**
 * Creates main GUI.
 */
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

    /**
     * main method,
     * @param args
     */
    public static void main(String[] args){
        Main main = new Main();
        main.buildGUI();
    }

    /**
     * Method provide whole board of particular game, only for test purpose.
     * @return current Board object,
     */
    public Board getBoard(){
        return board;
    }

    /**
     * Assemble working GUI form various elements.
     */
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

    /**
     * Assemble display and game board.
     */
    public void createDisplay(){
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(BorderLayout.CENTER, buildBoard());
        panel.add(BorderLayout.NORTH, buildDisplay());
        frame.getContentPane().add(BorderLayout.CENTER,panel);
    }

    /**
     * Build GUI menu bar.
     * @return menu,
     */
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
        JMenu about = new JMenu("About");
        JMenuItem help = new JMenuItem("Help");
        help.addActionListener(new HelpListener());
        JMenuItem info = new JMenuItem("About");
        info.addActionListener(new InfoListener());
        about.add(help);
        about.add(info);
        menu.add(options);
        menu.add(about);
        return menu;
    }

    /**
     * Build reset button and displays, for time and counter.
     * @return JPanel with components.
     */
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

    /**
     * Creates Board object of given size and mines number.
     * @return Board object,
     */
    public JPanel buildBoard(){
        board = new Board(side,mines);
        return  board.setBoard(this);
    }

    /**
     * Provides proper format of displayed text.
     * @param s, pattern
     * @return formatter object,
     */
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

    /**
     * Stops timer counting.
     */
    public void timerStop(){
        timer.stop();
    }

    /**
     * Recreates whole GUI for restart of game.
     */
    public void reset(){
        min = sec = 0;
        frame.remove(panel);
        createDisplay();
        frame.revalidate();
        timer.start();
    }

    /**
     * Initialize work of Timer object.
     */
    @Override
    public void run() {
        timer = new Timer(1000,new TimerAction());
        timer.setRepeats(true);
        timer.start();
    }

    /**
     * Action Listener of reset button.
     */
    public class RestartAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            reset();
        }
    }

    /**
     * ActionListener of Timer object.
     */
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

    /**
     * ActionListener for difficulty level menu options. It changes board size and mines number.
     */
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
                    mines = 250;
                    side = 32;
                    break;
            }
            reset();
            frame.pack();
        }
    }

    /**
     * ActionListener of help menu, displays help instruction.
     *
     */
    private class HelpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            File file = new File("src\\Game\\Help.txt");

            JTextArea help = new JTextArea();
            help.setSize(200,100);
            JScrollPane scrollPane = new JScrollPane(help);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(400,200));
            help.setEditable(false);
            help.setLineWrap(true);
            try{
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()){
                help.append(scanner.nextLine()+"\n");
            }
            }catch (IOException ex){
                System.out.println(file.getAbsolutePath().toString());
                ex.printStackTrace();
            }

            JOptionPane.showMessageDialog(frame, scrollPane, "Help", JOptionPane.PLAIN_MESSAGE);

        }
    }

    /**
     * Info ActionListener, displays information about programme.
     */
    private class InfoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String info = "This programme is a working duplicate of Windows Mines Sweeper game, created for learning purpose. Kraków 2015";
            JOptionPane.showMessageDialog(frame,info, "About programme",JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
