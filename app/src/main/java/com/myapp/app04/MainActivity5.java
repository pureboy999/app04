package com.myapp.app04;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity5 extends AppCompatActivity implements Runnable{
    Handler handler;
    ListView lv;
    int a,b,c;//保存shareP中的时间
    LocalDate localDate ;
    LocalDate localDate1 ;
    Period period ;
    SharedPreferences sp;//用于保存allrate
    private static final String TAG = "MainActivity3_test";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lv=(ListView)findViewById(R.id.mylist);//列表

        //读取之前保存的时间
        SharedPreferences sP1 = getSharedPreferences("mytime", Activity.MODE_PRIVATE);
        //读取以保存的年月日
        a = Integer.parseInt(sP1.getString("year",""));
        b = Integer.parseInt(sP1.getString("month",""));
        c = Integer.parseInt(sP1.getString("day",""));
        localDate = LocalDate.now();
        localDate1 = LocalDate.of(a, b, c);
        period = Period.between(localDate1, localDate);
        if(period.getDays()==0){
            String allrate = sp.getString("allRate","");
            String[] rateArray = allrate.split(",");
            List<String> list = Arrays.asList(rateArray);
            ListAdapter adapter = new ArrayAdapter<String>(MainActivity5.this,android.R.layout.simple_list_item_1,list);
            lv.setAdapter(adapter);
        }else{
            //更新数据
            updateRate();
        }
    }
//        ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
//        lv.setAdapter(adapter);


    private void updateRate(){
        //因为需要联网所以子线程
        Thread t = new Thread(this);
        t.start();

        handler = new Handler(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    String allrate = "";
                    List<String> list = (List<String>) msg.obj;
                    //将从网络上得到的新数据在列表中展示
                    ListAdapter adapter = new ArrayAdapter<String>(MainActivity5.this,android.R.layout.simple_list_item_1,list);
                    lv.setAdapter(adapter);

                    //将获取到的内容以“，”为间隔合成一个字符串
                    for(String s:list){
                        allrate += s + ",";
                    }
                    sp = getSharedPreferences("rate", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    //将内容保存下来
                    editor.putString("allRate",allrate);
                    Log.e(TAG, "handleMessage: allrate"+allrate);
                    editor.apply();
                    //更新时间
                    SharedPreferences sP1 = getSharedPreferences("mytime", Activity.MODE_PRIVATE);
                    String time=LocalDate.now().toString();
                    Log.e(TAG, "onCreate: "+time);
                    SharedPreferences.Editor edit1 =sP1.edit();
                    edit1.putString("year",time.split("-")[0]);
                    edit1.putString("month",time.split("-")[1]);
                    edit1.putString("day",time.split("-")[2]);
                    edit1.apply();
                }
                super.handleMessage(msg);
            }
        };
    }
    @Override
    public void run() {
        String url = "https://www.usd-cny.com/bankofchina.htm";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        }catch (IOException e){
//            Log.e(TAG, "run: " +  e.toString());
            e.printStackTrace();
        }

        List<String> message = getContent(doc);//读取list结构的内容
        Message msg = handler.obtainMessage(1);
        msg.obj = message;
        handler.sendMessage(msg);//发送消息1

    }

    //
    private List<String> getContent(Document doc){
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