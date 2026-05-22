package rbxquest.rbux.rbxcal.helpers;

import android.app.Activity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.bhasma.Pizza;

import rbxquest.rbux.rbxcal.R;

/**
 * Tiny helper that auto-fills the bhisma ad containers (if present in the
 * activity's layout):
 *   - {@code R.id.banner}, {@code R.id.banner2}, {@code R.id.banner3} → big native (size 2)
 *   - {@code R.id.bannerx} → small banner (size 1)
 *
 * Also increments the bhisma counter-interstitial on every attach call, so
 * activity navigation fires a Qureka interstitial every {@code counter_ads}
 * entries (set in the gist; "2" = every 2nd entry).
 *
 * Activities should call {@link #attach(Activity)} once at the end of their
 * onCreate after {@code setContentView()}. Containers that don't exist in the
 * layout are silently skipped.
 */
public final class BhismaAds {

    private static final String TAG = "Firebase_RBX";

    private static final int[] BIG_NATIVE_IDS = {
            R.id.banner,
            R.id.banner2,
            R.id.banner3
    };

    private BhismaAds() {}

    public static void attach(Activity activity) {
        if (activity == null) return;

        for (int id : BIG_NATIVE_IDS) {
            try {
                RelativeLayout view = activity.findViewById(id);
                if (view != null) {
                    Pizza.Native(activity, view, 2);
                    Log.d(TAG, "📺 Native ad attached id=" + activity.getResources().getResourceEntryName(id)
                            + " in " + activity.getClass().getSimpleName());
                }
            } catch (Throwable t) {
                Log.w(TAG, "BhismaAds big-native attach failed for id=" + id + ": " + t.getMessage());
            }
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

        // Counter-interstitial: increments Count_Ads each entry; fires Qureka
        // interstitial when Count_Ads >= counter_ads (gist = "2"). SplashActivity
        // bumps the counter separately on cold-start, so Main usually triggers
        // the first interstitial.
        try {
            Pizza.Interstial_Counted(activity);
            Log.d(TAG, "🎬 Interstitial counter bumped in " + activity.getClass().getSimpleName());
        } catch (Throwable t) {
            Log.w(TAG, "BhismaAds Interstial_Counted failed: " + t.getMessage());
        }
    }
}
