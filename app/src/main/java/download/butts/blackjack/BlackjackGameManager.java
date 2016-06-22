package download.butts.blackjack;

import java.util.ArrayList;

public class BlackjackGameManager {
    public enum GameResult {
        Doubled, Won, Neutral, Lost
    }

    protected ArrayList<Card> playerHand;
    protected ArrayList<Card> dealerHand;
    protected int playerTotal;
    protected int dealerTotal;
    protected boolean playerBusted = false;
    protected boolean dealerBusted = false;
    protected boolean gameOver = false;

    public BlackjackGameManager() {
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        hit();
        hit();
        dealerHand.add(new Card());
        dealerHand.add(new Card());
        dealerTotal = calculateTotal(dealerHand);
    }

    public static int calculateTotal(ArrayList<Card> cards) {
        int total = 0;
        int aces = 0;
        //Sort through the cards. We separate out aces for now.
        for (Card card : cards) {
            int faceValue = card.getFace();
            if (faceValue == 1) {
                aces++;
            } else if (faceValue > 10) {
                total += 10;
            } else {
                total += faceValue;
            }
        }
        //Add the aces in
        if (aces > 0) {
            for (; aces>0; aces--) {
                if ((total+11) > 21) {
                    total += 1;
                } else {
                    total += 11;
                }
            }
        }
        return total;
    }

    public static int countAces(ArrayList<Card> cards) {
        int aces = 0;
        for (Card card : cards) {
            if (card.getFace() == 1) {
                aces++;
            }
        }
        return aces;
    }

    public void hit() {
        playerHand.add(new Card());
        playerTotal = calculateTotal(playerHand);
        playerBusted = playerTotal > 21;
    }

    public GameResult doubleDown() {
        hit();
        GameResult result = stay();
        return (result == GameResult.Won) ? GameResult.Doubled : result;
    }

    public GameResult stay() {
        gameOver = true;
        if (playerBusted) {
            return GameResult.Lost;
        }
        dealerTurn();
        if (dealerBusted) {
            return GameResult.Won;
        } else if (playerTotal > dealerTotal) {
            return GameResult.Won;
        } else if (dealerTotal == playerTotal){
            return GameResult.Neutral;
        } else {
            return GameResult.Lost;
        }
    }

    // Stay at 18
    public void dealerTurn() {
        dealerTotal = calculateTotal(dealerHand);
        while (dealerTotal < 18) {
            dealerHand.add(new Card());
            dealerTotal = calculateTotal(dealerHand);
        }
        dealerBusted = dealerTotal > 21;
    }

}
