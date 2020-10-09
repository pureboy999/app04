package com.myapp.app04;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "MainActivity2";

    EditText ed1;
    EditText ed2;
    EditText ed3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        ed1 = findViewById(R.id.edit1);
        ed2 = findViewById(R.id.edit2);
        ed3 = findViewById(R.id.edit3);


        Intent intent = getIntent();
//        float dollar2 = intent.getFloatExtra("dollar_rate",0.0f);
//        float euro2 = intent.getFloatExtra("euro_rate",0.0f);
//        float won2 = intent.getFloatExtra("won_rate",0.0f);

        Bundle bdl=intent.getExtras();
        float dollar2 = bdl.getFloat("dollar_rate",0.0f);
        float euro2 = bdl.getFloat("euro_rate",0.0f);
        float won2 = bdl.getFloat("won_rate",0.0f);

        Log.i(TAG,"MainActivity2:dollor2="+dollar2);
        Log.i(TAG,"MainActivity2:euro2="+euro2);
        Log.i(TAG,"MainActivity2:won2="+won2);

        ed1.setText(dollar2+"");
        ed2.setText(euro2+"");
        ed3.setText(won2+"");


    }
    public void save(View V){
        String str1=ed1.getText().toString();
        String str2=ed2.getText().toString();
        String str3=ed3.getText().toString();

        Intent main  = new Intent(this,MainActivity.class);
        Log.i("findthis","MainActivity:back="+str1);
        Log.i(TAG,"MainActivity:euro2="+str2);
        Log.i(TAG,"MainActivity:won2="+str3);
        Bundle bdl = new Bundle();
        bdl.putFloat("dollar_rate",Float.parseFloat(str1));
        bdl.putFloat("euro_rate",Float.parseFloat(str2));
        bdl.putFloat("won_rate",Float.parseFloat(str3));
        main.putExtras(bdl);

        SharedPreferences sp= getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit =sp.edit();
        edit.putFloat("dollar_rate",Float.parseFloat(str1));
        edit.putFloat("euro_rate",Float.parseFloat(str2));
        edit.putFloat("won_rate",Float.parseFloat(str3));
        edit.apply();
        setResult(2,main);

        finish();

    }
}