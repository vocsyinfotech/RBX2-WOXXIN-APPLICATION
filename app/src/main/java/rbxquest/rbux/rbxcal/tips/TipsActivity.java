package rbxquest.rbux.rbxcal.tips;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bhasma.Pizza;
import rbxquest.rbux.rbxcal.R;
import rbxquest.rbux.rbxcal.helpers.BhismaAds;

public class TipsActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "tip_title";
    public static final String EXTRA_DETAIL = "tip_detail";

    private static final int[] TITLES = {
            R.string.robux_getting_started_with_robx,
            R.string.robux_earning_robx,
            R.string.robux_customizing_your_avatar,
            R.string.robux_maximizing_gameplay_with_robx,
            R.string.robux_spending_robx_wisely,
            R.string.robux_managing_your_robx_account,
            R.string.robux_tips_for_robx_savers,
            R.string.robux_tracking_robx_spending,
            R.string.robux_exclusive_robx_rewards,
            R.string.robux_exploring_the_roblox_ecosystem
    };

    private static final int[] DETAILS = {
            R.string.robux_getting_started_with_robx_details,
            R.string.robux_earning_robx_details,
            R.string.robux_customizing_your_avatar_details,
            R.string.robux_maximizing_gameplay_with_robx_details,
            R.string.robux_spending_robx_wisely_details,
            R.string.robux_managing_your_robx_account_details,
            R.string.robux_tips_for_robx_savers_details,
            R.string.robux_tracking_robx_spending_details,
            R.string.robux_exclusive_robx_rewards_details,
            R.string.robux_exploring_the_roblox_ecosystem_details
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        BhismaAds.attach(this);

        RecyclerView rv = findViewById(R.id.rvTips);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new TipsAdapter());
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
    }

    // List position 5 (= after TITLES[4] "Spending Robux Wisely") is reserved
    // for an in-list big native ad. Positions after that shift by 1.
    private static final int AD_POSITION = 5;
    private static final int TYPE_TIP = 0;
    private static final int TYPE_AD  = 1;

    private class TipsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemViewType(int position) {
            return position == AD_POSITION ? TYPE_AD : TYPE_TIP;
        }

        @NonNull @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == TYPE_AD) {
                View v = inflater.inflate(R.layout.item_tip_ad, parent, false);
                return new AdVH(v);
            }
            View v = inflater.inflate(R.layout.item_tip, parent, false);
            return new TipVH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof AdVH) {
                try {
                    Pizza.Native(TipsActivity.this, ((AdVH) holder).adContainer, 2);
                    Log.d("Firebase_RBX", "📺 In-list native ad attached at row " + position);
                } catch (Throwable t) {
                    Log.w("Firebase_RBX", "In-list native attach failed: " + t.getMessage());
                }
                return;
            }
            // Shift tip index for rows past the ad.
            final int titleIndex = position < AD_POSITION ? position : position - 1;
            TipVH tipHolder = (TipVH) holder;
            tipHolder.tvTitle.setText(getString(TITLES[titleIndex]));
            tipHolder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(TipsActivity.this, TipsDetailsActivity.class);
                intent.putExtra(EXTRA_TITLE, getString(TITLES[titleIndex]));
                intent.putExtra(EXTRA_DETAIL, getString(DETAILS[titleIndex]));
                startActivity(intent);
            });
        }

        @Override public int getItemCount() { return TITLES.length + 1; }

        class TipVH extends RecyclerView.ViewHolder {
            final TextView tvTitle;
            TipVH(View v) { super(v); tvTitle = v.findViewById(R.id.tvTipTitle); }
        }

        class AdVH extends RecyclerView.ViewHolder {
            final RelativeLayout adContainer;
            AdVH(View v) { super(v); adContainer = v.findViewById(R.id.bannerInList); }
        }
    }
}
