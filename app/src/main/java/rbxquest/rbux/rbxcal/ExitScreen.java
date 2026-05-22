package rbxquest.rbux.rbxcal;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import rbxquest.rbux.rbxcal.helpers.BhismaAds;

public class ExitScreen extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
        BhismaAds.attach(this);
        findViewById(R.id.btnYesExit).setOnClickListener(this);
        findViewById(R.id.btnNoExit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnYesExit) {
            finishAffinity();
        } else {
            onBackPressed();
        }
    }
}
