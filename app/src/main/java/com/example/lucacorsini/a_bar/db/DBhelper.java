package com.example.lucacorsini.a_bar.db;

import android.database.sqlite.*;
import android.content.Context;

public class DBhelper extends SQLiteOpenHelper{

    public DBhelper(Context context) {
        super(context.getApplicationContext(), dbCreate.dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String q = "";

        // ** Creazione tabelle ** //
        q=dbCreate.utenti;
        db.execSQL(q);
        q=dbCreate.tipologie;
        db.execSQL(q);
        q=dbCreate.prodotti;
        db.execSQL(q);
        q=dbCreate.acquisti;
        db.execSQL(q);
        q=dbCreate.versamenti;
        db.execSQL(q);
        q=dbCreate.rifornimenti;
        db.execSQL(q);
        q=dbCreate.movimenticassa;
        db.execSQL(q);
        q=dbCreate.utenze;
        db.execSQL(q);

        // ** Creazione indici tabelle ** //
        q=dbCreate.i1;
        db.execSQL(q);
        q=dbCreate.i2;
        db.execSQL(q);
        q=dbCreate.i3;
        db.execSQL(q);
        q=dbCreate.i4;
        db.execSQL(q);
        q=dbCreate.i5;
        db.execSQL(q);
        q=dbCreate.i6;
        db.execSQL(q);
        q=dbCreate.i7;
        db.execSQL(q);
        q=dbCreate.i8;
        db.execSQL(q);
        q=dbCreate.i9;
        db.execSQL(q);
        q=dbCreate.i10;
        db.execSQL(q);
        q=dbCreate.i11;
        db.execSQL(q);
        q=dbCreate.i12;
        db.execSQL(q);
        q=dbCreate.i13;
        db.execSQL(q);
        q=dbCreate.i14;
        db.execSQL(q);
        q=dbCreate.i15;
        db.execSQL(q);

        /*
        // ** Inserimento dati ** //
        // Utenti //
        q=dbCreate.ut1;
        db.execSQL(q);
        q=dbCreate.ut2;
        db.execSQL(q);
        q=dbCreate.ut3;
        db.execSQL(q);
        q=dbCreate.ut4;
        db.execSQL(q);
        q=dbCreate.ut5;
        db.execSQL(q);
        q=dbCreate.ut6;
        db.execSQL(q);
        q=dbCreate.ut7;
        db.execSQL(q);
        q=dbCreate.ut8;
        db.execSQL(q);
        q=dbCreate.ut9;
        db.execSQL(q);
        q=dbCreate.ut10;
        db.execSQL(q);
        q=dbCreate.ut11;
        db.execSQL(q);
        q=dbCreate.ut12;
        db.execSQL(q);
        q=dbCreate.ut13;
        db.execSQL(q);
        q=dbCreate.ut14;
        db.execSQL(q);
        q=dbCreate.ut15;
        db.execSQL(q);
        // Tipologie //
        q=dbCreate.t1;
        db.execSQL(q);
        q=dbCreate.t2;
        db.execSQL(q);
        q=dbCreate.t3;
        db.execSQL(q);
        q=dbCreate.t4;
        db.execSQL(q);
        q=dbCreate.t5;
        db.execSQL(q);
        q=dbCreate.t6;
        db.execSQL(q);
        q=dbCreate.t7;
        db.execSQL(q);
        // Prodotti //
        q=dbCreate.pr1;
        db.execSQL(q);
        q=dbCreate.pr2;
        db.execSQL(q);
        q=dbCreate.pr3;
        db.execSQL(q);
        q=dbCreate.pr4;
        db.execSQL(q);
        q=dbCreate.pr5;
        db.execSQL(q);
        q=dbCreate.pr6;
        db.execSQL(q);
        q=dbCreate.pr7;
        db.execSQL(q);
        q=dbCreate.pr8;
        db.execSQL(q);
        // Utenze //
        q=dbCreate.ute1;
        db.execSQL(q);
        q=dbCreate.ute2;
        db.execSQL(q);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {  }
}
