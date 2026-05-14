package rbx.rbxcalculator.calctool.app.robox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import rbx.rbxcalculator.calctool.app.robox.allcalc.DollarToRbxActivity;
import rbx.rbxcalculator.calctool.app.robox.allcalc.RbxToDollarActivity;
import rbx.rbxcalculator.calctool.app.robox.allcalc.RbxToUsdActivity;
import rbx.rbxcalculator.calctool.app.robox.allcalc.UsdToRbxActivity;
import rbx.rbxcalculator.calctool.app.robox.membership.BcToRbxActivity;
import rbx.rbxcalculator.calctool.app.robox.membership.MembershipCalculator;
import rbx.rbxcalculator.calctool.app.robox.membership.ObcToRbxActivity;
import rbx.rbxcalculator.calctool.app.robox.membership.TbcToUsdActivity;

public class AllRbxCalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_calc);

        int[] ids = {
                R.id.btnRbxToUsd, R.id.btnUsdToRbx,
                R.id.btnRbxToDollar, R.id.btnDollarToRbx,
                R.id.btnMembership, R.id.btnBcToRbx,
                R.id.btnObcToRbx, R.id.btnTbcToUsd,
                R.id.btnBack
        };
        for (int id : ids) {
            View v = findViewById(id);
            if (v != null) v.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnBack) { onBackPressed(); return; }

        Class<?> dest = null;
        if (id == R.id.btnRbxToUsd)     dest = RbxToUsdActivity.class;
        else if (id == R.id.btnUsdToRbx) dest = UsdToRbxActivity.class;
        else if (id == R.id.btnRbxToDollar) dest = RbxToDollarActivity.class;
        else if (id == R.id.btnDollarToRbx) dest = DollarToRbxActivity.class;
        else if (id == R.id.btnMembership) dest = MembershipCalculator.class;
        else if (id == R.id.btnBcToRbx) dest = BcToRbxActivity.class;
        else if (id == R.id.btnObcToRbx) dest = ObcToRbxActivity.class;
        else if (id == R.id.btnTbcToUsd) dest = TbcToUsdActivity.class;

        if (dest != null) startActivity(new Intent(this, dest));
    }
}
