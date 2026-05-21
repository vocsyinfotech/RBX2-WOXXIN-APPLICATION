package rbxquest.rbux.rbxcal.tips;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

    private class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.VH> {

        @NonNull @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tip, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.tvTitle.setText(getString(TITLES[position]));
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(TipsActivity.this, TipsDetailsActivity.class);
                intent.putExtra(EXTRA_TITLE, getString(TITLES[position]));
                intent.putExtra(EXTRA_DETAIL, getString(DETAILS[position]));
                startActivity(intent);
            });
        }

        @Override public int getItemCount() { return TITLES.length; }

        class VH extends RecyclerView.ViewHolder {
            TextView tvTitle;
            VH(View v) { super(v); tvTitle = v.findViewById(R.id.tvTipTitle); }
        }
    }
}
