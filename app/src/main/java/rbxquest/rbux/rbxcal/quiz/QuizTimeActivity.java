package rbxquest.rbux.rbxcal.quiz;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import rbxquest.rbux.rbxcal.R;
import rbxquest.rbux.rbxcal.helpers.BhismaAds;
import rbxquest.rbux.rbxcal.helpers.MyApp;

public class QuizTimeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Firebase_RBX";
    private static final int SESSION_SIZE = 10;

    // Format: {question, CORRECT_answer, wrong1, wrong2, wrong3}
    // Shuffled at display time — correct answer is always q[1] in data only.
    private static final String[][] ALL_QUESTIONS = {

        // ── Robux / App-specific ─────────────────────────────────────────────
        {"What is the currency in Roblox?",                             "Robux",                        "Lux",                      "Coins",                          "Gems"},
        {"How many Robux do you get for $10?",                          "800",                          "400",                      "100",                            "1000"},
        {"What does DevEx stand for?",                                  "Developer Exchange",           "Dev Export",               "Device Exchange",                "Dev Extra"},
        {"How many Robux equal 1 USD at the buy rate?",                 "80",                           "100",                      "50",                             "120"},
        {"What is OBC in Roblox?",                                      "Outrageous Builders Club",     "Original BC",              "Optional BC",                    "Old BC"},
        {"How much Robux does BC give per day?",                        "15 Robux",                     "10 Robux",                 "20 Robux",                       "25 Robux"},
        {"TBC stands for?",                                             "Turbo Builders Club",          "Top BC",                   "Tiny BC",                        "Total BC"},
        {"How many Robux for DevEx per 100k earned?",                   "$350",                         "$100",                     "$500",                           "$200"},
        {"What year was Roblox founded?",                               "2004",                         "2006",                     "2010",                           "2012"},
        {"What is the max Robux you can hold?",                         "No limit",                     "1 billion",                "100 million",                    "10 million"},

        // ── Easy – General Roblox ────────────────────────────────────────────
        {"What is the currency used in Roblox?",                        "Robux",                        "Coins",                    "Gems",                           "Stars"},
        {"In Adopt Me!, which of these is a Legendary pet?",            "Dragon",                       "Bunny",                    "Dog",                            "Cat"},
        {"What does 'obby' mean in Roblox?",                            "Obstacle course",              "Online battle",            "Obedient player",                "Orange background"},
        {"In Tower of Hell, how often does the tower change?",          "Every 8 minutes",              "Every 5 minutes",          "Every 15 minutes",               "Every 3 minutes"},
        {"Which game lets you rob a bank and escape in a car?",         "Jailbreak",                    "Adopt Me!",                "Royale High",                    "Piggy"},
        {"What is the main currency in Royale High?",                   "Diamonds",                     "Robux",                    "Gold Coins",                     "Stars"},
        {"In Murder Mystery 2, who has a gun at the start?",            "Sheriff",                      "Murderer",                 "Innocent",                       "All players"},
        {"How many roles are there in Murder Mystery 2?",               "3",                            "2",                        "4",                              "5"},
        {"What happens when a Devil Fruit user enters water in Blox Fruits?", "They take damage",       "They gain power",          "They transform",                 "Nothing happens"},
        {"What type of game is Brookhaven RP?",                         "Roleplay",                     "Shooter",                  "Tower climbing",                 "Horror"},
        {"In Piggy, which Chapter 1 location do you start in?",         "House",                        "School",                   "Police Station",                 "Forest"},
        {"Which game features a monster called the Beast?",             "Flee the Facility",            "Blox Fruits",              "Arsenal",                        "Rainbow Friends"},
        {"What must survivors do to escape in Flee the Facility?",      "Hack 5 computers",             "Find keys",                "Build a bridge",                 "Defeat the Beast"},
        {"In Bedwars, what is the most important thing to protect?",    "Your bed",                     "Your sword",               "Your island",                    "Your gold stash"},
        {"Which is the LAST pet growth stage in Adopt Me!?",            "Full Grown",                   "Teen",                     "Post-Teen",                      "Junior"},
        {"Which color monster is first in Rainbow Friends?",            "Blue",                         "Red",                      "Green",                          "Purple"},
        {"What anime inspired Blox Fruits?",                            "One Piece",                    "Naruto",                   "Dragon Ball",                    "Bleach"},
        {"How do you win in Arsenal?",                                  "First to cycle every weapon",  "Most kills",               "Last player alive",              "Highest score"},
        {"Which game is known for its magical school and princess theme?", "Royale High",              "Jailbreak",                "Piggy",                          "Phantom Forces"},
        {"What can you buy with Robux?",                                "Avatar items and game passes", "Food",                     "Houses in real life",            "Homework help"},
        {"What is the goal in Tower of Hell?",                          "Reach the top of the tower",  "Kill all players",         "Collect gems",                   "Find the exit door"},
        {"What are the two main teams in Jailbreak?",                   "Police and Criminals",         "Heroes and Villains",      "Hunters and Prey",               "Builders and Destroyers"},
        {"Who created Murder Mystery 2?",                               "Nikilis",                      "asimo3089",                "AlvinBlox",                      "StyLiS Studios"},
        {"In Blox Fruits, where do Devil Fruits spawn?",                "Under trees on islands",       "On mountain tops",         "In the ocean",                   "Inside caves"},
        {"What is Roblox's avatar shop also called?",                   "The Catalog",                  "The Vault",                "The Store Room",                 "The Marketplace"},
        {"Who is the main antagonist in Piggy?",                        "Piggy",                        "Mr. P",                    "Zizzy",                          "Pony"},
        {"In Phantom Forces, what is the main objective?",              "Eliminate enemies and capture objectives", "Rob a bank",  "Reach the top of a tower",      "Adopt animals"},
        {"What is the name of the roleplay neighborhood game on Roblox?", "Brookhaven RP",             "Blox Fruits",              "Rainbow Friends",                "Arsenal"},
        {"What do players do during the day in Rainbow Friends?",       "Complete tasks",               "Hide from monsters",       "Sleep",                         "Fight monsters"},
        {"Which Roblox game is a horror survival?",                     "Piggy",                        "Royale High",              "Jailbreak",                      "Arsenal"},
        {"In MM2, if the Sheriff dies, what can Innocents pick up?",    "The Sheriff's gun",            "A knife",                  "A shield",                       "A map"},
        {"What year was Roblox publicly released?",                     "2006",                         "2004",                     "2010",                           "2008"},
        {"In Flee the Facility, how many computers do survivors hack?", "5",                            "3",                        "4",                              "6"},
        {"In Bedwars, where do players collect resources from?",        "Resource spawners",            "Chests",                   "Trees",                          "Enemies"},
        {"Which Roblox game has a seasonal halo trading economy?",      "Royale High",                  "Jailbreak",                "Piggy",                          "Arsenal"},
        {"Which is a Common-tier pet in Adopt Me!?",                    "Dog",                          "Griffin",                  "Unicorn",                        "Shadow Dragon"},
        {"What is the First Sea also called in Blox Fruits?",           "The Starter Sea",              "Paradise",                 "New World",                      "The East Blue"},
        {"What can criminals use to travel fast in Jailbreak?",         "Cars, helicopters, and more",  "Only on foot",             "Teleportation",                  "Jetpacks only"},
        {"What is the main goal in Piggy?",                             "Escape each chapter",          "Defeat Piggy",             "Collect all keys",               "Find all clues"},
        {"What happens when you get a kill in Arsenal?",                "You get the next weapon",      "You get a new map",        "You get extra health",           "Nothing happens"},
        {"What type of game is Murder Mystery 2?",                      "Social deduction",             "Battle royale",            "Horror roleplay",                "Racing"},
        {"Where does Rainbow Friends take place?",                      "An abandoned theme park",      "A haunted house",          "A school",                       "A forest"},
        {"What is a 'Neon' pet in Adopt Me!?",                          "A glowing upgraded version",   "A legendary pet",          "A baby pet",                     "A rare pet"},
        {"What are the two teams in Phantom Forces?",                   "Phantoms and Ghosts",          "Red and Blue",             "SWAT and Criminal",              "Police and Army"},
        {"What level range is the Second Sea in Blox Fruits?",          "700-1500",                     "1-700",                    "1500-2450",                      "2450+"},
        {"What is the main way to earn XP in Royale High?",             "Attending classes and events", "Trading items",            "Winning battles",                "Buying Robux"},

        // ── Medium – General Roblox ──────────────────────────────────────────
        {"What does Buso Haki allow in Blox Fruits?",                   "Break Logia immunity",         "Fly faster",               "See through walls",              "Teleport to islands"},
        {"How many Full Grown pets make a Neon in Adopt Me!?",          "4",                            "2",                        "3",                              "5"},
        {"What was Blox Fruits originally called?",                     "Blox Piece",                   "Fruit Simulator",          "One Fruit",                      "Devil Fruits"},
        {"In Jailbreak, where does the Train heist occur?",             "On a moving train",            "Inside a building",        "Underground",                    "At the airport"},
        {"What rarity in MM2 is above Legendary?",                      "Divine",                       "Ancient",                  "Mythic",                         "Chroma"},
        {"What is the enchanted forest area in Royale High called?",    "Enchanted Forest",             "Fantasia Woods",           "Fairy World",                    "Sylvan Grove"},
        {"Best Blox Fruits fruit for PVP in Third Sea?",                "Leopard",                      "Flame",                    "Smoke",                          "Sand"},
        {"Most expensive weapon tier in Roblox Bedwars?",               "Diamond sword",                "Legendary sword",          "Gold sword",                     "Crystal axe"},
        {"Who is behind the infection in Piggy?",                       "Mr. P",                        "Pony",                     "Zizzy",                          "Mimi"},
        {"Where is Flower Hill island in Blox Fruits?",                 "First Sea",                    "Second Sea",               "Third Sea",                      "Fourth Sea"},
        {"Which Phantom Forces mode is pure team elimination?",         "Team Deathmatch",              "Capture the Flag",         "King of the Hill",               "Conquest"},
        {"What does the Beast do to caught survivors in Flee the Facility?", "Puts them in a frozen pod", "Eliminates them",       "Turns them into a Beast",        "Sends them to start"},
        {"What do criminals do at the Jewelry Store in Jailbreak?",     "Break cases and grab jewels",  "Use a bomb",               "Hack a computer",                "Steal a car"},
        {"Where do you create Neon pets in Adopt Me!?",                 "Neon Cave",                    "Rainbow Bridge",           "Light Tunnel",                   "Glow Grotto"},
        {"How often do Devil Fruits respawn in Blox Fruits?",           "Every 60 minutes",             "Every 30 minutes",         "Every 45 minutes",               "Every 2 hours"},
        {"What color represents 'Godly' rarity in MM2?",                "Gold",                         "Purple",                   "Red",                            "Rainbow"},
    };

    private static final int COLOR_GOLD    = 0xFFFFD700;
    private static final int COLOR_SURFACE = 0xFF1A1A1A;
    private static final int COLOR_CORRECT = 0xFF4CAF50;
    private static final int COLOR_WRONG   = 0xFFCF6679;

    private String[][] sessionQuestions;
    private int currentQ = 0;
    private int score = 0;
    private int shuffledCorrectIndex = 0;

    private TextView tvQuestion, tvScore, tvProgress;
    private Button[] optionBtns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        BhismaAds.attach(this);

        Log.d(TAG, "❓ QuizTimeActivity opened");
        MyApp.logEvent("screen_view", "screen_name", "quiz");

        // Pick SESSION_SIZE random questions from the full pool
        List<String[]> pool = new ArrayList<>(Arrays.asList(ALL_QUESTIONS));
        Collections.shuffle(pool);
        sessionQuestions = pool.subList(0, Math.min(SESSION_SIZE, pool.size()))
                               .toArray(new String[0][]);

        Log.d(TAG, "🎲 Quiz session: " + sessionQuestions.length + " questions selected from pool of " + ALL_QUESTIONS.length);

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
        if (currentQ >= sessionQuestions.length) {
            // Hide question card and buttons, show result card
            findViewById(R.id.cardQuestion).setVisibility(View.GONE);
            for (Button b : optionBtns) b.setVisibility(View.GONE);

            int earned = score * 10;
            String grade = score == sessionQuestions.length ? "Perfect! 🎯"
                         : score >= sessionQuestions.length * 0.7 ? "Great job! 🌟"
                         : score >= sessionQuestions.length * 0.4 ? "Good effort! 👍"
                         : "Keep practising! 💪";

            android.view.View layoutResult = findViewById(R.id.layoutResult);
            layoutResult.setVisibility(View.VISIBLE);
            ((android.widget.TextView) findViewById(R.id.tvResultTitle)).setText(grade);
            ((android.widget.TextView) findViewById(R.id.tvResultScore))
                    .setText("Score: " + score + " / " + sessionQuestions.length);
            ((android.widget.TextView) findViewById(R.id.tvResultEarned))
                    .setText("R$ " + earned);

            // Animate the earned amount
            android.view.View tvEarned = findViewById(R.id.tvResultEarned);
            tvEarned.setAlpha(0f);
            tvEarned.setScaleX(0.6f);
            tvEarned.setScaleY(0.6f);
            tvEarned.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(500).setStartDelay(300).start();

            Log.d(TAG, "══════════════════════════════════════════");
            Log.d(TAG, "🏆 QUIZ COMPLETE");
            Log.d(TAG, "  Score  : " + score + "/" + sessionQuestions.length);
            Log.d(TAG, "  Earned : R$" + earned);
            Log.d(TAG, "  Balance: R$" + MyApp.getBalance());
            Log.d(TAG, "══════════════════════════════════════════");
            MyApp.logEvent("quiz_complete", "score", score + "_of_" + sessionQuestions.length);
            MyApp.logEvent("quiz_complete", "earned", String.valueOf(earned));
            return;
        }

        String[] q = sessionQuestions[currentQ];
        tvQuestion.setText(q[0]);
        tvProgress.setText((currentQ + 1) + "/" + sessionQuestions.length);
        tvScore.setText("Score: " + score);

        // Shuffle 4 options; q[1]=correct, q[2..4]=wrong
        List<String> options = new ArrayList<>();
        for (int i = 1; i <= 4; i++) options.add(q[i]);
        Collections.shuffle(options);
        shuffledCorrectIndex = options.indexOf(q[1]);

        for (int i = 0; i < optionBtns.length; i++) {
            optionBtns[i].setText(options.get(i));
            optionBtns[i].setBackgroundColor(i % 2 == 0 ? COLOR_GOLD : COLOR_SURFACE);
            optionBtns[i].setTextColor(i % 2 == 0 ? 0xFF000000 : 0xFFFFFFFF);
            optionBtns[i].setEnabled(true);
            optionBtns[i].setVisibility(View.VISIBLE);
        }

        Log.d(TAG, "❓ Q" + (currentQ + 1) + ": " + q[0] + " | correct at slot " + shuffledCorrectIndex);
    }

    @Override
    public void onClick(View v) {
        int selected = -1;
        for (int i = 0; i < optionBtns.length; i++) {
            if (v.getId() == optionBtns[i].getId()) { selected = i; break; }
        }
        if (selected < 0) return;

        for (Button b : optionBtns) b.setEnabled(false);

        optionBtns[shuffledCorrectIndex].setBackgroundColor(COLOR_CORRECT);
        optionBtns[shuffledCorrectIndex].setTextColor(0xFFFFFFFF);
        boolean isCorrect = selected == shuffledCorrectIndex;
        if (!isCorrect) {
            optionBtns[selected].setBackgroundColor(COLOR_WRONG);
            optionBtns[selected].setTextColor(0xFFFFFFFF);
        } else {
            score++;
            MyApp.addToBalance(10); // 10 Robux per correct answer
        }

        String[] q = sessionQuestions[currentQ];
        Log.d(TAG, "══════════════════════════════════════════");
        Log.d(TAG, "📝 QUIZ ANSWER");
        Log.d(TAG, "  Question : " + q[0]);
        Log.d(TAG, "  Selected : " + optionBtns[selected].getText() + " (slot " + selected + ")");
        Log.d(TAG, "  Correct  : " + q[1] + " (slot " + shuffledCorrectIndex + ")");
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
