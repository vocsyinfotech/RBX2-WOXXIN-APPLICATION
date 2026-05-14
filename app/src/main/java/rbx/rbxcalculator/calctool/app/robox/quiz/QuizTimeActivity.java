package rbx.rbxcalculator.calctool.app.robox.quiz;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbx.rbxcalculator.calctool.app.robox.R;
import rbx.rbxcalculator.calctool.app.robox.helpers.MyApp;

public class QuizTimeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Firebase_RBX";

    private static final String[][] QUESTIONS = {
        {"What is the currency in Roblox?", "Robux", "Lux", "Coins", "Gems", "0"},
        {"How many Robux do you get for $10?", "800", "400", "100", "1000", "0"},
        {"What does DevEx stand for?", "Developer Exchange", "Dev Export", "Device Exchange", "Dev Extra", "0"},
        {"How many Robux is 1 USD at buy rate?", "80", "100", "50", "120", "0"},
        {"What is OBC in Roblox?", "Outrageous Builders Club", "Original BC", "Optional BC", "Old BC", "0"},
        {"How much does BC give per day?", "15 Robux", "10 Robux", "20 Robux", "25 Robux", "0"},
        {"TBC stands for?", "Turbo Builders Club", "Top BC", "Tiny BC", "Total BC", "0"},
        {"How many Robux for DevEx per 100k?", "$350", "$100", "$500", "$200", "0"},
        {"What year was Roblox founded?", "2004", "2006", "2010", "2012", "0"},
        {"What is the max Robux you can hold?", "No limit", "1 billion", "100 million", "10 million", "0"}
    };

    // Gold / dark theme colours (match the redesigned layout)
    private static final int COLOR_GOLD        = 0xFFFFD700;
    private static final int COLOR_SURFACE      = 0xFF1A1A1A;
    private static final int COLOR_CORRECT      = 0xFF4CAF50;
    private static final int COLOR_WRONG        = 0xFFCF6679;

    private int currentQ = 0;
    private int score = 0;

    private TextView tvQuestion, tvScore, tvProgress;
    private Button[] optionBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Log.d(TAG, "❓ QuizTimeActivity opened");
        MyApp.logEvent("screen_view", "screen_name", "quiz");

        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore    = findViewById(R.id.tvScore);
        tvProgress = findViewById(R.id.tvProgress);
        optionBtns = new Button[]{
                findViewById(R.id.btnOpt1),
                findViewById(R.id.btnOpt2),
                findViewById(R.id.btnOpt3),
                findViewById(R.id.btnOpt4)
        };
        for (Button b : optionBtns) b.setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQ >= QUESTIONS.length) {
            tvQuestion.setText("Quiz Complete!\nScore: " + score + "/" + QUESTIONS.length);
            for (Button b : optionBtns) b.setVisibility(View.GONE);

            Log.d(TAG, "══════════════════════════════════════════");
            Log.d(TAG, "🏆 QUIZ COMPLETE");
            Log.d(TAG, "  Final score : " + score + "/" + QUESTIONS.length);
            Log.d(TAG, "══════════════════════════════════════════");
            MyApp.logEvent("quiz_complete", "score", score + "_of_" + QUESTIONS.length);
            return;
        }
        String[] q = QUESTIONS[currentQ];
        tvQuestion.setText(q[0]);
        tvProgress.setText((currentQ + 1) + "/" + QUESTIONS.length);
        tvScore.setText("Score: " + score);

        // Reset button colours to match the dark/gold theme
        for (int i = 0; i < optionBtns.length; i++) {
            optionBtns[i].setText(q[i + 1]);
            optionBtns[i].setBackgroundColor(i % 2 == 0 ? COLOR_GOLD : COLOR_SURFACE);
            optionBtns[i].setTextColor(i % 2 == 0 ? 0xFF000000 : 0xFFFFFFFF);
            optionBtns[i].setEnabled(true);
        }

        Log.d(TAG, "❓ Q" + (currentQ + 1) + ": " + q[0]);
    }

    @Override
    public void onClick(View v) {
        String[] q = QUESTIONS[currentQ];
        int correct = Integer.parseInt(q[5]);
        int selected = -1;
        for (int i = 0; i < optionBtns.length; i++) {
            if (v.getId() == optionBtns[i].getId()) { selected = i; break; }
        }
        if (selected < 0) return;

        for (Button b : optionBtns) b.setEnabled(false);

        // Highlight correct answer green, wrong answer red
        optionBtns[correct].setBackgroundColor(COLOR_CORRECT);
        optionBtns[correct].setTextColor(0xFFFFFFFF);
        boolean isCorrect = selected == correct;
        if (!isCorrect) {
            optionBtns[selected].setBackgroundColor(COLOR_WRONG);
            optionBtns[selected].setTextColor(0xFFFFFFFF);
        } else {
            score++;
        }

        Log.d(TAG, "══════════════════════════════════════════");
        Log.d(TAG, "📝 QUIZ ANSWER");
        Log.d(TAG, "  Question : " + q[0]);
        Log.d(TAG, "  Selected : " + q[selected + 1] + " (index " + selected + ")");
        Log.d(TAG, "  Correct  : " + q[correct + 1] + " (index " + correct + ")");
        Log.d(TAG, "  Result   : " + (isCorrect ? "✅ CORRECT" : "❌ WRONG"));
        Log.d(TAG, "  Score    : " + score + "/" + (currentQ + 1));
        Log.d(TAG, "══════════════════════════════════════════");

        MyApp.logEvent("quiz_answer", "result", isCorrect ? "correct" : "wrong");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            currentQ++;
            loadQuestion();
        }, 1200);
    }
}
