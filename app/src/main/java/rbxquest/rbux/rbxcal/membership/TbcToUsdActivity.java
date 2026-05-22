package rbxquest.rbux.rbxcal.membership;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbxquest.rbux.rbxcal.R;
import rbxquest.rbux.rbxcal.helpers.BhismaAds;

public class TbcToUsdActivity extends AppCompatActivity {

    // TBC: 35 Robux/day = 1050/month
    // DevEx rate: $0.0035/Robux
    private static final int TBC_DAILY = 35;
    private static final double DEVEX_RATE = 0.0035;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        BhismaAds.attach(this);

        ((TextView) findViewById(R.id.tvCalcTitle)).setText("TBC to USD");
        ((TextView) findViewById(R.id.tvInputHint)).setText("Enter number of days");
        ((TextView) findViewById(R.id.tvResultLabel)).setText("USD Value (TBC DevEx):");

        EditText etInput = findViewById(R.id.etInput);
        TextView tvResult = findViewById(R.id.tvResult);
        tvResult.setText("TBC gives " + TBC_DAILY + " Robux/day\n1,050 Robux/month");

        findViewById(R.id.btnCalculate).setOnClickListener(v -> {
            String input = etInput.getText().toString().trim();
            if (TextUtils.isEmpty(input)) { tvResult.setText("Please enter days"); return; }
            try {
                int days = Integer.parseInt(input);
                int rbx = days * TBC_DAILY;
                double usd = rbx * DEVEX_RATE;
                tvResult.setText(String.format("%d Robux = $%.2f\n(%d days × %d/day)", rbx, usd, days, TBC_DAILY));
            } catch (NumberFormatException e) {
                tvResult.setText("Invalid number");
            }
        });
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }
}
