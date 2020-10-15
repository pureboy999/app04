package com.myapp.app04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity7 extends AppCompatActivity {

    TextView textView1,textView2;
    EditText editText;
    String name;
    Double rate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);


        textView1 = findViewById(R.id.name);
        textView2 = findViewById(R.id.calculate);
        editText = findViewById(R.id.rate);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("current_name","");
        rate = Double.parseDouble(bundle.getString("current_rate",""));

        rate = 1 / (rate / 100);

        textView1.setText(name);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double num = 0.0;
                try{
                    num = Double.parseDouble(editable.toString());
                }catch (Exception e){
                    textView2.setText("请输入正确的金额");
                    return;
                }
                textView2.setText(num * rate + "");
            }
        });
    }
}