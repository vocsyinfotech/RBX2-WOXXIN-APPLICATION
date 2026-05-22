package rbxquest.rbux.rbxcal.allcalc;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbxquest.rbux.rbxcal.R;
import rbxquest.rbux.rbxcal.helpers.BhismaAds;
import rbxquest.rbux.rbxcal.helpers.MyApp;

public class RbxToUsdActivity extends AppCompatActivity {

    private static final String TAG = "Firebase_RBX";
    private static final double RBX_TO_USD = 0.0125; // 80 Robux = $1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        BhismaAds.attach(this);

        Log.d(TAG, "💰 RbxToUsdActivity opened");
        MyApp.logEvent("screen_view", "screen_name", "rbx_to_usd");

        TextView tvTitle = findViewById(R.id.tvCalcTitle);
        TextView tvHint  = findViewById(R.id.tvInputHint);
        TextView tvResultLabel = findViewById(R.id.tvResultLabel);
        tvTitle.setText("Robux to USD");
        tvHint.setText("Enter Robux amount");
        tvResultLabel.setText("USD Value:");

        EditText etInput   = findViewById(R.id.etInput);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        TextView tvResult  = findViewById(R.id.tvResult);

        btnCalculate.setOnClickListener(v -> {
            String input = etInput.getText().toString().trim();
            if (TextUtils.isEmpty(input)) { tvResult.setText("Please enter a value"); return; }
            try {
                double rbx = Double.parseDouble(input);
                double usd = rbx * RBX_TO_USD;
                String result = String.format("$%.2f", usd);
                tvResult.setText(result);

                Log.d(TAG, "══════════════════════════════════════════");
                Log.d(TAG, "💱 CALCULATION: Robux → USD");
                Log.d(TAG, "  Input  : " + rbx + " Robux");
                Log.d(TAG, "  Rate   : 1 RBX = $" + RBX_TO_USD);
                Log.d(TAG, "  Result : " + result);
                Log.d(TAG, "══════════════════════════════════════════");

                MyApp.logEvent("calculate_rbx_to_usd", "robux_amount", String.valueOf((long) rbx));
            } catch (NumberFormatException e) {
                tvResult.setText("Invalid number");
                Log.w(TAG, "⚠️ RbxToUsd — invalid input: " + input);
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }
}
