package com.myapp.app04;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.transform.ErrorListener;

public class MainActivity extends AppCompatActivity {
    private EditText et;
    private TextView tv;
    private Button bt1;          //定义按钮
    private Button bt2;
    private Button bt3;
    private Button bt4;

    float dollar;
    float won;
    float euro;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et=(EditText)findViewById(R.id.num);
        tv=(TextView)findViewById(R.id.textView);


        Intent intent=getIntent();
        dollar = 2.0f;
        euro = 3.0f;
        won = 4.0f;


    }
//    public void btn1(View v){
//        Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
//        String str=et.getText().toString();
//        int temp=Integer.valueOf(str);
//        tv.setText(String.valueOf(temp*dollar));
//    }
//    public void btn2(View v){
//        String str=et.getText().toString();
//        int temp=Integer.valueOf(str);
//        tv.setText(String.valueOf(temp*euro));
//    }
//    public void btn3(View v){
//        String str=et.getText().toString();
//        int temp=Integer.valueOf(str);
//        tv.setText(String.valueOf(temp*won));
//    }
    public void allbtn(View v){
        String str;
        try {
            str=et.getText().toString();
        }catch (Error ee){
            Toast.makeText(this, "please input data", Toast.LENGTH_SHORT).show();
            return;
        }
        if(v.getId()==R.id.button){
            int temp=Integer.valueOf(str);
            tv.setText(String.valueOf(temp*dollar));
        }
        else if(v.getId()==R.id.button2){
            int temp=Integer.valueOf(str);
            tv.setText(String.valueOf(temp*euro));
        }
        else{
            int temp=Integer.valueOf(str);
            tv.setText(String.valueOf(temp*won));
        }
    }
    public void change(View v){
        Intent config  = new Intent(this,MainActivity2.class);
//        config.putExtra("dollar_rate",dollar);
//        config.putExtra("euro_rate",euro);
//        config.putExtra("won_rate",won);
        Bundle bdl = new Bundle();
        bdl.putFloat("dollar_rate",dollar);
        bdl.putFloat("euro_rate",euro);
        bdl.putFloat("won_rate",won);
        config.putExtras(bdl);

        Log.i(TAG,"MainActivity:dollarRate:"+dollar);
        Log.i(TAG,"MainActivity:euroRate:"+euro);
        Log.i(TAG,"MainActivity:wonRate:"+won);

        startActivityForResult(config,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==2){
            Bundle bundle = data.getExtras();
            dollar = bundle.getFloat("dollar_rate",2.0f);
            euro = bundle.getFloat("euro_rate",3.0f);
            won = bundle.getFloat("won_rate",4.0f);
            Log.i(TAG,"onActivityResult: dollarRate="+ dollar);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}