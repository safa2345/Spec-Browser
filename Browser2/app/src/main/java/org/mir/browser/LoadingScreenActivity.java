package org.mir.browser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingScreenActivity extends AppCompatActivity {

    private int splashTimeout = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent splashIntent = new Intent(LoadingScreenActivity.this, HomeActivity.class);
                startActivity(splashIntent);
                finish();
            }
        }, splashTimeout);
    }
}