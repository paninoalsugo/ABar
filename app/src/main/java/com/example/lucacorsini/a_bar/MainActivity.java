package com.example.lucacorsini.a_bar;

import android.content.Intent;
import android.content.Loader;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.widget.Button;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.database.Cursor;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucacorsini.a_bar.db.DbUtils;
import com.example.lucacorsini.a_bar.db.dbCreate;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DbUtils db = null;
    public static int width;
    public static int height;
    public int useridcassa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        width = Utils.getScreenResolution(this)[0];
        height = Utils.getScreenResolution(this)[1];
        //System.out.println("w: "+width+"  h: "+height);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        global.setPercorsoScontrino("scontrini");
        final Vibrator vibe = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

        db = new DbUtils(this);

        /*db.queryi("delete from prodotti");
        db.queryi("insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('caffè','caffè dalla nostra macchinetta','1',0.30,400,'caffe','0')");
        db.queryi("insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('acqua 1,5lt','bottiglia grande di acqua','1',0.25,20,'acqua_15','0')");
        db.queryi("insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('acqua 1lt','bottiglia piccola di acqua','1',0.20,20,'acqua_1','0')");
        db.queryi("insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('bibita','bibita in lattina','1',0.35,10,'bibite','0')");
        db.queryi("insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('birra','birra in lattina o bottiglia','1',0.70,10,'birra','0')");
        db.queryi("insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('gelato','gelato','1',0.50,10,'gelato','0')");
        db.queryi("insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('ghiacciolo','ghiacciolo','1',0.30,10,'ghiacciolo','0')");*/

        //db.queryi("update utenti set salto = 'A3' where cognome = 'corsini'");
        //db.queryi("update utenti set posizione = 9999 where cognome = 'cassa'");
        /*db.queryi("update utenti set salto = 'A4' where cognome = 'tilotta'");
        db.queryi("update utenti set salto = 'A1' where cognome = 'brunno'");
        db.queryi("update utenti set salto = 'A1' where cognome = 'guerini'");
        db.queryi("update utenti set salto = 'A2' where cognome = 'gaffuri'");*/
        //db.queryi("update utenti set nome = 'pietro',cognome = 'mancino', grado = 'vc', saldo = 0 where cognome = 'cognome'");
        //db.queryi("alter table utenti add column saldoticket integer");
        //db.queryi("update utenti set saldoticket = 0");
        //db.queryi("alter table rifornimenti add column confermato integer");

        //db.queryi(dbCreate.pr8);
        //db.queryi(dbCreate.rifornimenti);

        String turno = Utils.getTurnoDiAdesso();
        final Cursor utenti = db.query("select cognome,salto,id from utenti where f_annu = '0' and posizione < 9999 order by posizione asc");
        utenti.moveToFirst();
        //System.out.println(utenti.getCount()+"");
        int numut = utenti.getCount();
        int numrighe = 0;
        if((numut)%2!=0){
            numrighe=((numut)/2)+1;
        }
        else{
            numrighe=numrighe+((numut)/2);
        }
        //System.out.println(numrighe+"");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TableLayout tl = (TableLayout) findViewById(R.id.tabellautenti);
        tl.setGravity(Gravity.CENTER_HORIZONTAL);
        final Button users[] = new Button[numut];
        TableRow[] righe = new TableRow[numrighe];
        int k = 0;
        for(int i=0;i<numrighe;i++){
            righe[i] = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            righe[i].setLayoutParams(lp);
            righe[i].setPadding(0,15,0,15);

                for(int j = 0;j<2;j++){ //ciclo nelle colonne
                    if(k<numut){
                        users[k] = new Button(this);
                        users[k].setText(utenti.getString(0).toUpperCase());
                        users[k].setTextSize(46);
                        users[k].setTypeface(users[i].getTypeface(),Typeface.BOLD);
                        users[k].setTypeface(Typeface.SANS_SERIF);
                        users[k].setId(400+utenti.getInt(2));
                        users[k].setGravity(Gravity.CENTER);
                        users[k].setWidth((width-20)/2);
                        if(turno.equalsIgnoreCase(utenti.getString(1))){ //utente in salto
                            users[k].setTypeface(users[k].getTypeface(), Typeface.ITALIC);
                        }
                        users[k].setOnClickListener(ClickListener);
                        righe[i].addView(users[k],j);
                        utenti.moveToNext();
                        k++;
                    }
                }

            tl.addView(righe[i]);
        }
        //ultima riga
            TableRow rigacassa = new TableRow(this);
            TableRow.LayoutParams lpc = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            lpc.gravity = Gravity.CENTER;
            lpc.span = 2;
            rigacassa.setLayoutParams(lpc);
            rigacassa.setPadding(0,15,0,0);
            Button cassa = new Button(this);
            Cursor ca = db.query("select id,saldo,posizione,cognome from utenti where posizione = 9999 and cognome = 'cassa'");
            if(ca!=null){
                ca.moveToFirst();
                cassa.setId(400+ca.getInt(0));
                cassa.setTypeface(Typeface.SANS_SERIF);
                cassa.setText(ca.getString(3).toUpperCase());
                cassa.setTextColor(Color.RED);
                cassa.setTextSize(46);
                cassa.setTypeface(cassa.getTypeface(),Typeface.BOLD_ITALIC);
                cassa.setGravity(Gravity.CENTER);
                cassa.setWidth((width-20));
                cassa.setOnClickListener(ClickListener);
                rigacassa.addView(cassa,lpc);
                useridcassa = ca.getInt(0);
            }
        tl.addView(rigacassa);

    }

    private View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Vibrator vibe = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
            int id = v.getId();
            id = id - 400;
            vibe.vibrate(80);
            Intent in;
            global.setUserId(id);
            if(id==useridcassa){
                in = new Intent(v.getContext(),CassaActivity.class);
            }
            else{
                in = new Intent(v.getContext(),DettaglioActivity.class);
            }
            in.putExtra("utente",id);
            startActivity(in);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch(id)
        {
            case R.id.DB:
                Intent i = new Intent(this,DBActivity.class);
                startActivity(i);
                break;
            case R.id.action_settings:
			/*
			 	Codice di gestione
			 */
        }
        return false;
    }

}
