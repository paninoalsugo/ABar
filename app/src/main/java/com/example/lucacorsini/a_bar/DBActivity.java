package com.example.lucacorsini.a_bar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.database.Cursor;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lucacorsini.a_bar.db.DbUtils;

public class DBActivity extends AppCompatActivity {

    TextInputLayout til;
    TextInputEditText tiet;
    EditText rislab;

    private DbUtils db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DbUtils(this);

        Button send = (Button) findViewById(R.id.sendquerybutton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiet = (TextInputEditText) findViewById(R.id.querytext);
                String q = tiet.getText().toString();
                String comando = q.substring(0,q.indexOf(' '));
                //System.out.println("Comando: "+comando+" Query: "+q);
                rislab = (EditText) findViewById(R.id.risultati);
                if(comando.equalsIgnoreCase("select")){
                    Cursor res = db.query(q);
                    if(res!=null){
                        res.moveToFirst();
                        int numcol = res.getColumnCount();
                        int numrighe = res.getCount();
                        String ristxt = "";
                        for(int nc=0;nc<numcol;nc++){
                            ristxt = ristxt+" "+res.getColumnName(nc).toUpperCase();
                        }
                        String finalristxt = ristxt+"\n";
                        for(int i = 0;i<numrighe;i++){
                            //System.out.println("Riga "+i);
                                for(int j=0;j<numcol;j++){
                                    //System.out.println("Colonna "+j);
                                    //System.out.println(ristxt);
                                    if(j==0){
                                        int t = res.getType(j);
                                        switch (t) {
                                            case Cursor.FIELD_TYPE_INTEGER:
                                                ristxt = res.getInt(j)+",";
                                                break;
                                            case Cursor.FIELD_TYPE_STRING:
                                                ristxt = "'"+res.getString(j)+"',";
                                                break;
                                            case Cursor.FIELD_TYPE_FLOAT:
                                                ristxt = res.getFloat(j)+",";
                                                break;
                                            case Cursor.FIELD_TYPE_NULL:
                                                ristxt = "null,";
                                                break;
                                        }
                                    }
                                    else{
                                        if(j==numcol-1){
                                            int t = res.getType(j);
                                            switch (t) {
                                                case Cursor.FIELD_TYPE_INTEGER:
                                                    ristxt = ristxt+res.getInt(j);
                                                    break;
                                                case Cursor.FIELD_TYPE_STRING:
                                                    ristxt = ristxt+"'"+res.getString(j)+"'";
                                                    break;
                                                case Cursor.FIELD_TYPE_FLOAT:
                                                    ristxt = ristxt+res.getFloat(j);
                                                    break;
                                                case Cursor.FIELD_TYPE_NULL:
                                                    ristxt = ristxt+"null";
                                                    break;
                                            }
                                        }
                                        else{
                                            int t = res.getType(j);
                                            switch (t) {
                                                case Cursor.FIELD_TYPE_INTEGER:
                                                    ristxt = ristxt+res.getInt(j)+",";
                                                    break;
                                                case Cursor.FIELD_TYPE_STRING:
                                                    ristxt = ristxt+"'"+res.getString(j)+"',";
                                                    break;
                                                case Cursor.FIELD_TYPE_FLOAT:
                                                    ristxt = ristxt+res.getFloat(j)+",";
                                                    break;
                                                case Cursor.FIELD_TYPE_NULL:
                                                    ristxt = ristxt+"null,";
                                                    break;
                                            }
                                        }
                                    }
                                }
                            res.moveToNext();
                            finalristxt = finalristxt+"\n"+ristxt;
                        }
                        rislab.setText(finalristxt);
                    }

                }
                else{
                    db.queryi(q);
                    rislab.setText("Query eseguita");
                }
            }
        });
    }

}
