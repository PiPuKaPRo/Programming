package com.company.JSwing;

import com.company.Deck;
import com.company.Game;
import com.company.Logic;
import com.company.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GamePanel {
    private JFrame frame;
    private Container mainContainer;
    private GamePanel graphicPanel;

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

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.lightGray);
        mainContainer.add(bottomPanel, BorderLayout.SOUTH);


        Box leftPanel = createLeftPanel();
        mainContainer.add(leftPanel, BorderLayout.WEST);
        Box rightPanel = createRightPanel();
        mainContainer.add(rightPanel, BorderLayout.CENTER);
    }

    private Box createLeftPanel() {
        Box panel = Box.createVerticalBox();

        JLabel title = new JLabel("<html>Дурак ♠ ♥ ♣ ♦</html>");
        title.setFont(new Font(null, Font.BOLD, 36));
        panel.add(title);

        panel.add(Box.createRigidArea(new Dimension(5, 15)));

        JLabel title1 = new JLabel("<html>Выберите количество игроков: </html>");
        title1.setFont(new Font(null, Font.BOLD, 16));
        panel.add(title1);

        panel.setBackground(Color.gray);

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

        start.addActionListener(e -> {
            Table t = new Table();
            Logic logic = new Logic();
            Game g = new Game();
            g.setWindow(true);
            Deck dk = new Deck();
            dk.addCardsInGame(t);
            g.addPlayersInGame(t, players);
            logic.play(t,g);
        });
        return panel;
    }

    private Box createRightPanel(){
        Box box = Box.createHorizontalBox();
        return  box;
    }
}
