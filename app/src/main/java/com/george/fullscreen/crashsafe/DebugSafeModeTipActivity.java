package com.george.fullscreen.crashsafe;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.george.fullscreen.R;

public class DebugSafeModeTipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_safe_mode_tip);
        findViewById(R.id.log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(CrashLogFragment.class.getName());
                if (fragment == null) {
                    fragment = new CrashLogFragment();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment, CrashLogFragment.class.getName())
                        .commit();
            }
        });
    }
}
