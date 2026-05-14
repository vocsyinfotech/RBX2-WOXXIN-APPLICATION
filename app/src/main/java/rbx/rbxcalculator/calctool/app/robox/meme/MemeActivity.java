package rbx.rbxcalculator.calctool.app.robox.meme;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import rbx.rbxcalculator.calctool.app.robox.R;

public class MemeActivity extends AppCompatActivity {

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

    private int currentIndex = 0;
    private ImageView ivMeme;
    private TextView tvCaption, tvCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme);

        ivMeme = findViewById(R.id.ivMeme);
        tvCaption = findViewById(R.id.tvMemeCaption);
        tvCounter = findViewById(R.id.tvMemeCounter);

        showMeme(0);

        findViewById(R.id.btnPrevMeme).setOnClickListener(v -> {
            currentIndex = (currentIndex - 1 + MEME_IMAGES.length) % MEME_IMAGES.length;
            showMeme(currentIndex);
        });
        findViewById(R.id.btnNextMeme).setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % MEME_IMAGES.length;
            showMeme(currentIndex);
        });
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }

    private void showMeme(int idx) {
        ivMeme.setImageResource(MEME_IMAGES[idx]);
        tvCaption.setText(MEME_CAPTIONS[idx]);
        tvCounter.setText((idx + 1) + "/" + MEME_IMAGES.length);
    }
}
