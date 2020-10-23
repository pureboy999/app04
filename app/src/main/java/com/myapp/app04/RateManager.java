package com.myapp.app04;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.aware.PublishConfig;

import java.util.ArrayList;
import java.util.List;

public class RateManager {
    private  DBHelper dbHelper;
    private String TBNAME;

    public RateManager(Context context){
        dbHelper =new DBHelper(context);
        TBNAME =DBHelper.TB_NAME;
    }

    public void add(RateItem item){
        SQLiteDatabase db =dbHelper.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("curname",item.getCurName());
        values.put("currate",item.getCurRate());

        db.insert(TBNAME,null,values);
        db.close();
    }

    public RateItem findById(int id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME,null,"ID=?",new String[]{String.valueOf(id)},null,null,null);

        RateItem item=null;
        if(cursor!=null&&cursor.moveToFirst()){
            item=new RateItem();
            item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            item.setCurName(cursor.getString(cursor.getColumnIndex("CURNAME")));
            item.setCurRate(cursor.getString(cursor.getColumnIndex("CURRATE")));
            cursor.close();
        }
        db.close();
        return item;
    }

    public List<RateItem> findAll(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME,null,
                null,null,
                null,null,null);

        List<RateItem> list = new ArrayList<>();

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("curname"));
                String rate = cursor.getString(cursor.getColumnIndex("currate"));

                RateItem currency = new RateItem();
                currency.setId(id);
                currency.setCurName(name);
                currency.setCurRate(rate);

                list.add(currency);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return list;
    }


    public void addAll(List<RateItem> list){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for(RateItem currency:list) {
            ContentValues values = new ContentValues();
            values.put("curname", currency.getCurName());
            values.put("currate", currency.getCurRate());
            db.insert(TBNAME, null, values);
        }
        db.close();
    }
    public void deleteAll(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,null,null);
    }
}
