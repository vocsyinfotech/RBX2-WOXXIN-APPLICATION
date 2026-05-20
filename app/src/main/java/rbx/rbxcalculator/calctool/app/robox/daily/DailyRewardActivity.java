package rbx.rbxcalculator.calctool.app.robox.daily;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Locale;
import rbx.rbxcalculator.calctool.app.robox.R;
import rbx.rbxcalculator.calctool.app.robox.helpers.MyApp;

public class DailyRewardActivity extends AppCompatActivity {

    private static final String TAG = "Firebase_RBX";

    // Robux reward per day (Day 1–7)
    static final int[] REWARDS = {50, 100, 200, 300, 500, 750, 1000};

    static final String KEY_LAST_CLAIM  = "daily_last_claim_millis";
    public static final String KEY_CLAIMED_DAY = "daily_claimed_day";
    public static final String KEY_TOTAL_ROBUX = "daily_total_robux";

    private int  todayDayIndex;   // 0–6
    private boolean canClaim;
    private boolean alreadyClaimed;

    private final Handler  tickHandler  = new Handler(Looper.getMainLooper());
    private       Runnable tickRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_reward);

        Log.d(TAG, "🎁 DailyRewardActivity opened");
        MyApp.logEvent("screen_view", "screen_name", "daily_reward");

        computeState();
        renderDayCards();
        bindUI();

        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }

    // ── State ────────────────────────────────────────────────────────────────

    private void computeState() {
        long lastClaimMillis = MyApp.prefs.getLong(KEY_LAST_CLAIM, 0);
        int  lastClaimedDay  = MyApp.prefs.getInt(KEY_CLAIMED_DAY, -1);

        if (lastClaimMillis == 0) {
            todayDayIndex  = 0;
            canClaim       = true;
            alreadyClaimed = false;
            return;
        }

        // Normalize to midnight for accurate day-diff
        Calendar last = Calendar.getInstance();
        last.setTimeInMillis(lastClaimMillis);
        last.set(Calendar.HOUR_OF_DAY, 0);
        last.set(Calendar.MINUTE, 0);
        last.set(Calendar.SECOND, 0);
        last.set(Calendar.MILLISECOND, 0);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        long daysDiff = (today.getTimeInMillis() - last.getTimeInMillis()) / 86_400_000L;

        if (daysDiff == 0) {
            // Same day — already claimed
            todayDayIndex  = lastClaimedDay;
            canClaim       = false;
            alreadyClaimed = true;
        } else if (daysDiff == 1) {
            // Next day — continue streak
            todayDayIndex  = (lastClaimedDay + 1) % 7;
            canClaim       = true;
            alreadyClaimed = false;
        } else {
            // Missed a day — streak resets
            todayDayIndex  = 0;
            canClaim       = true;
            alreadyClaimed = false;
            MyApp.prefs.edit().putInt(KEY_CLAIMED_DAY, -1).apply();
        }
    }

    // ── Day cards ────────────────────────────────────────────────────────────

    private void renderDayCards() {
        LinearLayout row1 = findViewById(R.id.dayCardsRow1);
        LinearLayout row2 = findViewById(R.id.dayCardsRow2);
        row1.removeAllViews();
        row2.removeAllViews();

        // Row 2: half-weight spacers to centre 3 cards at same width as row-1 cards
        View spacerStart = new View(this);
        spacerStart.setLayoutParams(new LinearLayout.LayoutParams(0, 1, 0.5f));
        View spacerEnd = new View(this);
        spacerEnd.setLayoutParams(new LinearLayout.LayoutParams(0, 1, 0.5f));

        for (int d = 0; d < 7; d++) {
            View card = buildDayCard(d);
            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(0, dpToPx(88), 1f);
            lp.setMargins(dpToPx(4), 0, dpToPx(4), 0);
            card.setLayoutParams(lp);

            if (d < 4) {
                row1.addView(card);
            } else {
                if (d == 4) row2.addView(spacerStart);
                row2.addView(card);
            }
        }
        row2.addView(spacerEnd);
    }

    private View buildDayCard(int d) {
        boolean isClaimed = alreadyClaimed ? (d <= todayDayIndex) : (d < todayDayIndex);
        boolean isActive  = (d == todayDayIndex);
        boolean isLocked  = (d > todayDayIndex);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(dpToPx(4), dpToPx(8), dpToPx(4), dpToPx(8));

        // Background
        GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(dpToPx(12));
        if (isActive) {
            bg.setColor(Color.parseColor("#2A2000"));
            bg.setStroke(dpToPx(2), Color.parseColor("#FFD700"));
        } else if (isClaimed) {
            bg.setColor(Color.parseColor("#0D200D"));
            bg.setStroke(dpToPx(2), Color.parseColor("#4CAF50"));
        } else {
            bg.setColor(Color.parseColor("#1A1A1A"));
            bg.setStroke(dpToPx(1), Color.parseColor("#2A2A2A"));
        }
        card.setBackground(bg);

        // "Day N" label
        TextView tvDay = new TextView(this);
        tvDay.setText("Day " + (d + 1));
        tvDay.setTextSize(9);
        tvDay.setGravity(Gravity.CENTER);
        tvDay.setTextColor(isLocked ? Color.parseColor("#444444")
                : isActive         ? Color.parseColor("#FFD700")
                : Color.parseColor("#4CAF50"));

        // Reward or checkmark
        TextView tvAmt = new TextView(this);
        tvAmt.setGravity(Gravity.CENTER);
        if (isClaimed && !isActive) {
            tvAmt.setText("✓");
            tvAmt.setTextSize(18);
            tvAmt.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            tvAmt.setText("R$\n" + REWARDS[d]);
            tvAmt.setTextSize(isActive ? 13 : 11);
            tvAmt.setTypeface(null, isActive ? Typeface.BOLD : Typeface.NORMAL);
            tvAmt.setTextColor(isLocked ? Color.parseColor("#3A3A3A")
                    : isActive         ? Color.parseColor("#FFD700")
                    : Color.parseColor("#9E9E9E"));
        }

        card.addView(tvDay);
        card.addView(tvAmt);

        // Pulse animation on active+claimable card
        if (isActive && canClaim) {
            ObjectAnimator px = ObjectAnimator.ofFloat(card, "scaleX", 1f, 1.05f, 1f);
            ObjectAnimator py = ObjectAnimator.ofFloat(card, "scaleY", 1f, 1.05f, 1f);
            px.setDuration(900); px.setRepeatCount(ObjectAnimator.INFINITE);
            py.setDuration(900); py.setRepeatCount(ObjectAnimator.INFINITE);
            px.start(); py.start();
        }

        return card;
    }

    // ── UI binding ───────────────────────────────────────────────────────────

    private void bindUI() {
        TextView tvStreakTitle = findViewById(R.id.tvStreakTitle);
        TextView tvStreakSub   = findViewById(R.id.tvStreakSub);
        TextView tvTotalRobux  = findViewById(R.id.tvTotalRobux);
        TextView tvTodayReward = findViewById(R.id.tvTodayReward);
        androidx.appcompat.widget.AppCompatButton btnClaim = findViewById(R.id.btnClaim);
        View layoutCountdown = findViewById(R.id.layoutCountdown);

        // Streak headline
        int streakCount = alreadyClaimed ? todayDayIndex + 1 : todayDayIndex;
        tvStreakTitle.setText(streakCount > 0 ? "🔥 " + streakCount + " Day Streak!" : "Start Your Streak!");
        tvStreakSub.setText("Day " + (todayDayIndex + 1) + " of 7");

        // Total earned
        long total = MyApp.prefs.getLong(KEY_TOTAL_ROBUX, 0);
        tvTotalRobux.setText("Total Earned: R$ " + total);

        // Today's reward display
        tvTodayReward.setText("R$ " + REWARDS[todayDayIndex]);

        // Claim button + countdown
        if (alreadyClaimed) {
            btnClaim.setText("Claimed ✓  Come Back Tomorrow");
            btnClaim.setEnabled(false);
            btnClaim.setAlpha(0.45f);
            layoutCountdown.setVisibility(View.VISIBLE);
            startCountdown();
        } else {
            btnClaim.setText("Claim  R$ " + REWARDS[todayDayIndex]);
            btnClaim.setEnabled(true);
            btnClaim.setAlpha(1f);
            btnClaim.setOnClickListener(v -> claimReward());
            layoutCountdown.setVisibility(View.GONE);
            stopCountdown();
        }
    }

    // ── Countdown timer ──────────────────────────────────────────────────────

    private void startCountdown() {
        stopCountdown(); // clear any existing ticker
        TextView tvCountdown = findViewById(R.id.tvCountdown);
        if (tvCountdown == null) return;

        tickRunnable = new Runnable() {
            @Override
            public void run() {
                long remaining = millisUntilMidnight();
                if (remaining <= 0) {
                    tvCountdown.setText("00:00:00");
                    // New day arrived — recompute state and refresh UI
                    computeState();
                    renderDayCards();
                    bindUI();
                    return;
                }
                long h = remaining / 3_600_000L;
                long m = (remaining % 3_600_000L) / 60_000L;
                long s = (remaining % 60_000L) / 1_000L;
                tvCountdown.setText(String.format(Locale.US, "%02d:%02d:%02d", h, m, s));
                tickHandler.postDelayed(this, 1_000L);
            }
        };
        tickHandler.post(tickRunnable);
    }

    private void stopCountdown() {
        if (tickRunnable != null) {
            tickHandler.removeCallbacks(tickRunnable);
            tickRunnable = null;
        }
    }

    /** Milliseconds from now until the next local midnight. */
    private long millisUntilMidnight() {
        Calendar midnight = Calendar.getInstance();
        midnight.add(Calendar.DAY_OF_MONTH, 1);
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        return midnight.getTimeInMillis() - System.currentTimeMillis();
    }

    private void claimReward() {
        int reward = REWARDS[todayDayIndex];

        MyApp.prefs.edit()
                .putLong(KEY_LAST_CLAIM,  System.currentTimeMillis())
                .putInt(KEY_CLAIMED_DAY,  todayDayIndex)
                .putLong(KEY_TOTAL_ROBUX, MyApp.prefs.getLong(KEY_TOTAL_ROBUX, 0) + reward)
                .apply();
        MyApp.addToBalance(reward); // also add to unified balance

        alreadyClaimed = true;
        canClaim       = false;

        renderDayCards();
        bindUI();

        // Celebrate: bounce the reward amount
        View tvReward = findViewById(R.id.tvTodayReward);
        AnimatorSet bounce = new AnimatorSet();
        bounce.playTogether(
                ObjectAnimator.ofFloat(tvReward, "scaleX", 1f, 1.5f, 1f),
                ObjectAnimator.ofFloat(tvReward, "scaleY", 1f, 1.5f, 1f),
                ObjectAnimator.ofFloat(tvReward, "alpha",  0.4f, 1f)
        );
        bounce.setDuration(500).start();

        Toast.makeText(this, "🎉 You earned R$ " + reward + " Robux!", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "══════════════════════════════════════════");
        Log.d(TAG, "🎁 DAILY REWARD CLAIMED");
        Log.d(TAG, "  Day    : " + (todayDayIndex + 1) + " / 7");
        Log.d(TAG, "  Reward : R$ " + reward);
        Log.d(TAG, "  Total  : R$ " + MyApp.prefs.getLong(KEY_TOTAL_ROBUX, 0));
        Log.d(TAG, "══════════════════════════════════════════");
        MyApp.logEvent("daily_reward_claimed", "day",    String.valueOf(todayDayIndex + 1));
        MyApp.logEvent("daily_reward_claimed", "amount", String.valueOf(reward));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCountdown();
    }

    /** True if the user has an unclaimed reward ready right now. */
    public static boolean hasRewardAvailable() {
        long lastClaim   = MyApp.prefs.getLong(KEY_LAST_CLAIM, 0);
        if (lastClaim == 0) return true;

        Calendar last = Calendar.getInstance();
        last.setTimeInMillis(lastClaim);
        last.set(Calendar.HOUR_OF_DAY, 0); last.set(Calendar.MINUTE, 0);
        last.set(Calendar.SECOND, 0);      last.set(Calendar.MILLISECOND, 0);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0); today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);      today.set(Calendar.MILLISECOND, 0);

        long daysDiff = (today.getTimeInMillis() - last.getTimeInMillis()) / 86_400_000L;
        return daysDiff >= 1;
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
