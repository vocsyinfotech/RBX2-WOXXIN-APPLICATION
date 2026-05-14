package rbx.rbxcalculator.calctool.app.robox.helpers;

import android.app.Application;
import android.content.SharedPreferences;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MyApp extends Application {

    public static SharedPreferences prefs;
    private static FirebaseAnalytics analytics;

    @Override
    public void onCreate() {
        super.onCreate();
        analytics = FirebaseAnalytics.getInstance(this);
        prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
    }

    public static FirebaseAnalytics getAnalytics() {
        return analytics;
    }
}
