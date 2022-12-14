package com.company.JSwing;

import com.company.utils.JTableUtils;

import javax.swing.*;
import java.awt.*;

public class CenterPanel extends Box {
    public static int number;

    public static int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    JTable targetDeck = new JTable();
    JTable cardsOnTable = new JTable();
    JTable soursDeck = new JTable();
    JButton move = new JButton("Сходить");
    JButton action = new JButton("Бито/Взять");


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

}
