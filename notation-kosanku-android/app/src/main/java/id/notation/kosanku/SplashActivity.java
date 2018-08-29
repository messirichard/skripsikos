package id.notation.kosanku;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import id.notation.kosanku.utils.SharedPreferenceManager;

public class SplashActivity extends AppCompatActivity {

    Intent newActivityIntent;
    SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.azure_primary_dark));

        sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  Log.d("##", "run: " + sharedPreferenceManager.getLoginState());
                  if (sharedPreferenceManager.getLoginState()) {
                      newActivityIntent = new Intent(SplashActivity.this, MainActivity.class);
                  } else {
                      newActivityIntent = new Intent(SplashActivity.this, LoginActivity.class);
                  }

                  newActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(newActivityIntent);
                  finish();
              }
        }, 2000);
    }
}
