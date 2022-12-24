package com.company.JSwing;

import com.company.*;
import com.company.utils.JTableUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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

    public void setRound(Round round, RightPanel rightPanel) {
        this.round = round;
        this.source = round.getSource();
        this.target = round.getTarget();
        this.battle = new Battle();
        this.rightPanel = rightPanel;
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
    JButton nextBattle = new JButton("Далее");
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
        add(Box.createRigidArea(new Dimension(5, 7)));
        add(nextBattle);

        nextBattle.addActionListener(e -> {
            battle = new Battle();
            int maxCountBattles = (t.getRounds().size() == 0) ? 5 : 6;
            // если можно сделать батл
            if (round.getBattles().size() <= maxCountBattles) {
                // если нападает бот - он ходит сразу
                inf.setText("Ходит: " + source);
                if (isBot(source)) {
                    botAttackersMove();
                    if (battle.getAttackCard() != null && isBot(target)){// если защита тоже бот, то ходит сразу
                        botDefendersMove();
                        inf.setText("Нажмите \"Далее\" ");
                    }
                    inf.setText("Ходит: " + target);
                }
            }
            repaintC();
        });

        action.addActionListener(e -> {
            if (!isBot(target)) {
                inf.setText(target + " взял, нажмите \"Далее\"");
                round.addBattle(battle);
                battle = new Battle();
                endRound(true);
            } else if (!isBot(source)) {
                inf.setText("Бито, нажмите \"Далее\" ");
                endRound(false);
            }
            repaintC();
        });

        move.addActionListener(e -> {
            // выбрал ли игрок карту
            cardNum = downDeck.getSelectedColumn();
            if (cardNum != -1) {
                if (!isBot(source)) {
                    if (Logic.checkAttCard(source.getPlayersCards(), cardNum, round, round.getBattles().size() == 0)) {
                        battle.setAttackCard(source.getPlayersCards().remove(cardNum));
                        inf.setText("Батл окончен. Нажмите \"Далее\" ");
                        round.addBattle(battle);
                        botDefendersMove();
                    } else {
                        inf.setText("Выберите другую карту!!!");
                    }
                } else if (!isBot(target)) {
                    if (Logic.checkDefCard(target.getPlayersCards(), cardNum, t.getTrumpCard(), battle.getAttackCard())) {
                        battle.setDefendCard(target.getPlayersCards().remove(cardNum));
                        round.addBattle(battle);
                        inf.setText("Нажмите \"Далее\" ");
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

    public static boolean isBot(Player player) {
        return player.getNumber() != 1;
    }

    private void repaintC() {
        if (!isBot(target)) {
            setDownDeck(Round.getStringPlayersCards(target.getPlayersCards()));
            setUpDeck(Round.getStringPlayersCards(source.getPlayersCards()));
        } else {
            setDownDeck(Round.getStringPlayersCards(source.getPlayersCards()));
            setUpDeck(Round.getStringPlayersCards(target.getPlayersCards()));
        }
        setCardsOnTable(getStringCardsOnTable(round.getBattles(), battle));
    }

    public String[][] getStringCardsOnTable(List<Battle> battles, Battle currentBattle) {
        String[][] array = new String[2][battles.size() + 1];
        for (int i = 0; i < battles.size(); i++) {
            if (isBot(target)) {
                if (battles.get(i).getAttackCard() != null) {
                    array[1][i] = battles.get(i).getAttackCard().toString();
                } else {
                    inf.setText("Бито, нажмите \"Далее\" ");
                }
                if (battles.get(i).getDefendCard() != null) {
                    array[0][i] = battles.get(i).getDefendCard().toString();
                }
            } else {
                if (battles.get(i).getAttackCard() != null) {
                    array[0][i] = battles.get(i).getAttackCard().toString();
                } else {
                    inf.setText("Бито, нажмите \"Далее\" ");
                }
                if (battles.get(i).getDefendCard() != null) {
                    array[1][i] = battles.get(i).getDefendCard().toString();
                }
            }
        }
        if (currentBattle.getDefendCard() != null) {
            if (isBot(target)) {
                array[0][battles.size()] = currentBattle.getDefendCard().toString();
            } else {
                array[1][battles.size()] = currentBattle.getDefendCard().toString();
            }
        }
        if (currentBattle.getAttackCard() != null) {
            if (isBot(source)) {
                array[0][battles.size()] = currentBattle.getAttackCard().toString();
            } else {
                array[1][battles.size()] = currentBattle.getAttackCard().toString();
            }

        }
        return array;
    }


    private void botAttackersMove() {
        Card attackCard = Logic.attackersMove(true, round, source, t, g, round.getBattles().size());
        if (attackCard == null) {
            inf.setText("Бито, нажмите \"Далее\" ");
            endRound(false);
        } else {
            battle.setAttackCard(attackCard);
        }
    }

    private void botDefendersMove() {
        battle.setDefendCard(Logic.defendersMove(t, target, round, battle.getAttackCard(), g));
        if (battle.getDefendCard() == null) {
            inf.setText("Игрок " + target + " берёт, раунд окончен. Нажмите \"Далее\" ");
            endRound(true);
        }
    }

    /**
     * закончить текущий раунд, сохранить его, и создать новый раунд
     *
     * @param isTargetTakes
     */
    private void endRound(boolean isTargetTakes) {
        t.addRound(round);
        // если защита взяла карты, то
        if (isTargetTakes) {
            Round.addRoundCardsToPlayer(t, round);
            Deck.addCardToFull(t, source);
            source = Table.getNextPlayingPlayer(t, target);
            target = Table.getNextPlayingPlayer(t, source);
            round = new Round(source, target);
        } else {
            Deck.addCardToFull(t, source);
            Deck.addCardToFull(t, target);
            Player prevSource = source;
            source = target;
            if (Player.isActive(t, source)) {
                target = Table.getNextPlayingPlayer(t, source);
                if (target == null) {
                    inf.setText("Игра окончена, проиграл " + source);
                } else {
                    round = new Round(source, target);
                    battle = new Battle();
                }
            } else {
                source = Table.getNextPlayingPlayer(t, source);
                if (source == null) {
                    inf.setText("Игра окончена, ничья!");
                } else if (source == prevSource) {
                    inf.setText("Игра окончена, проиграл " + source);
                }
            }
        }
        rightPanel.setCardsCount(t.getCards().size());
    }

}
