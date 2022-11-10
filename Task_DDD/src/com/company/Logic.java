package com.company;

import java.util.*;

public class Logic {
    /**
     * добавление карт в игру
     *
     * @param g
     */
    public void addCardsInGame(Table g) {
        ArrayList<Card> allCards = new ArrayList<>();
        Stack<Card> cards = new Stack<>();
        for (CardsNumberAndPower rank : CardsNumberAndPower.values()) {
            for (Masti suit : Masti.values()) {
                allCards.add(new Card(suit, rank));
                Collections.shuffle(allCards);
            }
        }
        Card trump = allCards.remove(35);
        g.setTrumpCard(trump);
        cards.push(trump);
        for (Card card : allCards) {
            cards.push(card);
        }
        g.setCards(cards);
    }

    /**
     * добавление игроков в игру
     *
     * @param g
     * @param playersCount
     */
    public void addPlayersInGame(Table g, int playersCount) {
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= playersCount; i++) {
            players.add(new Player(Integer.toString(i)));
        }
        g.setPlayers(players);
    }

    public void play(Table g) {
        dialCards(g);  //раздаем карты
        System.out.println((char) 27 + "[46m" + g.getTrumpCard() + (char) 27 + "[0m");
        System.out.println(Console.playerCardsToString(g));
        System.out.println(g.getCards().size());

        Player source = getPlayerWhoMovedFirst(g);  // кто ходит первым
        System.out.print("---1 Round---\nPlaying " + source.toString());
        Player target = getNextPlayingPlayer(g, source);  // кто отбивает
        System.out.println(" and " + target.toString());

        Round firstRound = new Round(source, target);  // создаем 1ый раунд
        source = playingRound(g, firstRound, true);  // играем 1 раунд и возвращаем игрока, который ходит следующим
        g.addRound(firstRound); // сохраняем инфрмацию о 1ом раунде
        target = getNextPlayingPlayer(g, source);   // находим игрока, который будет отбиваться следующим
        int i = 2;
        while (isGameActive(g)) {
            System.out.println("---| " + i + " Round |---\nPlaying " + source.toString() + " and " + target.toString());
            Round round = new Round(source, target);   // создаем новый раунд
            source = playingRound(g, round, false);
            g.addRound(round);  // сохраняем инфрмацию о раунде
            // играем раунд и возвращаем игрока, который ходит следующим
            if (source == null) {
                break;
            }
            target = getNextPlayingPlayer(g, source);   // находим игрока, который будет отбиваться следующим
            if (target == null) {
                break;
            }
            i++;
        }
        Player stupid = getLastPlayer(g);
        if (stupid == null) {
            System.out.println("draw");
        } else {
            System.out.println("----| " + stupid + " lose |----");
        }
        System.out.println((char) 27 + "[31m|||---||| Game  end |||---|||" + (char) 27 + "[0m");
    }

    private void dialCards(Table g) {   // раздача карт
        Map<Player, List<Card>> playersCards = new HashMap<>(); // лист с картами всех игорков

        for (Player p : g.getPlayers()) {
            List<Card> pc = new ArrayList<>();  // карты p-го игрока
            for (int i = 0; i < 6; i++) {
                pc.add(g.getCards().pop());
            }
            Collections.sort(pc);

            /***/
            pc = addTrumpsToEnd(pc, g.getTrumpCard().getSuit());
            /***/

            playersCards.put(p, pc);
            p.setPlayersCards(pc);// его карты сохранятся в лист с картами всех игроков
        }
    }

    /***/
    private List<Card> addTrumpsToEnd(List<Card> cards, Masti trump) { //перестевляем козыри в конец
        Card lastCard = cards.get(cards.size() - 1);
        int i = 0;
        while (cards.get(i) != lastCard) {
            if (cards.get(i).getSuit() == trump) {
                Card c = cards.remove(i);
                cards.add(c);
            } else {
                i++;
            }
        }
        if (cards.get(i).getSuit() == trump) {
            cards.add(cards.remove(i));
        }
        return cards;
    }

    /***/

    /**
     * определение кто ходит первым
     *
     * @param g
     * @return
     */
    private Player getPlayerWhoMovedFirst(Table g) { // кто ходит первым
        int minRang = 14;
        int maxRang = 0;
        Card currentTrump = g.getTrumpCard(); // текущий козырь
        Player playerWhoMovedFirst = null;

        if (currentTrump.getRank().getPow() > 10) {
            playerWhoMovedFirst = searchMinTrumpInGame(g, g.getTrumpCard().getSuit());
            System.out.println("The youngest trump card is " + playerWhoMovedFirst.toString() + ", so he goes first");
        } else {
            playerWhoMovedFirst = searchMaxTrumpInGame(g, g.getTrumpCard().getSuit());
            System.out.println("The most senior trump card is " + playerWhoMovedFirst.toString() + ", so he goes first");
        }
        if (playerWhoMovedFirst == null) {
            playerWhoMovedFirst = searchMaxCardInGame(g);
            System.out.println("The players have no trumps in their hands, the highest card is" + playerWhoMovedFirst);
        }
        return playerWhoMovedFirst;
    }

    private Player searchMaxTrumpInGame(Table g, Masti currentTrump) {
        Player playerWithMaxTrump = null;  // номер игрока с самым большим козырем
        Card maxTrumpInGame = null;   // самый старший козырь из карт в игре
        for (int i = 0; i < g.getPlayers().size(); i++) {
            Player iPlayer = g.getPlayers().get(i);  // i-ый игрок
            List<Card> cardsIPlayer = iPlayer.getPlayersCards();  // карты i-го игроко
            Card maxTrumpInPlayer = null;   // наибольший козырь у игрока
            for (int j = cardsIPlayer.size() - 1; j > -1; j--) {
                if (cardsIPlayer.get(i).getSuit() == currentTrump) {
                    if (maxTrumpInPlayer == null) {
                        maxTrumpInPlayer = cardsIPlayer.get(i);
                        break;
                    }
                }
            }
            for (Card card : cardsIPlayer) {
                if (card.getSuit() == currentTrump) {
                    if (maxTrumpInPlayer == null) {
                        maxTrumpInPlayer = card;
                        break;
                    }
                }
            }
            if (maxTrumpInPlayer != null) {
                if (maxTrumpInGame == null) {
                    maxTrumpInGame = maxTrumpInPlayer;
                    playerWithMaxTrump = iPlayer;
                } else {
                    if (maxTrumpInGame.compareTo(maxTrumpInPlayer) < 0) {   //если меньше 0, то заменяем
                        maxTrumpInGame = maxTrumpInPlayer;
                        playerWithMaxTrump = iPlayer;
                    }
                }
            }
        }

        //если ни у кого нет козыря
        return playerWithMaxTrump;
    }

    private Player searchMinTrumpInGame(Table g, Masti currentTrump) { // search - поиск
        Player playerWithMinTrump = null;   // игрок с самым маленьким козырем
        Card minTrumpInGame = null;   // самый младший козырь из карт в игре
        for (int i = 0; i < g.getPlayers().size(); i++) {
            Player iPlayer = g.getPlayers().get(i);   // i-ый игрок
            List<Card> cardsIPlayer = iPlayer.getPlayersCards();  // карты i-го игрока
            Card minTrumpInPlayer = null;  // наименьший козырь у игрока
            for (Card card : cardsIPlayer) {
                if (card.getSuit() == currentTrump) {
                    if (minTrumpInPlayer == null) {
                        minTrumpInPlayer = card;
                        break;
                    }
                }
            }
            if (minTrumpInPlayer != null) {
                if (minTrumpInGame == null) {
                    minTrumpInGame = minTrumpInPlayer;
                    playerWithMinTrump = iPlayer;
                } else {
                    if (minTrumpInGame.compareTo(minTrumpInPlayer) > 0) { //если больше 0, то заменяем
                        minTrumpInGame = minTrumpInPlayer;
                        playerWithMinTrump = iPlayer;
                    }
                }
            }
        }
        return playerWithMinTrump;
    }

    private Player searchMaxCardInGame(Table g) {
        List<Player> players = g.getPlayers();
        Player playerWithMaxCard = players.get(0);

        List<Card> cards = playerWithMaxCard.getPlayersCards();
        Card maxCard = cards.get(cards.size() - 1);
        for (int i = 1; i < players.size(); i++) {
            cards = players.get(i).getPlayersCards();
            Card maxPlayerCard = cards.get(cards.size() - 1);
            if (maxCard.compareTo(maxPlayerCard) < 0) {
                maxCard = maxPlayerCard;
                playerWithMaxCard = players.get(i);
            }
        }
        return playerWithMaxCard;
    }

    private Player getNextPlayingPlayer(Table g, Player playerSource) { // возвращает следующего по очереди игрока, который ещё в игре
        int playerSourceNumber = Integer.valueOf(playerSource.getNumber());
        List<Player> players = g.getPlayers();
        Player nextPlayer;
        if (playerSourceNumber == players.size()) {
            nextPlayer = searchNextPlayer(g, players, 0, players.size() - 1);
        } else if (playerSourceNumber == 1) {
            nextPlayer = searchNextPlayer(g, players, 1, players.size());
        } else {
            nextPlayer = searchNextPlayer(g, players, playerSourceNumber, players.size());
            if (nextPlayer == null) {
                nextPlayer = searchNextPlayer(g, players, 0, playerSourceNumber - 1);
            }
        }
        return nextPlayer;
    }

    private Player searchNextPlayer(Table g, List<Player> players, int start, int finish) {
        Player nextPlayer = null;
        for (int i = start; i < finish; i++) {
            if (isPlayerActive(g, players.get(i))) {
                nextPlayer = players.get(i);
                break;
            }
        }
        return nextPlayer;
    }

    /***/

    public Player playingRound(Table g, Round round, boolean isFirstRound) { // играем раунд
        Player source = round.getSource();
        Player target = round.getTarget();
        List<Battle> battles = new ArrayList<>();
        Player nextPlayer = null;
        int maxCountBattles = isFirstRound ? 5 : 6;

        int battleNumber = 0;
        System.out.println("-*-*-| " + (battleNumber) + " fight |-*-*-");
        // первый ход в раунде
        Card down = attackersTurn(g, round, source, battleNumber); // карта которой атакуют
        System.out.println(down.toString());
        Card up = defendersMove(g, target, round, down); // карта защищающ
        System.out.println(up.toString());
        if (up == null) {

        }
        while (battles.size() <= maxCountBattles && up != null) { // пока кол-во батлов не достиг макс и если атакер не сказал бито
            System.out.println("-*-*-| " + (battleNumber + 1) + " fight |-*-*-");
            down = attackersTurn(g, round, source, battleNumber);
            System.out.println(down.toString());
            if (down != null) { // если игрок сходил(не сказал бито)
                up = defendersMove(g, target, round, down); // тогда необходимо биться
                System.out.println(up.toString());
            } else { // сказал бито
                System.out.println("-*-*-*-*-*-*-*-*-*-\n" + source.toString() + " strayed");
                break;
            }
            battleNumber++;
        }
        // сохраняем батл
        Battle battle = new Battle(down, up);
        battles.add(battle);
        round.setFights(battles);

        // если защитник не побил
        if (!battle.isCovered()) {
            // то защитн берет
            System.out.println(target.toString() + " takes");
            // отмечаем это в раунде
            round.setPickedUp(false);
            // получаем все карты раунда
            List<Card> cardsInRound = getCardsInRound(round);
            for (Card card : cardsInRound) {
                addCard(g, target, card);
            }
            if (isGameActive(g)) {
                nextPlayer = getNextPlayingPlayer(g, target);
            } else {
                System.out.println(Console.playerCardsToString(g));
                return null;
                //игрок target проиграл
            }


        } else {
            System.out.println(up.toString());

            round.setPickedUp(true);
            if (isPlayerActive(g, target)) {
                nextPlayer = target;
            } else {
                nextPlayer = getNextPlayingPlayer(g, target);
            }
        }


        /***/   //игрок добирает карты до 6 (если ему это необходимо)
        if (source.getPlayersCards().size() < 6) {
            addCardToFull(g, source);
        }
        if (target.getPlayersCards().size() < 6) {
            addCardToFull(g, target);
        }
        /***/
        g.addRound(round);  //сохраняем раунд

        System.out.println(Console.playerCardsToString(g));
        System.out.println(g.getCards().size());
        return nextPlayer;
    }

    private List<Card> getCardsInRound(Round round) { // все карты за раунд
        List<Card> cardsInRound = new ArrayList<>();
        for (Battle fight : round.getFights()) {
            cardsInRound.add(fight.getDown());
            if (fight.isCovered()) {
                cardsInRound.add(fight.getUp());
            }
        }
        return cardsInRound;
    }

    /***/
    private void addCardToFull(Table g, Player player) { //добор карт игроком после раунда
        List<Card> cards = player.getPlayersCards();
        Stack<Card> deck = g.getCards();
        int n = 6 - cards.size(); // сколько карт не хватает
        for (int i = 0; i < n; i++) {
            if (deck.size() != 0) {
                addCard(g, cards, deck.pop());
            } else {
                break;
            }
        }
        player.setPlayersCards(cards);
    }

    public void addCard(Table g, Player player, Card card) {  //добавление карты игроку
        List<Card> cards = player.getPlayersCards();
        addCard(g, cards, card);
        player.setPlayersCards(cards);
    }

    public void addCard(Table g, List<Card> cards, Card card) {
        Masti trump = g.getTrumpCard().getSuit();

        int i = 0;
        if (card.getSuit() != trump) {
            while (i < cards.size() && cards.get(i).getSuit() != trump) {
                if (card.compareTo(cards.get(i)) <= 0) {
                    break;
                }
                i++;
            }
        } else {
            while (i < cards.size() && cards.get(i).getSuit() != trump) {
                i++;
            }
            while (i < cards.size()) {
                if (card.compareTo(cards.get(i)) <= 0) {
                    break;
                }
                i++;
            }
        }
        cards.add(i, card);
    }

    public Card attackersTurn(Table g, Round round, Player player, int battleNumber) {
        System.out.println(Console.getPlayerCardsForBattle(g, player));// ход атакующего (возвращает карту, которую надо будет побить)
        List<Card> cards = player.getPlayersCards();
        Card card = null;
        if (battleNumber != 0) {
            // попросить заменить на карту которую введут
            while (true) {//пока не появится карта, которая есть в раунде (которой можно ходить)
                int numberCard = Console.input();
                if (numberCard == -1) {
                    card = null;
                    break;
                }
                card = cards.get(numberCard);

                if (!round.nominalInRound(card.getRank())) { // если можно сходить данной картой, цикл стоп
                    cards.remove(numberCard);
                    break;
                }

            }
        }

        if (card == null) { //если card == null, то значит игрок не может сходить и автоматически бито
            round.setPickedUp(true);
        } else {
            round.addRank(card.getRank());
        }
        return card;
    }

    public Card defendersMove(Table g, Player player, Round round, Card down) {
        //ход защищающегося (возвращает карту, которой будем бить карту down)
        //ищем самую маленькую карту, которой можем побить карту down
        System.out.println(Console.getPlayerCardsForBattle(g, player));
        List<Card> cards = player.getPlayersCards();
        int numberCard = Console.input();
        Card cardForMove = null;

        boolean isTrump = (down.getSuit() == g.getTrumpCard().getSuit());       //карта, которую нужно побить
        //является козырем

        if (cards.get(numberCard).compareTo(down) > 0) { //>0, значит карта card больше по номиналу чем карта down
            if (cards.get(numberCard).getSuit() == down.getSuit()) {
                cardForMove = cards.get(numberCard);

            }
        }

        //может побить и козырной картой
        /***/
        if (!isTrump) {                                     //если карта, которую нужно побить не является козырем
            if (cards.get(numberCard).getSuit() == g.getTrumpCard().getSuit()) { //то мы можем побить её любой козырной картой
                cardForMove = cards.get(numberCard);                         //(самой маленькой)
            }
        }
        /***/


        if (cardForMove != null) {
            round.addRank(cardForMove.getRank());
            cards.remove(numberCard);
        }
        return cardForMove;
    }


    private boolean isGameActive(Table g) { // игра еще продолжается?
        int count = 0; // кол-во игроков
        for (Player p : g.getPlayers()) {
            if (!isPlayerActive(g, p)) {
                count++;
            }
        }
        if (count <= 1) {
            return true;
        }
        return false;
    }

    private boolean isPlayerActive(Table g, Player p) {  // игрок еще в игре?
        if (!p.getPlayersCards().isEmpty() || !isDeckEmpty(g)) {
            return true;
        }
        return false;
    }

    private boolean isDeckEmpty(Table g) {
        if (g.getCards().isEmpty()) {
            return true;
        }
        return false;
    }

    private Player getLastPlayer(Table g) {
        Player lastPlayer = null;
        for (int i = 0; i < g.getPlayers().size(); i++) {
            lastPlayer = g.getPlayers().get(i);
            if (isPlayerActive(g, lastPlayer)) {
                break;
            }
        }
        return lastPlayer;
    }
}
 // https://miro.com/app/board/uXjVPEkbv-Q=/