package com.myapp.app04;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity6 extends AppCompatActivity implements Runnable,AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    Handler handler;
    ListView lv;
    MyAdapter myad;
    SharedPreferences sp;
    String date;
    private static final String TAG = "MainActivity6_test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lv=(ListView)findViewById(R.id.mylist);

        sp = getSharedPreferences("rate", Activity.MODE_PRIVATE);
        date = sp.getString("date","");
        //当前日期
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sdf.format(now);

        //是同一天则直接从文件中读出数据
        if(date.equals(nowDate)){
            //从网络中获取数据
            getData();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("date",nowDate);
            editor.apply();
//            List<RateItem> list = new ArrayList<>();
//            RateManager rateManager = new RateManager(getBaseContext());
//            list = rateManager.findAll();
//            myad=new MyAdapter(MainActivity6.this,R.layout.mylist2,list);
////                    SimpleAdapter listItemAdapter = new SimpleAdapter(MainActivity6.this,list,R.layout.mylist2,new String[]{"itemTitle","itemDetail"},new int[]{R.id.itemTitle,R.id.itemDetail});
//            lv.setAdapter(myad);
//            Log.i(TAG,"MESSAGE_WHAT1");
        }else{
            //从网络中获取数据
            getData();
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("date",nowDate);
            editor.apply();
        }
//        handler=new Handler(){
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//
//                if(msg.what==1){
//                    List<RateItem> list = (List<RateItem>) msg.obj;
//                    myad=new MyAdapter(MainActivity6.this,R.layout.mylist2,list);
////                    SimpleAdapter listItemAdapter = new SimpleAdapter(MainActivity6.this,list,R.layout.mylist2,new String[]{"itemTitle","itemDetail"},new int[]{R.id.itemTitle,R.id.itemDetail});
//                    lv.setAdapter(myad);
//                    Log.i(TAG,"MESSAGE_WHAT1");
//
//                }
//                super.handleMessage(msg);
//            }
//        };
//        ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
//        lv.setAdapter(adapter);
        lv.setEmptyView(findViewById(R.id.nodata));
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);

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

        List<RateItem> message = getMessage(doc);

        Message msg = handler.obtainMessage(1);
        msg.obj = message;
        handler.sendMessage(msg);

    }

    private List<RateItem> getMessage(Document doc){
        Elements tables = doc.getElementsByTag("table");
        Element table = tables.get(0);

        List<RateItem> list = new ArrayList<>();

        Elements trs = table.getElementsByTag("tr");
        Element e = null;
        for(int i = 1;i < trs.size();i++){
            e = trs.get(i);
//            HashMap<String,String> map = new HashMap<>();
//            map.put("itemTitle",e.getElementsByTag("td").get(0).text());
//            map.put("itemDetail",e.getElementsByTag("td").get(0).text());
            RateItem map = new RateItem();
            map.setCurRate(e.getElementsByTag("td").get(5).text());
            map.setCurName(e.getElementsByTag("td").get(0).text());
            list.add(map);
        }
        return list;

    }

    private void getData(){
        Thread t = new Thread(this);
        t.start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    String allRate = "";
                    List<RateItem> list = (List<RateItem>) msg.obj;
                    myad = new MyAdapter(MainActivity6.this,
                            R.layout.mylist2,
                            list);

                    lv.setAdapter(myad);

                    for (RateItem currency : list) {
                        allRate += currency.getCurName() + ":" + currency.getCurRate() + ",";
                    }

                    sp = getSharedPreferences("rate", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("allRate", allRate);
                    editor.apply();

                    RateManager rateManager = new RateManager(getBaseContext());
                    rateManager.deleteAll();
                    rateManager.addAll(list);
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAtPosition =lv.getItemAtPosition(position);
        RateItem map=(RateItem)itemAtPosition;
        String titleStr = map.getCurName();
        String detailerStr = map.getCurRate();

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

        //删除数据

    }
    int i;
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        i=position;
        b.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG,"onClick:对话框事件处理");
                        myad.remove(lv.getItemAtPosition(i));
                    }
                }).setNegativeButton("否",null);
        b.create().show();
        return true;
    }
}