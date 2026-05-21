package rbxquest.rbux.rbxcal.membership;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbxquest.rbux.rbxcal.R;

public class ObcToRbxActivity extends AppCompatActivity {

    // OBC: 60 Robux/day
    private static final int OBC_DAILY = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        ((TextView) findViewById(R.id.tvCalcTitle)).setText("OBC to Robux");
        ((TextView) findViewById(R.id.tvInputHint)).setText("Enter number of days");
        ((TextView) findViewById(R.id.tvResultLabel)).setText("Robux Earned (OBC):");

        EditText etInput = findViewById(R.id.etInput);
        TextView tvResult = findViewById(R.id.tvResult);

        tvResult.setText("OBC gives " + OBC_DAILY + " Robux/day\n1,800 Robux/month");

        findViewById(R.id.btnCalculate).setOnClickListener(v -> {
            String input = etInput.getText().toString().trim();
            if (TextUtils.isEmpty(input)) { tvResult.setText("Please enter days"); return; }
            try {
                int days = Integer.parseInt(input);
                int total = days * OBC_DAILY;
                tvResult.setText(total + " Robux\n(" + days + " days × " + OBC_DAILY + " Robux/day)");
            } catch (NumberFormatException e) {
                tvResult.setText("Invalid number");
            }
        });
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }
}
