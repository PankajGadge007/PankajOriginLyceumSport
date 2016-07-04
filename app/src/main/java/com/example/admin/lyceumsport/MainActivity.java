package com.example.admin.lyceumsport;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras=getIntent().getExtras();
       id=extras.getString("id");


        ImageButton work = (ImageButton) findViewById(R.id.img1);
        work.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent1 = new Intent(MainActivity.this, WorkFragment.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("id" ,id);
                myIntent1.putExtras(mBundle);
                startActivity(myIntent1);

            }
        });
        ImageButton edu = (ImageButton) findViewById(R.id.img2);
        edu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent2 = new Intent(MainActivity.this, EducationalFragment.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("id" ,id);
                myIntent2.putExtras(mBundle);
                startActivity(myIntent2);

            }
        });
        ImageButton dev = (ImageButton) findViewById(R.id.img3);
        dev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity.this, DevelopmentFragment.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("id" ,id);
                intent3.putExtras(mBundle);
                startActivity(intent3);

            }
        });
        ImageButton cri = (ImageButton) findViewById(R.id.img4);
        cri.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CurricullumFragment.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("id" ,id);
                intent.putExtras(mBundle);
                startActivity(intent);

            }
        });
    }
}
