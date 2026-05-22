package rbxquest.rbux.rbxcal.allcalc;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbxquest.rbux.rbxcal.R;
import rbxquest.rbux.rbxcal.helpers.BhismaAds;

public class DollarToRbxActivity extends AppCompatActivity {

    // Buy rate: 80 Robux per $1
    private static final double DOLLAR_TO_RBX = 80.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        BhismaAds.attach(this);

        ((TextView) findViewById(R.id.tvCalcTitle)).setText("Dollar to Robux");
        ((TextView) findViewById(R.id.tvInputHint)).setText("Enter Dollar amount");
        ((TextView) findViewById(R.id.tvResultLabel)).setText("Robux You Get:");

        EditText etInput = findViewById(R.id.etInput);
        TextView tvResult = findViewById(R.id.tvResult);

        findViewById(R.id.btnCalculate).setOnClickListener(v -> {
            String input = etInput.getText().toString().trim();
            if (TextUtils.isEmpty(input)) { tvResult.setText("Please enter a value"); return; }
            try {
                double dollars = Double.parseDouble(input);
                long rbx = Math.round(dollars * DOLLAR_TO_RBX);
                tvResult.setText(rbx + " Robux");
            } catch (NumberFormatException e) {
                tvResult.setText("Invalid number");
            }
        });
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }
}
