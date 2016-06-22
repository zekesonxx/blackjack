package download.butts.blackjack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BetChooserActivity extends AppCompatActivity {

    protected int balance;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betchooser);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EditText betAmountField = (EditText) findViewById(R.id.bet_amount);
        betAmountField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    startGame(v);
                    return true;
                } else {
                    return false;
                }
            }
        });
        betAmountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateScreen();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.pref_balance_default);
        balance = sharedPref.getInt(getString(R.string.pref_balance), defaultValue);
        TextView balanceText = (TextView) findViewById(R.id.balance_amount);
        balanceText.setText("$" + String.valueOf(balance));
        betAmountField.setText(sharedPref.getString(getString(R.string.pref_last_bet_amount), getString(R.string.pref_last_bet_amount_default)));
        updateScreen();
    }

    private void updateScreen() {
        Button dealBtn = (Button) findViewById(R.id.deal_btn);
        String betAmountText = ((EditText) findViewById(R.id.bet_amount)).getText().toString();
        if (!betAmountText.isEmpty()) {
            int betAmount = Integer.parseInt(betAmountText);
            dealBtn.setEnabled(isValidBet(betAmount));
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.pref_last_bet_amount), betAmountText);
        editor.apply();


    }

    private boolean isValidBet(int bet) {
        return (bet < balance);
    }

    public void resetBalance(View view) {
        balance = 1337;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("balance", balance);
        editor.apply();
        TextView balanceText = (TextView) findViewById(R.id.balance_amount);
        balanceText.setText(String.valueOf(balance));
    }

    public void startGame(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();
        Intent game = new Intent(this, MainGameActivity.class);
        int betAmount = Integer.parseInt(((EditText) findViewById(R.id.bet_amount)).getText().toString());
        editor.putInt("balance", balance-betAmount);
        editor.apply();
        game.putExtra(MainGameActivity.BET_AMOUNT, betAmount);
        startActivity(game);
    }
}
