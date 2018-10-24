package com.example.lucacorsini.a_bar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.lucacorsini.a_bar.db.DbUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class AdminScontrinoActivity extends AppCompatActivity {

    private DbUtils db = null;
    public NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
    public int selezionati[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_scontrino);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        format.setCurrency(Currency.getInstance(Locale.ITALY));
        format.setMinimumFractionDigits(2);
        disegnaTabellaScontrini();
        azioniBottoni();
    }

    public void disegnaTabellaScontrini() {
        TableLayout tl = (TableLayout) findViewById(R.id.tabellaconfermascontrini);
        tl.removeAllViews();
        db = new DbUtils(this);
        Cursor r = db.query("select * from rifornimenti where confermato = 0 and f_annu = '0' order by data desc");
        if(r!=null){
            r.moveToFirst();
            int numscontrini = r.getCount();
            int col = 4;
            TableRow righe[] = new TableRow[numscontrini];
            TextView nomi[] = new TextView[numscontrini];
            TextView descrizioni[] = new TextView[numscontrini];
            Button scontrini[] = new Button[numscontrini];
            final CheckBox conferme[] = new CheckBox[numscontrini];
            selezionati = new int[numscontrini];
            for(int i=0;i<numscontrini;i++){
                righe[i] = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                lp.gravity = Gravity.CENTER;
                righe[i].setLayoutParams(lp);
                righe[i].setGravity(Gravity.CENTER);
                if(i%2==0){
                    righe[i].setBackgroundColor(Color.rgb(240,240,240));
                }
                else{
                    righe[i].setBackgroundColor(Color.rgb(240,255,240));
                }
                righe[i].setPadding(16,5,16,5);
                nomi[i] = new TextView(this);
                descrizioni[i] = new TextView(this);
                scontrini[i] = new Button(this);
                conferme[i] = new CheckBox(this);
                Cursor curute = db.query("select cognome from utenti where id = "+r.getInt(1));
                curute.moveToFirst();
                nomi[i].setText(r.getString(5).substring(0,11)+
                        "\n"+r.getString(5).substring(11)+
                        "\n"+curute.getString(0).toUpperCase(Locale.ITALIAN));
                nomi[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2f));
                Cursor curprod = db.query("select nome from prodotti where id = "+r.getInt(2));
                curprod.moveToFirst();
                String descrizione =    "Prodotto: "+curprod.getString(0)+
                        "\nQuantità: "+r.getInt(3)+
                        "\nPrezzo: "+format.format(r.getFloat(4))+
                        "\nDescrizione: "+r.getString(7);
                descrizioni[i].setText(descrizione);
                descrizioni[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 6f));
                scontrini[i].setText("Scontrino");
                scontrini[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2f));
                conferme[i].setId(r.getInt(0));
                conferme[i].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                conferme[i].setSelected(false);
                final int j = i;
                conferme[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(conferme[j].isChecked()){
                            selezionati[j]=conferme[j].getId();
                        }else{
                            selezionati[j]=0;
                        }
                    }
                });
                righe[i].addView(nomi[i],0);
                righe[i].addView(descrizioni[i],1);
                righe[i].addView(scontrini[i],2);
                righe[i].addView(conferme[i],3);
                tl.addView(righe[i]);
                r.moveToNext();
            }
        }
    }

    public void azioniBottoni(){
        Button cancellascontrini = (Button) findViewById(R.id.scontrinicancellaselezionati);
        cancellascontrini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<selezionati.length;i++){
                    if(selezionati[i]>0){
                        db.queryi("update rifornimenti set f_annu = '1' where id = "+selezionati[i]);
                    }
                }
                final AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("Successo");
                builder.setMessage("Gli scontrini selezionati sono stati annullati");
                builder.setPositiveButton("OK gentilissimo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        disegnaTabellaScontrini();
                    }
                });
                builder.show();
            }
        });
        Button confermascontrini = (Button) findViewById(R.id.scontriniconfermaselezionati);
        confermascontrini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<selezionati.length;i++){
                    if(selezionati[i]>0){
                        Cursor selected = db.query("select * from rifornimenti where id = "+selezionati[i]);
                        if(selected!=null){
                            selected.moveToFirst();
                            db.queryi("update prodotti set qta = qta+"+selected.getInt(3)+" where id = "+selected.getInt(2));
                            db.queryi("update utenti set saldo = saldo+"+selected.getFloat(4)+" where id = "+selected.getInt(1));
                            db.queryi("update rifornimenti set confermato = 1 where id = "+selezionati[i]);
                        }
                    }
                }
                final AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("Successo");
                builder.setMessage("Gli scontrini selezionati sono stati confermati");
                builder.setPositiveButton("OK gentilissimo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        disegnaTabellaScontrini();
                    }
                });
                builder.show();
            }
        });
    }

}
