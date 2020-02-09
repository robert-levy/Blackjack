/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Robert
 */
public class Blackjack {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Welcome player and get their name
        System.out.println("Welcome to Blackjack! Enter your name");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();

        //create the deck, players hand, dealers heand and player object and ask for bet
        Player player = new Player(name, 100);
        Deck deck = new Deck();
        System.out.println("Hello " + name);

        //start game
        while (true) {
            //check if need a new deck
            if (deck.checkReshuffle()) {
                System.out.println("Creating new deck ...");
                deck = new Deck();
            }

            //Ask player for bet
            System.out.println(". You have " + player.getBank() + " credits in your bank.");
            double bet = -9999;
            while (!player.verifyBet(player, bet)) {
                try {
                    bet = Double.parseDouble(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("Invalid input.");
                }
            }

            //deduct bet from players bank and create hands
            Hand playerHand = new Hand(deck, player);
            Hand dealerHand = new Hand(deck, player);
            player.makeBet(bet, playerHand);
            ArrayList<Hand> playerHands = new ArrayList<>();
            playerHands.add(playerHand);
            boolean showDealersCard = false;
            for (int i = 0; i < playerHands.size(); i++) {
                while (playerHands.get(i).getHandValue() < 21 && !playerHands.get(i).standing()) {
                    //Display hands
                    System.out.println("hand value: " + playerHands.get(i).getHandValue());
                    System.out.println("You have " + playerHands.get(i).getHand() + ". The dealer has " + dealerHand.revealFirstCard() + " and *****");

                    //Give the player there choices (e.g. hit, stand, split, double-down)
                    String choices = playerHands.get(i).displayChoices(player);
                    System.out.println(choices);

                    //Validate and fulfill choice
                    String choice = sc.nextLine();
                    if (!playerHands.get(i).validateChoice(choice, player)) {
                        System.out.println("Invalid input. Try again.");
                    } else {
                        playerHands.get(i).fulfillChoice(choice, player, deck, i);
                        if (playerHands.get(i).hasSplit()) {
                            playerHands = playerHands.get(i).split(player, deck, playerHands, playerHands.get(i));
                            i = 0;
                        }
                    }
                }
                //check for 21 in split (quick fix), busted, bj
                playerHands.get(i).checkHand();

                //check if a hand has not busted (means we need to check dealers hand)
                // also checking if player got blackjack straight away, dealer should not hit
                if (!playerHands.get(i).hasBusted() && !(playerHands.get(i).hasBlackjack()
                        && playerHands.size() == 1)) {
                    showDealersCard = true;
                }
            }
            //Only show dealers card if player hasn't busted all hands (need to let nat BJ in here)
            if (showDealersCard) {
                //Dealers turn. Show cards and hit to 17. REMOVE IF AND PUT CONDITION IN WHILE
                System.out.println("The dealer has " + dealerHand.getHand());
                while (dealerHand.getHandValue() < 17) {
                    dealerHand.dealersTurn(deck);
                    System.out.println("The dealer has " + dealerHand.getHand());
                }
                //for each hand object, check who won
                for (Hand hand : playerHands) {
                    if (hand.hasBusted()) {
                        System.out.println("You Busted. You lose.");
                        hand.setHandResult(-1);
                    } else if (dealerHand.getHandValue() > 21) {
                        System.out.println("The dealer busts. You win!");
                        hand.setHandResult(1);
                    } else if (hand.getHandValue() < dealerHand.getHandValue()) {
                        System.out.println("You lose");
                        hand.setHandResult(-1);
                    } else if (hand.getHandValue() > dealerHand.getHandValue()) {
                        System.out.println("You win!");
                        hand.setHandResult(1);
                    } else if (!hand.hasBlackjack() && dealerHand.hasBlackjack()) { //both have 21 but dealer has BJ
                        System.out.println("Dealer has blackjack. You lose.");
                        hand.setHandResult(-1);
                    } else if (hand.hasBlackjack() && !dealerHand.hasBlackjack()) {  //both have 21 but player has BJ
                        System.out.println("You win with natural blackjack"); //I NEVER GET HERE
                        hand.setHandResult(2);
                    } else if (hand.hasBlackjack() && dealerHand.hasBlackjack()) { //both have blackjack
                        System.out.println("Dealer has blackjack too. Push.");
                        hand.setHandResult(0);
                    } else if (hand.getHandValue() == dealerHand.getHandValue()) {
                        System.out.println("Push");
                        hand.setHandResult(0);
                    }
                } //only come here if natural blackjack (what if all hands are busted?)
            } else if (!playerHands.get(0).hasBusted()) { //avoid busted case. its getting in here (if all hands are busted)
                System.out.println("The dealer has " + dealerHand.getHand());
                for (Hand hand : playerHands) { //can replace with just indexing at 0

                    if (dealerHand.hasBlackjack()) {
                        System.out.println("Dealer has blackjack. Push");
                        hand.setHandResult(0);
                    } else {
                        System.out.println("You win.");
                        hand.setHandResult(2);
                    }
                }

            } else if (playerHands.get(0).hasBusted()) {
                //all hands have busted (work out the edge cases)
                
            }

            //Pay the player winnings
            player.payWinnings(playerHands);

            //check players credit. Gameover if 0
            if (player.getBank() == 0) {
                System.out.println("Gameover");
                break;
            }
        }
    }
}
