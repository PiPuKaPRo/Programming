package com.company.JSwing;

import com.company.*;

import javax.swing.*;
import java.awt.*;

import static com.company.Table.getNextPlayingPlayer;

public class GamePanel {
    private JFrame frame;
    private Container mainContainer;
    public Table t;
    private Game g;
    private int players;
    private RightPanel rightPanel;
    private CenterPanel centerPanel;
    private Player source;
    private Player target;

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
        centerPanel = new CenterPanel();
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
        mainContainer.add(centerPanel, BorderLayout.CENTER);
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
            centerPanel.setT(t);
            centerPanel.setG(g);
        });

        startGame.addActionListener(e -> {
            Player.dialCards(t);
            rightPanel.setCardsCount(t.getCards().size());
            source = Game.getPlayerWhoMovedFirst(t);
            target = getNextPlayingPlayer(t, source);
            Round firstRound = new Round(source, target);
            centerPanel.setRound(firstRound);
            centerPanel.inf.setText("Ходит: " + source);
            if (!centerPanel.isBot(target)){
                centerPanel.player.setText("Player " + source.getNumber());
                centerPanel.player1.setText("Player " + target.getNumber());
                centerPanel.setDownDeck(Round.getStringPlayersCards(target.getPlayersCards()));
                centerPanel.setUpDeck(Round.getStringPlayersCards(source.getPlayersCards()));
            } else {
                centerPanel.player.setText("Player " + target.getNumber());
                centerPanel.player1.setText("Player " + source.getNumber());
                centerPanel.setDownDeck(Round.getStringPlayersCards(source.getPlayersCards()));
                centerPanel.setUpDeck(Round.getStringPlayersCards(target.getPlayersCards()));
            }

        });

        return panel;
    }


}
