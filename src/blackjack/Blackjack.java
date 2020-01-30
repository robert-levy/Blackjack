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
            player.makeBet(bet);
            Hand playerHand = new Hand(deck);
            Hand dealerHand = new Hand(deck);

            //while < 21 or player hasn't stood or doubled down, repeat steps
            while (playerHand.getHandValue() < 21 && !playerHand.standing()) {
                System.out.println("hand value: " + playerHand.getHandValue());
                System.out.println("You have " + playerHand.getHand() + ". The dealer has " + dealerHand.revealFirstCard() + " and *****");

                //Give the player there choices (e.g. hit, stand, split, double-down)
                String choices = playerHand.displayChoices(player);
                System.out.println(choices);

                //Validate and fulfill choice
                String choice = sc.nextLine();
                if (!playerHand.validateChoice(choice, player)) {
                    System.out.println("Invalid input. Try again.");
                } else {
                    playerHand.fulfillChoice(choice, player, deck);
                    //break out if a split occured
                }
            }
            
            //Check for busted, blackjack, 21
            if (playerHand.getHandValue() > 21) {
                playerHand.setBusted(true);
                System.out.println("You busted with " + playerHand.getHand());
            } else if (playerHand.hasBlackjack()) {
                System.out.println("BLACKJACK! " + playerHand.getHand());
            } else if (playerHand.getHandValue() == 21) {
                System.out.println("You got 21 with " + playerHand.getHand());
            }
            
            //Dealers turn. Reveal hidden card and keeping hitting until 17 or bust
            if (!playerHand.hasBusted()) {
                System.out.println("The dealer has " + dealerHand.getHand());
                //check if player has BJ. If so only check two of the dealers card. No need to draw more
                if (playerHand.hasBlackjack()) {
                    if (dealerHand.hasBlackjack()) {
                        System.out.println("Push. Dealer has blackjack");
                        playerHand.returnBet(player);
                    } else {
                        System.out.println("You win!");
                        playerHand.payWinnings(player); //change to include makes odds 1.5 instead of 1:1
                    }
                } else {
                    while (dealerHand.getHandValue() < 17) {
                        dealerHand.dealersTurn(deck);
                        System.out.println("The dealer has " + dealerHand.getHand());
                    }
                    
                    //check who won
                    if (dealerHand.getHandValue() > 21) {
                        System.out.println("The dealer busts. You win!");
                        playerHand.payWinnings(player);
                    } else if (playerHand.getHandValue() > dealerHand.getHandValue()) {
                        System.out.println("You win!");
                        playerHand.payWinnings(player);
                    } else if (playerHand.getHandValue() < dealerHand.getHandValue() && dealerHand.getHand().size() == 2 && dealerHand.getHandValue() == 21) {
                        System.out.println("Dealer has blackjack. You lose.");
                    } else if (playerHand.getHandValue() < dealerHand.getHandValue()) {
                        System.out.println("You lose");
                    } else if (playerHand.getHandValue() == dealerHand.getHandValue()) {
                        System.out.println("Push");
                        playerHand.returnBet(player);
                    }
                }
            }
            //check players credit. Gameover if 0
            if (player.getBank() == 0) {
                System.out.println("Gameover");
                break;
            }
        }
    }
}
