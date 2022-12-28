package com.company;

import java.util.Arrays;
import java.util.List;

import static com.company.Table.getNextPlayingPlayer;

public class GUILogic {
    Game g;
    Table t;
    Deck deck;

    Round round;
    Battle battle;
    Player source;
    Player target;
    String info;

    /*
    Базовые методы класса
     */
    public String[] getUpCards() {
        if (isBot(source)) {
            return Round.getStringPlayersCards(source.getPlayersCards());
        } else {
            return Round.getStringPlayersCards(target.getPlayersCards());
        }
    }

    public String[] getDownCards() {
        if (isBot(source)) {
            return Round.getStringPlayersCards(target.getPlayersCards());
        } else {
            return Round.getStringPlayersCards(source.getPlayersCards());
        }
    }

    public String  getTrumpCard(){
        return t.getTrumpCard().toString();
    }

    public String getInfo() {
        return info;
    }

    public void initializeGame() {

        this.t = new Table();
        this.g = new Game();
        this.deck = new Deck();
        g.addPlayersInGame(t, 2);


        deck.addCardsInGame(t);
    }

    public int start() {
        Player.dialCards(t);
        source = Game.getPlayerWhoMovedFirst(t);
        target = getNextPlayingPlayer(t, source);
        round = new Round(source, target);
        battle = new Battle();

        return t.getCards().size();
    }

    public void action() {
        if (isBot(target)) {
            info = "Бито, нажмите \"Далее\" ";
            endRound(false);
        } else if (isBot(source)) {
            info = target + " взял, нажмите \"Далее\" ";
            round.addBattle(battle);
            battle = new Battle();
            endRound(true);
        }
    }

    public void next() {
        battle = new Battle();
        int maxCountBattles = (t.getRounds().size() == 0) ? 5 : 6;
        // если можно сделать батл
        if (round.getBattles().size() <= maxCountBattles) {
            // если нападает бот - он ходит сразу
            info = "Ходит: " + source;
            if (isBot(source)) {
                botAttackersMove();
                if (battle.getAttackCard() != null && isBot(target)) { // если защита тоже бот, то ходит сразу
                    botDefendersMove();
                }
                info = "Move: " + target;
            }
        }
    }

    public void move(int cardNum) {
        if (cardNum != -1) {
            if (!isBot(source)) {
                if (Logic.checkAttCard(source.getPlayersCards(), cardNum, round, round.getBattles().size() == 0)) {
                    battle.setAttackCard(source.getPlayersCards().remove(cardNum));
                    info = "Батл окончен. Нажмите \"Далее\" ";
                    round.addBattle(battle);
                    botDefendersMove();
                } else {
                    info = "Выберите другую карту!!!";
                }
            } else if (!isBot(target)) {
                if (Logic.checkDefCard(target.getPlayersCards(), cardNum, t.getTrumpCard(), battle.getAttackCard())) {
                    battle.setDefendCard(target.getPlayersCards().remove(cardNum));
                    round.addBattle(battle);
                    info = "Нажмите \"Далее\" ";
                } else {
                    info = "Выберите другую карту!!!";
                }
            }
        } else {
            info = "Выберите карту!!!";
        }
    }

    private void botAttackersMove() {
        Card attackCard = Logic.attackersMove(true, round, source, t, g, round.getBattles().size());
        if (attackCard == null) {
            info = "Бито, нажмите \"Далее\" ";
            endRound(false);
        } else {
            battle.setAttackCard(attackCard);
        }
    }

    private void botDefendersMove() {
        Card defendCard = Logic.defendersMove(t, target, round, battle.getAttackCard(), g);
        if (defendCard == null) {
            info = "Бот Валера взял карты нажмите \"Далее\" ";
            endRound(true);
        } else {
            battle.setDefendCard(defendCard);
        }
    }

    public int endRound(boolean isTargetTakes) {
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
            if (Player.isActive(t, source)){
                target = Table.getNextPlayingPlayer(t, source);
                if (target == null) {
                    info = "Игра окончена, проиграл " + source;
                } else {
                    round = new Round(source, target);
                    battle = new Battle();
                }
            } else {
                source = Table.getNextPlayingPlayer(t, source);
                if (source == null){
                    info = "Игра окончена, ничья!";
                } else if (source == prevSource){
                    info = "Игра окончена, проиграл " + source;
                }
            }
        }
        return t.getCards().size();
    }

    public String convertGUIInfoTOString() {
        String s = "";
        if (!isBot(target)) {
            s += Arrays.toString(Round.getStringPlayersCards(source.getPlayersCards())) + "_";
            s += Arrays.toString(Round.getStringPlayersCards(target.getPlayersCards())) + "_";
            s += battleCardsToString(round.getBattles(), battle, true) + "_";
            s += battleCardsToString(round.getBattles(), battle, false) + "_";
        } else {
            s += Arrays.toString(Round.getStringPlayersCards(target.getPlayersCards())) + "_";
            s += Arrays.toString(Round.getStringPlayersCards(source.getPlayersCards())) + "_";
            s += battleCardsToString(round.getBattles(), battle, false) + "_";
            s += battleCardsToString(round.getBattles(), battle, true) + "_";
        }
        // кол-во карт в колоде
        // инф
        s += t.getCards().size() + "_";
        s += info + "_";

        return s;
    }

    public String[][] getBattleCards() {
        List<Battle> battles = round.getBattles();
        String[][] array = new String[2][battles.size() + 1];
        for (int i = 0; i < battles.size(); i++) {
            addBattleToStringBattleArray(array, battles.get(i), i);
        }

//        addBattleToStringBattleArray(array, battle, battles.size());

        return array;
    }

    private void addBattleToStringBattleArray(String[][] array, Battle currentBattle, int index) {
        if (isBot(source)) {
            if (currentBattle.getAttackCard() != null) {
                array[0][index] = currentBattle.getAttackCard().toString();
            }
            if (currentBattle.getDefendCard() != null) {
                array[1][index] = currentBattle.getDefendCard().toString();
            }
        }
        if (isBot(target)){
            if (currentBattle.getAttackCard() != null) {
                array[1][index] = currentBattle.getAttackCard().toString();
            }
            if (currentBattle.getDefendCard() != null) {
                array[0][index] = currentBattle.getDefendCard().toString();
            }
        }
    }

    private String battleCardsToString(List<Battle> battles, Battle current, boolean upCards) {
        StringBuilder s = new StringBuilder();
        boolean sourceIsBot = isBot(source);

        for (Battle b : battles) {
            if (sourceIsBot)
                s.append(upCards ? b.getAttackCard() : b.getDefendCard());
            else
                s.append(upCards ? b.getDefendCard() : b.getAttackCard());
            s.append(" ");
        }

        s.append(upCards ? current.getAttackCard() : current.getDefendCard());

        return s.toString();
    }

    public static boolean isBot(Player player) {
        return player.getNumber() != 1;
    }

}
