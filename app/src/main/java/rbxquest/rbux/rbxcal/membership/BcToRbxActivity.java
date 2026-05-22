package rbxquest.rbux.rbxcal.membership;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbxquest.rbux.rbxcal.R;
import rbxquest.rbux.rbxcal.helpers.BhismaAds;

public class BcToRbxActivity extends AppCompatActivity {

    // BC: 15 Robux/day
    private static final int BC_DAILY = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        BhismaAds.attach(this);

        ((TextView) findViewById(R.id.tvCalcTitle)).setText("BC to Robux");
        ((TextView) findViewById(R.id.tvInputHint)).setText("Enter number of days");
        ((TextView) findViewById(R.id.tvResultLabel)).setText("Robux Earned (BC):");

        EditText etInput = findViewById(R.id.etInput);
        TextView tvResult = findViewById(R.id.tvResult);

        // Show monthly info
        tvResult.setText("BC gives " + BC_DAILY + " Robux/day\n450 Robux/month");

        findViewById(R.id.btnCalculate).setOnClickListener(v -> {
            String input = etInput.getText().toString().trim();
            if (TextUtils.isEmpty(input)) { tvResult.setText("Please enter days"); return; }
            try {
                int days = Integer.parseInt(input);
                int total = days * BC_DAILY;
                tvResult.setText(total + " Robux\n(" + days + " days × " + BC_DAILY + " Robux/day)");
            } catch (NumberFormatException e) {
                tvResult.setText("Invalid number");
            }
        });
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }
}
