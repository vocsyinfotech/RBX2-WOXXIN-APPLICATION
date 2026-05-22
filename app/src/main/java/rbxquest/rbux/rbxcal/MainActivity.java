package rbxquest.rbux.rbxcal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbxquest.rbux.rbxcal.allcalc.DollarToRbxActivity;
import rbxquest.rbux.rbxcal.allcalc.RbxToDollarActivity;
import rbxquest.rbux.rbxcal.allcalc.RbxToUsdActivity;
import rbxquest.rbux.rbxcal.allcalc.UsdToRbxActivity;
import rbxquest.rbux.rbxcal.daily.DailyRewardActivity;
import rbxquest.rbux.rbxcal.helpers.BhismaAds;
import rbxquest.rbux.rbxcal.helpers.MyApp;
import rbxquest.rbux.rbxcal.membership.MembershipCalculator;
import rbxquest.rbux.rbxcal.meme.MemeActivity;
import rbxquest.rbux.rbxcal.quiz.QuizTimeActivity;
import rbxquest.rbux.rbxcal.scratchcard.ScratchcardActivity;
import rbxquest.rbux.rbxcal.spin.SpinWheelActivity;
import rbxquest.rbux.rbxcal.tips.TipsActivity;
import rbxquest.rbux.rbxcal.transfer.TransferActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Firebase_RBX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BhismaAds.attach(this);

        Log.d(TAG, "🏠 MainActivity opened");
        MyApp.logEvent("screen_view", "screen_name", "main");

        // Balance chip → opens Transfer flow
        View balanceChip = findViewById(R.id.balanceChip);
        if (balanceChip != null) {
            balanceChip.setOnClickListener(v ->
                    startActivity(new Intent(this, TransferActivity.class)));
        }


        int[] cardIds = {
                R.id.cardRbxToUsd, R.id.cardUsdToRbx,
                R.id.cardRbxToDollar, R.id.cardDollarToRbx,
                R.id.cardMembership, R.id.cardTips,
                R.id.cardSpin, R.id.cardQuiz,
                R.id.cardScratch, R.id.cardMeme,
                R.id.cardSettings, R.id.cardExit,
                R.id.cardAllCalc, R.id.cardDailyReward
        };
        for (int id : cardIds) {
            View v = findViewById(id);
            if (v != null) v.setOnClickListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDailyRewardBadge();
    }

    /** Refreshes the NEW badge, reward card subtitle, and the header balance chip. */
    private void updateDailyRewardBadge() {
        boolean available   = DailyRewardActivity.hasRewardAvailable();
        long    dailyTotal  = MyApp.prefs.getLong(DailyRewardActivity.KEY_TOTAL_ROBUX, 0);
        int     day         = MyApp.prefs.getInt(DailyRewardActivity.KEY_CLAIMED_DAY, -1);
        long    balance     = MyApp.getBalance(); // unified: spin + quiz + scratch + daily

        // NEW badge on the daily reward card
        View badge = findViewById(R.id.tvRewardBadge);
        if (badge != null) badge.setVisibility(available ? View.VISIBLE : View.GONE);

        // Daily reward card subtitle
        TextView status = findViewById(R.id.tvRewardStatus);
        if (status != null) {
            if (available) {
                status.setText("Reward ready to claim!");
            } else {
                status.setText("Day " + (day + 1) + " claimed  •  R$ " + dailyTotal + " from daily");
            }
        }

        // Header balance chip — shows total from ALL sources
        TextView tvBalance = findViewById(R.id.tvHeaderBalance);
        if (tvBalance != null) {
            tvBalance.setText(String.valueOf(balance));

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Class<?> dest = null;
        String screenName = null;

        if      (id == R.id.cardRbxToUsd)      { dest = RbxToUsdActivity.class;        screenName = "rbx_to_usd"; }
        else if (id == R.id.cardUsdToRbx)      { dest = UsdToRbxActivity.class;        screenName = "usd_to_rbx"; }
        else if (id == R.id.cardRbxToDollar)   { dest = RbxToDollarActivity.class;     screenName = "rbx_to_dollar"; }
        else if (id == R.id.cardDollarToRbx)   { dest = DollarToRbxActivity.class;     screenName = "dollar_to_rbx"; }
        else if (id == R.id.cardMembership)    { dest = MembershipCalculator.class;    screenName = "membership"; }
        else if (id == R.id.cardAllCalc)       { dest = AllRbxCalculatorActivity.class; screenName = "all_calc"; }
        else if (id == R.id.cardTips)          { dest = TipsActivity.class;            screenName = "tips"; }
        else if (id == R.id.cardSpin)          { dest = SpinWheelActivity.class;       screenName = "spin_wheel"; }
        else if (id == R.id.cardQuiz)          { dest = QuizTimeActivity.class;        screenName = "quiz"; }
        else if (id == R.id.cardScratch)       { dest = ScratchcardActivity.class;     screenName = "scratch_card"; }
        else if (id == R.id.cardMeme)          { dest = MemeActivity.class;            screenName = "memes"; }
        else if (id == R.id.cardDailyReward)   { dest = DailyRewardActivity.class;     screenName = "daily_reward"; }
        else if (id == R.id.cardSettings)      { dest = SettingActivity.class;         screenName = "settings"; }
        else if (id == R.id.cardExit)          { dest = ExitScreen.class;              screenName = "exit"; }

        if (dest != null) {
            Log.d(TAG, "🔀 Navigating to: " + screenName);
            MyApp.logEvent("menu_tap", "destination", screenName);
            startActivity(new Intent(this, dest));
        }
    }

    @Override
    public void onBackPressed() {
        // Home back-press → open the existing ExitScreen confirmation popup
        // (which already has Yes/No buttons + a Qureka native ad).
        Log.d(TAG, "🔙 Home back → opening ExitScreen");
        startActivity(new Intent(this, ExitScreen.class));
    }
}
