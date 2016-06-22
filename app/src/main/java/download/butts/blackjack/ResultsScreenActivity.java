package download.butts.blackjack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ResultsScreenActivity extends AppCompatActivity {
    public static final String RESULT = "download.butts.blackjack.RESULT";

    private String result;
    private int betAmount;
    private int balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_screen);
        Intent intent = getIntent();
        betAmount = intent.getIntExtra(MainGameActivity.BET_AMOUNT, 0);
        result = intent.getStringExtra(RESULT);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.pref_balance_default);
        balance = sharedPref.getInt(getString(R.string.pref_balance), defaultValue);
        switch (result) {
            case "Won":
                balance += 2*betAmount;
                break;
            case "Lost":
                //bet was already subtracted from balance
                //no changes needed
                break;
            case "Neutral":
                balance += betAmount;
                break;
            case "Doubled":
                balance += 4*betAmount;
                break;
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("balance", balance);
        editor.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView congratsText = (TextView) findViewById(R.id.congratsText);
        switch (result) {
            case "Won":
                congratsText.setText("You Won!");
                break;
            case "Lost":
                congratsText.setText("You Lost.");
                break;
            case "Neutral":
                congratsText.setText("You Broke Even!");
                break;
            case "Doubled":
                congratsText.setText("You Doubled!");
                break;
        }
        TextView balanceAmount = (TextView) findViewById(R.id.new_balance_amount);
        balanceAmount.setText("$" + String.valueOf(balance));
    }

    public void playAgainBtnCallback(View view) {
        Intent intent = new Intent(this, BetChooserActivity.class);
        startActivity(intent);
    }
}
