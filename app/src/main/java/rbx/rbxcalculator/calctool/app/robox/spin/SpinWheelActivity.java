package rbx.rbxcalculator.calctool.app.robox.spin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbx.rbxcalculator.calctool.app.robox.R;
import rbx.rbxcalculator.calctool.app.robox.helpers.MyApp;
import java.util.Random;

public class SpinWheelActivity extends AppCompatActivity {

    private static final String TAG = "Firebase_RBX";

    // Sector count and labels come from WheelView so they stay in sync
    private static final int   SECTOR_COUNT  = WheelView.LABELS.length;
    private static final float SECTOR_ANGLE  = 360f / SECTOR_COUNT;

    private WheelView   wheelView;
    private TextView    tvResult;
    private Button      btnSpin;

    private float       currentRotation = 0f;
    private boolean     isSpinning      = false;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_wheel);

        Log.d(TAG, "🎡 SpinWheelActivity opened");
        MyApp.logEvent("screen_view", "screen_name", "spin_wheel");

        wheelView = findViewById(R.id.wheelView);
        tvResult  = findViewById(R.id.tvSpinResult);
        btnSpin   = findViewById(R.id.btnSpin);

        try { mediaPlayer = MediaPlayer.create(this, R.raw.coins_sounds); }
        catch (Exception ignored) {}

        btnSpin.setOnClickListener(v -> spinWheel());
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }

    private void spinWheel() {
        if (isSpinning) return;
        isSpinning = true;
        btnSpin.setEnabled(false);
        tvResult.setText("Spinning…");

        // Pick a random winning sector
        int sector = new Random().nextInt(SECTOR_COUNT);

        // Spin at least 5 full rotations, then land on the winning sector.
        // Wheel starts at -90° (top = index 0). Each sector occupies SECTOR_ANGLE degrees.
        // To land sector i under the top pointer: rotate so (currentRotation + extra) % 360
        // places sector i at the top.
        float extra = 360f * 5 + (360f - sector * SECTOR_ANGLE) - (currentRotation % 360f);
        float target = currentRotation + extra;

        Log.d(TAG, "🎡 Spin — sector=" + sector + " label=" + WheelView.LABELS[sector]);

        ObjectAnimator anim = ObjectAnimator.ofFloat(wheelView, "rotation", currentRotation, target);
        anim.setDuration(4500);
        anim.setInterpolator(new DecelerateInterpolator(3.5f));
        anim.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                isSpinning = false;
                btnSpin.setEnabled(true);
                currentRotation = target % 360f;

                String prize = WheelView.LABELS[sector];
                String msg   = "🎉  You won " + prize + " Robux!";
                tvResult.setText(msg);

                // Add winnings to unified balance
                try { MyApp.addToBalance(Long.parseLong(prize)); } catch (NumberFormatException ignored) {}

                Log.d(TAG, "══════════════════════════════════════════");
                Log.d(TAG, "🎉 SPIN RESULT");
                Log.d(TAG, "  Prize   : " + prize + " Robux");
                Log.d(TAG, "  Sector  : " + sector);
                Log.d(TAG, "  Balance : R$" + MyApp.getBalance());
                Log.d(TAG, "══════════════════════════════════════════");

                MyApp.logEvent("spin_wheel_result", "prize", prize);

                if (mediaPlayer != null) { mediaPlayer.seekTo(0); mediaPlayer.start(); }
            }
        });
        anim.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) { mediaPlayer.release(); mediaPlayer = null; }
    }
}
