package com.company.JSwing;

import com.company.*;
import com.company.client_server.Client;
import com.company.utils.JTableUtils;

import javax.swing.*;
import java.awt.*;

public class CenterPanel extends Box {
    private RightPanel rightPanel;


    GUILogic guiLogic;
    private boolean isServerVersion;
    private String[] upCards;
    private String[] downCards;
    private String infoText;
    private int countInDeck;
    private String[][] battleCards = new String[2][6];


    JTable upDeck = new JTable();
    JTable cardsOnTable = new JTable();
    JTable downDeck = new JTable();
    JButton move = new JButton("Сходить");
    JButton action = new JButton("Бито/Беру");
    JButton nextBattle = new JButton("Далее");
    JLabel info = new JLabel(" ");
    JLabel playerUpName = new JLabel("Бот Валера");
    JLabel playerDownName = new JLabel("Я");


    public CenterPanel() {
        super(BoxLayout.Y_AXIS);
        add(playerUpName);
        add(upDeck);
        add(Box.createRigidArea(new Dimension(5, 200)));
        add(cardsOnTable);
        add(Box.createRigidArea(new Dimension(5, 20)));
        add(info);
        add(Box.createRigidArea(new Dimension(5, 200)));
        add(playerDownName);
        add(downDeck);
        add(Box.createRigidArea(new Dimension(5, 10)));
        add(move);
        add(Box.createRigidArea(new Dimension(5, 7)));
        add(action);
        add(Box.createRigidArea(new Dimension(5, 7)));
        add(nextBattle);

        nextBattle.addActionListener(e -> {
            if (isServerVersion) {
                String response = Client.start();
                parseResponse(response);
            } else {
                guiLogic.next();
                upCards = guiLogic.getUpCards();
                downCards = guiLogic.getDownCards();
                infoText = guiLogic.getInfo();
            }

            repaintC();
        });

        action.addActionListener(e -> {
            if (isServerVersion) {
                String s = Client.action();
                parseResponse(s);
            } else {
                guiLogic.action();
            }
            repaintC();
        });

        move.addActionListener(e -> {
            int cardNum = downDeck.getSelectedColumn();
            if (isServerVersion) {
                String response = Client.move(cardNum);
                parseResponse(response);
            } else {
                guiLogic.move(cardNum);
                upCards = guiLogic.getUpCards();
                downCards = guiLogic.getDownCards();
                battleCards = guiLogic.getBattleCards();
                infoText = guiLogic.getInfo();
            }
            // если нет, то в лейбл "выбери карту"
            // если да, то проверка можно ли этой картой сходить и определение типа игрока(защита или атака)
            // если можно сходить то сохр в довн или ап
            repaintC();
        });

    }

    public void setRightPanel(RightPanel rightPanel) {
        this.rightPanel = rightPanel;
    }

    public void setIsServerVersion(boolean isServerVersion) {
        this.isServerVersion = isServerVersion;
    }

    public void setDownDeck(String[] soursDeck1) {
        JTableUtils.writeArrayToJTable(downDeck, soursDeck1);
    }

    public void setCardsOnTable(String[][] deck) {
        JTableUtils.writeArrayToJTable(cardsOnTable, deck);
    }

    public void setUpDeck(String[] targetDeck1) {
        JTableUtils.writeArrayToJTable(upDeck, targetDeck1);
    }

    private void repaintC() {
        //if (isServerVersion) {
            setDownDeck(downCards);
            setUpDeck(upCards);
            setCardsOnTable(battleCards);
            rightPanel.setCardsCount(countInDeck);
            info.setText(infoText);
        /*} else {

            if (!isBot(target)) {
                setDownDeck(Round.getStringPlayersCards(target.getPlayersCards()));
                setUpDeck(Round.getStringPlayersCards(source.getPlayersCards()));
            } else {
                setDownDeck(Round.getStringPlayersCards(source.getPlayersCards()));
                setUpDeck(Round.getStringPlayersCards(target.getPlayersCards()));
            }
            setCardsOnTable(getStringCardsOnTable(round.getBattles(), battle));
        }*/
    }

    public void parseResponse(String response) {
        String[] str = response.split("_");

        upCards = str[0].split(" ");
        downCards = str[1].split(" ");
        infoText = str[2];
        countInDeck = Integer.parseInt(str[3]);

        battleCards[0] = str[4].split(" ");
        battleCards[1] = str[5].split(" ");
    }

    public void setGUILogic(GUILogic gu) {
        guiLogic = gu;
    }
}
