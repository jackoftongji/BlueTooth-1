package ych.com.bluetooth.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import ych.com.bluetooth.R;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);
        TextView textView = new TextView(this);
        textView.setText(data);
        linearLayout.addView(textView);
    }
}
