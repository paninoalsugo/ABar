package com.example.lucacorsini.a_bar;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.lucacorsini.a_bar.db.DBhelper;
import com.example.lucacorsini.a_bar.db.DbUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class BackupAndRestore {
    private static DBhelper dbhelper;

    public static void importDB(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                dbhelper=new DBhelper(context);
                File backupDB = context.getDatabasePath(dbhelper.getDatabaseName());
                String backupDBPath = String.format("%s.bak", dbhelper.getDatabaseName());
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                //MyApplication.toastSomething(context, "Import Successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportDB(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                dbhelper=new DBhelper(context);
                String backupDBPath = String.format("%s.bak", dbhelper.getDatabaseName());
                File currentDB = context.getDatabasePath(dbhelper.getDatabaseName());
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                //MyApplication.toastSomething(context, "Backup Successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
