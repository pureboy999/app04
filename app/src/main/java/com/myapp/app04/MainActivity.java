package com.myapp.app04;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.transform.ErrorListener;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity implements  Runnable {
    private EditText et;
    private TextView tv;

    LocalTime t1;

    private static final String TAG = "MainActivity";
    float dollar;
    float won;
    float euro;
    Handler handler;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et=(EditText)findViewById(R.id.num);
        tv=(TextView)findViewById(R.id.textView);




        SharedPreferences sP = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        dollar = sP.getFloat("dollar_rate",2.0f);
        euro = sP.getFloat("euro_rate",3.0f);
        won = sP.getFloat("won_rate",4.0f);


        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==5){
                    String str=(String) msg.obj;
                    Log.i(TAG,"handleMessage:getMassage msg ="+str);
                    //show.setTexr(str);
                }
                super.handleMessage(msg);
            }
        };


        Thread t = new Thread(this);
        t.start();


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

    public void duration(LocalTime t1){


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

    @Override
    public void run() {
        String url = "https://www.usd-cny.com/bankofchina.htm";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        }catch (IOException e){
            e.printStackTrace();
        }

        List<String> message = getMessage(doc);

        Message msg = handler.obtainMessage(2);
        msg.obj = message;
        handler.sendMessage(msg);
//        URL url=null;
//        try{
//        url =new URL("https://www.baidu.com");
//            HttpsURLConnection http=(HttpsURLConnection) url.openConnection();
//            InputStream in = http.getInputStream();
//
//            String html =inputStream2String(in);
//            Log.i(TAG,"RUN:HTML="+html);
//
//        }catch (MalformedURLException e){
//            e.printStackTrace();
//        }catch (IOException e){
//            e.printStackTrace();
//        }

    }
    private  String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize =1024;
        final char[] buffer =new char[bufferSize];
        final StringBuilder out=new StringBuilder();
        Reader in=new InputStreamReader(inputStream,"utf-8");
        while(true){
            int rsz=in.read(buffer,0,buffer.length);
            if(rsz<0)
                break;
            out.append(buffer,0,rsz);
        }
        return  out.toString();
    }


    private List<String> getMessage(Document doc){
        Elements tables = doc.getElementsByTag("table");
        Element table = tables.get(0);

        List<String> list = new ArrayList<>();

        Elements trs = table.getElementsByTag("tr");
        Element e = null;
        for(int i = 1;i < trs.size();i++){
            e = trs.get(i);
            list.add(e.getElementsByTag("td").get(0).text() + ":" + e.getElementsByTag("td").get(5).text());
        }

        return list;
    }
}