package com.superg280.dev.luanco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView txVersion = findViewById( R.id.textView_version);

        txVersion.setText(String.format("%s %s", getString(R.string.about_version), getString(R.string.luanco_version)));
    }
}
