package com.company.JSwing;

import com.company.*;
import com.company.utils.JTableUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.company.Table.getNextPlayingPlayer;

public class CenterPanel extends Box {
    private Round round;
    private Player source;
    private Player target;
    private Battle battle;
    private int i;
    private int cardNum;
    private Table t;
    private Game g;
    private RightPanel rightPanel;

    public void setRound(Round round) {
        this.round = round;
        this.source = round.getSource();
        this.target = round.getTarget();
    }

    public void setT(Table t) {
        this.t = t;
    }

    public void setG(Game g) {
        this.g = g;
    }

    JTable upDeck = new JTable();
    JTable cardsOnTable = new JTable();
    JTable downDeck = new JTable();
    JButton move = new JButton("Сходить");
    JButton action = new JButton("Бито/Беру");
    JButton nextRound = new JButton("Следующий раунд");
    JButton nextBattle = new JButton("Следующий батл");
    JLabel inf = new JLabel(" ");
    JLabel player = new JLabel(" ");
    JLabel player1 = new JLabel(" ");


    public CenterPanel() {
        super(BoxLayout.Y_AXIS);
        add(player);
        add(upDeck);
        add(Box.createRigidArea(new Dimension(5, 200)));
        add(cardsOnTable);
        add(Box.createRigidArea(new Dimension(5, 20)));
        add(inf);
        add(Box.createRigidArea(new Dimension(5, 200)));
        add(player1);
        add(downDeck);
        add(Box.createRigidArea(new Dimension(5, 10)));
        add(move);
        add(Box.createRigidArea(new Dimension(5, 7)));
        add(action);
        add(Box.createRigidArea(new Dimension(5, 10)));
        add(nextRound);
        add(Box.createRigidArea(new Dimension(5, 7)));
        add(nextBattle);

        nextRound.addActionListener(e -> {
            if (round != null) {
                target = Table.getNextPlayingPlayer(t, round.getSource());
                t.addRound(round);
            }
            if (Game.isGameActive(t)) {
                round = new Round(source, target);
                if (isHuman(target)) {
                    setDownDeck(Round.getStringPlayersCards(target.getPlayersCards()));
                    setUpDeck(Round.getStringPlayersCards(source.getPlayersCards()));
                    player.setText("Player " + source.getNumber());
                    player1.setText("Player " + target.getNumber());
                } else {
                    setDownDeck(Round.getStringPlayersCards(source.getPlayersCards()));
                    setUpDeck(Round.getStringPlayersCards(target.getPlayersCards()));
                    player.setText("Player " + target.getNumber());
                    player1.setText("Player " + source.getNumber());
                }

                inf.setText("Раунд: " + (i + 1));
                if (source == null) {
                    inf.setText("Ничья");
                    return;
                }
                source = getNextPlayingPlayer(t, source);
                if (target == null) {
                    inf.setText("Игра окончена, проиграл игрок - " + source.getNumber());
                }
            }
            i++;
            rightPanel.setCardsCount(t.getCards().size());
            repaintC();
        });

        action.addActionListener(e -> {
            if (isHuman(round.getTarget())) {
                inf.setText("Беру, нажмите следующий раунд");
                round.addBattle(battle);
                return;
            }
            if (isHuman(round.getSource())) {
                inf.setText("Бито, нажмите следующий раунд");
                round.addBattle(battle);
                return;
            }
            repaintC();
        });

        nextBattle.addActionListener(e -> {
            battle = new Battle();
            boolean isFirstRound = t.getRounds().size() == 0;
            int maxCountBattles = isFirstRound ? 5 : 6;

            if (round.getBattles().size() <= maxCountBattles) {
                inf.setText("Ходит: " + source);
                if (!isHuman(source)) {
                    Card attackCard = Logic.attackersMove(true, round, source, t, g, round.getBattles().size());
                    if (attackCard == null){
                        battle.setDefendCard(round.getBattles().get(round.getBattles().size() - 1).getDefendCard());
                        inf.setText("Бито, нажмите \"Следующий раунд\" ");
                    } else {
                        battle.setAttackCard(attackCard);
                    }
                    if (!isHuman(target)) {
                        battle.setDefendCard(Logic.defendersMove(t, target, round, battle.getAttackCard(), g));
                    }
                    inf.setText("Нажмите \"Следующий батл\" ");
                }
            }
            repaintC();
        });

        move.addActionListener(e -> {
            // выбрал ли игрок карту
            cardNum = downDeck.getSelectedColumn();
            if (cardNum != -1) {
                if (isHuman(source)) {
                    boolean isFirstBattle = round.getBattles().size() == 0;
                    if (Logic.checkAttCard(source.getPlayersCards(), cardNum, round, isFirstBattle)) {
                        battle.setAttackCard(source.getPlayersCards().remove(cardNum));
                        battle.setDefendCard(Logic.defendersMove(t,target, round, battle.getAttackCard(), g));
                        round.addBattle(battle);

                    } else {
                        inf.setText("Выберите другую карту!!!");
                    }
                } else if (isHuman(target)) {
                    if (Logic.checkDefCard(target.getPlayersCards(), cardNum, t.getTrumpCard(), battle.getAttackCard())) {
                        battle.setDefendCard(target.getPlayersCards().remove(cardNum));
                        round.addBattle(battle);
                    } else {
                        inf.setText("Выберите другую карту!!!");
                    }
                }
            } else {
                inf.setText("Выберите карту!!!");
            }
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

    public static boolean isHuman(Player player) {
        return player.getNumber() == 1;
    }

    private void repaintC() {
        if (isHuman(target)) {
            setDownDeck(Round.getStringPlayersCards(target.getPlayersCards()));
            setUpDeck(Round.getStringPlayersCards(source.getPlayersCards()));
        } else {
            setDownDeck(Round.getStringPlayersCards(source.getPlayersCards()));
            setUpDeck(Round.getStringPlayersCards(target.getPlayersCards()));
        }
        setCardsOnTable(getStringCardsOnTable(round.getBattles(), battle));
    }

    public static String[][] getStringCardsOnTable(List<Battle> battles, Battle currentBattle) {
        String[][] array = new String[2][battles.size() + 1];
        for (int i = 0; i < battles.size(); i++) {
           array[0][i] = currentBattle.getDefendCard().toString();
           array[1][i] = currentBattle.getAttackCard().toString();
        }
        if (currentBattle.getDefendCard() != null){
            array[0][battles.size()] = currentBattle.getDefendCard().toString();
        }
        if (currentBattle.getAttackCard() != null){
            array[1][battles.size()] = currentBattle.getAttackCard().toString();
        }


        return array;
    }
}
