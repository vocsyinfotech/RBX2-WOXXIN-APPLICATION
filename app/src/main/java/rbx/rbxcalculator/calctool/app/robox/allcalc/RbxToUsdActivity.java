package rbx.rbxcalculator.calctool.app.robox.allcalc;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbx.rbxcalculator.calctool.app.robox.R;

public class RbxToUsdActivity extends AppCompatActivity {

    // 1 Robux = $0.0125 (buy price: 80 Robux = $1)
    private static final double RBX_TO_USD = 0.0125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        TextView tvTitle = findViewById(R.id.tvCalcTitle);
        TextView tvHint = findViewById(R.id.tvInputHint);
        TextView tvResultLabel = findViewById(R.id.tvResultLabel);
        tvTitle.setText("Robux to USD");
        tvHint.setText("Enter Robux amount");
        tvResultLabel.setText("USD Value:");

        EditText etInput = findViewById(R.id.etInput);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        TextView tvResult = findViewById(R.id.tvResult);

        btnCalculate.setOnClickListener(v -> {
            String input = etInput.getText().toString().trim();
            if (TextUtils.isEmpty(input)) { tvResult.setText("Please enter a value"); return; }
            try {
                double rbx = Double.parseDouble(input);
                double usd = rbx * RBX_TO_USD;
                tvResult.setText(String.format("$%.2f", usd));
            } catch (NumberFormatException e) {
                tvResult.setText("Invalid number");
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }
}
