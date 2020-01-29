/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import java.util.ArrayList;

/**
 *
 * @author Robert
 */
public class Dealer {

    private final String dealer = "dealer";
    ArrayList<Card> cards = new ArrayList<>(); //stores dealers cards

    public Dealer(Deck deck) {
        hit(deck);
        hit(deck);
    }

    public void hit(Deck deck) {
        cards.add(deck.takeCard());
    }

    public String revealFirstCard() { //can change to just Card
        String firstCard = cards.get(0).getFaceValue() + " of " + cards.get(0).getSuit();
        return firstCard;
    }

    public ArrayList revealCards() {
        return cards;
    }
    
    public int getHandValue(){
        int value = 0;
        for (Card card : cards) {
            value += card.getFaceValue();
        }
        return value;
    }
    
//    @Override
//    public String toString(){
//        StringBuilder sb = new StringBuilder();
//        for (Card card : cards) {
//            sb.append(card.getFaceName() + " of " + card.getSuit());
//        }
//        return sb.toString();
//    }

    public String playerOrDealer() {
        return dealer;
    }

}
