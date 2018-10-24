package com.example.lucacorsini.a_bar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Surface;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.Manifest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.widget.Toast;

import com.example.lucacorsini.a_bar.db.DbUtils;

public class ScontrinoActivity extends AppCompatActivity {

    Spinner sprCoun;
    private DbUtils db = null;

    Intent intent ;
    public  static final int RequestPermissionCode  = 1 ;
    Bitmap bitmap;
    int products[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scontrino);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addItemsOnProductSpinner();
        sprCoun.setOnItemSelectedListener(new SpinnerHandler(this));
        //EnableRuntimePermission();
        ImageView imageView = (ImageView) findViewById(R.id.imageViewScontrino);
        //System.out.println("user: "+global.getUserId()+" userscontrino: "+global.getscontrinoUserId());
        if(global.getScontrino()!=null && global.getUserId()==global.getscontrinoUserId()){
            imageView.setImageBitmap(global.getScontrino());
        }
        createActionForButton();
    }

    // add items into spinner dynamically
    public void addItemsOnProductSpinner() {
        db = new DbUtils(this);
        sprCoun = (Spinner) findViewById(R.id.productspinner);
        Cursor prod = db.query("select id,nome,prezzo from prodotti where tipo = 1 and f_annu = '0' order by id asc");
        List<String> list = new ArrayList<String>();
        list.add("");                                       // posizione 1
        list.add("NUOVO PRODOTTO");                         // posizione 2
        int prodotti[] = new int[prod.getCount()];
        int i=0;
        if(prod!=null){
            prod.moveToFirst();
            list.add(prod.getString(1));        // posizione 3
            prodotti[i]=prod.getInt(0);
            i++;
            while(prod.moveToNext()){
                list.add(prod.getString(1));    // posizione 4 ->
                prodotti[i]=prod.getInt(0);
                i++;
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.my_spinner,R.id.textViewspin, list);
        dataAdapter.setDropDownViewResource(R.layout.my_spinner);
        sprCoun.setAdapter(dataAdapter);
        products = prodotti;
        //R.layout.my_spinner
    }

    public void createActionForButton(){

        // bottone per acquisizione immagine
        final Button scb = (Button) findViewById(R.id.scontrinobutton1);
        scb.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHmmss");
        String now = df.format(c.getTime());
        Cursor user = db.query("select cognome from utenti where id = "+global.getUserId());
        if(user!=null){
            user.moveToFirst();
            now = user.getString(0)+"_"+now+".jpeg";
        }
        global.setNomeimmagine(now);
        //intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory()+File.separator + global.getNomeimmagine());
        //File file = new File(System.getenv("SECONDARY_STORAGE")+"/tmp/"+global.getNomeimmagine());
        //System.out.println(MediaStore.EXTRA_OUTPUT);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, 7);
            }
        });

        // bottone di invio scontrino e dati
        final Button send = (Button) findViewById(R.id.inviascontrino);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean errore = false;
                final int idutente = global.getUserId();
                int idprodotto = 0;
                final String percorsoscontrino;
                int posizioneprodotto = sprCoun.getSelectedItemPosition();
                Cursor cu;
                if(posizioneprodotto==1){ // NUOVO PRODOTTO
                    cu=db.query("select id from prodotti where tipo = 7");
                    cu.moveToFirst();
                    idprodotto = cu.getInt(0);
                }
                else{
                    if(posizioneprodotto > 1){
                        idprodotto = products[posizioneprodotto-2];
                    }
                    else{
                        errore = true;
                    }
                }
                if(idprodotto<1){
                    errore = true;
                }
                final EditText qtatxt = (EditText) findViewById(R.id.scontrinoqta);
                int qta = 0;
                if(!qtatxt.getText().toString().equalsIgnoreCase("")){
                    qta = Integer.parseInt(qtatxt.getText().toString());
                }
                else{
                    errore = true;
                }
                final EditText prezzotxt = (EditText) findViewById(R.id.scontrinocifra);
                float prezzo = 0;
                if(!prezzotxt.getText().toString().equalsIgnoreCase("")){
                    prezzo = Float.parseFloat(prezzotxt.getText().toString());
                }
                else{
                    errore = true;
                }
                final Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
                String data = df.format(c.getTime());
                String datains = data;
                final EditText descrizionetxt = (EditText) findViewById(R.id.descrizionescontrino);
                String descrizione = descrizionetxt.getText().toString();
                if(posizioneprodotto==1 && descrizione.equalsIgnoreCase("")){
                    errore = true;
                }
                if(errore){
                    AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Errore");
                    builder.setMessage("Occorre valorizzare TUTTI i campi");
                    builder.setNegativeButton("OK, ho capito, non lo faccio più", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });
                    builder.show();
                }
                else{
                    final ImageView im = (ImageView) findViewById(R.id.imageViewScontrino);
                    moveFile(Environment.getExternalStorageDirectory()+File.separator,global.getNomeimmagine(),System.getenv("SECONDARY_STORAGE")+"/scontrini/");
                    percorsoscontrino = System.getenv("SECONDARY_STORAGE")+"/scontrini/"+global.getNomeimmagine();
                    String insert = "insert into rifornimenti (utente,prodotto,qta,prezzo,data,data_ins,descrizione,scontrino,f_annu,confermato) "+
                            "values ("+idutente+","+idprodotto+","+qta+","+prezzo+",'"+data+"','"+datains+"','"+descrizione+"','"+percorsoscontrino+"','0',0)";
                    try{
                        db.queryi(insert);
                        System.out.println(insert);
                        final AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Successo");
                        builder.setMessage("Lo scontrino è stato memorizzato con successo, occorre attendere la conferma da parte di un amministratore");
                        builder.setPositiveButton("OK grazie mille", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent in = new Intent(builder.getContext(),DettaglioActivity.class);
                                in.putExtra("utente",idutente);
                                startActivity(in);
                            }
                        });
                        builder.show();
                    }
                    catch(SQLException e){
                        final AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Errore");
                        builder.setMessage(e.getMessage());
                        builder.setPositiveButton("OK, non c'ho capito niente ma mi fido", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                moveFile(System.getenv("SECONDARY_STORAGE")+"/scontrini/"+File.separator,global.getNomeimmagine(),System.getenv("SECONDARY_STORAGE")+"/scontrini/errori/");
                                Intent in = new Intent(builder.getContext(),DettaglioActivity.class);
                                in.putExtra("utente",idutente);
                                startActivity(in);
                            }
                        });
                        builder.show();
                    }
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 7 && resultCode == RESULT_OK) {
            /*Bitmap immagine = (Bitmap) data.getExtras().get("data");
            //System.out.println(immagine.getWidth()+"X"+immagine.getHeight());
            global.setScontrino(immagine);*/
            ImageView imageView = (ImageView) findViewById(R.id.imageViewScontrino);/*
            imageView.setImageBitmap(global.getScontrino());
            global.setScontrinoUserId(global.getUserId());
            ImageSaver is = new ImageSaver(this);
            is.setFileName(global.getNomeimmagine());
            is.setDirectoryName(global.getPercorsoScontrino());
            is.setExternal(true);
            is.save(global.getScontrino());*/
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + global.getNomeimmagine());
            //get bitmap from path with size of
            Bitmap immagine = decodeSampledBitmapFromFile(file.getAbsolutePath(), 150, 200);
            global.setScontrino(immagine);
            imageView.setImageBitmap(immagine);
            global.setScontrinoUserId(global.getUserId());
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //Query bitmap without allocating memory
        options.inJustDecodeBounds = true;
        //decode file from path
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        //decode according to configuration or according best match
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;
        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;
        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }
        //if value is greater than 1,sub sample the original image
        options.inSampleSize = inSampleSize;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap res = BitmapFactory.decodeFile(path, options);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        return Bitmap.createBitmap(res, 0, 0, res.getWidth(),res.getHeight(), matrix, false);
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(ScontrinoActivity.this,
                Manifest.permission.CAMERA))
        {

            Toast.makeText(ScontrinoActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(ScontrinoActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(ScontrinoActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(ScontrinoActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();


        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
}
