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
            Hand playerHand = new Hand(deck);
            Hand dealerHand = new Hand(deck);
            player.makeBet(bet, playerHand);
            ArrayList<Hand> playerHands = new ArrayList<>();
            playerHands.add(playerHand);
            boolean showDealersCard = false;

            //If dealer has BJ and player doesn't, go to next round
            if (dealerHand.hasBlackjack() && !playerHand.hasBlackjack()) {
                System.out.println("You have " + playerHand.getHand() + ". The dealer has " + dealerHand.getHand() + "\n Dealer has Blackjack. You lose.");
                //check players credit. Gameover if 0
                if (player.getBank() == 0) {
                    System.out.println("Gameover");
                    break;
                }
                //reset player bets and winnings
                player.resetBets();
                continue;
            }

            //Loop through each hand (splitting causes multiple hands)
            for (int i = 0; i < playerHands.size(); i++) {
                //While hand hasn't busted and players doesn't want to stand
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
                        //If player chose to split, must pass extra arguments and change index to first split hand
                        if (playerHands.get(i).hasSplit()) {
                            playerHands = playerHands.get(i).split(deck, playerHands, playerHands.get(i));
                            i = 0; // Go through hands again, look at first split hand
                        }
                    }
                }
                //check for 21, busted, bj
                playerHands.get(i).checkHand();

                //check if a hand has not busted (means we need to check dealers hand)
                // also checking if player got blackjack straight away, dealer should not hit
                if (!playerHands.get(i).hasBusted() && !(playerHands.get(i).hasBlackjack()
                        && playerHands.size() == 1)) {
                    showDealersCard = true;
                }
            }

            //Only show dealers card if player hasn't busted all hands
            if (showDealersCard) {
                //Dealers turn. Show cards and hit to 17. REMOVE IF AND PUT CONDITION IN WHILE
                System.out.println("The dealer has " + dealerHand.getHand());
                while (dealerHand.getHandValue() < 17) {
                    dealerHand.dealersTurn(deck);
                    System.out.println("The dealer has " + dealerHand.getHand());
                }
                if (dealerHand.hasBlackjack()) {
                    System.out.println("Dealer has Blackjack");
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
                    } else if (hand.hasBlackjack() && dealerHand.hasBlackjack()) { //both have blackjack
                        System.out.println("Dealer has blackjack too. Push.");
                        hand.setHandResult(0);
                    } else if (hand.getHandValue() == dealerHand.getHandValue()) {
                        System.out.println("Push");
                        hand.setHandResult(0);
                    }
                }
                //only come here if natural blackjack or all hands have busted
                //avoid busted case (only need to check first hand) 
            } else if (!playerHands.get(0).hasBusted()) {
                System.out.println("The dealer has " + dealerHand.getHand());
                if (dealerHand.hasBlackjack()) {
                    System.out.println("Dealer has blackjack. Push");
                    playerHands.get(0).setHandResult(0);
                } else {
                    System.out.println("Blackjack. You win.");
                    playerHands.get(0).setHandResult(2);
                }
            } else {
                //all hands have busted
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
