package com.bhasma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.firebase.analytics.FirebaseAnalytics;

public class InstallReferrerHelper {

    private static final String TAG = "InstallReferrer";

    public static void fetchIfNeeded(final Context context) {
        if (context == null) return;
        if (SharPerf.getInstallReferrerFetched(context)) return;

        final InstallReferrerClient client = InstallReferrerClient.newBuilder(context.getApplicationContext()).build();
        try {
            client.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    String referrer = "";
                    String status = "unknown";
                    try {
                        if (responseCode == InstallReferrerClient.InstallReferrerResponse.OK) {
                            ReferrerDetails details = client.getInstallReferrer();
                            referrer = details != null ? details.getInstallReferrer() : "";
                            if (!TextUtils.isEmpty(referrer)) {
                                SharPerf.setInstallReferrer(context, referrer);
                                Log.d(TAG, "Install referrer captured: " + referrer);
                                status = "ok_has_data";
                            } else {
                                status = "ok_empty";
                            }
                            SharPerf.setInstallReferrerFetched(context, true);
                        } else if (responseCode == InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED) {
                            status = "feature_not_supported";
                            SharPerf.setInstallReferrerFetched(context, true);
                        } else if (responseCode == InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE) {
                            status = "service_unavailable";
                            SharPerf.setInstallReferrerFetched(context, true);
                        } else {
                            status = "response_code_" + responseCode;
                        }
                        logReferrerResult(context, status, referrer);
                    } catch (Exception e) {
                        Log.w(TAG, "Failed to read install referrer", e);
                        logReferrerResult(context, "exception", "");
                    } finally {
                        try {
                            client.endConnection();
                        } catch (Exception ignored) {
                        }
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                }
            });
        } catch (Exception e) {
            Log.w(TAG, "Failed to connect to install referrer service", e);
            logReferrerResult(context, "connect_exception", "");
        }
    }

    private static void logReferrerResult(Context ctx, String status, String referrer) {
        try {
            FirebaseAnalytics fa = FirebaseAnalytics.getInstance(ctx.getApplicationContext());
            Bundle b = new Bundle();
            b.putString("status", status);
            b.putBoolean("has_referrer", !TextUtils.isEmpty(referrer));
            String utmSource = readParam(referrer, "utm_source");
            String utmCampaign = readParam(referrer, "utm_campaign");
            String utmMedium = readParam(referrer, "utm_medium");
            String utmContent = readParam(referrer, "utm_content");
            String utmAd = readParam(referrer, "utm_ad");
            String gclid = readParam(referrer, "gclid");
            if (TextUtils.isEmpty(gclid)) gclid = readParam(referrer, "gbraid");
            if (TextUtils.isEmpty(gclid)) gclid = readParam(referrer, "wbraid");

            b.putString("utm_source", utmSource);
            b.putString("utm_campaign", utmCampaign);
            b.putString("utm_medium", utmMedium);
            b.putString("utm_content", utmContent);
            b.putString("utm_ad", utmAd);
            b.putString("gclid", gclid);
            b.putBoolean("has_utm_campaign", !TextUtils.isEmpty(utmCampaign));
            b.putBoolean("has_gclid", !TextUtils.isEmpty(gclid));
            b.putBoolean("is_organic", "google-play".equals(utmSource) || "(not set)".equals(utmSource));
            fa.logEvent("install_referrer_captured", b);

            // Set user properties so EVERY future event (cct_open, purchase, screen_view, etc.)
            // is automatically tagged with this user's attribution. Lets you slice/filter
            // any report by gclid or campaign without re-logging on every event.
            fa.setUserProperty("attr_source", truncate(utmSource));
            fa.setUserProperty("attr_campaign", truncate(utmCampaign));
            fa.setUserProperty("attr_medium", truncate(utmMedium));
            fa.setUserProperty("user_gclid", truncate(gclid));
            fa.setUserProperty("install_status", classifyInstall(utmSource, utmCampaign, gclid));
        } catch (Exception ignored) {
        }
    }

    private static String readParam(String referrer, String key) {
        if (TextUtils.isEmpty(referrer)) return "";
        try {
            Uri uri = Uri.parse("https://x/?" + referrer);
            String value = uri.getQueryParameter(key);
            return value == null ? "" : value;
        } catch (Exception e) {
            return "";
        }
    }

    private static String truncate(String s) {
        if (TextUtils.isEmpty(s)) return "(none)";
        return s.length() > 36 ? s.substring(0, 36) : s;
    }

    private static String classifyInstall(String utmSource, String utmCampaign, String gclid) {
        if (TextUtils.isEmpty(utmSource) && TextUtils.isEmpty(utmCampaign) && TextUtils.isEmpty(gclid)) {
            return "empty";
        }
        if ("google-play".equals(utmSource) && TextUtils.isEmpty(utmCampaign) && TextUtils.isEmpty(gclid)) {
            return "organic";
        }
        if (!TextUtils.isEmpty(utmCampaign)) return "paid_with_campaign";
        if (!TextUtils.isEmpty(gclid)) return "paid_with_gclid_only";
        return "unknown";
    }

    public static String paramFromReferrer(Context context, String key) {
        String referrer = SharPerf.getInstallReferrer(context);
        if (TextUtils.isEmpty(referrer) || TextUtils.isEmpty(key)) return "";
        try {
            Uri uri = Uri.parse("https://x/?" + referrer);
            String value = uri.getQueryParameter(key);
            return value == null ? "" : value;
        } catch (Exception e) {
            return "";
        }
    }
}
