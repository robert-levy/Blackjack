/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Robert
 */
public class Deck { 

    private ArrayList<Card> deck = new ArrayList<>(); //deck of cards
    private String[] suits = {"Clubs", "Diamonds", "Spades", "Hearts"};
    private String[] faces = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11}; //ace is hardcoded at 11

    /**
     * Constructor that creates a deck of cards
     */
    public Deck() {
        //fill up deck with 8 * 52 cards, 
        for (int x = 0; x < 8; x++) {
            int suit = 0;
            for (int i = 1; i <= 4; i++) {
                for (int j = 0; j < 13; j++) {
                    //a card needs a suit, face, value
                    deck.add(new Card(suits[suit], faces[j], values[j]));
                }
                suit++;
            }
        }
        //Shuffle deck
        shuffle();
    }

    //FOR TESTING PURPOSES
    public Deck(int i) {

        //
        deck.add(new Card(suits[0], faces[8], values[8])); 
        deck.add(new Card(suits[0], faces[8], values[8])); 
        deck.add(new Card(suits[0], faces[8], values[8])); 
        deck.add(new Card(suits[0], faces[2], values[2])); 
        deck.add(new Card(suits[0], faces[8], values[8]));
        deck.add(new Card(suits[0], faces[7], values[7]));
        deck.add(new Card(suits[0], faces[12], values[12]));
        deck.add(new Card(suits[0], faces[8], values[8]));
        deck.add(new Card(suits[0], faces[8], values[8]));
        deck.add(new Card(suits[0], faces[8], values[8]));

    }

    /**
     *
     * @return take a card from the front of deck
     */
    public Card takeCard() {
        return deck.remove(0);
    }

    /**
     * shuffle deck
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }

    /**
     * Unused at the moment
     *
     * @return true when deck is half empty
     */
    public boolean checkReshuffle() {
        if (deck.size() <= 8 * 52 / 2) {
            return true;
        }
        return false;
    }

    /**
     *
     * @return string representation of deck (should probably use StringBuilder
     * instead)
     */
    @Override
    public String toString() {
        String result = "";
        for (Card card : deck) {
            result += card.toString() + " \n";
        }
        return result;
    }
}
