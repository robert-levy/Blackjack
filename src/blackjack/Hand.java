package blackjack;

import java.util.ArrayList;

/**
 *
 * @author Robert
 */ 
public class Hand {

    private boolean standing = false, split = false, busted = false, hasBlackjack;
    ArrayList<Card> cards = new ArrayList<>(); //stores players cards
    int handResult = -9999; // -1=lost, 0=push, 1=win, 2=BJ

    /**
     * Constructor called to start a new hand
     * 
     * @param deck 
     */
    public Hand(Deck deck) {
        //take two cards from the Deck
        cards.add(hit(deck));
        cards.add(hit(deck));
        //check for blackjack
        checkBlackjack();
    }

    /**
     * Constructor used when splitting a hand.
     * 
     * @param deck holds card objects
     * @param card card from prior hand split
     */
    public Hand(Deck deck, Card card) {
        //add card from initial split
        cards.add(card);
        //add second card from deck
        cards.add(hit(deck));
    }

    /**
     * Used by the dealer to reveal the first card
     * @return first card in cards ArrayList
     */
    public Card revealFirstCard() {
        return cards.get(0);
    }

    /**
     * Shows hand
     * @return ArrayList of card objects
     */
    public ArrayList getHand() {
        return cards;
    }

    /**
     * Adds the value of every card in cards ArrayList
     * @return the numeric value of the hand
     */
    public int getHandValue() {
        int aceFound = 0;
        int value = 0;
        for (Card card : cards) {
            value += card.getFaceValue();
            if (card.getFaceValue() == 11) {
                aceFound++;
            }
        }
        //Check for aces and change them to a value of 1 if over 21
        while (aceFound > 0 && value > 21) {
            value -= 10;
            aceFound--;
        }

        return value;
    }

    /**
     * @return string representation of card objects in cards ArrayList
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.getFaceName()).append(" of ").append(card.getSuit() + ".. ");
        }
        return sb.toString();
    }

    /**
     * This method tells the player what actions they can do
     * 
     * @player player object used to check bank balance
     * @return string explaining the players choices (e.g.
     * hit,stand,split,double-down)
     */
    public String displayChoices(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append("Options: \n");
        sb.append("hit \n");
        if (cards.size() == 2 && player.getBank() >= player.getLastBet()) {
            sb.append("double down \n");
        }

        sb.append("stand \n");
        //Split
        if (cards.get(0).getFaceName().equals(cards.get(1).getFaceName()) && cards.size() == 2
                && player.getBank() >= player.getLastBet()) {
            sb.append("split");
        }

        return sb.toString();
    }

    /**
     * Checks user made a valid choice. E.g. not doubling down after hitting
     * first.
     *
     * @param choice user entered action. E.g. Hit, stand, etc
     * @param player player object incase their bank needs to be checked
     * @return true if choice is valid, else false
     */
    public boolean validateChoice(String choice, Player player) {
        switch (choice.toLowerCase()) {
            //if current hand is under 21 (unecessary. Is already checked inside main method)
            case "hit":
                if (getHandValue() < 21) {
                    return true;
                }
            //can always stand
            case "stand":
                return true;

            //if both players cards are the same
            case "split":
                if (cards.get(0).getFaceName().equals(cards.get(1).getFaceName()) && cards.size() == 2) {
                    return true;
                }
            //if players balance is high enough
            case "double down":
                if (player.getBank() >= player.getBet() && cards.size() == 2) {
                    return true;
                }
        }
        //no matching choice
        return false;
    }

    /**
     * Performs action player chose.
     *
     * @param choice hit,stand,split,double down
     * @param player player object, used to increase bets when splitting or double downing
     * @param deck deck object
     * @param index location of hand in playerHands array in Blackjack class
     */
    public void fulfillChoice(String choice, Player player, Deck deck, int index) {
        switch (choice.toLowerCase()) {
            case "hit":
                cards.add(hit(deck));
                break;
            case "stand":
                standing = true;
                break;
            case "split":
                player.split(index);
                split = true;
                break;
            case "double down":
                player.doubleDown(index);
                cards.add(hit(deck));
                System.out.println("You double downed on " + getHand());
                standing = true;
                break;
        }
    }

    /**
     * Checks whether hand has busted, BJ or 21
     */
    public void checkHand() {
        if (getHandValue() > 21) {
            setBusted(true);
            System.out.println("You busted with " + getHand());
        } else if (hasBlackjack()) {
            System.out.println("BLACKJACK! " + getHand());
        } else if (getHandValue() == 21) {
            System.out.println("You got 21 with " + getHand());
        }
    }

    /**
     * Checks whether a hand is a blackjack
     */
    public void checkBlackjack() {
        if (cards.get(0).getFaceValue() + cards.get(1).getFaceValue() == 21) {
            hasBlackjack = true;
        } else {
            hasBlackjack = false;
        }
    }

    
     // @return true if the hand is a blackjack 
    public boolean hasBlackjack() {
        return hasBlackjack;
    }

    public void setBusted(boolean value) {
        busted = value;
    }

    // @return -1:lost, 0:push, 1:win, 2:BJ
    public int getHandResult() {
        return handResult;
    }

    // sets result of hand
    public void setHandResult(int value) {
        handResult = value;
    }

    // @return true if hand has busted
    public boolean hasBusted() {
        return busted;
    }

     // @return true if player decides to stand
    public boolean standing() {
        return standing;
    }

    /**
     * Called by player to get another card
     *
     * @param deck deck object
     * @return a new card
     */
    public Card hit(Deck deck) {
        //take a card from the deck and return the value
        return deck.takeCard();
    }

    /**
     * Called by the dealer until hand value is > 16
     *
     * @param deck deck object
     */
    public void dealersTurn(Deck deck) {
        cards.add(deck.takeCard());
    }

    // @return true if hand has been split
    public boolean hasSplit() {
        return split;
    }

    /**
     * Method handles split logic of a hand
     * 
     * @param deck deck object
     * @param playerHands ArrayList containing hands during current round
     * @param currentHand hand object that is being played now
     * @return ArrayList containing the two new hands from a split
     */
    public ArrayList split(Deck deck, ArrayList playerHands, Hand currentHand) {

        //create two new hand objects 
        Hand firstSplit = new Hand(deck, cards.get(0));
        Hand secondSplit = new Hand(deck, cards.get(1));

        int index = playerHands.indexOf(currentHand);
        playerHands.remove(currentHand); //remove this hand (this object)
        playerHands.add(index, firstSplit);
        playerHands.add(index + 1, secondSplit);

        return playerHands;
    }
}