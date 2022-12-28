package com.company;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private Player source;
    private Player target;
    private List<Battle> battles = new ArrayList();
    private boolean isPickedUp = false;
    private List<CardsNumberAndPower> allRanksInRound = new ArrayList<>();

    public Round(Player source, Player target) {
        this.source = source;
        this.target = target;
    }

    public Player getSource() {
        return source;
    }

    public Player getTarget() {
        return target;
    }

    public static String[] getStringPlayersCards(List<Card> playersCards){
        String[] array = new String[playersCards.size()];
        int index = 0;
        for (Card value : playersCards) {
            array[index] = String.valueOf(value);
            index++;
        }
        return array;
    }

    public List<Battle> getBattles() {
        return battles;
    }

    // добавляем все бои за 1 раунд
    public void setBattles(List<Battle> battles) {
        this.battles = battles;
    }

    public void setPickedUp(boolean pickedUp) {
        isPickedUp = pickedUp;
    }

    public void addRank(CardsNumberAndPower rank) {    //добавляем номинал карты
        if (!this.nominalInRound(rank)) {
            allRanksInRound.add(rank);      //если нет, то добавляем
        }
    }

    public boolean nominalInRound(CardsNumberAndPower rank) {  //проверяем наличие номинала rank в раунде
        for (CardsNumberAndPower r: allRanksInRound) {         //цикл по всем сохранённым номиналам
            if (r.equals(rank)) return true;             //если rank совпадает с r, то такой номинал есть
        }
        return false;
    }

    public static List<Card> getCardsInRound(Round round) {
        List<Card> cardsInRound = new ArrayList<>();
        for (Battle battle : round.getBattles()) {
            cardsInRound.add(battle.getAttackCard());
            if (battle.isCovered()) {
                cardsInRound.add(battle.getDefendCard());
            }
        }
        return cardsInRound;
    }

    public static Player playingRound(Table t, Round round, boolean isFirstRound, Game g) { // играем раунд
        Player source = round.getSource();
        Player target = round.getTarget();
        List<Battle> battles = new ArrayList<>();
        Player nextPlayer;
        int maxCountBattles = isFirstRound ? 5 : 6;

        int battleNumber = 0;
        // первый баттл в раунде
        Battle battle = null;
        System.out.println("-*-*-| " + (battleNumber + 1) + " battle |-*-*-");

        // карта которую надо побить

        Card down = Logic.attackersMove(true, round, source, t, g, battleNumber);
        // карта, которой бьют
        Card up;
        while (battles.size() <= maxCountBattles) { // пока кол-во батлов не достиг макс и если атака не сказала бито

            // если атака сказала бито
            if (down == null) {
                System.out.println("bito");
                break;
            }


            System.out.println(down);

            // иначе защита делает ход
            up = Logic.defendersMove(t, target, round, down,g);
            // если защита сказала беру
            if (up == null) {
                // сохраняем батл и заканчиваем раунд (break)
                battle = new Battle(down, up);
                battles.add(battle);
                round.setBattles(battles);
                break;
            }
            System.out.println(up);

            // если никто не говорил ни "бито", ни "беру"
            // сохраняем раунд и продолжаем раунд
            battle = new Battle(down, up);
            battles.add(battle);
            round.setBattles(battles);
            battleNumber++;
            // начинается новый батл
            System.out.println("-*-*-| " + (battleNumber + 1) + " battle |-*-*-");
            // атака делает новый ход
            down = Logic.attackersMove(false, round, source, t, g, battleNumber);
        }

        nextPlayer = Table.getNextPlayer(t, round, battle);

        t.addRound(round);  //сохраняем раунд

        // выводим карты игроков и размер колоды
        System.out.println(Console.playerCardsToString(t));
        System.out.println(t.getCards().size());

        // возвращаем следующего игрока
        return nextPlayer;
    }
    public void addBattle(Battle battle) {
        battles.add(battle);
    }

    public static void addRoundCardsToPlayer(Table t, Round r){
        List<Card> cardsInRound = Round.getCardsInRound(r);
        for (Card card : cardsInRound) {
            Deck.addCardToPlayer(t, r.getTarget(), card);
        }
    }
}

