package com.example.batyaa.tulan_cosmetics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class saplshScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saplsh_screen);
        Thread timer = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(5000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent intent = new Intent(saplshScreen.this,LoginActivity.class);
                    startActivity(intent);

                }
            }

        };
        timer.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
