package com.example.lucacorsini.a_bar.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import java.text.*;

public class DbUtils {

    private DBhelper dbhelper;

    public DbUtils(Context ctx){

        dbhelper=new DBhelper(ctx);

    }

    public Cursor query(String q){
        Cursor crs = null;
        SQLiteDatabase dbr=dbhelper.getReadableDatabase();
        try
        {
            crs = dbr.rawQuery(q, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }

    public void queryi(String q){
        SQLiteDatabase dbw=dbhelper.getWritableDatabase();
        try{
            dbw.execSQL(q);
        }
        catch(SQLiteException sqle){
            System.out.println(sqle.toString());
        }
    }

    public String getDBname(){
        return dbhelper.getDatabaseName();
    }
}
