package rbxquest.rbux.rbxcal.membership;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import rbxquest.rbux.rbxcal.R;
import rbxquest.rbux.rbxcal.helpers.BhismaAds;

public class MembershipCalculator extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_menu);
        BhismaAds.attach(this);

        findViewById(R.id.btnBcToRbx).setOnClickListener(this);
        findViewById(R.id.btnTbcToUsd).setOnClickListener(this);
        findViewById(R.id.btnObcToRbx).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnBack) { onBackPressed(); return; }
        Class<?> dest = null;
        if (id == R.id.btnBcToRbx)       dest = BcToRbxActivity.class;
        else if (id == R.id.btnTbcToUsd) dest = TbcToUsdActivity.class;
        else if (id == R.id.btnObcToRbx) dest = ObcToRbxActivity.class;
        if (dest != null) startActivity(new Intent(this, dest));
    }
}
