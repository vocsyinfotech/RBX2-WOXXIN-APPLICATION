package rbxquest.rbux.rbxcal.helpers;

import android.app.Activity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.bhasma.Pizza;

import rbxquest.rbux.rbxcal.R;

/**
 * Tiny helper that auto-fills the two bhisma ad containers (if present in the
 * activity's layout):
 *   - {@code R.id.banner}  → big native (size 2)
 *   - {@code R.id.bannerx} → small banner (size 1)
 *
 * Activities should call {@link #attach(Activity)} once at the end of their
 * onCreate after {@code setContentView()}. Containers that don't exist in the
 * layout are silently skipped.
 */
public final class BhismaAds {

    private static final String TAG = "Firebase_RBX";

    private BhismaAds() {}

    public static void attach(Activity activity) {
        if (activity == null) return;
        try {
            RelativeLayout banner = activity.findViewById(R.id.banner);
            if (banner != null) {
                Pizza.Native(activity, banner, 2);
                Log.d(TAG, "📺 Native ad attached to R.id.banner in " + activity.getClass().getSimpleName());
            }
        } catch (Throwable t) {
            Log.w(TAG, "BhismaAds R.id.banner attach failed: " + t.getMessage());
        }
        try {
            RelativeLayout bannerx = activity.findViewById(R.id.bannerx);
            if (bannerx != null) {
                // Pizza.Banner (not Pizza.Native): in show_ads==2 mode, Pizza.Native has no
                // branch for nativeType=1, so the small banner would render blank. Pizza.Banner
                // routes through QurekaAds.loadQurekaBanner which serves the qurekaBanner images.
                Pizza.Banner(activity, bannerx, 1);
                Log.d(TAG, "📺 Small banner attached to R.id.bannerx in " + activity.getClass().getSimpleName());
            }
        } catch (Throwable t) {
            Log.w(TAG, "BhismaAds R.id.bannerx attach failed: " + t.getMessage());
        }
    }
}
