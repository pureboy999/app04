package com.myapp.app04;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity6 extends AppCompatActivity implements Runnable{
    Handler handler;
    ListView lv;
    private static final String TAG = "MainActivity6_test";
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
                List<HashMap<String ,String>> list2=new ArrayList<>();
                List<String> temp=new ArrayList<>();
                String a[];
                if(msg.what==1){
                    temp=(List<String>)msg.obj;
                    a=temp.toString().split(";");
                    for(int i=0;i<a.length-1;i++){
                        HashMap<String,String>map=new HashMap<String, String>();
                        map.put("ItemTitle","Rate:"+a[i].split("!")[0]);
                        map.put("ItemDeTail","detail"+a[i].split("!")[1]);
                        list2.add(map);
                    }
                    SimpleAdapter listItemAdapter = new SimpleAdapter(MainActivity6.this,list2,R.layout.mylist2,new String[]{"ItemTitle","ItemDeTail"},new int[]{R.id.itemTitle,R.id.itemDetail});
                    lv.setAdapter(listItemAdapter);
                    Log.i(TAG,"MESSAGE_WHAT1");

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
            list.add(e.getElementsByTag("td").get(0).text() + "!" + e.getElementsByTag("td").get(5).text()+";");
        }
        return list;

    }
}