package com.company;

import java.util.*;

public class Logic {

    public void addCardsInGame(Table g) {
        ArrayList<Card> allCards = new ArrayList<>();
        Stack<Card> cards = new Stack<>();
        for (CardsNumberAndPower rank : CardsNumberAndPower.values()) {
            for (Mast suit : Mast.values()) {
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

    public void addPlayersInGame(Table g, int playersCount) {
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= playersCount; i++) {
            players.add(new Player(Integer.toString(i)));
        }
        g.setPlayers(players);
    }

    public void play(Table t) {
        dialCards(t);  //раздаем карты
        System.out.println((char) 27 + "[46m" + t.getTrumpCard() + (char) 27 + "[0m");
        System.out.println(Console.playerCardsToString(t));
        System.out.println(t.getCards().size());

        Player source = getPlayerWhoMovedFirst(t);  // кто ходит первым
        System.out.print("---1 Round---\nPlaying " + source.toString());
        Player target = getNextPlayingPlayer(t, source);  // кто отбивает
        System.out.println(" and " + target.toString());

        Round firstRound = new Round(source, target);  // создаем 1ый раунд
        source = playingRound(t, firstRound, true);  // играем 1 раунд и возвращаем игрока, который ходит следующим
        target = getNextPlayingPlayer(t, source);   // находим игрока, который будет отбиваться следующим
        int i = 2;
        while (isGameActive(t)) {
            System.out.println("---| " + i + " Round |---\nPlaying " + source.toString() + " and " + target.toString());
            Round round = new Round(source, target);   // создаем новый раунд
            source = playingRound(t, round, false);
            t.addRound(round);  // сохраняем инфрмацию о раунде
            // играем раунд и возвращаем игрока, который ходит следующим
            if (source == null) {
                break;
            }
            target = getNextPlayingPlayer(t, source);   // находим игрока, который будет отбиваться следующим
            if (target == null) {
                break;
            }
            i++;
        }
        Player stupid = getLastPlayer(t);
        if (stupid == null) {
            System.out.println("draw");
        } else {
            System.out.println("----| " + stupid + " lose |----");
        }
        System.out.println((char) 27 + "[31m|||---||| Game  end |||---|||" + (char) 27 + "[0m");
    }

    private void dialCards(Table t) {   // раздача карт
        Map<Player, List<Card>> playersCards = new HashMap<>(); // лист с картами всех игорков

        for (Player p : t.getPlayers()) {
            List<Card> pc = new ArrayList<>();  // карты p-го игрока
            for (int i = 0; i < 6; i++) {
                pc.add(t.getCards().pop());
            }
            Collections.sort(pc);

            pc = addTrumpsToEnd(pc, t.getTrumpCard().getSuit());

            playersCards.put(p, pc);
            p.setPlayersCards(pc);// его карты сохранятся в лист с картами всех игроков
        }
    }

    private List<Card> addTrumpsToEnd(List<Card> cards, Mast trump) { //перестевляем козыри в конец
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

    private Player getPlayerWhoMovedFirst(Table t) { // кто ходит первым
        Card currentTrump = t.getTrumpCard(); // текущий козырь
        Player playerWhoMovedFirst = null;

        if (currentTrump.getRank().getPower() > 10) {
            playerWhoMovedFirst = searchMinTrumpInGame(t, t.getTrumpCard().getSuit());
            System.out.println("The youngest trump card is " + playerWhoMovedFirst.toString() + ", so he goes first");
        } else {
            playerWhoMovedFirst = searchMaxTrumpInGame(t, t.getTrumpCard().getSuit());
            System.out.println("The most senior trump card is " + playerWhoMovedFirst.toString() + ", so he goes first");
        }
        if (playerWhoMovedFirst == null) {
            playerWhoMovedFirst = searchMaxCardInGame(t);
            System.out.println("The players have no trumps in their hands, the highest card is" + playerWhoMovedFirst);
        }
        return playerWhoMovedFirst;
    }

    private Player searchMaxTrumpInGame(Table t, Mast currentTrump) {
        Player playerWithMaxTrump = null;  // номер игрока с самым большим козырем
        Card maxTrumpInGame = null;   // самый старший козырь из карт в игре
        for (int i = 0; i < t.getPlayers().size(); i++) {
            Player iPlayer = t.getPlayers().get(i);  // i-ый игрок
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

    private Player searchMinTrumpInGame(Table t, Mast currentTrump) { // search - поиск
        Player playerWithMinTrump = null;   // игрок с самым маленьким козырем
        Card minTrumpInGame = null;   // самый младший козырь из карт в игре
        for (int i = 0; i < t.getPlayers().size(); i++) {
            Player iPlayer = t.getPlayers().get(i);   // i-ый игрок
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

    private Player searchMaxCardInGame(Table t) {
        List<Player> players = t.getPlayers();
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

    private Player getNextPlayingPlayer(Table t, Player playerSource) { // возвращает следующего по очереди игрока, который ещё в игре
        int playerSourceNumber = Integer.valueOf(playerSource.getNumber());
        List<Player> players = t.getPlayers();
        Player nextPlayer;
        if (playerSourceNumber == players.size()) {
            nextPlayer = searchNextPlayer(t, players, 0, players.size() - 1);
        }  else {
            nextPlayer = searchNextPlayer(t, players, playerSourceNumber, players.size());
        }
        if (nextPlayer == null) {
            nextPlayer = searchNextPlayer(t, players, 0, playerSourceNumber - 1);
        }
        return nextPlayer;
    }

    private Player searchNextPlayer(Table t, List<Player> players, int start, int finish) {
        Player nextPlayer = null;
        for (int i = start; i < finish; i++) {
            if (isPlayerActive(t, players.get(i))) {
                nextPlayer = players.get(i);
                break;
            }
        }
        return nextPlayer;
    }

    public Player playingRound(Table t, Round round, boolean isFirstRound) { // играем раунд
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

        Card down = attackersMove(true, round, source, t);
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
            up = defendersMove(t, target, round, down);
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
            down = attackersMove(false, round, source, t);
        }

        nextPlayer = getNextPlayer(t, round, battle);

        t.addRound(round);  //сохраняем раунд

        // выводим карты игроков и размер колоды
        System.out.println(Console.playerCardsToString(t));
        System.out.println(t.getCards().size());

        // возвращаем следующего игрока
        return nextPlayer;
    }

    private Player getNextPlayer(Table t, Round r, Battle b) {
        if (!b.isCovered()) {
            System.out.println(r.getTarget().toString() + " takes");
            r.setPickedUp(false);
            List<Card> cardsInRound = getCardsInRound(r);
            for (Card card : cardsInRound) {
                addCardToPlayer(t, r.getTarget(), card);
            }

            addCardToFull(t, r.getSource());

            if (isGameActive(t)) return getNextPlayingPlayer(t, r.getTarget());

            System.out.println(Console.playerCardsToString(t));
            return null;

        } else {

            System.out.println(b.defendCard().toString());

            r.setPickedUp(true);


            addCardToFull(t, r.getSource());
            addCardToFull(t, r.getTarget());

            if (isPlayerActive(t, r.getTarget())) {
                return r.getTarget();
            } else {
                return getNextPlayingPlayer(t, r.getTarget());
            }
        }
    }

    private List<Card> getCardsInRound(Round round) {
        List<Card> cardsInRound = new ArrayList<>();
        for (Battle battle : round.getBattles()) {
            cardsInRound.add(battle.getAttackCard());
            if (battle.isCovered()) {
                cardsInRound.add(battle.getDefendCard());
            }
        }
        return cardsInRound;
    }

    private void addCardToFull(Table t, Player player) {
        if (player.getPlayersCards().size() > 6) return;

        List<Card> cards = player.getPlayersCards();
        Stack<Card> deck = t.getCards();
        int n = 6 - cards.size();
        for (int i = 0; i < n; i++) {
            if (deck.size() != 0) {
                addCardToPlayersList(t, cards, deck.pop());
            } else {
                break;
            }
        }
        player.setPlayersCards(cards);
    }

    public void addCardToPlayer(Table t, Player player, Card card) {  //добавление карты игроку
        List<Card> cards = player.getPlayersCards();
        addCardToPlayersList(t, cards, card);
        player.setPlayersCards(cards);
    }

    private void addCardToPlayersList(Table t, List<Card> cards, Card card) {
        Mast trump = t.getTrumpCard().getSuit();

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

    private Card attackersMove(boolean isFirstBattle, Round round, Player source, Table t) {
        StringBuilder sb = new StringBuilder();

        List<Card> cards = source.getPlayersCards();
        if (!isFirstBattle) sb.append("-1 - bito ");
        sb.append(Console.getPlayerCardsForBattle(t, source));
        System.out.println(sb);

        if (isFirstBattle) {
            System.out.println("Choose card:");
        } else {
            System.out.println("Choose card or say bito: ");
        }

        Card cardForMove = null;
        int action;

        while (true) {
            action = Console.input();
            if (action == -1) {
                if (isFirstBattle) {
                    System.out.println("You cannot say bito. Choose card: ");
                } else {
                    break;
                }
            } else if (round.nominalInRound(cards.get(action).getRank()) || isFirstBattle) {
                cardForMove = cards.get(action);
                break;
            } else {
                System.out.println("You cannot move this card. Choose another card:");
            }
        }

        if (cardForMove != null) {
            round.addRank(cardForMove.getRank());
            cards.remove(action);
        }

        return cardForMove;
    }

    public Card defendersMove(Table t, Player player, Round round, Card down) {
        System.out.println(Console.getPlayerCardsForBattle(t, player));
        List<Card> cards = player.getPlayersCards();
        int numberCard;
        Card cardForMove = null;


            System.out.println("Choose card or say take: ");

        while (true) {
            numberCard = Console.input();

            if (numberCard == -1) break;

            if (cards.get(numberCard).compareTo(down) > 0) {
                if (cards.get(numberCard).getSuit() == down.getSuit()) {
                    cardForMove = cards.get(numberCard);
                    break;
                }
            }

            if (down.getSuit() != t.getTrumpCard().getSuit()) {
                if (cards.get(numberCard).getSuit() == t.getTrumpCard().getSuit()) {
                    cardForMove = cards.get(numberCard);
                    break;
                }
            }
        }

        if (cardForMove != null) {
            round.addRank(cardForMove.getRank());
            cards.remove(numberCard);
        }

        return cardForMove;
    }


    private boolean isGameActive(Table t) {
        int count = 0;
        for (Player p : t.getPlayers()) {
            if (isPlayerActive(t, p)) {
                count++;

                if (count > 1) return true;
            }
        }
        return false;
    }

    private boolean isPlayerActive(Table t, Player p) {
        return !p.getPlayersCards().isEmpty() || !isDeckEmpty(t);
    }

    private boolean isDeckEmpty(Table t) {
        return t.getCards().isEmpty();
    }

    private Player getLastPlayer(Table t) {
        for (int i = 0; i < t.getPlayers().size(); i++) {
            Player lastPlayer = t.getPlayers().get(i);
            if (isPlayerActive(t, lastPlayer)) return lastPlayer;
        }
        return null;
    }
}