package com.myapp.app04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//listView 数据点击
public class MainActivity3 extends AppCompatActivity implements Runnable{
    Handler handler;
    ListView lv;
    private static final String TAG = "MainActivity3_test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lv=(ListView)findViewById(R.id.mylist);
        String data[]={"one","two"};
        //子线程任务


        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                List<String> list2=new ArrayList<>();
                if(msg.what==1){
                    list2=(List<String>)msg.obj;
                    ListAdapter ad =new ArrayAdapter<String>(MainActivity3.this,android.R.layout.simple_list_item_1,list2);
                    lv.setAdapter(ad);
                    Log.i(TAG,"MESSAGE_WHAT1");

                }
                List<String> list3=new ArrayList<>();
                if(msg.what==2){
                    list3=(List<String>)msg.obj;
                    list3.addAll(list2);
                    Log.i(TAG, "handleMessage: " + list2.size() + "" + list3.size());
                    Log.i(TAG,"MESSAGE_WHAT2");
                    ListAdapter ad =new ArrayAdapter<String>(MainActivity3.this,android.R.layout.simple_list_item_1,list3);
                    lv.setAdapter(ad);


                }
                super.handleMessage(msg);
            }
        };
//        ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
//        lv.setAdapter(adapter);


        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        String url = "https://www.usd-cny.com/bankofchina.htm";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        }catch (IOException e){
            Log.e(TAG, "run: " +  e.toString());
            e.printStackTrace();
        }

        List<String> message = getMessage(doc);

        Message msg = handler.obtainMessage(1);
        msg.obj = message;
        handler.sendMessage(msg);

        Message msg1 = handler.obtainMessage(2);
        List<String> message2=new ArrayList<>();
        message2.add("hello");
        message2.add("word");
        msg1.obj = message2;
        handler.sendMessage(msg1);
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