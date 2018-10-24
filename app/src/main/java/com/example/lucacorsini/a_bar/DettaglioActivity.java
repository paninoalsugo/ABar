package com.example.lucacorsini.a_bar;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.View;
import android.content.Intent;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ImageButton;
import android.app.AlertDialog;
import android.database.Cursor;
import android.content.Context;
import android.widget.Toast;

import com.example.lucacorsini.a_bar.db.DbUtils;

import java.text.NumberFormat;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Locale;

public class DettaglioActivity extends AppCompatActivity {

    private DbUtils db = null;
    final int numcolonne = 3;
    final int hhh = 666;
    public int UserId = 0;
    public static int width;
    public static int height;

    public NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i=getIntent();
        int userid = i.getIntExtra("utente",0);
        if(userid>0){
            global.setUserId(userid);
        }
        UserId=global.getUserId();
        //System.out.println("Username: "+username);
        width = Utils.getScreenResolution(this)[0];
        height = Utils.getScreenResolution(this)[1];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DbUtils(this);

        format.setCurrency(Currency.getInstance(Locale.ITALY));
        format.setMinimumFractionDigits(2);
        String q;
        q = "select id,nome,cognome,grado,saldo from utenti where id = "+UserId+" and f_annu = '0'";
        //System.out.println(q);
        Cursor res = db.query(q);
        res.moveToFirst();
        TextView user=(TextView) findViewById(R.id.Titolo);
        user.setText(res.getString(3).toUpperCase()+" "+res.getString(1).toUpperCase()+" "+res.getString(2).toUpperCase());
        float saldo = res.getFloat(4);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("E dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        Cursor prod = db.query("select id,nome,descrizione,prezzo,icon,qta from prodotti where tipo = 1 and f_annu = '0' order by id asc");
        int numprod = prod.getCount();
        /*prod.moveToFirst();
        for(int u = 0;u<numprod;u++){
            System.out.println(prod.getInt(0)+" "+prod.getString(1)+" "+prod.getString(2)+" "+prod.getFloat(3)+" "+prod.getString(4));
            prod.moveToNext();
        }*/
        TableLayout tl = (TableLayout) findViewById(R.id.tabellaprodotti);
        tl.setVerticalScrollBarEnabled(true);
        ImageButton[] product = new ImageButton[numprod];
        TextView[] plab = new TextView[numprod];
        int numrow = 0;
        int temp = numprod;
        int k = 0;
        int kl = 0;
        while(temp%numcolonne!=0){
            temp++;
        }
        numrow = temp/numcolonne;
        numrow = (numrow * 2) + 3; // le righe saranno relative a tutti i prodotti più l'etichetta sotto e altre tre, la data in alto, i pulsanti e il saldo in basso
        TableRow[] righe = new TableRow[numrow];
        String[] labels = new String[numcolonne];
        int[] labid = new int[numcolonne];
        //System.out.println("temp: "+temp+" righe: "+numrow+" colonne: "+numcolonne+ " prodotti: "+numprod);
        prod.moveToFirst();
        //********* CICLO NELLE RIGHE DELLA TABELLA *********//
        for (int j=0;j<numrow;j++){
            righe[j] = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.gravity = Gravity.CENTER;
            righe[j].setLayoutParams(lp);
            if(j==0){ // prima riga, metto la data
                TextView oggi = new TextView(this);
                oggi.setTextSize(30);
                oggi.setTypeface(null, Typeface.ITALIC);
                oggi.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                oggi.setText(formattedDate+"  TURNO  "+Utils.getTurnoDiAdesso());
                oggi.setGravity(Gravity.CENTER);
                TableRow.LayoutParams lpo = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                lpo.span = 3;
                oggi.setLayoutParams(lpo);
                righe[j].addView(oggi);
            }
            if(j%2==1){ // riga dispari, metto i pulsanti con i prodotti
                for(int h=0;h<numcolonne;h++){ // ciclo nelle colonne dei prodotti inserendo i pulsanti
                    if(k<numprod){
                        //System.out.println("k: "+k);
                        product[k] = new ImageButton(this);
                        product[k].setId(prod.getInt(0));
                        product[k].setContentDescription(prod.getString(2));
                        product[k].setScaleType(ImageView.ScaleType.CENTER_CROP);
                        product[k].setOnClickListener(ClickListener);
                        product[k].setMaxHeight(width/3);
                        product[k].setMaxWidth(width/3);
                        Context context = product[k].getContext();
                        int id = context.getResources().getIdentifier(prod.getString(4), "drawable", context.getPackageName());
                        product[k].setImageResource(id);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String oggi = sdf.format(c.getTime());
                        Cursor acquistiprodotto = db.query("select sum(qta) from acquisti where utente = "+UserId+" and prodotto = "+prod.getInt(0)+" and substr(data,1,10) = '"+oggi+"' and f_annu = '0'");
                        acquistiprodotto.moveToFirst();
                        String numoggi = "";
                        if(acquistiprodotto!=null){
                            numoggi = acquistiprodotto.getInt(0)+"";
                        }
                        labels[h]=format.format(prod.getFloat(3))+"  ("+prod.getInt(5)+")\n"+prod.getString(1)+" di oggi: "+numoggi;
                        labid[h]=prod.getInt(0)+1000;
                        TableRow.LayoutParams lpb = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                        lpb.height = width/3;
                        lpb.width = width/3;
                        product[k].setLayoutParams(lpb);
                        righe[j].addView(product[k],h);
                        prod.moveToNext();
                        k++;
                    }
                }
            }
            if(j%2==0 && j!=0){ // riga pari, metto le label con il testo dei prodotti
                for(int h=0;h<numcolonne;h++){
                    if(kl<numprod){
                        //System.out.println("kl: "+kl);
                        plab[kl] = new TextView(this);
                        plab[kl].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        plab[kl].setTextColor(Color.BLACK);
                        plab[kl].setTextSize(18);
                        plab[kl].setText(labels[h]);
                        plab[kl].setId(labid[h]);
                        righe[j].addView(plab[kl],h);
                        kl++;
                    }
                }
            }
            if(j==numrow-2){ // penultima riga, metto i pulsanti versamento e scontrino
                TableRow.LayoutParams lprv = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                lprv.gravity = Gravity.CENTER;
                righe[j].setLayoutParams(lprv);
                righe[j].setPadding(0,15,0,15);
                /*final Button versamento = new Button(this);
                versamento.setTextSize(24);
                versamento.setText("Versamento");
                versamento.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                versamento.setTypeface(null, Typeface.BOLD);
                versamento.setTypeface(null,Typeface.SANS_SERIF.getStyle());
                TableRow.LayoutParams lpv = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lpv.gravity = Gravity.CENTER;
                lpv.weight = 1;
                versamento.setLayoutParams(lpv);
                versamento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Nuovo versamento");
                        builder.setMessage("Inserire l'importo versato in cassa");
                        // Set an EditText view to get user input
                        final EditText input = new EditText(view.getContext());
                        builder.setView(input);
                        input.setTextSize(48);
                        int type;
                        type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
                        input.setInputType(type);
                        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                        builder.setPositiveButton("Versa", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String value = input.getText().toString();
                                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
                                String adesso = df2.format(c.getTime());
                                Cursor saldoadesso = db.query("select saldo from utenti where id = "+UserId);
                                saldoadesso.moveToFirst();
                                float saldonow = 0;
                                if(saldoadesso!=null){
                                    saldonow = Float.parseFloat(value) + saldoadesso.getFloat(0);
                                }
                                db.queryi("insert into versamenti (utente,cifra,ticket,valoreticket,data,f_annu) values ("+UserId+","+value+",0,0,'"+adesso+"','0')");
                                db.queryi("insert into movimenticassa (tipo,cifra,ticket,valoreticket,descrizione,data,f_annu) values (4,"+value+",0,0,'idutente "+UserId+"','"+adesso+"','0')");
                                db.queryi("update utenti set saldo = "+String.valueOf(saldonow)+" where id = "+UserId);

                                TextView saldotxt = (TextView)findViewById(hhh);
                                if(saldonow>=0) {
                                    saldotxt.setTextColor(Color.GREEN);
                                }
                                else{
                                    saldotxt.setTextColor(Color.RED);
                                }
                                saldotxt.setText(format.format(saldonow));
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                            }
                        });
                        builder.show();
                    }
                });
                righe[j].addView(versamento,lpv);*/

                final Button scontrino = new Button(this);
                scontrino.setTextSize(24);
                scontrino.setText("Scontrino");
                scontrino.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                scontrino.setTypeface(null, Typeface.BOLD);
                scontrino.setTypeface(null,Typeface.SANS_SERIF.getStyle());
                TableRow.LayoutParams lps = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
                lps.gravity = Gravity.CENTER;
                lps.weight = 1;
                //versamento.setLayoutParams(lps);
                scontrino.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(),ScontrinoActivity.class);
                        startActivity(i);
                    }
                });
                righe[j].addView(scontrino,lps);
            }
            if(j==numrow-1){ // ultima riga, metto i dettagli del saldo
                TableRow.LayoutParams lprs = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lprs.gravity = Gravity.CENTER;
                righe[j].setLayoutParams(lprs);
                //righe[j].setPadding(0,15,0,0);
                TextView saldolabel = new TextView(this);
                TableRow.LayoutParams lpsa = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lpsa.gravity = Gravity.CENTER;
                lpsa.weight = 1;
                saldolabel.setLayoutParams(lpsa);
                saldolabel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                saldolabel.setAllCaps(true);
                saldolabel.setTextColor(Color.BLACK);
                saldolabel.setTextSize(36);
                saldolabel.setText("SALDO");
                righe[j].addView(saldolabel,0,lpsa);

                TableRow.LayoutParams lprsa = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lprs.gravity = Gravity.CENTER;
                righe[j].setLayoutParams(lprsa);
                TextView saldotxt = new TextView(this);
                TableRow.LayoutParams lpsal = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                lpsal.gravity = Gravity.CENTER;
                lpsal.weight = 1;
                saldotxt.setLayoutParams(lpsa);
                saldotxt.setTag("saldoamount");
                saldotxt.setId(hhh);
                saldotxt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                saldotxt.setAllCaps(true);
                saldotxt.setTextSize(36);
                if(saldo>=0) {
                    saldotxt.setTextColor(Color.GREEN);
                }
                else{
                    saldotxt.setTextColor(Color.RED);
                }

                saldotxt.setText(format.format(saldo));
                righe[j].addView(saldotxt,1,lpsal);
            }
            tl.addView(righe[j]);
        }

    }

    private View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Vibrator vibe = (Vibrator) DettaglioActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
            int id = v.getId();
            vibe.vibrate(80);
            float prezzo = 0;
            final Calendar c = Calendar.getInstance();
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
            String adesso = df2.format(c.getTime());
            Cursor prodotto = db.query("select prezzo,nome,prezzo from prodotti where id = "+id+" and f_annu = '0'");
            if(prodotto!=null){
                prodotto.moveToFirst();
                prezzo = prodotto.getFloat(0);
                db.queryi("insert into acquisti (utente,prodotto,qta,prezzo,data,f_annu) values ("+UserId+","+id+",1,"+prezzo+",'"+adesso+"','0')");
                float saldonow = updateSaldoUtente(UserId,0-prezzo);
                TextView s = (TextView) findViewById(hhh);
                if(saldonow>=0) {
                    s.setTextColor(Color.GREEN);
                }
                else{
                    s.setTextColor(Color.RED);
                }
                s.setText(format.format(saldonow));
                TextView q = (TextView) findViewById(id+1000);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String oggi = sdf.format(c.getTime());
                Cursor acquistiprodotto = db.query("select sum(qta) from acquisti where utente = "+UserId+" and prodotto = "+id+" and substr(data,1,10) = '"+oggi+"' and f_annu = '0'");
                acquistiprodotto.moveToFirst();
                String numoggi = "";
                if(acquistiprodotto!=null){
                    numoggi = acquistiprodotto.getInt(0)+"";
                }
                int newqta = decrementaProdotto(id);
                String newlabel = format.format(prodotto.getFloat(2))+"  ("+newqta+")\n"+prodotto.getString(1)+" di oggi: "+numoggi;
                q.setText(newlabel);
                String tost = "Il prodotto '"+prodotto.getString(1).toUpperCase()+"' ";
                if(newqta<1){
                    tost = tost+"è finito";
                }
                else {
                    if(newqta>=1&&newqta<5){
                        tost = tost+"sta finendo";
                    }
                    else{
                        if(newqta==-999){
                            tost = tost+"non ha nessuna quantità impostata o non esiste";
                        }
                        else{
                            tost = tost+"è stato acquistato";
                        }
                    }
                }
                Toast.makeText(DettaglioActivity.this,tost,Toast.LENGTH_LONG).show();
            }
        }
    };

    public float updateSaldoUtente (int user,float value){
        Cursor saldoadesso = db.query("select saldo from utenti where id = "+user);
        saldoadesso.moveToFirst();
        float saldonow = 0;
        if(saldoadesso!=null){
            saldonow = saldoadesso.getFloat(0)+value;
            db.queryi("update utenti set saldo = "+saldonow+" where id = "+user);
        }
        return saldonow;
    }

    public int decrementaProdotto (int prodotto){
        db.queryi("update prodotti set qta = qta-1 where id = "+prodotto);
        Cursor pr = db.query("select qta from prodotti where id = "+prodotto);
        if(pr!=null){
            pr.moveToFirst();
            return pr.getInt(0);
        }
        else{
            return -999;
        }
    }

    public int getQtaProdotto (int prodotto){
        Cursor pr = db.query("select qta from prodotti where id = "+prodotto);
        if(pr!=null){
            pr.moveToFirst();
            return pr.getInt(0);
        }
        else{
            return -999;
        }
    }

    public int getQtaUtenteOggi (int user, int prodotto){
        int qta = 0;
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String oggi = sdf.format(c.getTime());
        Cursor cur = db.query("select sum(qta) from acquisti where utente = "+user+" and prodotto = "+prodotto+" and substr(data,1,10) = '"+oggi+"' and f_annu = '0'");
        if(cur!=null){
            cur.moveToFirst();
            qta = cur.getInt(0);
        }
        return qta;
    }

}
