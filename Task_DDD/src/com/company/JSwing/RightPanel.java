package com.company.JSwing;

import com.company.Card;
import com.company.utils.JTableUtils;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class RightPanel extends Box {
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

    public void setTrumpCard(String  trumpCard){
        trumpLabel.setText(trumpCard);
    }

    public void setCardsCount(int cardsCount){
        cardsCountLabel.setText(String.valueOf(cardsCount));
    }

    public void setTableOfPlayers(String[][] players){
        JTableUtils.writeArrayToJTable(tableOfPlayers, players);
    }
}
