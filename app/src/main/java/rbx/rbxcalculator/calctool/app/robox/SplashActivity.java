package rbx.rbxcalculator.calctool.app.robox;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import rbx.rbxcalculator.calctool.app.robox.helpers.MyApp;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "Firebase_RBX";
    private static final int NOTIFICATION_PERMISSION_CODE = 1001;
    private static final long SPLASH_DELAY = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.d(TAG, "📱 SplashActivity launched");
        MyApp.logEvent("screen_view", "screen_name", "splash");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "🔔 Requesting POST_NOTIFICATIONS permission");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
                return;
            }
        }
        navigateToMain();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        Log.d(TAG, "🔔 Notification permission " + (granted ? "GRANTED" : "DENIED"));
        MyApp.logEvent("notification_permission", "granted", String.valueOf(granted));
        navigateToMain();
    }

    private void navigateToMain() {
        Log.d(TAG, "⏩ Navigating to MainActivity after " + SPLASH_DELAY + "ms");
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_DELAY);
    }
}
