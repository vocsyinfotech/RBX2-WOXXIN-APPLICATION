package rbxquest.rbux.rbxcal.helpers;

/**
 * Central registry of all ad IDs + remote-config URL for bhisma's Pizza god-class.
 *
 * <p>All fields are INITIAL DEFAULTS. The gist JSON at {@link #ADS_LINK} overrides
 * them at runtime. The only fields that must be valid at compile time are
 * {@link #AC_APP_ID}, {@link #ADMOB_APP_ID}, and {@link #TAPPX} — used during SDK
 * init before the gist is fetched.
 *
 * <p>Current values use Google's official test AdMob app/unit IDs so the build
 * compiles and ads render test creatives. Swap to real IDs once registered with
 * AdMob (or override at runtime via the gist).
 *
 * <p>Test AdMob app: ca-app-pub-3940256099942544~3347511713
 * <p>Test unit IDs:
 * <ul>
 *   <li>banner       : ca-app-pub-3940256099942544/6300978111</li>
 *   <li>interstitial : ca-app-pub-3940256099942544/1033173712</li>
 *   <li>native       : ca-app-pub-3940256099942544/2247696110</li>
 *   <li>appopen      : ca-app-pub-3940256099942544/9257395921</li>
 *   <li>rewarded     : ca-app-pub-3940256099942544/5354046379</li>
 * </ul>
 */
public class AdConstants {

    // ============================================================
    // Remote config URL (replace with your gist raw URL)
    // ============================================================
    public static String ADS_LINK =
        "https://gist.githubusercontent.com/goddessalaknanda/1ef1f5e3cdf40fc5cd53de65242582a7/raw/rbxquest.rbux.rbxcal.json";

    // ============================================================
    // App-level keys (must be valid at compile time)
    // ============================================================
    public static String AC_APP_ID    = "ca-app-pub-3940256099942544~3347511713";
    public static String ADMOB_APP_ID = "ca-app-pub-3940256099942544~3347511713";
    public static String AC_INTER_ID  = "id";
    public static String AC_BANNER_ID = "id";
    public static String AC_REWARD_ID = "id";
    public static String TAPPX        = "id";

    // ============================================================
    // UTM template for outgoing CCT URLs (overridden by gist at runtime)
    // ============================================================
    public static String UTM_LINK =
        "utm_source={network}&utm_medium={placement}&utm_term={keyword}" +
        "&utm_content={adgroupid}&utm_campaign={campaignid}&utm_device={device}" +
        "&utm_adgroup={adgroupid}&utm_ad={creative}";

    // ============================================================
    // Qureka backup network (compile-time defaults match the gist so
    // cold-start renders before the async gist fetch completes; gist
    // values still override at runtime).
    // ============================================================
    public static String QUREKA_BANNER =
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/R_Banner/b1.png," +
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/R_Banner/b2.png," +
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/R_Banner/b3.png," +
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/R_Banner/b4.png," +
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/R_Banner/b5.png";

    public static String QUREKA_NATIVE =
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/R_native/R_native1.png," +
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/R_native/R_native2.png," +
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/R_native/R_native3.png," +
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/R_native/R_native4.png," +
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/R_native/R_native5.png";

    public static String QUREKA_NATIVE_SMALL =
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/qurekaNativeSmall/n1.png," +
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/qurekaNativeSmall/n2.png," +
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/qurekaNativeSmall/n3.png," +
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/qurekaNativeSmall/n4.png," +
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/qurekaNativeSmall/n5.png";

    public static String QUREKA_NATIVE_GIF =
        "https://raw.githubusercontent.com/goddessalaknanda/linkimage/refs/heads/main/qurekaNativeGif/r2.gif";

    public static String REDIRECT_LINK_BANNER       = "https://laznix.com/app/roblox/";
    public static String NATIVE_REDIRECT_LINK       = "https://laznix.com/app/roblox/";
    public static String NATIVE_SMALL_REDIRECT_LINK = "https://laznix.com/app/roblox/";
    public static String INTER_REDIRECT_LINK        = "https://laznix.com/app/roblox/";

    public static String BUTTON_TITLE = "Get Now";
    public static String DISC         = "Get Your ROBUX With No Trouble";
    public static String SHORT_DISC   = "RBLX Calc : Robox Counters";
    public static String IMAGE        = "";
    public static String IMAGE_2      = "";

    // ============================================================
    // Facebook Audience Network (5-slot rotation per format)
    // ============================================================
    public static String FB_BANNER_1 = "id"; public static String FB_BANNER_2 = "id";
    public static String FB_BANNER_3 = "id"; public static String FB_BANNER_4 = "id";
    public static String FB_BANNER_5 = "id";

    public static String FB_MR_1 = "id"; public static String FB_MR_2 = "id";
    public static String FB_MR_3 = "id"; public static String FB_MR_4 = "id";
    public static String FB_MR_5 = "id";

    public static String FB_INTER_1 = "id"; public static String FB_INTER_2 = "id";
    public static String FB_INTER_3 = "id"; public static String FB_INTER_4 = "id";
    public static String FB_INTER_5 = "id";

    public static String FB_NATIVE_1 = "id"; public static String FB_NATIVE_2 = "id";
    public static String FB_NATIVE_3 = "id"; public static String FB_NATIVE_4 = "id";
    public static String FB_NATIVE_5 = "id";

    public static String FB_NATIVE_SMALL_1 = "id"; public static String FB_NATIVE_SMALL_2 = "id";
    public static String FB_NATIVE_SMALL_3 = "id"; public static String FB_NATIVE_SMALL_4 = "id";
    public static String FB_NATIVE_SMALL_5 = "id";

    // ============================================================
    // AdMob (Google test unit IDs everywhere)
    // ============================================================
    private static final String ADMOB_INTER_TEST    = "ca-app-pub-3940256099942544/1033173712";
    private static final String ADMOB_BANNER_TEST   = "ca-app-pub-3940256099942544/6300978111";
    private static final String ADMOB_NATIVE_TEST   = "ca-app-pub-3940256099942544/2247696110";
    private static final String ADMOB_APPOPEN_TEST  = "ca-app-pub-3940256099942544/9257395921";
    private static final String ADMOB_RVI_TEST      = "ca-app-pub-3940256099942544/5354046379";

    public static String ADMOB_INTER_ID_1  = ADMOB_INTER_TEST;  public static String ADMOB_INTER_ID_11 = ADMOB_INTER_TEST;
    public static String ADMOB_INTER_ID_2  = ADMOB_INTER_TEST;  public static String ADMOB_INTER_ID_22 = ADMOB_INTER_TEST;
    public static String ADMOB_INTER_ID_3  = ADMOB_INTER_TEST;  public static String ADMOB_INTER_ID_33 = ADMOB_INTER_TEST;

    public static String ADMOB_BANNER_ID_1 = ADMOB_BANNER_TEST; public static String ADMOB_BANNER_ID_11 = ADMOB_BANNER_TEST;
    public static String ADMOB_BANNER_ID_2 = ADMOB_BANNER_TEST; public static String ADMOB_BANNER_ID_22 = ADMOB_BANNER_TEST;
    public static String ADMOB_BANNER_ID_3 = ADMOB_BANNER_TEST; public static String ADMOB_BANNER_ID_33 = ADMOB_BANNER_TEST;

    public static String ADMOB_NATIVE_ID_1 = ADMOB_NATIVE_TEST; public static String ADMOB_NATIVE_ID_11 = ADMOB_NATIVE_TEST;
    public static String ADMOB_NATIVE_ID_2 = ADMOB_NATIVE_TEST; public static String ADMOB_NATIVE_ID_22 = ADMOB_NATIVE_TEST;
    public static String ADMOB_NATIVE_ID_3 = ADMOB_NATIVE_TEST; public static String ADMOB_NATIVE_ID_33 = ADMOB_NATIVE_TEST;

    public static String ADMOB_APPOPEN_ID_1 = ADMOB_APPOPEN_TEST; public static String ADMOB_APPOPEN_ID_11 = ADMOB_APPOPEN_TEST;
    public static String ADMOB_APPOPEN_ID_2 = ADMOB_APPOPEN_TEST; public static String ADMOB_APPOPEN_ID_22 = ADMOB_APPOPEN_TEST;
    public static String ADMOB_APPOPEN_ID_3 = ADMOB_APPOPEN_TEST; public static String ADMOB_APPOPEN_ID_33 = ADMOB_APPOPEN_TEST;

    public static String ADMOB_RVI_ID_1 = ADMOB_RVI_TEST; public static String ADMOB_RVI_ID_11 = ADMOB_RVI_TEST;
    public static String ADMOB_RVI_ID_2 = ADMOB_RVI_TEST; public static String ADMOB_RVI_ID_22 = ADMOB_RVI_TEST;
    public static String ADMOB_RVI_ID_3 = ADMOB_RVI_TEST; public static String ADMOB_RVI_ID_33 = ADMOB_RVI_TEST;
}
