package rbx.rbxcalculator.calctool.app.robox.quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbx.rbxcalculator.calctool.app.robox.R;

public class QuizTimeActivity extends AppCompatActivity implements View.OnClickListener {

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

    private int currentQ = 0;
    private int score = 0;

    private TextView tvQuestion, tvScore, tvProgress;
    private Button[] optionBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScore);
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
            return;
        }
        String[] q = QUESTIONS[currentQ];
        tvQuestion.setText(q[0]);
        tvProgress.setText((currentQ + 1) + "/" + QUESTIONS.length);
        tvScore.setText("Score: " + score);
        for (int i = 0; i < optionBtns.length; i++) {
            optionBtns[i].setText(q[i + 1]);
            optionBtns[i].setBackgroundColor(Color.parseColor("#11746e"));
            optionBtns[i].setEnabled(true);
        }
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
        optionBtns[correct].setBackgroundColor(Color.GREEN);
        if (selected != correct) {
            optionBtns[selected].setBackgroundColor(Color.RED);
        } else {
            score++;
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            currentQ++;
            loadQuestion();
        }, 1200);
    }
}
