package com.company;

import com.company.JSwing.CenterPanel;

import java.util.*;

import static com.company.Round.playingRound;
import static com.company.Table.getNextPlayingPlayer;

public class Logic {

    public void play(Table t, Game g) {
        Player.dialCards(t);  //раздаем карты
        System.out.println((char) 27 + "[46m" + t.getTrumpCard() + (char) 27 + "[0m");
        System.out.println(Console.playerCardsToString(t));
        System.out.println(t.getCards().size());

        Player source = Game.getPlayerWhoMovedFirst(t);  // кто ходит первым
        System.out.print("---1 Round---\nPlaying " + source.toString());
        Player target = getNextPlayingPlayer(t, source);  // кто отбивает
        System.out.println(" and " + target.toString());

        Round firstRound = new Round(source, target);  // создаем 1ый раунд
        source = playingRound(t, firstRound, true, g);  // играем 1 раунд и возвращаем игрока, который ходит следующим
        target = getNextPlayingPlayer(t, source);   // находим игрока, который будет отбиваться следующим
        int i = 2;
        while (Game.isGameActive(t)) {
            System.out.println("---| " + i + " Round |---\nPlaying " + source.toString() + " and " + target.toString());
            Round round = new Round(source, target);   // создаем новый раунд
            source = playingRound(t, round, false, g);
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
        Player stupid = Table.getLastPlayer(t);
        if (stupid == null) {
            System.out.println("draw");
        } else {
            System.out.println("----| " + stupid + " lose |----");
        }
        System.out.println((char) 27 + "[31m|||---||| Game  end |||---|||" + (char) 27 + "[0m");
    }

    public static Card attackersMove(boolean isFirstBattle, Round round, Player source, Table t, Game g, int i) {
        StringBuilder sb = new StringBuilder();
        List<Card> cards = source.getPlayersCards();

        Card cardForMove = null;
        int action = 0;

        if (source.getNumber() == 1) {
            if (!isFirstBattle) sb.append("-1 - bito ");
            sb.append(Console.getPlayerCardsForBattle(t, source));
            System.out.println(sb);

            if (isFirstBattle) {
                System.out.println("Choose card:");
            } else {
                System.out.println("Choose card or say bito: ");
            }
            while (true) {
                action = Console.input();
                if (action == -1) {
                    if (isFirstBattle) {
                        System.out.println("You cannot say bito. Choose card: ");
                    } else {
                        break;
                    }
                } else if (checkAttCard(cards, action, round, isFirstBattle)) {
                    cardForMove = cards.get(action);
                    break;
                } else {
                    System.out.println("You cannot move this card. Choose another card:");
                }
            }
        } else {
            if (i == 0) {
                cardForMove = cards.get(i);
                action = i;
            } else {
                //сходить самой маленькой подходящей картой игрока, которая при этом не козырная
                for (int j = 0; j < cards.size(); j++) {
                    if (round.nominalInRound(cards.get(j).getRank())) {
                        cardForMove = cards.get(j);
                        action = j;
                        break;
                    }
                }
            }
            if (cardForMove == null) round.setPickedUp(true);
        }
        if (cardForMove != null) {
            round.addRank(cardForMove.getRank());
            cards.remove(action);
        }

        return cardForMove;
    }

    public static Card defendersMove(Table t, Player player, Round round, Card down, Game g) {
        System.out.println(Console.getPlayerCardsForBattle(t, player));
        List<Card> cards = player.getPlayersCards();
        int numberCard = 0;
        Card cardForMove = null;

        System.out.println("Choose card or say take: ");

        if (player.getNumber() == 1) {
            while (true) {
                numberCard = Console.input();

                if (numberCard == -1) break;

                if (checkDefCard(cards, numberCard, t, down)) {
                    cardForMove = cards.get(numberCard);
                    break;
                } else System.out.println("You cannot play with this card, please choose another:");
            }
        } else {
            boolean isTrump = (down.getSuit() == t.getTrumpCard().getSuit());       //карта, которую нужно побить
            //является козырем
            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i).compareTo(down) > 0) { //>0, значит карта card больше по номиналу чем карта down
                    if (cards.get(i).getSuit() == down.getSuit()) {
                        cardForMove = cards.get(i);
                        numberCard = i;
                        break;
                    }
                }

                //может побить и козырной картой
                if (!isTrump) {                                     //если карта, которую нужно побить не является козырем
                    if (cards.get(i).getSuit() == t.getTrumpCard().getSuit()) { //то мы можем побить её любой козырной картой
                        cardForMove = cards.get(i);                         //(самой маленькой)
                        numberCard = i;
                        break;
                    }
                }

            }
        }

        if (cardForMove != null) {
            round.addRank(cardForMove.getRank());
            cards.remove(numberCard);
        }
        return cardForMove;
    }

    private static boolean checkDefCard(List<Card> cards, int numberCard, Table t, Card down) {
        if (cards.get(numberCard).compareTo(down) > 0) {
            if (cards.get(numberCard).getSuit() == down.getSuit()) {
                return true;
            }
        }

        if (down.getSuit() != t.getTrumpCard().getSuit()) {
            if (cards.get(numberCard).getSuit() == t.getTrumpCard().getSuit()) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkAttCard(List<Card> cards, int numberCard, Round round, boolean isFirstBattle) {
        if (round.nominalInRound(cards.get(numberCard).getRank()) || isFirstBattle) {
            return true;
        }
        return false;
    }
}