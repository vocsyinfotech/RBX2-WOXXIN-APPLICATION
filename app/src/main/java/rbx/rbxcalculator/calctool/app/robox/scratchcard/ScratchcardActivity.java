package rbx.rbxcalculator.calctool.app.robox.scratchcard;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbx.rbxcalculator.calctool.app.robox.R;
import java.util.Random;

public class ScratchcardActivity extends AppCompatActivity {

    private static final String[] PRIZES = {
            "You won 100 Robux!", "You won 50 Robux!",
            "You won 200 Robux!", "You won Free Spin!",
            "You won 10 Robux!", "You won 500 Robux!"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratchcard);

        String prize = PRIZES[new Random().nextInt(PRIZES.length)];
        TextView tvPrize = findViewById(R.id.tvScratchPrize);
        tvPrize.setText(prize);
        tvPrize.setAlpha(0f);

        ScratchView scratch = findViewById(R.id.scratchView);
        scratch.setRevealListener(() -> tvPrize.setAlpha(1f));

        TextView tvHint = findViewById(R.id.tvScratchHint);

        findViewById(R.id.btnNewCard).setOnClickListener(v -> {
            String newPrize = PRIZES[new Random().nextInt(PRIZES.length)];
            tvPrize.setText(newPrize);
            tvPrize.setAlpha(0f);
            scratch.reset();
            tvHint.setText("Scratch to reveal your prize!");
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }
}
