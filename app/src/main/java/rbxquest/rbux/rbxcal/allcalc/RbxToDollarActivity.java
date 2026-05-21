package rbxquest.rbux.rbxcal.allcalc;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbxquest.rbux.rbxcal.R;

public class RbxToDollarActivity extends AppCompatActivity {

    // DevEx rate: 100,000 Robux = $350 → $0.0035/Robux
    private static final double RBX_TO_DOLLAR = 0.0035;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        ((TextView) findViewById(R.id.tvCalcTitle)).setText("Robux to Dollar (DevEx)");
        ((TextView) findViewById(R.id.tvInputHint)).setText("Enter Robux amount");
        ((TextView) findViewById(R.id.tvResultLabel)).setText("Dollar Value (DevEx):");

        EditText etInput = findViewById(R.id.etInput);
        TextView tvResult = findViewById(R.id.tvResult);

        findViewById(R.id.btnCalculate).setOnClickListener(v -> {
            String input = etInput.getText().toString().trim();
            if (TextUtils.isEmpty(input)) { tvResult.setText("Please enter a value"); return; }
            try {
                double rbx = Double.parseDouble(input);
                double dollar = rbx * RBX_TO_DOLLAR;
                tvResult.setText(String.format("$%.2f", dollar));
            } catch (NumberFormatException e) {
                tvResult.setText("Invalid number");
            }
        });
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }
}
