package com.company.JSwing;

import com.company.*;
import com.company.client_server.Client;
import com.company.client_server.Server;

import javax.swing.*;
import java.awt.*;

public class GamePanel {
    private JFrame frame;
    private Container mainContainer;
    private RightPanel rightPanel;
    private CenterPanel centerPanel;

    boolean isServerVersion;
    GUILogic guiLogic = new GUILogic();
    String[] upCards;
    String[] downCards;
    String info;
    int countInDeck;
    String[][] battleCards = new String[2][6];

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

        JRadioButton gameType = new JRadioButton("Play on server");
        panel.add(gameType);
        panel.add(Box.createRigidArea(new Dimension(5, 7)));

        JButton start = new JButton("Start game");
        panel.add(start);

        start.addActionListener(e -> {
            if (gameType.isSelected()) {
                isServerVersion = true;
//                Server.start(8080);
//                Server.runServerApp(8080);
                String response = Client.start();
                parseResponse(response);
            } else {
                isServerVersion = false;
                guiLogic.initializeGame();
                rightPanel.setTrumpCard(guiLogic.getTrumpCard());
                centerPanel.setRightPanel(rightPanel);
                centerPanel.setGUILogic(guiLogic);
                countInDeck = guiLogic.start();
                upCards = guiLogic.getUpCards();
                downCards = guiLogic.getDownCards();
                info = guiLogic.getInfo();
            }

            centerPanel.setUpDeck(upCards);
            centerPanel.setDownDeck(downCards);
            rightPanel.setCardsCount(countInDeck);
            centerPanel.info.setText(info);

            centerPanel.setIsServerVersion(isServerVersion);
        });

        return panel;
    }

    public void parseResponse(String response) {
        String[] str = response.split("_");

        upCards = str[0].split(" ");
        downCards = str[1].split(" ");
        info = str[2];
        countInDeck = Integer.parseInt(str[3]);

        battleCards[0] = str[4].split(" ");
        battleCards[1] = str[5].split(" ");
    }
}
