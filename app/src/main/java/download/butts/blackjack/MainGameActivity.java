package download.butts.blackjack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainGameActivity extends AppCompatActivity {
    public static String BET_AMOUNT = "download.butts.blackjack.BET_AMOUNT";
    private BlackjackGameManager theGame;
    private int betAmount;
    private BlackjackGameManager.GameResult result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        Intent intent = getIntent();
        betAmount = intent.getIntExtra(BET_AMOUNT, 0);
        this.theGame = new BlackjackGameManager();
        renderBoard();
    }

    public void renderBoard() {

        TextView test = (TextView) findViewById(R.id.textView);
        String dealerText = "";
        if (theGame.gameOver) {
            dealerText = String.format("Dealer's hand:\n %s (%s)", Card.renderStack(theGame.dealerHand), theGame.dealerTotal);
        } else {
            dealerText = String.format("Dealer's hole card: %s\n", theGame.dealerHand.get(0).toString(true));
        }
        test.setText(String.format("%s\nYour hand:\n %s (%s)", dealerText, Card.renderStack(theGame.playerHand), theGame.playerTotal));
        Button hitBtn = (Button) findViewById(R.id.hitBtn);
        Button doubleDownBtn = (Button) findViewById(R.id.doubleDownBtn);
        Button stayBtn = (Button) findViewById(R.id.stayBtn);
        if (theGame.gameOver) {
            hitBtn.setEnabled(false);
            doubleDownBtn.setEnabled(false);
            stayBtn.setEnabled(true);
            stayBtn.setText("End Game");
        } else {
            hitBtn.setEnabled(!theGame.playerBusted);
            doubleDownBtn.setEnabled(theGame.playerHand.size() == 2);
            if (theGame.playerTotal == 21) {
                hitBtn.setEnabled(false);
                doubleDownBtn.setEnabled(false);
            }
        }
    }

    public void hitBtnCallback(View view) {
        theGame.hit();
        if (theGame.playerBusted) {
            this.stayBtnCallback(view);
        } else {
            renderBoard();
        }
    }

    public void stayBtnCallback(View view) {
        if (theGame.gameOver) {
            endGame(result);
        } else {
            result = theGame.stay();
            renderBoard();
            Toast toast = Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void doubleDownBtnCallback(View view) {
        result = theGame.doubleDown();
        renderBoard();
        Toast toast = Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void endGame(BlackjackGameManager.GameResult result) {
        Intent intent = new Intent(this, ResultsScreenActivity.class);
        intent.putExtra(BET_AMOUNT, betAmount);
        intent.putExtra(ResultsScreenActivity.RESULT, result.toString());
        startActivity(intent);
    }
}
