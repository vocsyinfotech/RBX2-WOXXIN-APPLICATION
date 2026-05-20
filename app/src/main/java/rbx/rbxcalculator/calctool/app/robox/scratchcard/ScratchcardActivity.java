package rbx.rbxcalculator.calctool.app.robox.scratchcard;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbx.rbxcalculator.calctool.app.robox.R;
import rbx.rbxcalculator.calctool.app.robox.helpers.MyApp;
import java.util.Random;

public class ScratchcardActivity extends AppCompatActivity {

    private static final String TAG = "Firebase_RBX";

    private static final String[] PRIZE_AMOUNTS = {"100", "50", "200", "🎡", "10", "500"};
    private static final String[] PRIZE_LABELS  = {"ROBUX", "ROBUX", "ROBUX", "FREE SPIN", "ROBUX", "ROBUX"};
    private static final int[]    PRIZE_ROBUX   = {100, 50, 200, 0, 10, 500};

    private int     currentPrizeRobux = 0;
    private int     ticketNumber      = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratchcard);

        MyApp.logEvent("screen_view", "screen_name", "scratch_card");

        TextView tvAmount  = findViewById(R.id.tvPrizeAmount);
        TextView tvLabel   = findViewById(R.id.tvPrizeLabel);
        TextView tvTicket  = findViewById(R.id.tvTicketNumber);
        TextView tvWonNote = findViewById(R.id.tvWonNote);
        TextView tvHint    = findViewById(R.id.tvScratchHint);
        ScratchView scratch = findViewById(R.id.scratchView);

        loadNewCard(tvAmount, tvLabel, tvTicket, tvWonNote, tvHint, scratch);

        scratch.setRevealListener(() -> {
            tvAmount.animate().alpha(1f).setDuration(400).start();
            tvLabel.animate().alpha(1f).setDuration(400).start();

            if (currentPrizeRobux > 0) {
                MyApp.addToBalance(currentPrizeRobux);
                tvWonNote.setText("🎉  +" + currentPrizeRobux + " Robux added to your balance!");
            } else {
                tvWonNote.setText("Better luck next time!");
            }

            Log.d(TAG, "══════════════════════════════════════════");
            Log.d(TAG, "🎴 SCRATCH CARD REVEALED");
            Log.d(TAG, "  Prize   : R$" + currentPrizeRobux);
            Log.d(TAG, "  Balance : R$" + MyApp.getBalance());
            Log.d(TAG, "══════════════════════════════════════════");
            MyApp.logEvent("scratch_card_revealed", "amount", String.valueOf(currentPrizeRobux));
        });

        findViewById(R.id.btnNewCard).setOnClickListener(v ->
                loadNewCard(tvAmount, tvLabel, tvTicket, tvWonNote, tvHint, scratch));

        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }

    private void loadNewCard(TextView tvAmount, TextView tvLabel, TextView tvTicket,
                             TextView tvWonNote, TextView tvHint, ScratchView scratch) {
        int idx = new Random().nextInt(PRIZE_AMOUNTS.length);
        tvAmount.setText(PRIZE_AMOUNTS[idx]);
        tvAmount.setAlpha(0f);
        tvLabel.setText(PRIZE_LABELS[idx]);
        tvLabel.setAlpha(0f);
        currentPrizeRobux = PRIZE_ROBUX[idx];

        tvTicket.setText(String.format("%06d", ticketNumber++));
        tvWonNote.setText("");
        tvHint.setText("Scratch to reveal your prize!");

        scratch.reset();

        Log.d(TAG, "🎴 New scratch card loaded — prize hidden (R$" + currentPrizeRobux + ")");
    }
}
