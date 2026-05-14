package rbx.rbxcalculator.calctool.app.robox;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import rbx.rbxcalculator.calctool.app.robox.allcalc.RbxToUsdActivity;
import rbx.rbxcalculator.calctool.app.robox.allcalc.UsdToRbxActivity;
import rbx.rbxcalculator.calctool.app.robox.allcalc.RbxToDollarActivity;
import rbx.rbxcalculator.calctool.app.robox.allcalc.DollarToRbxActivity;
import rbx.rbxcalculator.calctool.app.robox.helpers.MyApp;
import rbx.rbxcalculator.calctool.app.robox.membership.MembershipCalculator;
import rbx.rbxcalculator.calctool.app.robox.tips.TipsActivity;
import rbx.rbxcalculator.calctool.app.robox.spin.SpinWheelActivity;
import rbx.rbxcalculator.calctool.app.robox.quiz.QuizTimeActivity;
import rbx.rbxcalculator.calctool.app.robox.scratchcard.ScratchcardActivity;
import rbx.rbxcalculator.calctool.app.robox.meme.MemeActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Firebase_RBX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "🏠 MainActivity opened");
        MyApp.logEvent("screen_view", "screen_name", "main");

        int[] cardIds = {
                R.id.cardRbxToUsd, R.id.cardUsdToRbx,
                R.id.cardRbxToDollar, R.id.cardDollarToRbx,
                R.id.cardMembership, R.id.cardTips,
                R.id.cardSpin, R.id.cardQuiz,
                R.id.cardScratch, R.id.cardMeme,
                R.id.cardSettings, R.id.cardExit,
                R.id.cardAllCalc
        };
        for (int id : cardIds) {
            View v = findViewById(id);
            if (v != null) v.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Class<?> dest = null;
        String screenName = null;

        if      (id == R.id.cardRbxToUsd)    { dest = RbxToUsdActivity.class;    screenName = "rbx_to_usd"; }
        else if (id == R.id.cardUsdToRbx)    { dest = UsdToRbxActivity.class;    screenName = "usd_to_rbx"; }
        else if (id == R.id.cardRbxToDollar) { dest = RbxToDollarActivity.class; screenName = "rbx_to_dollar"; }
        else if (id == R.id.cardDollarToRbx) { dest = DollarToRbxActivity.class; screenName = "dollar_to_rbx"; }
        else if (id == R.id.cardMembership)  { dest = MembershipCalculator.class; screenName = "membership"; }
        else if (id == R.id.cardAllCalc)     { dest = AllRbxCalculatorActivity.class; screenName = "all_calc"; }
        else if (id == R.id.cardTips)        { dest = TipsActivity.class;        screenName = "tips"; }
        else if (id == R.id.cardSpin)        { dest = SpinWheelActivity.class;   screenName = "spin_wheel"; }
        else if (id == R.id.cardQuiz)        { dest = QuizTimeActivity.class;    screenName = "quiz"; }
        else if (id == R.id.cardScratch)     { dest = ScratchcardActivity.class; screenName = "scratch_card"; }
        else if (id == R.id.cardMeme)        { dest = MemeActivity.class;        screenName = "memes"; }
        else if (id == R.id.cardSettings)    { dest = SettingActivity.class;     screenName = "settings"; }
        else if (id == R.id.cardExit)        { dest = ExitScreen.class;          screenName = "exit"; }

        if (dest != null) {
            Log.d(TAG, "🔀 Navigating to: " + screenName);
            MyApp.logEvent("menu_tap", "destination", screenName);
            startActivity(new Intent(this, dest));
        }
    }
}
