package com.bhasma;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.browser.customtabs.CustomTabsIntent;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;


public class QurekaAds {
    public void showToolBarAds(Activity activity) {
    }


    public static QurekaAds getInstance() {
        return new QurekaAds();
    }

    public void loadQurekaBanner(Context cont_ads, ViewGroup viewGroup) {

        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(cont_ads)
                .inflate(R.layout.ads_qureka_banner, null);
        viewGroup.removeAllViews();
        viewGroup.addView(frameLayout);
        ImageView imageView = (ImageView) viewGroup.findViewById(R.id.QurekaAds_banner);
        Random random = new Random();

        ArrayList arrayList = new ArrayList(Arrays.asList(SharPerf.getqurekaBanner(cont_ads).split(",")));

        Glide.with(cont_ads).load((String) arrayList.get(random.nextInt(arrayList.size()))).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                QurekaAds.QurekaBanner(cont_ads, view);
            }
        });
    }


    static void QurekaBanner(Context cont_ads, View view) {
        try {
            logClickEvents(cont_ads, "banner", "qureka_banner");
            String url = appendUtm(cont_ads, SharPerf.getredirectLink_banner(cont_ads));
            launchCct(cont_ads, "banner", "qureka_banner", url, Color.parseColor("#FFFFFF"));
        } catch (Exception unused) {
        }
    }

    private static String appendUtm(Context ctx, String base) {
        if (TextUtils.isEmpty(base)) return base;
        String utm = SharPerf.getutm_link(ctx);
        if (TextUtils.isEmpty(utm) || "0".equals(utm) || "id".equals(utm)) return base;
        String resolved = resolvePlaceholders(ctx, utm);
        String url = base + (base.contains("?") ? "&" : "?") + resolved;
        // App Campaigns often pass gclid instead of utm_campaign — forward it too so
        // the website can join to Google Ads even when utm_campaign is empty.
        String gclid = InstallReferrerHelper.paramFromReferrer(ctx, "gclid");
        if (TextUtils.isEmpty(gclid)) gclid = InstallReferrerHelper.paramFromReferrer(ctx, "gbraid");
        if (TextUtils.isEmpty(gclid)) gclid = InstallReferrerHelper.paramFromReferrer(ctx, "wbraid");
        if (!TextUtils.isEmpty(gclid)) {
            url = url + "&gclid=" + Uri.encode(gclid);
        }
        return url;
    }

    private static final double PROXY_PURCHASE_VALUE = 0.10;
    private static final String PROXY_CURRENCY = "USD";

    public static volatile long cctOpenedAtMs = 0L;
    public static volatile String cctOpenedFormat = "";
    public static volatile String cctOpenedAdUnit = "";

    private static void logClickEvents(Context ctx, String adFormat, String adUnit) {
        cctOpenedAtMs = System.currentTimeMillis();
        cctOpenedFormat = adFormat;
        cctOpenedAdUnit = adUnit;

        try {
            FirebaseAnalytics fa = FirebaseAnalytics.getInstance(ctx.getApplicationContext());

            String utmCampaign = InstallReferrerHelper.paramFromReferrer(ctx, "utm_campaign");
            String utmSource = InstallReferrerHelper.paramFromReferrer(ctx, "utm_source");
            String gclid = InstallReferrerHelper.paramFromReferrer(ctx, "gclid");
            if (TextUtils.isEmpty(gclid)) gclid = InstallReferrerHelper.paramFromReferrer(ctx, "gbraid");
            if (TextUtils.isEmpty(gclid)) gclid = InstallReferrerHelper.paramFromReferrer(ctx, "wbraid");
            String referrerState = classifyReferrer(utmSource, utmCampaign, gclid);

            Bundle impr = new Bundle();
            impr.putString(FirebaseAnalytics.Param.AD_FORMAT, adFormat);
            impr.putString(FirebaseAnalytics.Param.AD_UNIT_NAME, adUnit);
            impr.putString(FirebaseAnalytics.Param.AD_SOURCE, "qureka");
            impr.putDouble(FirebaseAnalytics.Param.VALUE, PROXY_PURCHASE_VALUE);
            impr.putString(FirebaseAnalytics.Param.CURRENCY, PROXY_CURRENCY);
            fa.logEvent(FirebaseAnalytics.Event.AD_IMPRESSION, impr);

            Bundle click = new Bundle();
            click.putString("ad_format", adFormat);
            click.putString("ad_unit", adUnit);
            fa.logEvent("ad_click_qureka", click);

            Bundle pur = new Bundle();
            pur.putString(FirebaseAnalytics.Param.ITEM_ID, "qureka_" + adUnit);
            pur.putDouble(FirebaseAnalytics.Param.VALUE, PROXY_PURCHASE_VALUE);
            pur.putString(FirebaseAnalytics.Param.CURRENCY, PROXY_CURRENCY);
            pur.putString(FirebaseAnalytics.Param.TRANSACTION_ID, "qureka_" + adUnit + "_" + System.currentTimeMillis());
            fa.logEvent(FirebaseAnalytics.Event.PURCHASE, pur);

            Bundle open = new Bundle();
            open.putString("ad_format", adFormat);
            open.putString("ad_unit", adUnit);
            open.putString("campaign_id", utmCampaign);
            open.putString("network", utmSource);
            open.putString("gclid", gclid);
            open.putString("referrer_state", referrerState);
            open.putBoolean("has_utm_campaign", !TextUtils.isEmpty(utmCampaign));
            open.putBoolean("has_gclid", !TextUtils.isEmpty(gclid));
            fa.logEvent("cct_open", open);
        } catch (Exception ignored) {
        }
    }

    private static String classifyReferrer(String utmSource, String utmCampaign, String gclid) {
        if (TextUtils.isEmpty(utmSource) && TextUtils.isEmpty(utmCampaign) && TextUtils.isEmpty(gclid)) {
            return "empty";
        }
        if ("google-play".equals(utmSource) && TextUtils.isEmpty(utmCampaign) && TextUtils.isEmpty(gclid)) {
            return "organic";
        }
        if (!TextUtils.isEmpty(utmCampaign)) {
            return "paid_with_campaign";
        }
        if (!TextUtils.isEmpty(gclid)) {
            return "paid_with_gclid_only";
        }
        return "unknown";
    }

    /**
     * Single chokepoint for opening a CCT. Adds:
     *  - cct_url_built event before launch (proves URL substitution result)
     *  - cct_launch_success after successful launchUrl
     *  - cct_launch_error in catch
     *  - cct_fallback_used if system browser is used as fallback
     */
    private static void launchCct(Context ctx, String adFormat, String adUnit, String url, int toolbarColor) {
        logUrlBuilt(ctx, adFormat, adUnit, url);
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            try { builder.setToolbarColor(toolbarColor); } catch (Exception ignored) {}
            builder.setShowTitle(true);
            CustomTabsIntent build = builder.build();
            build.intent.setPackage("com.android.chrome");
            build.launchUrl(ctx, Uri.parse(url));
            logLaunchResult(ctx, adFormat, adUnit, "cct_launch_success", null);
            return;
        } catch (Exception e) {
            logLaunchResult(ctx, adFormat, adUnit, "cct_launch_error", e);
        }
        // Fallback: open in any browser the system has
        try {
            Intent fallback = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            fallback.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(fallback);
            logLaunchResult(ctx, adFormat, adUnit, "cct_fallback_used", null);
        } catch (Exception e2) {
            logLaunchResult(ctx, adFormat, adUnit, "cct_fallback_error", e2);
        }
    }

    private static void logUrlBuilt(Context ctx, String adFormat, String adUnit, String url) {
        try {
            FirebaseAnalytics fa = FirebaseAnalytics.getInstance(ctx.getApplicationContext());
            Bundle b = new Bundle();
            b.putString("ad_format", adFormat);
            b.putString("ad_unit", adUnit);
            b.putBoolean("url_empty", TextUtils.isEmpty(url));
            if (!TextUtils.isEmpty(url)) {
                try {
                    Uri u = Uri.parse(url);
                    b.putString("url_host", u.getHost() == null ? "" : u.getHost());
                    b.putString("url_path", u.getPath() == null ? "" : u.getPath());
                    int paramCount = 0;
                    try {
                        paramCount = u.getQueryParameterNames().size();
                    } catch (Exception ignored) {}
                    b.putLong("url_param_count", paramCount);
                    b.putBoolean("url_has_utm_campaign", !TextUtils.isEmpty(u.getQueryParameter("utm_campaign")));
                    b.putBoolean("url_has_gclid", !TextUtils.isEmpty(u.getQueryParameter("gclid")));
                    b.putBoolean("url_has_utm_source", !TextUtils.isEmpty(u.getQueryParameter("utm_source")));
                } catch (Exception ignored) {}
                b.putLong("url_length", url.length());
            }
            fa.logEvent("cct_url_built", b);
        } catch (Exception ignored) {}
    }

    private static void logLaunchResult(Context ctx, String adFormat, String adUnit, String eventName, Exception e) {
        try {
            FirebaseAnalytics fa = FirebaseAnalytics.getInstance(ctx.getApplicationContext());
            Bundle b = new Bundle();
            b.putString("ad_format", adFormat);
            b.putString("ad_unit", adUnit);
            if (e != null) {
                String type = e.getClass().getSimpleName();
                String msg = e.getMessage() == null ? "" : e.getMessage();
                if (msg.length() > 80) msg = msg.substring(0, 80);
                b.putString("error_type", type);
                b.putString("error_message", msg);
            }
            fa.logEvent(eventName, b);
        } catch (Exception ignored) {}
    }

    private static String resolvePlaceholders(Context ctx, String template) {
        String network = InstallReferrerHelper.paramFromReferrer(ctx, "utm_source");
        String placement = InstallReferrerHelper.paramFromReferrer(ctx, "utm_medium");
        if (TextUtils.isEmpty(placement)) placement = InstallReferrerHelper.paramFromReferrer(ctx, "utm_placement");
        String keyword = InstallReferrerHelper.paramFromReferrer(ctx, "utm_term");
        if (TextUtils.isEmpty(keyword)) keyword = InstallReferrerHelper.paramFromReferrer(ctx, "utm_keyword");
        String adgroupid = InstallReferrerHelper.paramFromReferrer(ctx, "utm_content");
        if (TextUtils.isEmpty(adgroupid)) adgroupid = InstallReferrerHelper.paramFromReferrer(ctx, "utm_adgroup");
        String campaignid = InstallReferrerHelper.paramFromReferrer(ctx, "utm_campaign");
        String creative = InstallReferrerHelper.paramFromReferrer(ctx, "utm_ad");
        if (TextUtils.isEmpty(creative)) creative = InstallReferrerHelper.paramFromReferrer(ctx, "utm_creative");
        // Prefer Google Ads' {device} value from install referrer ("m"/"t"/"c"),
        // fall back to actual device model when no referrer data (e.g., sideloaded install).
        String device = InstallReferrerHelper.paramFromReferrer(ctx, "utm_device");
        if (TextUtils.isEmpty(device)) {
            device = Build.MODEL == null ? "" : Build.MODEL;
        }

        return template
                .replace("{network}", Uri.encode(network))
                .replace("{placement}", Uri.encode(placement))
                .replace("{keyword}", Uri.encode(keyword))
                .replace("{adgroupid}", Uri.encode(adgroupid))
                .replace("{campaignid}", Uri.encode(campaignid))
                .replace("{creative}", Uri.encode(creative))
                .replace("{device}", Uri.encode(device));
    }

    public void loadQurekaNativeSmall(Context cont_ads, ViewGroup viewGroup) {


        try {
            ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(cont_ads).inflate(R.layout.ads_qureka_native_small, null);

            if (viewGroup2 != null) {
                viewGroup.removeAllViews();
            }


            viewGroup.addView(viewGroup2);
            ImageView imageView = (ImageView) viewGroup2.findViewById(R.id.imageView);
            GifImageView gifImageView = (GifImageView) viewGroup.findViewById(R.id.gifimagview);
            TextView textView3 = (TextView) viewGroup.findViewById(R.id.actionbutton);
            TextView textView = (TextView) viewGroup.findViewById(R.id.description);
            TextView textView2 = (TextView) viewGroup.findViewById(R.id.shortdiscrip);
            RelativeLayout ad_view = (RelativeLayout) viewGroup.findViewById(R.id.ad_view);

            final String str = SharPerf.getnative_small_redirectLink(cont_ads);
            Random random = new Random();
            ArrayList arrayList = new ArrayList(Arrays.asList(SharPerf.getqurekaNativeSmall(cont_ads).split(",")));
            Glide.with(cont_ads).load((String) arrayList.get(random.nextInt(arrayList.size()))).into(imageView);
            String str1 =  SharPerf.getbutton_title(cont_ads);
            String str2 = SharPerf.getdisc(cont_ads);
            String str5 = SharPerf.getshort_disc(cont_ads);

            textView3.setText("" + str1);
            textView2.setText("" + str5);
            textView.setText("" + str2);

            Glide.with(cont_ads).load((String) arrayList.get(random.nextInt(arrayList.size()))).into(gifImageView);
            ad_view.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view) {
                    QurekaAds.QurekaNativeSmall(cont_ads, str, view);
                }
            });
        } catch (Exception e) {

        }

    }



    @SuppressLint("ResourceType")
    static void QurekaNativeSmall(Context cont_ads, String str, View view) {
        try {
            logClickEvents(cont_ads, "native", "qureka_native_small");
            String url = appendUtm(cont_ads, str);
            int color;
            try {
                color = Color.parseColor(cont_ads.getString(R.color.black));
            } catch (Exception e) {
                color = Color.BLACK;
            }
            launchCct(cont_ads, "native", "qureka_native_small", url, color);
        } catch (Exception unused) {
        }
    }

    public void loadQurekaNative(Context cont_ads, ViewGroup viewGroup) {

        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(cont_ads)
                .inflate(R.layout.ads_qureka_native_big, null);

        try {
            viewGroup.removeAllViews();
            viewGroup.addView(viewGroup2);



            ImageView imageView = (ImageView) viewGroup.findViewById(R.id.imageView);
            GifImageView gifImageView = (GifImageView) viewGroup.findViewById(R.id.gifimagview);
            TextView textView = (TextView) viewGroup.findViewById(R.id.description);
            TextView textView2 = (TextView) viewGroup.findViewById(R.id.shortdiscrip);
            TextView textView3 = (TextView) viewGroup.findViewById(R.id.actionbutton);
            String str = SharPerf.getbutton_title(cont_ads);
            String str2 = SharPerf.getdisc(cont_ads);
            String str3 = SharPerf.getimage(cont_ads);
            String str4 = SharPerf.getimage2(cont_ads);
            String str5 = SharPerf.getshort_disc(cont_ads);
            final String str6 = SharPerf.getnative_redirectLink(cont_ads);
            textView3.setText("" + str);
            textView2.setText("" + str5);
            textView.setText("" + str2);
            Random random = new Random();
            ArrayList arrayList = new ArrayList(Arrays.asList(SharPerf.getqurekaNative(cont_ads).split(",")));
            Glide.with(cont_ads).load((String) arrayList.get(random.nextInt(arrayList.size()))).into(imageView);
            ArrayList arrayList2 = new ArrayList(Arrays.asList(SharPerf.getqurekaNativeGif(cont_ads).split(",")));
            Glide.with(cont_ads).load((String) arrayList2.get(random.nextInt(arrayList2.size()))).into(gifImageView);


            ((LinearLayout) viewGroup.findViewById(R.id.QurekaAds_native)).setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view2) {
                    QurekaAds.QurekaNative(cont_ads, str6, view2);
                }
            });
        } catch (Exception e) {

        }

    }

    public void newloadQurekaNative(Context cont_ads, ViewGroup viewGroup) {

        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(cont_ads)
                .inflate(R.layout.new_ads_qureka_native_big, null);

        try {
            viewGroup.removeAllViews();
            viewGroup.addView(viewGroup2);



            ImageView imageView = (ImageView) viewGroup.findViewById(R.id.imageView);
            GifImageView gifImageView = (GifImageView) viewGroup.findViewById(R.id.gifimagview);
            TextView textView = (TextView) viewGroup.findViewById(R.id.description);
            TextView textView2 = (TextView) viewGroup.findViewById(R.id.shortdiscrip);
            TextView textView3 = (TextView) viewGroup.findViewById(R.id.actionbutton);
            String str = SharPerf.getbutton_title(cont_ads);
            String str2 = SharPerf.getdisc(cont_ads);
            String str3 = SharPerf.getimage(cont_ads);
            String str4 = SharPerf.getimage2(cont_ads);
            String str5 = SharPerf.getshort_disc(cont_ads);
            final String str6 = SharPerf.getnative_redirectLink(cont_ads);
            textView3.setText("" + str);
            textView2.setText("" + str5);
            textView.setText("" + str2);
            Random random = new Random();
            ArrayList arrayList = new ArrayList(Arrays.asList(SharPerf.getqurekaNative(cont_ads).split(",")));
            Glide.with(cont_ads).load((String) arrayList.get(random.nextInt(arrayList.size()))).into(imageView);
            ArrayList arrayList2 = new ArrayList(Arrays.asList(SharPerf.getqurekaNativeGif(cont_ads).split(",")));
            Glide.with(cont_ads).load((String) arrayList2.get(random.nextInt(arrayList2.size()))).into(gifImageView);


            ((LinearLayout) viewGroup.findViewById(R.id.QurekaAds_native)).setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view2) {
                    QurekaAds.QurekaNative(cont_ads, str6, view2);
                }
            });
        } catch (Exception e) {

        }

    }


    public void full_big_loadQurekaNative(Context cont_ads, ViewGroup viewGroup) {

        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(cont_ads)
                .inflate(R.layout.big_ads_qureka_native_big, null);

        try {
            viewGroup.removeAllViews();
            viewGroup.addView(viewGroup2);



            ImageView imageView = (ImageView) viewGroup.findViewById(R.id.imagview);
            GifImageView gifImageView = (GifImageView) viewGroup.findViewById(R.id.gifimagview);
            TextView textView = (TextView) viewGroup.findViewById(R.id.description);
            TextView textView2 = (TextView) viewGroup.findViewById(R.id.shortdiscrip);
            TextView textView3 = (TextView) viewGroup.findViewById(R.id.actionbutton);
            String str = SharPerf.getbutton_title(cont_ads);
            String str2 = SharPerf.getdisc(cont_ads);
            String str3 = SharPerf.getimage(cont_ads);
            String str4 = SharPerf.getimage2(cont_ads);
            String str5 = SharPerf.getshort_disc(cont_ads);
            final String str6 = SharPerf.getnative_redirectLink(cont_ads);
            textView3.setText("" + str);
            textView2.setText("" + str5);
            textView.setText("" + str2);
            Random random = new Random();
            ArrayList arrayList = new ArrayList(Arrays.asList(SharPerf.getqurekaNative(cont_ads).split(",")));
            Glide.with(cont_ads).load((String) arrayList.get(random.nextInt(arrayList.size()))).into(imageView);
            ArrayList arrayList2 = new ArrayList(Arrays.asList(SharPerf.getqurekaNativeGif(cont_ads).split(",")));
            Glide.with(cont_ads).load((String) arrayList2.get(random.nextInt(arrayList2.size()))).into(gifImageView);


            ((LinearLayout) viewGroup.findViewById(R.id.QurekaAds_native)).setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view2) {
                    QurekaAds.QurekaNative(cont_ads, str6, view2);
                }
            });
        } catch (Exception e) {

        }

    }

    public void app_open_full_big_loadQurekaNative(Context cont_ads, ViewGroup viewGroup) {

        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(cont_ads).inflate(R.layout.appopen_big_ads_qureka_native_big, null);
        try {
            viewGroup.removeAllViews();
            viewGroup.addView(viewGroup2);

            ImageView imageView = (ImageView) viewGroup.findViewById(R.id.imageView);
            GifImageView gifImageView = (GifImageView) viewGroup.findViewById(R.id.gifimagview);
            TextView textView = (TextView) viewGroup.findViewById(R.id.description);
            TextView textView2 = (TextView) viewGroup.findViewById(R.id.shortdiscrip);
            TextView textView3 = (TextView) viewGroup.findViewById(R.id.actionbutton);
            String str = SharPerf.getbutton_title(cont_ads);
            String str2 = SharPerf.getdisc(cont_ads);
            String str3 = SharPerf.getimage(cont_ads);
            String str4 = SharPerf.getimage2(cont_ads);
            String str5 = SharPerf.getshort_disc(cont_ads);
            final String str6 = SharPerf.getnative_redirectLink(cont_ads);
            textView3.setText("" + str);
            textView2.setText("" + str5);
            textView.setText("" + str2);
            Random random = new Random();
            ArrayList arrayList = new ArrayList(Arrays.asList(SharPerf.getqurekaNative(cont_ads).split(",")));
            Glide.with(cont_ads).load((String) arrayList.get(random.nextInt(arrayList.size()))).into(imageView);
            ArrayList arrayList2 = new ArrayList(Arrays.asList(SharPerf.getqurekaNativeGif(cont_ads).split(",")));
            Glide.with(cont_ads).load((String) arrayList2.get(random.nextInt(arrayList2.size()))).into(gifImageView);


            ((LinearLayout) viewGroup.findViewById(R.id.QurekaAds_native)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    QurekaAds.QurekaNative(cont_ads, str6, view2);
                }
            });
        } catch (Exception e) {
        }

    }



    @SuppressLint("ResourceType")
    public static void QurekaNative(Context cont_ads, String str, View view) {
        try {
            logClickEvents(cont_ads, "native", "qureka_native");
            String url = appendUtm(cont_ads, str);
            int color;
            try {
                color = Color.parseColor(cont_ads.getString(R.color.white));
            } catch (Exception e) {
                color = Color.WHITE;
            }
            launchCct(cont_ads, "native", "qureka_native", url, color);
        } catch (Exception unused) {
        }
    }

    public void loadQurekaInter(Context cont_ads) {
        try {
            logClickEvents(cont_ads, "interstitial", "qureka_interstitial");
            String url = appendUtm(cont_ads, SharPerf.getinter_redirectLink(cont_ads));
            launchCct(cont_ads, "interstitial", "qureka_interstitial", url, Color.parseColor("#FFFFFF"));
        } catch (Exception unused) {
        }
    }

}
