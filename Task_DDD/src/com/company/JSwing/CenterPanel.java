package com.company.JSwing;

import com.company.utils.JTableUtils;

import javax.swing.*;
import java.awt.*;

public class CenterPanel extends Box {
    JTable targetDeck = new JTable();
    JTable cardsOnTable = new JTable();
    JTable soursDeck = new JTable();
    JButton move = new JButton("Сходить");
    JButton action = new JButton();
    public CenterPanel() {
        super(BoxLayout.Y_AXIS);
        add(targetDeck);
        add(Box.createRigidArea(new Dimension(5, 200)));
        add(cardsOnTable);
        add(Box.createRigidArea(new Dimension(5, 200)));
        add(soursDeck);
        add(Box.createRigidArea(new Dimension(5, 500)));
        add(move);
        add(Box.createRigidArea(new Dimension(5, 7)));
        add(action);
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

    public int numberCardForMove() {
        return 0;
    }
}
