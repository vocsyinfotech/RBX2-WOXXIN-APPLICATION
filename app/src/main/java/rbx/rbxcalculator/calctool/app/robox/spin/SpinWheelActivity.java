package rbx.rbxcalculator.calctool.app.robox.spin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbx.rbxcalculator.calctool.app.robox.R;
import java.util.Random;

public class SpinWheelActivity extends AppCompatActivity {

    private static final String[] PRIZES = {
            "100 Robux", "50 Robux", "200 Robux", "Free Spin",
            "10 Robux", "500 Robux", "25 Robux", "Lucky!"
    };

    private ImageView ivWheel;
    private TextView tvResult;
    private boolean isSpinning = false;
    private float currentRotation = 0f;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_wheel);

        ivWheel = findViewById(R.id.ivSpinWheel);
        tvResult = findViewById(R.id.tvSpinResult);

        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.coins_sounds);
        } catch (Exception ignored) {}

        findViewById(R.id.btnSpin).setOnClickListener(v -> spinWheel());
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }

    private void spinWheel() {
        if (isSpinning) return;
        isSpinning = true;
        tvResult.setText("Spinning...");

        int prize = new Random().nextInt(PRIZES.length);
        float sectorAngle = 360f / PRIZES.length;
        float targetAngle = currentRotation + 1440f + (360f - prize * sectorAngle);

        ObjectAnimator animator = ObjectAnimator.ofFloat(ivWheel, "rotation", currentRotation, targetAngle);
        animator.setDuration(4000);
        animator.setInterpolator(new DecelerateInterpolator(3f));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                isSpinning = false;
                currentRotation = targetAngle % 360f;
                tvResult.setText("You won: " + PRIZES[prize] + "!");
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(0);
                    mediaPlayer.start();
                }
            }
        });
        animator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) { mediaPlayer.release(); mediaPlayer = null; }
    }
}
