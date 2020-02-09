package blackjack;

public class Card {

    private String faceName, suit;
    private int faceValue;

    /**
     *
     * @param suit diamond, heart, spade, club
     * @param face 2,3,4,5,6,7,8,9,10,J,Q,K,A
     * @param value 2,3,4,5,6,7,8,9,10,10,10,10,1/11
     */
    public Card(String suit, String face, int value) { 
        this.suit = suit;
        this.faceName = face;
        this.faceValue = value;

    }

    public String toString() {
        return faceName + " of " + suit;
    }

    public int getFaceValue() {
        return faceValue;
    }
    
    public String getFaceName(){
        return faceName;
    }
        
    public String getSuit(){
        return suit;
    }
}
