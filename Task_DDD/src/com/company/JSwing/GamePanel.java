package com.company.JSwing;

import com.company.*;
import com.company.utils.JTableUtils;
import com.company.utils.SwingUtils;

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

        Box leftPanel = createLeftPanel();
        mainContainer.add(leftPanel, BorderLayout.WEST);
        createStartMenu();
        createGameMenu();

    }

    private void createStartMenu(){
        rightPanel = new RightPanel();
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
        mainContainer.add(rightPanel, BorderLayout.EAST);
    }

    private void createGameMenu(){
        gamePanel = new CenterPanel();
        gamePanel.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
        mainContainer.add(gamePanel, BorderLayout.CENTER);
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

    class RightPanel extends Box {
        JLabel trumpLabel = new JLabel(" ");
        JLabel cardsCountLabel = new JLabel(" ");
        JTable tableOfPlayers = new JTable();
        public RightPanel() {
            super(BoxLayout.Y_AXIS);
            add(trumpLabel);
            add(Box.createRigidArea(new Dimension(5, 10)));
            add(cardsCountLabel);
            add(Box.createRigidArea(new Dimension(5, 15)));
            add(tableOfPlayers);
        }

        public void setTrumpCard(Card trumpCard){
            trumpLabel.setText(String.valueOf(trumpCard));
        }

        public void setCardsCount(int cardsCount){
          cardsCountLabel.setText(String.valueOf(cardsCount));
        }

        public void setTableOfPlayers(String[][] players){
            JTableUtils.writeArrayToJTable(tableOfPlayers, players);
        }
    }

    class CenterPanel extends Box{
        JTable targetDeck = new JTable();
        JTable cardsOnTable = new JTable();
        JTable soursDeck = new JTable();
        public CenterPanel() {
            super(BoxLayout.Y_AXIS);
            add(targetDeck);
            add(Box.createRigidArea(new Dimension(5, 200)));
            add(cardsOnTable);
            add(Box.createRigidArea(new Dimension(5, 200)));
            add(soursDeck);
        }
        public void setSoursDeck(String[] soursDeck1){
            JTableUtils.writeArrayToJTable(soursDeck, soursDeck1);
        }

        public void setCardsOnTable(String[][] deck){
            JTableUtils.writeArrayToJTable(cardsOnTable, deck);
        }

        public void setTargetDeck(String[] targetDeck1){
            JTableUtils.writeArrayToJTable(targetDeck, targetDeck1);
        }
    }

}
