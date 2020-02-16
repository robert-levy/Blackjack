package blackjack;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author Robert
 */
public class Player {

    private double bank = 0, //players bank roll
            lastBet = 0, //the initial bet placed at the start of the round
            bet = 0, //the current bet (including doubles downs and splits)
            winnings = 0;   //the amount of winnings the player has at the end
    private ArrayList<Double> bets = new ArrayList<>(); //holds bets within round
    private final String name;

    /**
     *
     * @param name name of the player
     * @param startAmount the money the player starts with
     */
    public Player(String name, double startAmount) {
        this.name = name;
        this.bank = startAmount;
    }

    /**
     *
     * @param bet amount player bets on hand
     * @return a Hand object which deals the player two cards
     */
    public void makeBet(double bet, Hand playerHand) {
        lastBet = bet; //store initial bet 
        this.bet = bet; //store current total bet
        bank = bank - bet; //deduct bet amount from bank
        //add bet to bets array
        bets.add(bet);
    }

    /**
     *
     * @param bet players bet
     * @return true if bet is valid
     */
    public boolean verifyBet(Player player, double bet) {
        if (bet == -9999) {
            return false;
        } else if (bet <= 0) {
            System.out.println("Bet must be greater than 0");
            return false;
        } else if (BigDecimal.valueOf(bet).scale() > 1) {
            System.out.println("Only one decimal place allowed");
            return false;
        } else if (bet > player.getBank()) {
            System.out.println("Bet is greater than amount in bank");
            return false;
        }
        return true;
    }

    /**
     * This method pays the player their winnings
     *
     * @param hands ArrayList of hands from last round
     */
    public void payWinnings(ArrayList<Hand> hands) {
        //loop through bets and hands and pay necessary winnings
        if (hands.size() != bets.size()) {
            System.out.println("they don't match"); //TESTING PURPOSES
        }
        for (int i = 0; i < hands.size(); i++) {
            if (hands.get(i).getHandResult() == 0) {
                //push
                setBank(getBank() + bets.get(i));
                winnings += bets.get(i);
            } else if (hands.get(i).getHandResult() == 1) {
                //player wins
                setBank(getBank() + bets.get(i) * 2);
                winnings += bets.get(i) * 2;
            } else if (hands.get(i).getHandResult() == 2) {
                //player got natural blackjack
                setBank(getBank() + bets.get(i) + bets.get(i) + bets.get(i) / 2);
                winnings += bets.get(i) * 2.5;
            }
        }

        System.out.println("winnings: " + winnings);

        //reset for next game
        winnings = 0;
        bets = new ArrayList<Double>();
    }

    /**
     * Double current bet on hand
     */
    public void doubleDown(int index) {
        bank = bank - lastBet;
        bet = bet + lastBet;
        //double bet in array (get the right index. They should match playerHands array in Blackjack class)
        bets.set(index, bets.get(index) * 2);
    }

    public void split(int index) {
        bank = bank - lastBet;
        bet = bet + lastBet;
        //create new bet in array with lastBet (must index in right place)
        bets.add(index, lastBet);
    }
    
    /**
     * created for when round is skipped due to dealer BJ. resets bets for next round.
     */
    public void resetBets(){
        //reset for next game
        winnings = 0;
        bets = new ArrayList<Double>();
    }

    // @return amount of credit in bank
    public double getBank() {
        return bank; 
    }

    // @return the last bet placed (before cards are dealt)
    public double getLastBet() {
        return lastBet;
    }

    // @return the current stake of the bet
    public double getBet() {
        return bet;
    }

    // @param value value to set bank roll
    public void setBank(double value) {
        bank = value;
    }
}
