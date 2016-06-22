package download.butts.blackjack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        newGame();
    }

    private void newGame() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int balance = sharedPref.getInt(getString(R.string.pref_balance), 1000);
        editor.putInt(getString(R.string.pref_balance), balance-betAmount);
        editor.apply();
        this.theGame = new BlackjackGameManager();
        renderBoard();
    }

    public void renderBoard() {

        TextView test = (TextView) findViewById(R.id.textView);
        String dealerText;
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
            doubleDownBtn.setEnabled(true);
            doubleDownBtn.setText("End Game");
            stayBtn.setEnabled(true);
            stayBtn.setText("Play Again");
        } else {
            doubleDownBtn.setText(R.string.double_down_btn);
            stayBtn.setText(R.string.stay_btn);
            hitBtn.setEnabled(!theGame.playerBusted);
            doubleDownBtn.setEnabled(theGame.playerHand.size() == 2);
            if (theGame.playerTotal == 21) {
                hitBtn.setEnabled(false);
                doubleDownBtn.setEnabled(false);
            }
        }
    }

    private void gameEnded(BlackjackGameManager.GameResult result) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.pref_balance_default);
        int balance = sharedPref.getInt(getString(R.string.pref_balance), defaultValue);
        switch (result) {
            case Won:
                balance += 2*betAmount;
                break;
            case Lost:
                //bet was already subtracted from balance
                //no changes needed
                break;
            case Neutral:
                balance += betAmount;
                break;
            case Doubled:
                balance += 4*betAmount;
                break;
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("balance", balance);
        editor.apply();
        Toast toast = Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT);
        toast.show();
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
            newGame();
        } else {
            result = theGame.stay();
            gameEnded(result);
            renderBoard();
        }
    }

    public void doubleDownBtnCallback(View view) {
        if (theGame.gameOver) {
            endGame(result);
        } else {
            result = theGame.doubleDown();
            gameEnded(result);
            renderBoard();
        }
    }

    public void endGame(BlackjackGameManager.GameResult result) {
        Intent intent = new Intent(this, ResultsScreenActivity.class);
        intent.putExtra(BET_AMOUNT, betAmount);
        intent.putExtra(ResultsScreenActivity.RESULT, result.toString());
        startActivity(intent);
    }
}
