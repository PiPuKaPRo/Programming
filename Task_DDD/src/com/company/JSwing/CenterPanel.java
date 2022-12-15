package com.company.JSwing;

import com.company.*;
import com.company.utils.JTableUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.company.Table.getNextPlayingPlayer;

public class CenterPanel extends Box {
    private Round round;
    private Player source;
    private Player target;
    private Battle battle;
    private Card down;
    private Card up;


    JTable upDeck = new JTable();
    JTable cardsOnTable = new JTable();
    JTable downDeck = new JTable();
    JButton move = new JButton("Сходить");
    JButton action = new JButton(" ");
    JButton nextRound = new JButton("Следующий раунд");
    JButton nextBattle = new JButton("Следующий батл");
    JLabel inf = new JLabel(" ");


    public CenterPanel(Table t, Game g) {
        super(BoxLayout.Y_AXIS);
        add(upDeck);
        add(Box.createRigidArea(new Dimension(5, 200)));
        add(cardsOnTable);
        add(Box.createRigidArea(new Dimension(5, 200)));
        add(downDeck);
        add(Box.createRigidArea(new Dimension(5, 10)));
        add(move);
        add(Box.createRigidArea(new Dimension(5, 7)));
        add(action);
        add(Box.createRigidArea(new Dimension(5, 10)));
        add(nextRound);
        add(Box.createRigidArea(new Dimension(5, 7)));
        add(nextBattle);
        add(Box.createRigidArea(new Dimension(5, 20)));
        add(inf);


        nextRound.addActionListener(e -> {
            if (round != null) {
                Table.getNextPlayer(t, round, battle);
                t.addRound(round);
            }
            if (Game.isGameActive(t)) {
                round = new Round(source, target);
                // вывести карты игроков и написать что новый раунд
                if (source == null) {
                    inf.setText("Ничья");
                    return;
                }
                target = getNextPlayingPlayer(t, source);
                if (target == null) {
                    inf.setText("Игра окончена, проиграл игрок - " + source.getNumber());
                }
            }
            repaintC();
        });

        action.addActionListener(e -> {
            if (isHuman(round.getTarget())) {
                inf.setText("Беру");
                round.addBattle(battle);
                return;
            }
            if (isHuman(round.getSource())) {
                inf.setText("Бито");
                round.addBattle(battle);
                return;
            }
            repaintC();
        });

        nextBattle.addActionListener(e -> {
            boolean isFirstRound = round.getBattles().size() == 0;
            int maxCountBattles = isFirstRound ? 5 : 6;

            if (round.getBattles().size() <= maxCountBattles){
                // писать кто ходит
                if (!isHuman(source)) {
                    down = Logic.attackersMove(true, round, source, t, g, round.getBattles().size());
                    // писать кто ходит
                    if (!isHuman(target)) {
                        up = Logic.defendersMove(t, target, round, down, g);
                    }
                }
            }
            repaintC();
        });

        move.addActionListener(e -> {
            // выбрал ли игрок карту
            // если нет, то в лейбл "выбери карту"
            // если да, то проверка можно ли этой картой сходить и определение типа игрока(защита или атака)
            // если можно сходить то сохр в довн или ап
            repaintC();
        });

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

    private static boolean isHuman(Player player) {
        return player.getNumber() == 1;
    }

    private void repaintC() {
        setDownDeck(Round.getStringPlayersCards(source.getPlayersCards()));
        setUpDeck(Round.getStringPlayersCards(target.getPlayersCards()));
        setCardsOnTable(getStringCardsOnTable(round.getBattles()));
    }

    public static String[][] getStringCardsOnTable(List<Battle> battles) {
        String[][] array = new String[2][battles.size()];
        for (int i = 0; i < battles.size(); i++) {
            array[0][i] = battles.get(i).getDefendCard().toString();
            array[1][i] = battles.get(i).getAttackCard().toString();
        }
        return array;
    }
}
