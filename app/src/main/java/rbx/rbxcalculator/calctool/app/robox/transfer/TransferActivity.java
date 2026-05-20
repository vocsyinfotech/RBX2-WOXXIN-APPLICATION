package rbx.rbxcalculator.calctool.app.robox.transfer;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import rbx.rbxcalculator.calctool.app.robox.R;
import rbx.rbxcalculator.calctool.app.robox.helpers.MyApp;
import java.util.Random;

public class TransferActivity extends AppCompatActivity {

    private static final String TAG = "Firebase_RBX";
    private static final int MIN_TRANSFER = 100;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private String username     = "";
    private long   transferAmt  = 0;
    private long   balance      = 0;

    private View layoutStep1, layoutStep2, layoutStep3, layoutStep4, layoutSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        MyApp.logEvent("screen_view", "screen_name", "transfer");

        balance      = MyApp.getBalance();
        layoutStep1  = findViewById(R.id.layoutStep1);
        layoutStep2  = findViewById(R.id.layoutStep2);
        layoutStep3  = findViewById(R.id.layoutStep3);
        layoutStep4  = findViewById(R.id.layoutStep4);
        layoutSuccess = findViewById(R.id.layoutSuccess);

        setupStep1();
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }

    // ── Step 1: Username ─────────────────────────────────────────────────────

    private void setupStep1() {
        ((TextView) findViewById(R.id.tvStep1Balance)).setText("R$ " + balance);

        AppCompatButton btnFind = findViewById(R.id.btnFindAccount);
        EditText etUsername     = findViewById(R.id.etUsername);

        if (balance < MIN_TRANSFER) {
            findViewById(R.id.tvStep1LowBalance).setVisibility(View.VISIBLE);
            btnFind.setEnabled(false);
            btnFind.setAlpha(0.4f);
        }

        btnFind.setOnClickListener(v -> {
            String uname = etUsername.getText().toString().trim();
            if (uname.isEmpty()) {
                etUsername.setError("Enter your Roblox username");
                return;
            }
            username = uname;
            hideKeyboard();
            btnFind.setText("Searching...");
            btnFind.setEnabled(false);
            btnFind.setAlpha(0.6f);

            handler.postDelayed(() -> {
                setupStep2();
                showStep(layoutStep2);
            }, 2200);
        });
    }

    // ── Step 2: Account found + amount selection ──────────────────────────────

    private void setupStep2() {
        ((TextView) findViewById(R.id.tvFoundUsername)).setText("@" + username);

        // Avatar initials circle
        TextView tvAvatar = findViewById(R.id.tvAvatarInitial);
        tvAvatar.setText(username.length() > 0
                ? String.valueOf(username.charAt(0)).toUpperCase() : "R");
        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(Color.parseColor("#FFD700"));
        tvAvatar.setBackground(circle);

        ((TextView) findViewById(R.id.tvStep2Note))
                .setText("Balance: R$ " + balance + "  •  Min transfer: R$ " + MIN_TRANSFER);

        buildAmountChips();

        EditText etCustom = findViewById(R.id.etCustomAmount);
        // Typing in custom clears any chip selection
        etCustom.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                if (s.length() > 0) transferAmt = 0;
                highlightChips(null);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Next
        ((AppCompatButton) findViewById(R.id.btnStep2Next)).setOnClickListener(v -> {
            // Custom field takes priority if filled
            String custom = etCustom.getText() != null ? etCustom.getText().toString().trim() : "";
            if (!custom.isEmpty()) {
                try { transferAmt = Long.parseLong(custom); }
                catch (NumberFormatException ignored) { transferAmt = 0; }
            }
            if (transferAmt < MIN_TRANSFER) {
                Toast.makeText(this, "Minimum transfer is R$ " + MIN_TRANSFER, Toast.LENGTH_SHORT).show();
                return;
            }
            if (transferAmt > balance) {
                Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                return;
            }
            hideKeyboard();
            setupStep3();
            showStep(layoutStep3);
        });

        // Back
        ((AppCompatButton) findViewById(R.id.btnStep2Back)).setOnClickListener(v -> {
            AppCompatButton btnFind = findViewById(R.id.btnFindAccount);
            btnFind.setText("Find Roblox Account  →");
            btnFind.setEnabled(balance >= MIN_TRANSFER);
            btnFind.setAlpha(balance >= MIN_TRANSFER ? 1f : 0.4f);
            showStep(layoutStep1);
        });
    }

    private void buildAmountChips() {
        long[] presets = buildPresets();
        LinearLayout chipRow = findViewById(R.id.chipRowAmount);
        chipRow.removeAllViews();
        transferAmt = 0;

        for (long preset : presets) {
            String label = (preset == balance)
                    ? "All  (R$" + balance + ")" : "R$ " + preset;

            AppCompatButton chip = new AppCompatButton(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                    dpToPx(44), 1f);
            lp.setMargins(dpToPx(4), 0, dpToPx(4), 0);
            chip.setLayoutParams(lp);
            chip.setText(label);
            chip.setTextSize(11);
            chip.setAllCaps(false);
            chip.setPadding(dpToPx(2), 0, dpToPx(2), 0);
            chip.setBackground(ContextCompat.getDrawable(this, R.drawable.chip_unselected));
            chip.setTextColor(0xFFAAAAAA);

            long finalPreset = preset;
            chip.setOnClickListener(cv -> {
                transferAmt = finalPreset;
                ((EditText) findViewById(R.id.etCustomAmount)).setText("");
                highlightChips(chip);
            });

            chipRow.addView(chip);
        }
    }

    private void highlightChips(AppCompatButton selected) {
        LinearLayout row = findViewById(R.id.chipRowAmount);
        for (int i = 0; i < row.getChildCount(); i++) {
            AppCompatButton chip = (AppCompatButton) row.getChildAt(i);
            boolean isSelected = (chip == selected);
            chip.setBackground(ContextCompat.getDrawable(this,
                    isSelected ? R.drawable.chip_selected : R.drawable.chip_unselected));
            chip.setTextColor(isSelected ? 0xFF000000 : 0xFFAAAAAA);
        }
    }

    private long[] buildPresets() {
        if (balance >= 500) return new long[]{100, 250, 500, balance};
        if (balance >= 250) return new long[]{100, 250, balance};
        if (balance >= 100) return new long[]{100, balance};
        return new long[]{balance};
    }

    // ── Step 3: Confirm ───────────────────────────────────────────────────────

    private void setupStep3() {
        ((TextView) findViewById(R.id.tvConfirmUser)).setText("@" + username);
        ((TextView) findViewById(R.id.tvConfirmAmount)).setText("R$ " + transferAmt);
        ((TextView) findViewById(R.id.tvConfirmBalance))
                .setText("R$ " + (balance - transferAmt) + " remaining");

        ((AppCompatButton) findViewById(R.id.btnConfirmTransfer)).setOnClickListener(v -> {
            showStep(layoutStep4);
            startProcessing();
        });

        ((AppCompatButton) findViewById(R.id.btnStep3Back))
                .setOnClickListener(v -> showStep(layoutStep2));
    }

    // ── Step 4: Processing ────────────────────────────────────────────────────

    private void startProcessing() {
        ((TextView) findViewById(R.id.tvProcessingAmount)).setText("R$ " + transferAmt);
        ((TextView) findViewById(R.id.tvProcessingTo)).setText("to  @" + username);

        int[] rowIds   = {R.id.processRow1, R.id.processRow2, R.id.processRow3,
                          R.id.processRow4, R.id.processRow5};
        int[] checkIds = {R.id.check1, R.id.check2, R.id.check3, R.id.check4, R.id.check5};
        int[] labelIds = {R.id.processLabel1, R.id.processLabel2, R.id.processLabel3,
                          R.id.processLabel4, R.id.processLabel5};
        String[] messages = {
            "Connecting to Roblox servers...",
            "Verifying account:  @" + username,
            "Securing transfer channel...",
            "Processing  R$ " + transferAmt + "  transfer...",
            "Finalising transaction..."
        };

        for (int i = 0; i < messages.length; i++) {
            ((TextView) findViewById(labelIds[i])).setText(messages[i]);
            findViewById(rowIds[i]).setAlpha(0f);
            findViewById(checkIds[i]).setVisibility(View.INVISIBLE);
        }

        // Animate progress bar over the full processing duration (~8 s)
        ProgressBar pb = findViewById(R.id.transferProgressBar);
        pb.setProgress(0);
        ObjectAnimator progressAnim = ObjectAnimator.ofInt(pb, "progress", 0, 100);
        progressAnim.setDuration(8_200);
        progressAnim.start();

        // Staggered row reveal: row appears → 1.4 s later checkmark pops in
        long delay = 0L;
        for (int i = 0; i < rowIds.length; i++) {
            final int idx = i;
            handler.postDelayed(() -> fadeIn(findViewById(rowIds[idx])), delay);
            handler.postDelayed(() -> {
                View chk = findViewById(checkIds[idx]);
                chk.setVisibility(View.VISIBLE);
                chk.setScaleX(0f); chk.setScaleY(0f);
                chk.animate().scaleX(1f).scaleY(1f).setDuration(280).start();
            }, delay + 1_400);
            delay += 1_600;
        }

        // Show success after all steps complete
        handler.postDelayed(this::showSuccess, delay + 500);
    }

    // ── Success ───────────────────────────────────────────────────────────────

    private void showSuccess() {
        // Deduct from balance
        long newBal = Math.max(0, balance - transferAmt);
        MyApp.prefs.edit().putLong(MyApp.KEY_TOTAL_BALANCE, newBal).apply();
        balance = newBal;

        showStep(layoutSuccess);

        ((TextView) findViewById(R.id.tvSuccessAmount)).setText("R$ " + transferAmt);
        ((TextView) findViewById(R.id.tvSuccessUser)).setText("sent to  @" + username);
        ((TextView) findViewById(R.id.tvSuccessTxId)).setText("TXN#" + generateTxId());
        ((TextView) findViewById(R.id.tvSuccessBalance)).setText("R$ " + balance);

        // Bounce the big amount in
        View tvAmt = findViewById(R.id.tvSuccessAmount);
        tvAmt.setScaleX(0.3f); tvAmt.setScaleY(0.3f); tvAmt.setAlpha(0f);
        tvAmt.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(600)
                .setInterpolator(new android.view.animation.OvershootInterpolator(1.4f)).start();

        // Update toolbar title
        ((TextView) findViewById(R.id.tvToolbarTitle)).setText("Transfer Complete");
        findViewById(R.id.tvStepIndicator).setVisibility(View.INVISIBLE);

        MyApp.logEvent("transfer_complete", "amount", String.valueOf(transferAmt));

        ((AppCompatButton) findViewById(R.id.btnSuccessDone))
                .setOnClickListener(v -> finish());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void showStep(View target) {
        View[] all = {layoutStep1, layoutStep2, layoutStep3, layoutStep4, layoutSuccess};
        for (View v : all) {
            if (v == target) { v.setVisibility(View.VISIBLE); fadeIn(v); }
            else              v.setVisibility(View.GONE);
        }
        updateStepIndicator(target);
    }

    private void updateStepIndicator(View step) {
        TextView tv = findViewById(R.id.tvStepIndicator);
        if (tv == null) return;
        tv.setVisibility(View.VISIBLE);
        if      (step == layoutStep1)  tv.setText("Step 1 of 4  —  Enter Username");
        else if (step == layoutStep2)  tv.setText("Step 2 of 4  —  Choose Amount");
        else if (step == layoutStep3)  tv.setText("Step 3 of 4  —  Confirm");
        else if (step == layoutStep4)  tv.setText("Step 4 of 4  —  Processing");
        else                           tv.setVisibility(View.INVISIBLE);
    }

    private void fadeIn(View v) {
        v.setAlpha(0f);
        v.animate().alpha(1f).setDuration(380).start();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View focus = getCurrentFocus();
        if (imm != null && focus != null)
            imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
    }

    private String generateTxId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            if (i == 5) sb.append('-');
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
