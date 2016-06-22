package download.butts.blackjack;

public enum Suit {
    Hearts, Spades, Clubs, Diamonds;

    @Override
    public String toString() {
        switch (this) {
            case Hearts:
                return "Hearts";
            case Spades:
                return "Spades";
            case Clubs:
                return "Clubs";
            case Diamonds:
                return "Diamonds";
        }
        throw new Error("Unreachable Code");
    }

    public String toShortString() {
        switch (this) {
            case Hearts:
                return "H";
            case Spades:
                return "S";
            case Clubs:
                return "C";
            case Diamonds:
                return "D";
        }
        throw new Error("Unreachable Code");
    }
    public String toSymbol() {
        switch (this) {
            case Hearts:
                return "\u2665";
            case Spades:
                return "\u2660";
            case Clubs:
                return "\u2663";
            case Diamonds:
                return "\u2666";
        }
        throw new Error("Unreachable Code");
    }
}