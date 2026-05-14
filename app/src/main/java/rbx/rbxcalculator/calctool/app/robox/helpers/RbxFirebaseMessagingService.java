package rbx.rbxcalculator.calctool.app.robox.helpers;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class RbxFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "Firebase_RBX";

    /**
     * Called when a new FCM token is generated (first launch, token refresh, app reinstall).
     * Token is printed in logcat — copy it from here for direct push testing.
     */
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "══════════════════════════════════════════");
        Log.d(TAG, "🔑 NEW FCM TOKEN GENERATED");
        Log.d(TAG, "Token: " + token);
        Log.d(TAG, "══════════════════════════════════════════");

        // Persist token for reuse
        if (MyApp.prefs != null) {
            MyApp.prefs.edit().putString("fcm_token", token).apply();
        }

        // Log token-refresh analytics event
        MyApp.logEvent("fcm_token_refreshed", "token_prefix", token.substring(0, Math.min(20, token.length())));
    }

    /**
     * Called when a push notification / data message arrives while the app is in foreground.
     * Every field of the message is printed to logcat under tag Firebase_RBX.
     */
    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        Log.d(TAG, "══════════════════════════════════════════");
        Log.d(TAG, "📩 FCM MESSAGE RECEIVED");
        Log.d(TAG, "From       : " + message.getFrom());
        Log.d(TAG, "Message ID : " + message.getMessageId());
        Log.d(TAG, "Sent time  : " + message.getSentTime());
        Log.d(TAG, "TTL        : " + message.getTtl());
        Log.d(TAG, "Priority   : " + message.getPriority());

        // ── Notification payload ──────────────────────────────────────
        if (message.getNotification() != null) {
            RemoteMessage.Notification n = message.getNotification();
            Log.d(TAG, "── Notification ──────────────────────────");
            Log.d(TAG, "  Title  : " + n.getTitle());
            Log.d(TAG, "  Body   : " + n.getBody());
            Log.d(TAG, "  Icon   : " + n.getIcon());
            Log.d(TAG, "  Color  : " + n.getColor());
            Log.d(TAG, "  Sound  : " + n.getSound());
            Log.d(TAG, "  Click  : " + n.getClickAction());
            Log.d(TAG, "  Image  : " + n.getImageUrl());
        } else {
            Log.d(TAG, "── Notification: (none — data-only message)");
        }

        // ── Data payload ──────────────────────────────────────────────
        Map<String, String> data = message.getData();
        if (!data.isEmpty()) {
            Log.d(TAG, "── Data payload ──────────────────────────");
            for (Map.Entry<String, String> entry : data.entrySet()) {
                Log.d(TAG, "  " + entry.getKey() + " : " + entry.getValue());
            }
        } else {
            Log.d(TAG, "── Data payload: (empty)");
        }

        Log.d(TAG, "══════════════════════════════════════════");

        // Log analytics event for received push
        String title = message.getNotification() != null
                ? message.getNotification().getTitle() : "data_message";
        MyApp.logEvent("push_received", "title", title);
    }
}
