package rbx.rbxcalculator.calctool.app.robox.postcall;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import rbx.rbxcalculator.calctool.app.robox.R;

public class ActivityPostCallForAd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_call_ad);
        // Auto-close after showing brief ad placeholder
        new Handler(Looper.getMainLooper()).postDelayed(this::finish, 3000);
    }
}
