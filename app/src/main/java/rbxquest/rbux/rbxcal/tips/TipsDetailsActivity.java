package rbxquest.rbux.rbxcal.tips;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbxquest.rbux.rbxcal.R;

public class TipsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_details);

        String title = getIntent().getStringExtra(TipsActivity.EXTRA_TITLE);
        String detail = getIntent().getStringExtra(TipsActivity.EXTRA_DETAIL);

        ((TextView) findViewById(R.id.tvTipDetailTitle)).setText(title);
        ((TextView) findViewById(R.id.tvTipDetailBody)).setText(detail);
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }
}
