package rbx.rbxcalculator.calctool.app.robox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import rbx.rbxcalculator.calctool.app.robox.allcalc.RbxToUsdActivity;
import rbx.rbxcalculator.calctool.app.robox.allcalc.UsdToRbxActivity;
import rbx.rbxcalculator.calctool.app.robox.allcalc.RbxToDollarActivity;
import rbx.rbxcalculator.calctool.app.robox.allcalc.DollarToRbxActivity;
import rbx.rbxcalculator.calctool.app.robox.membership.MembershipCalculator;
import rbx.rbxcalculator.calctool.app.robox.tips.TipsActivity;
import rbx.rbxcalculator.calctool.app.robox.spin.SpinWheelActivity;
import rbx.rbxcalculator.calctool.app.robox.quiz.QuizTimeActivity;
import rbx.rbxcalculator.calctool.app.robox.scratchcard.ScratchcardActivity;
import rbx.rbxcalculator.calctool.app.robox.meme.MemeActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        if (id == R.id.cardRbxToUsd)      dest = RbxToUsdActivity.class;
        else if (id == R.id.cardUsdToRbx) dest = UsdToRbxActivity.class;
        else if (id == R.id.cardRbxToDollar) dest = RbxToDollarActivity.class;
        else if (id == R.id.cardDollarToRbx) dest = DollarToRbxActivity.class;
        else if (id == R.id.cardMembership) dest = MembershipCalculator.class;
        else if (id == R.id.cardAllCalc)  dest = AllRbxCalculatorActivity.class;
        else if (id == R.id.cardTips)     dest = TipsActivity.class;
        else if (id == R.id.cardSpin)     dest = SpinWheelActivity.class;
        else if (id == R.id.cardQuiz)     dest = QuizTimeActivity.class;
        else if (id == R.id.cardScratch)  dest = ScratchcardActivity.class;
        else if (id == R.id.cardMeme)     dest = MemeActivity.class;
        else if (id == R.id.cardSettings) dest = SettingActivity.class;
        else if (id == R.id.cardExit)     dest = ExitScreen.class;

        if (dest != null) {
            startActivity(new Intent(this, dest));
        }
    }
}
