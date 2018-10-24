package com.example.lucacorsini.a_bar;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String getTurnoDiAdesso(){
        String turno = "";
        int salto;
        Calendar c = Calendar.getInstance();
        Calendar a1 = Calendar.getInstance();
        a1.set(2018,9,5); // non so perchè ma va messo un mese in meno...
        long msDiff = c.getTimeInMillis() - a1.getTimeInMillis();
        int daysDiff = (int) (long) TimeUnit.MILLISECONDS.toDays(msDiff);
        SimpleDateFormat df = new SimpleDateFormat("H");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if(Integer.parseInt(df.format(c.getTime()))>=20){
            switch ((daysDiff+3)%4){
                case 0:
                    turno = "A";
                    break;
                case 1:
                    turno = "B";
                    break;
                case 2:
                    turno = "C";
                    break;
                case 3:
                    turno = "D";
                    break;
            }
            if((daysDiff/4)%8==0){
                salto = (daysDiff/4)%8;
            }
            else{
                salto = 1+((daysDiff/4)%8);
            }
            if(salto==0){
                salto = 8;
            }
            turno = turno+salto;
        }
        else if(Integer.parseInt(df.format(c.getTime()))<=7){
            switch ((daysDiff+2)%4){
                case 0:
                    turno = "A";
                    break;
                case 1:
                    turno = "B";
                    break;
                case 2:
                    turno = "C";
                    break;
                case 3:
                    turno = "D";
                    break;
            }
            daysDiff = daysDiff-1;
            if((daysDiff/4)%8==0){
                salto = (daysDiff/4)%8;
            }
            else{
                salto = 1+((daysDiff/4)%8);
            }
            if(salto==0){
                salto = 8;
            }
            turno = turno+salto;
        }
        else{
            switch (daysDiff%4){
                case 0:
                    turno = "A";
                    break;
                case 1:
                    turno = "B";
                    break;
                case 2:
                    turno = "C";
                    break;
                case 3:
                    turno = "D";
                    break;
            }
            salto =((daysDiff/4)%8)+1;
            turno = turno+salto;
        }
        //System.out.println("giorno di rif: "+sdf.format(a1.getTime())+" daysDiff: "+daysDiff+" turno: "+turno+" salto: "+salto);
        return turno;
    }

    public static int[] getScreenResolution(Context context)
    {
        int size[] = new int[2];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        size[0] = width;
        size[1] = height;

        return size;
    }


}
