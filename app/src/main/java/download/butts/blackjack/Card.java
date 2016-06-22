package download.butts.blackjack;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Card {

    private Suit suit;
    private int  face;

    public Card() {
        Random random = new Random();
        face = random.nextInt((13 - 1) + 1) + 1;
        suit = Suit.values()[random.nextInt(4)];
    }
    public Card(Suit suit, int face) {
        this.suit = suit;
        this.face = face;
    }


    public String convertFace() {
        switch (this.face) {
            case 13:
                return "K";
            case 12:
                return "Q";
            case 11:
                return "J";
            case 1:
                return "A";
            default:
                return String.valueOf(this.face);
        }
    }

    public String toString() {
        return this.toString(false);
    }
    public String toString(boolean symbolize) {
        return String.format("%s%s", (symbolize) ? this.suit.toSymbol() : this.suit.toShortString(), this.convertFace());
    }


    public int getFace() {
        return face;
    }

    public static String renderStack(ArrayList<Card> cards) {
        String output = "";
        for (Card card : cards) {
            output += card.suit.toSymbol();
            output += card.convertFace();
            output += " ";
        }
        return output;
    }
}
