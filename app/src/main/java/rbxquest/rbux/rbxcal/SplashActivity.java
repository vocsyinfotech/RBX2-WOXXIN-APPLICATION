package rbxquest.rbux.rbxcal;

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

import com.bhasma.OnJsonCallBackListner;
import com.bhasma.Pizza;

import rbxquest.rbux.rbxcal.helpers.AdConstants;
import rbxquest.rbux.rbxcal.helpers.MyApp;

public class SplashActivity extends AppCompatActivity implements OnJsonCallBackListner {

    private static final String TAG = "Firebase_RBX";
    private static final int NOTIFICATION_PERMISSION_CODE = 1001;
    private static final long SPLASH_DELAY = 2500;

    /** Singleton handle to the bhisma Pizza god-class. Read by other activities. */
    public static Pizza pizza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.d(TAG, "📱 SplashActivity launched");
        MyApp.logEvent("screen_view", "screen_name", "splash");

        // ── Bhisma Pizza god-class construction (86 positional args) ─────
        // All IDs come from AdConstants; gist JSON overrides at runtime.
        pizza = new Pizza(
                this,                                  // 1. Context
                this,                                  // 2. OnJsonCallBackListner
                getString(R.string.app_name),          // 3. App name
                AdConstants.ADS_LINK,                  // 4. Gist URL
                getPackageName(),                      // 5. Package name
                2, 1, "L", 1, 1, "4",                  // 6-11. ints + gl_setup + splash_anim
                                                       //       ^ show_ads = 2 → Qureka-only fast path
                                                       //         (prevents cold-start Google fallback
                                                       //          before async gist fetch lands)

                // 12-16. Facebook Banner ID 1-5
                AdConstants.FB_BANNER_1, AdConstants.FB_BANNER_2, AdConstants.FB_BANNER_3,
                AdConstants.FB_BANNER_4, AdConstants.FB_BANNER_5,

                // 17-21. Facebook MR ID 1-5
                AdConstants.FB_MR_1, AdConstants.FB_MR_2, AdConstants.FB_MR_3,
                AdConstants.FB_MR_4, AdConstants.FB_MR_5,

                // 22-26. Facebook Interstitial ID 1-5
                AdConstants.FB_INTER_1, AdConstants.FB_INTER_2, AdConstants.FB_INTER_3,
                AdConstants.FB_INTER_4, AdConstants.FB_INTER_5,

                // 27-31. Facebook Native ID 1-5
                AdConstants.FB_NATIVE_1, AdConstants.FB_NATIVE_2, AdConstants.FB_NATIVE_3,
                AdConstants.FB_NATIVE_4, AdConstants.FB_NATIVE_5,

                // 32-36. Facebook Native Small ID 1-5
                AdConstants.FB_NATIVE_SMALL_1, AdConstants.FB_NATIVE_SMALL_2, AdConstants.FB_NATIVE_SMALL_3,
                AdConstants.FB_NATIVE_SMALL_4, AdConstants.FB_NATIVE_SMALL_5,

                // 37-40. Applovin Carbon: app, inter, banner, reward
                AdConstants.AC_APP_ID, AdConstants.AC_INTER_ID, AdConstants.AC_BANNER_ID, AdConstants.AC_REWARD_ID,

                AdConstants.TAPPX,                     // 41. Tappx
                AdConstants.ADMOB_APP_ID,              // 42. AdMob app ID

                // 43-48. AdMob Interstitial 1-3 + rotation
                AdConstants.ADMOB_INTER_ID_1,  AdConstants.ADMOB_INTER_ID_11,
                AdConstants.ADMOB_INTER_ID_2,  AdConstants.ADMOB_INTER_ID_22,
                AdConstants.ADMOB_INTER_ID_3,  AdConstants.ADMOB_INTER_ID_33,

                // 49-54. AdMob Banner 1-3 + rotation
                AdConstants.ADMOB_BANNER_ID_1, AdConstants.ADMOB_BANNER_ID_11,
                AdConstants.ADMOB_BANNER_ID_2, AdConstants.ADMOB_BANNER_ID_22,
                AdConstants.ADMOB_BANNER_ID_3, AdConstants.ADMOB_BANNER_ID_33,

                // 55-60. AdMob Native 1-3 + rotation
                AdConstants.ADMOB_NATIVE_ID_1, AdConstants.ADMOB_NATIVE_ID_11,
                AdConstants.ADMOB_NATIVE_ID_2, AdConstants.ADMOB_NATIVE_ID_22,
                AdConstants.ADMOB_NATIVE_ID_3, AdConstants.ADMOB_NATIVE_ID_33,

                // 61-66. AdMob App Open 1-3 + rotation
                AdConstants.ADMOB_APPOPEN_ID_1, AdConstants.ADMOB_APPOPEN_ID_11,
                AdConstants.ADMOB_APPOPEN_ID_2, AdConstants.ADMOB_APPOPEN_ID_22,
                AdConstants.ADMOB_APPOPEN_ID_3, AdConstants.ADMOB_APPOPEN_ID_33,

                // 67-72. AdMob Rewarded Interstitial 1-3 + rotation
                AdConstants.ADMOB_RVI_ID_1, AdConstants.ADMOB_RVI_ID_11,
                AdConstants.ADMOB_RVI_ID_2, AdConstants.ADMOB_RVI_ID_22,
                AdConstants.ADMOB_RVI_ID_3, AdConstants.ADMOB_RVI_ID_33,

                // 73-85. Qureka backup network (overridden by gist)
                AdConstants.QUREKA_BANNER,
                AdConstants.REDIRECT_LINK_BANNER,
                AdConstants.NATIVE_SMALL_REDIRECT_LINK,
                AdConstants.QUREKA_NATIVE_SMALL,
                AdConstants.BUTTON_TITLE,
                AdConstants.DISC,
                AdConstants.IMAGE,
                AdConstants.IMAGE_2,
                AdConstants.SHORT_DISC,
                AdConstants.NATIVE_REDIRECT_LINK,
                AdConstants.QUREKA_NATIVE,
                AdConstants.QUREKA_NATIVE_GIF,
                AdConstants.INTER_REDIRECT_LINK,

                // 86. UTM template (REQUIRED)
                AdConstants.UTM_LINK
        );
        Log.d(TAG, "✅ Bhisma Pizza initialized");

        // Trigger gist fetch + interstitial counter (no UI takeover — keeps existing layout)
        pizza.Interstial_Counted(this);

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

    /** Called by bhisma once the gist JSON has been fetched + applied. */
    @Override
    public void OnJsonDone() {
        Log.d(TAG, "📦 Bhisma gist JSON loaded");
    }
}
