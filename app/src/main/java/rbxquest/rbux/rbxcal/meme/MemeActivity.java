package rbxquest.rbux.rbxcal.meme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import rbxquest.rbux.rbxcal.R;
import rbxquest.rbux.rbxcal.helpers.BhismaAds;
import rbxquest.rbux.rbxcal.helpers.MyApp;

public class MemeActivity extends AppCompatActivity {

    private static final String TAG = "Firebase_RBX";

    private static final int[] MEME_IMAGES = {
            R.drawable.app_banner_1, R.drawable.app_banner_2,
            R.drawable.app_banner_3, R.drawable.app_banner_4,
            R.drawable.app_banner_5, R.drawable.app_banner_6,
            R.drawable.app_banner_7, R.drawable.app_banner_8
    };

    private static final String[] MEME_CAPTIONS = {
            "When you finally get 100 Robux 😂",
            "Me waiting for free Robux 💀",
            "Trading be like... 🤣",
            "When your friend has more Robux 😤",
            "That DevEx money hitting different 💰",
            "Roblox economy explained 📊",
            "When the game costs 500 Robux 😭",
            "Rich Roblox players be like 👑"
    };

    private static final int TOTAL = MEME_IMAGES.length;

    private ViewPager2      viewPager;
    private TextView        tvCounter;
    private TextView        tvCaption;
    private LinearLayout    dotsContainer;
    private final boolean[] liked = new boolean[TOTAL];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme);
        BhismaAds.attach(this);

        Log.d(TAG, "😂 MemeActivity opened");
        MyApp.logEvent("screen_view", "screen_name", "memes");

        viewPager    = findViewById(R.id.viewPagerMeme);
        tvCounter    = findViewById(R.id.tvMemeCounter);
        tvCaption    = findViewById(R.id.tvMemeCaption);
        dotsContainer = findViewById(R.id.dotsContainer);

        // Wire up adapter
        viewPager.setAdapter(new MemeAdapter(MEME_IMAGES));
        viewPager.setOffscreenPageLimit(1);

        // Page-change callback → update counter, dots, caption
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateUI(position);
                Log.d(TAG, "📸 Meme " + (position + 1) + "/" + TOTAL
                        + " — " + MEME_CAPTIONS[position]);
                MyApp.logEvent("meme_viewed", "index", String.valueOf(position + 1));
            }
        });

        // Build initial dot indicators
        buildDots(TOTAL);
        updateUI(0);

        // Prev / Next buttons
        findViewById(R.id.btnPrevMeme).setOnClickListener(v -> {
            int cur = viewPager.getCurrentItem();
            if (cur > 0) viewPager.setCurrentItem(cur - 1, true);
            else         viewPager.setCurrentItem(TOTAL - 1, true); // wrap around
        });

        findViewById(R.id.btnNextMeme).setOnClickListener(v -> {
            int cur = viewPager.getCurrentItem();
            if (cur < TOTAL - 1) viewPager.setCurrentItem(cur + 1, true);
            else                  viewPager.setCurrentItem(0, true);    // wrap around
        });

        // Like button — toggles per meme, with visual feedback
        ImageButton btnLike = null; // defined as AppCompatButton in layout
        findViewById(R.id.btnLike).setOnClickListener(v -> {
            int cur = viewPager.getCurrentItem();
            liked[cur] = !liked[cur];
            updateLikeButton(cur);
            Toast.makeText(this,
                    liked[cur] ? "❤ Liked!" : "Like removed",
                    Toast.LENGTH_SHORT).show();
            MyApp.logEvent("meme_like", "index", String.valueOf(cur + 1));
        });

        // Share button (toolbar)
        findViewById(R.id.btnShare).setOnClickListener(v -> shareMeme(viewPager.getCurrentItem()));

        // Back
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }

    // ── UI helpers ────────────────────────────────────────────────────────────

    private void updateUI(int position) {
        tvCounter.setText((position + 1) + " / " + TOTAL);
        tvCaption.setText(MEME_CAPTIONS[position]);
        highlightDot(position);
        updateLikeButton(position);
    }

    private void buildDots(int count) {
        dotsContainer.removeAllViews();
        int dotSizePx  = dpToPx(8);
        int dotMarginPx = dpToPx(5);

        for (int i = 0; i < count; i++) {
            android.widget.ImageView dot = new android.widget.ImageView(this);
            dot.setId(i); // tag by index
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dotSizePx, dotSizePx);
            lp.setMargins(dotMarginPx, 0, dotMarginPx, 0);
            dot.setLayoutParams(lp);
            dot.setBackgroundColor(Color.parseColor("#333333")); // inactive
            // Make it a circle via shape
            android.graphics.drawable.GradientDrawable circle =
                    new android.graphics.drawable.GradientDrawable();
            circle.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            circle.setColor(Color.parseColor("#333333"));
            dot.setBackground(circle);
            dotsContainer.addView(dot);
        }
    }

    private void highlightDot(int activeIndex) {
        for (int i = 0; i < dotsContainer.getChildCount(); i++) {
            android.widget.ImageView dot = (android.widget.ImageView) dotsContainer.getChildAt(i);
            android.graphics.drawable.GradientDrawable circle =
                    new android.graphics.drawable.GradientDrawable();
            circle.setShape(android.graphics.drawable.GradientDrawable.OVAL);

            if (i == activeIndex) {
                // Active dot: gold, wider
                circle.setColor(Color.parseColor("#FFD700"));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dpToPx(20), dpToPx(8));
                lp.setMargins(dpToPx(5), 0, dpToPx(5), 0);
                dot.setLayoutParams(lp);
                circle.setCornerRadius(dpToPx(4));
            } else {
                // Inactive dot: dim grey, small circle
                circle.setColor(Color.parseColor("#444444"));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dpToPx(8), dpToPx(8));
                lp.setMargins(dpToPx(5), 0, dpToPx(5), 0);
                dot.setLayoutParams(lp);
            }
            dot.setBackground(circle);
        }
    }

    private void updateLikeButton(int index) {
        androidx.appcompat.widget.AppCompatButton btnLike =
                (androidx.appcompat.widget.AppCompatButton) findViewById(R.id.btnLike);
        if (btnLike == null) return;
        btnLike.setText(liked[index] ? "❤" : "🤍");
    }

    private void shareMeme(int index) {
        String shareText = MEME_CAPTIONS[index]
                + "\n\n😂 Check out more Roblox memes in Rbx Calculator!";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share Meme"));
        MyApp.logEvent("meme_share", "index", String.valueOf(index + 1));
        Log.d(TAG, "📤 Sharing meme " + (index + 1));
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
