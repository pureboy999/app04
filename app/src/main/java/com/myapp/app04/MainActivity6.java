package com.myapp.app04;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

public class MainActivity6 extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener {
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

                if(msg.what==1){
                    List<HashMap<String,String>> list = (List<HashMap<String,String>>) msg.obj;
                    MyAdapter myad=new MyAdapter(MainActivity6.this,R.layout.mylist2,list);
//                    SimpleAdapter listItemAdapter = new SimpleAdapter(MainActivity6.this,list,R.layout.mylist2,new String[]{"itemTitle","itemDetail"},new int[]{R.id.itemTitle,R.id.itemDetail});
                    lv.setAdapter(myad);
                    Log.i(TAG,"MESSAGE_WHAT1");

                }
                super.handleMessage(msg);
            }
        };
//        ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
//        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);


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

        List<HashMap<String,String>> message = getMessage(doc);

        Message msg = handler.obtainMessage(1);
        msg.obj = message;
        handler.sendMessage(msg);

    }

    private List<HashMap<String,String>> getMessage(Document doc){
        Elements tables = doc.getElementsByTag("table");
        Element table = tables.get(0);

        List<HashMap<String,String>> list = new ArrayList<>();

        Elements trs = table.getElementsByTag("tr");
        Element e = null;
        for(int i = 1;i < trs.size();i++){
            e = trs.get(i);
            HashMap<String,String> map = new HashMap<>();
            map.put("itemTitle",e.getElementsByTag("td").get(0).text());
            map.put("itemDetail",e.getElementsByTag("td").get(5).text());
            list.add(map);
        }
        return list;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAtPosition =lv.getItemAtPosition(position);
        HashMap<String,String> map=(HashMap<String,String>)itemAtPosition;
        String titleStr = map.get("itemTitle");
        String detailerStr = map.get("itemDetail");

//        TextView title = (TextView) view.findViewById(R.id.itemTitle);
//        TextView detail =(TextView) view.findViewById(R.id.itemDetail);
//        String title2=String.valueOf(title.getText());
//        String detail2=String.valueOf(detail.getText());
        Intent config  = new Intent(this,MainActivity7.class);
        Bundle bdl = new Bundle();
        bdl.putString("current_name",titleStr);
        bdl.putString("current_rate",detailerStr);
        config.putExtras(bdl);
        startActivityForResult(config,1);
    }
}