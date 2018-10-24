package com.example.lucacorsini.a_bar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucacorsini.a_bar.db.DbUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class CassaActivity extends AppCompatActivity {

    private boolean amministratore;
    private boolean giaentrato = false;
    public NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
    public DbUtils db;
    public Calendar c;
    Spinner spinner;
    public TextView saldocassaeuro;
    public TextView saldocassaticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DbUtils(this);
        if(!giaentrato){
            login();
        }
        else{
            creaActivityCassa(amministratore);
        }
    }

    private void login(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Login amministratore");
        builder.setMessage("Inserire la password di amministrazione");
        // Set an EditText view to get user input
        final EditText passwordtxt = new EditText(this);
        builder.setView(passwordtxt);
        passwordtxt.setTextSize(48);
        passwordtxt.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        passwordtxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = passwordtxt.getText().toString();
                Cursor c = db.query("select password from utenze where username = 'cassa'");
                c.moveToFirst();
                String password = c.getString(0);
                if(value.equals(password)){
                    amministratore = true;
                    giaentrato = true;
                    creaActivityCassa(amministratore);
                }
                else{
                    Toast.makeText(CassaActivity.this,"Password errata", Toast.LENGTH_LONG).show();
                    login();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                amministratore = false;
                giaentrato = true;
                creaActivityCassa(amministratore);
            }
        });
        builder.show();
    }

    protected void creaActivityCassa(boolean admin){
        int width = Utils.getScreenResolution(this)[0];
        int height = Utils.getScreenResolution(this)[1];
        format.setCurrency(Currency.getInstance(Locale.ITALY));
        format.setMinimumFractionDigits(2);
        setContentView(R.layout.activity_cassa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TableLayout tl = (TableLayout) findViewById(R.id.tabellacassa);
        saldocassaeuro = (TextView) findViewById(R.id.saldocassatxt);
        saldocassaticket = (TextView) findViewById(R.id.saldocassatxtticket);
        final Button movimenticassa = (Button) findViewById(R.id.movimentibutton);
        Button versamento = (Button) findViewById(R.id.versamentobutton);
        Button prelievo = (Button) findViewById(R.id.prelievobutton);
        Button adminprodotti = (Button) findViewById(R.id.adminprodottibutton);
        Button adminutenti = (Button) findViewById(R.id.adminutentibutton);
        final Button adminscontrini = (Button) findViewById(R.id.adminscontrinibutton);
        Button ticket = (Button) findViewById(R.id.adminticketbutton);
        final TableLayout tm = (TableLayout) findViewById(R.id.tabellamovimenti);
        //System.out.println(""+admin);
        versamento.setEnabled(admin);
        prelievo.setEnabled(admin);
        adminprodotti.setEnabled(admin);
        adminutenti.setEnabled(admin);
        adminscontrini.setEnabled(admin);
        ticket.setEnabled(admin);
        tm.setVisibility(View.INVISIBLE);
        updateSaldoCassa();
        movimenticassa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor mov = db.query("select distinct "+
                        "t.tipo,m.cifra,m.ticket,u.cognome,m.data "+
                        "from movimenticassa m left join tipologie t on t.id = m.tipo "+
                        "left join utenti u on u.id = substr(m.descrizione,9,3) "+
                        "where m.f_annu = '0' and t.tabella = 'movimenticassa' "+
                        "order by m.id desc");
                if(mov!=null){
                    tm.setVisibility(View.VISIBLE);
                    tm.removeAllViews();
                    mov.moveToFirst();
                    int nummov = mov.getCount();
                    TableRow[] righe = new TableRow[nummov];
                    TextView[] celle = new TextView[nummov*5];
                    int k = 0;
                    for(int i=0;i<nummov;i++){
                        int color;
                        if(mov.getFloat(1)<0||mov.getInt(2)<0){
                            color = Color.RED;
                        }
                        else{
                            color = Color.BLACK;
                        }
                        righe[i] = new TableRow(tm.getContext());
                        righe[i].setGravity(Gravity.CENTER);
                        celle[k] = new TextView(tm.getContext());
                        celle[k].setTextColor(color);
                        celle[k].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2f));
                        celle[k].setText(mov.getString(0));
                        righe[i].addView(celle[k]);
                        k++;
                        celle[k] = new TextView(tm.getContext());
                        celle[k].setTextColor(color);
                        celle[k].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        celle[k].setText(format.format(mov.getFloat(1)));
                        righe[i].addView(celle[k]);
                        k++;
                        celle[k] = new TextView(tm.getContext());
                        celle[k].setTextColor(color);
                        celle[k].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        celle[k].setText(mov.getInt(2)+"");
                        righe[i].addView(celle[k]);
                        k++;
                        celle[k] = new TextView(tm.getContext());
                        celle[k].setTextColor(color);
                        celle[k].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 3f));
                        celle[k].setText(mov.getString(3));
                        righe[i].addView(celle[k]);
                        k++;
                        celle[k] = new TextView(tm.getContext());
                        celle[k].setTextColor(color);
                        celle[k].setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2f));
                        celle[k].setText(mov.getString(4));
                        righe[i].addView(celle[k]);
                        k++;
                        tm.addView(righe[i]);
                        mov.moveToNext();
                    }
                }
            }
        });

        versamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("Nuovo versamento");
                builder.setMessage("Inserire l'importo versato in cassa");

                LayoutInflater layoutInflater = getLayoutInflater();
                View v=layoutInflater.inflate(R.layout.versamento_dialog,null);
                final EditText input = (EditText)v.findViewById(R.id.versamento_txt);
                input.setTextSize(32);
                int type;
                type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
                input.setInputType(type);
                input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                spinner = (Spinner)v.findViewById(R.id.versamento_spinner);
                addItemsOnProductSpinner();
                spinner.setOnItemSelectedListener(new SpinnerHandlerCassa(com.example.lucacorsini.a_bar.CassaActivity.this));
                builder.setPositiveButton("Versa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(spinner.getSelectedItemPosition()!=0 && input.getText().toString()!= ""){
                            Cursor c = db.query("select id from utenti where f_annu = '0' and posizione = "+spinner.getSelectedItemPosition());
                            c.moveToFirst();
                            int userid = c.getInt(0);
                            float versato = Float.parseFloat(input.getText().toString());
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
                            String adesso = df2.format(cal.getTime());
                            db.queryi("insert into versamenti (utente,cifra,ticket,valoreticket,data,f_annu) values ("+userid+","+versato+",0,0,'"+adesso+"','0')");
                            db.queryi("insert into movimenticassa (tipo,cifra,ticket,valoreticket,descrizione,data,f_annu) values (4,"+versato+",0,0,'idutente "+userid+"','"+adesso+"','0')");
                            db.queryi("update utenti set saldo = saldo + "+versato+" where id = "+userid);
                            updateSaldoCassa();
                        }
                        else{
                            AlertDialog alertDialog = new AlertDialog.Builder(CassaActivity.this).create();
                            alertDialog.setMessage("Devi selezionare un utente ed inserire un importo");
                            alertDialog.show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                builder.setView(v);
                builder.show();
            }
        });

        adminscontrini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(adminscontrini.getContext(),AdminScontrinoActivity.class);
                startActivity(i);
            }
        });

    }

    public void addItemsOnProductSpinner() {
        db = new DbUtils(this);
        //spinner = (Spinner) findViewById(idspi);
        Cursor utenti = db.query("select id,nome,cognome,grado from utenti where f_annu = '0' and grado <> '' order by posizione asc");
        List<String> list = new ArrayList<String>();
        list.add("");
        if(utenti!=null){
            utenti.moveToFirst();
            list.add(utenti.getString(3).toUpperCase()+" "+utenti.getString(1).toUpperCase()+" "+utenti.getString(2).toUpperCase());
            while(utenti.moveToNext()){
                list.add(utenti.getString(3).toUpperCase()+" "+utenti.getString(1).toUpperCase()+" "+utenti.getString(2).toUpperCase());
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.my_spinner,R.id.textViewspin, list);
        dataAdapter.setDropDownViewResource(R.layout.my_spinner);
        spinner.setAdapter(dataAdapter);
        //R.layout.my_spinner
    }

    public void updateSaldoCassa(){
        Cursor c = db.query("select sum(cifra), sum(ticket) from movimenticassa where f_annu = '0'");
        c.moveToFirst();
        float saldo = c.getFloat(0);
        int saldoticket = c.getInt(1);
        db.queryi("update utenti set saldo = "+saldo+", saldoticket = "+saldoticket+" where cognome = 'cassa'");
        saldocassaeuro.setText(format.format(saldo));
        if(saldo<=0){
            saldocassaeuro.setTextColor(Color.RED);
        }
        saldocassaticket.setText(saldoticket+" ticket");
        if(saldoticket<=0){
            saldocassaticket.setTextColor(Color.RED);
        }
    }

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
