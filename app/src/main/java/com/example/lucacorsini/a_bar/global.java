package com.example.lucacorsini.a_bar;

import android.graphics.Bitmap;

public class global {

    private static int UserId;
    private static String CognomeUtente;
    public static Bitmap scontrino;
    public static int scontrinoUserId;
    public static String nomeimmagine;
    public static String percorsoscontrino;

    public static int getUserId(){
        return UserId;
    }

    public static void setUserId(int u){
        UserId = u;
    }

    public static String getCognomeUtente(){
        return CognomeUtente;
    }

    public static void setCognomeUtente(String c){
        CognomeUtente = c;
    }

    public static Bitmap getScontrino(){ return scontrino; }

    public static void setScontrino(Bitmap b){ scontrino = b; }

    public static int getscontrinoUserId(){
        return scontrinoUserId;
    }

    public static void setScontrinoUserId(int u){
        scontrinoUserId = u;
    }

    public static String getNomeimmagine() { return nomeimmagine; }

    public static void setNomeimmagine(String n) { nomeimmagine = n; }

    public static String getPercorsoScontrino() { return percorsoscontrino; }

    public static void setPercorsoScontrino(String p) { percorsoscontrino = p; }

}
