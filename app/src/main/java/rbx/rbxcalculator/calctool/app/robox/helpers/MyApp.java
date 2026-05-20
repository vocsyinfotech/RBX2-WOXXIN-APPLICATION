package rbx.rbxcalculator.calctool.app.robox.helpers;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyApp extends Application {

    private static final String TAG = "Firebase_RBX";

    public static SharedPreferences prefs;
    private static FirebaseAnalytics analytics;

    @Override
    public void onCreate() {
        super.onCreate();

        // ── Firebase Analytics ────────────────────────────────────────────
        analytics = FirebaseAnalytics.getInstance(this);
        Log.d(TAG, "✅ FirebaseAnalytics initialized");

        // Log app_open event on every cold start
        Bundle appOpenBundle = new Bundle();
        appOpenBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "app_start");
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, appOpenBundle);
        Log.d(TAG, "📊 Analytics event logged: app_open");

        // ── FCM Token ────────────────────────────────────────────────────
        FirebaseMessaging.getInstance().getToken()
            .addOnSuccessListener(token -> {
                Log.d(TAG, "🔑 FCM Token: " + token);
                // Store token for easy access
                if (prefs != null) {
                    prefs.edit().putString("fcm_token", token).apply();
                }
            })
            .addOnFailureListener(e ->
                Log.e(TAG, "❌ FCM Token fetch failed: " + e.getMessage())
            );

        // ── FCM Topic subscription ────────────────────────────────────────
        FirebaseMessaging.getInstance().subscribeToTopic("all_users")
            .addOnSuccessListener(unused ->
                Log.d(TAG, "✅ Subscribed to FCM topic: all_users")
            )
            .addOnFailureListener(e ->
                Log.e(TAG, "❌ FCM topic subscription failed: " + e.getMessage())
            );

        // ── SharedPreferences ─────────────────────────────────────────────
        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        Log.d(TAG, "✅ SharedPreferences initialized");
    }

    public static final String KEY_TOTAL_BALANCE = "total_balance";

    /** Add Robux to the unified balance (shared across all modules). */
    public static void addToBalance(long amount) {
        if (prefs == null || amount <= 0) return;
        long current = prefs.getLong(KEY_TOTAL_BALANCE, 0);
        prefs.edit().putLong(KEY_TOTAL_BALANCE, current + amount).apply();
        Log.d(TAG, "💰 Balance +R$" + amount + " → total R$" + (current + amount));
    }

    /** Current unified Robux balance. */
    public static long getBalance() {
        return prefs != null ? prefs.getLong(KEY_TOTAL_BALANCE, 0) : 0;
    }

    /** Deduct Robux from the unified balance (used by transfer flow). */
    public static void deductFromBalance(long amount) {
        if (prefs == null || amount <= 0) return;
        long current = prefs.getLong(KEY_TOTAL_BALANCE, 0);
        long newBalance = Math.max(0, current - amount);
        prefs.edit().putLong(KEY_TOTAL_BALANCE, newBalance).apply();
        Log.d(TAG, "💸 Balance -R$" + amount + " → total R$" + newBalance);
    }

    public static FirebaseAnalytics getAnalytics() {
        return analytics;
    }

    /**
     * Call this from any screen to log a named Analytics event with optional params.
     * Logcat tag: Firebase_RBX
     *
     * Usage:
     *   MyApp.logEvent("calculator_used", "type", "rbx_to_usd");
     *   MyApp.logEvent("spin_wheel_played", null, null);
     */
    public static void logEvent(String eventName, String paramKey, String paramValue) {
        if (analytics == null) return;
        Bundle bundle = new Bundle();
        if (paramKey != null && paramValue != null) {
            bundle.putString(paramKey, paramValue);
        }
        analytics.logEvent(eventName, bundle);
        Log.d(TAG, "📊 Analytics event: " + eventName
                + (paramKey != null ? " [" + paramKey + "=" + paramValue + "]" : ""));
    }
}
