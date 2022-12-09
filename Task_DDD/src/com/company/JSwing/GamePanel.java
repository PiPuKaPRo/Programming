package com.company.JSwing;

import com.company.*;
import com.company.utils.JTableUtils;
import com.company.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

public class GamePanel {
    private JFrame frame;
    private Container mainContainer;
    private GamePanel graphicPanel;
    private JTable tableOfPlayers;
    private Table t;
    private Logic logic;
    private Game g;
    private JLabel l1;
    private JLabel l2;
    private JTable sours;
    private JTable target;
    private int players;


    public GamePanel() {
        createFrame();
        initElem();
    }

    private void createFrame() {
        frame = new JFrame("Карточная игра Дурак");
        frame.setSize(1500, 1024);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void show() {
        frame.setVisible(true);
    }

    private void initElem() {
        mainContainer = frame.getContentPane();
        mainContainer.setLayout(new BorderLayout());

        Box leftPanel = createLeftPanel();
        mainContainer.add(leftPanel, BorderLayout.WEST);
        createStartMenu();
    }

    private void createStartMenu(){
        Box rightPanel = createRightPanel();
        mainContainer.add(rightPanel, BorderLayout.EAST);
    }
    private Box createLeftPanel() {
        Box panel = Box.createVerticalBox();

        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));

        JLabel title = new JLabel("<html>Дурак ♠ ♥ ♣ ♦</html>");
        title.setFont(new Font(null, Font.BOLD, 36));
        panel.add(title);

        panel.add(Box.createRigidArea(new Dimension(5, 15)));

        JLabel title1 = new JLabel("<html>Выберите количество игроков: </html>");
        title1.setFont(new Font(null, Font.BOLD, 16));
        panel.add(title1);

        JRadioButton b2 = new JRadioButton("2 Players");
        players = 2;
        panel.add(b2);

        JRadioButton b3 = new JRadioButton("3 Players");
        players = 3;
        panel.add(b3);

        JRadioButton b4 = new JRadioButton("4 Players");
        players = 4;
        panel.add(b4);

        JButton start = new JButton("Play");
        panel.add(start);

        panel.add(Box.createRigidArea(new Dimension(5, 7)));

        JButton startGame = new JButton("Start game");
        panel.add(startGame);

        start.addActionListener(e -> {
            t = new Table();
            logic = new Logic();
            g = new Game();
            g.setWindow(true);
            Deck dk = new Deck();
            dk.addCardsInGame(t);
            g.addPlayersInGame(t, players);
            l1 = new JLabel(String.valueOf(Table.getTrumpCard()));
            l1.setFont(new Font(null, Font.BOLD, 20));
            l2 = new JLabel(Table.getPlayersSize());
            l2.setFont(new Font(null, Font.BOLD, 20));
            String[][] matrix = Table.getStringPlayers(Table.getPlayers());
            JTableUtils.writeArrayToJTable(tableOfPlayers, matrix);
        });

        startGame.addActionListener(e -> {
            logic.play(t,g);
        });

        return panel;
    }

    private Box createRightPanel(){
        Box box = Box.createHorizontalBox();
        box.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
        l1 = new JLabel(" ");
        box.add(l1);

        box.add(Box.createRigidArea(new Dimension(5, 15)));

        l2 = new JLabel(" ");
        box.add(l2);

        box.add(Box.createRigidArea(new Dimension(5, 15)));

        tableOfPlayers = new JTable();
        box.add(tableOfPlayers);
        return  box;
    }
}
