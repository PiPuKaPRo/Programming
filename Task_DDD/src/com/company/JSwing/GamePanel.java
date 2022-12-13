package com.company.JSwing;

import com.company.*;

import javax.swing.*;
import java.awt.*;

public class GamePanel {
    private JFrame frame;
    private Container mainContainer;
    private Table t;
    private Logic logic;
    private Game g;
    private int players;
    private RightPanel rightPanel;
    private CenterPanel gamePanel;
    private Round round;


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

        createMenu();
        createInfMenu();
        createGame();

    }

    private void createInfMenu(){
        rightPanel = new RightPanel();
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
        mainContainer.add(rightPanel, BorderLayout.EAST);
    }

    private void createGame(){
        gamePanel = new CenterPanel();
        gamePanel.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
        mainContainer.add(gamePanel, BorderLayout.CENTER);
    }

    private void createMenu(){
        Box leftPanel = createLeftPanel();
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
        mainContainer.add(leftPanel, BorderLayout.WEST);
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

        JRadioButton b2 = new JRadioButton("2 Players");
        panel.add(b2);

        JRadioButton b3 = new JRadioButton("3 Players");
        panel.add(b3);

        JRadioButton b4 = new JRadioButton("4 Players");
        panel.add(b4);

        JButton start = new JButton("Play");
        panel.add(start);

        panel.add(Box.createRigidArea(new Dimension(5, 7)));

        JButton startGame = new JButton("Start game");
        panel.add(startGame);

        start.addActionListener(e -> {
            if (b2.isSelected()) players = 2;
            if (b3.isSelected()) players = 3;
            if (b4.isSelected()) players = 4;
            t = new Table();
            logic = new Logic();
            g = new Game();
            g.setWindow(true);
            Deck dk = new Deck();
            dk.addCardsInGame(t);
            g.addPlayersInGame(t, players);
            rightPanel.setTrumpCard(t.getTrumpCard());
            rightPanel.setFont(new Font(null, Font.BOLD, 20));
            rightPanel.setCardsCount(t.getCards().size());
            rightPanel.setFont(new Font(null, Font.BOLD, 20));
            rightPanel.setTableOfPlayers(t.getStringPlayers(t.getPlayers()));
        });

        startGame.addActionListener(e -> {
            logic.play(t,g);
            gamePanel.setSoursDeck(round.getStringPlayersCards(round.getSource().getPlayersCards()));
            gamePanel.setTargetDeck(round.getStringPlayersCards(round.getTarget().getPlayersCards()));
        });

        return panel;
    }
}
