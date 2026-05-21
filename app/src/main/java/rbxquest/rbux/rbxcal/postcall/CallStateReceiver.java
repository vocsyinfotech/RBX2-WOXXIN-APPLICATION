package rbxquest.rbux.rbxcal.postcall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class CallStateReceiver extends BroadcastReceiver {

    private static boolean wasRinging = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction())) return;
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            wasRinging = true;
        } else if (TelephonyManager.EXTRA_STATE_IDLE.equals(state) && wasRinging) {
            wasRinging = false;
            Intent adIntent = new Intent(context, ActivityPostCallForAd.class);
            adIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(adIntent);
        }
    }
}
