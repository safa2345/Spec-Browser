package org.mir.browser.Sync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.mir.browser.R;
import org.mir.browser.Sync.SignInActivity;

public class SyncActivity extends AppCompatActivity {
    private Boolean isSingedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        if(isSingedIn == false) {
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
        }

    }
}