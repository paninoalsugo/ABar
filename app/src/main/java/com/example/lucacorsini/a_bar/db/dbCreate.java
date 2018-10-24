package com.example.lucacorsini.a_bar.db;

public class dbCreate {

    public static final String dbname = "bardesio";

    public static final String acquisti = "CREATE TABLE \"acquisti\" (" +
            "  \"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  \"utente\" integer NOT NULL," +
            "  \"prodotto\" integer NOT NULL,\n" +
            "\"qta\" integer," +
            "  \"prezzo\" real NOT NULL," +
            "  \"data\" text(20) NOT NULL," +
            "\"f_annu\" text(1)," +
            "CONSTRAINT \"acquisti_utenti_fk\" FOREIGN KEY (\"utente\") REFERENCES \"utenti\" (\"id\")," +
            "CONSTRAINT \"prodotti_utenti_fk\" FOREIGN KEY (\"prodotto\") REFERENCES \"prodotti\" (\"id\")" +
            ")";

    public static final String movimenticassa = "CREATE TABLE \"movimenticassa\" (" +
            "  \"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  \"tipo\" integer NOT NULL," +
            "  \"cifra\" real," +
            "  \"ticket\" integer," +
            "  \"valoreticket\" real," +
            "  \"descrizione\" text(200)," +
            "  \"data\" text(20)," +
            "  \"f_annu\" text(1)," +
            "CONSTRAINT \"movimenticassa_tipologie_fk\" FOREIGN KEY (\"tipo\") REFERENCES \"tipologie\" (\"id\")" +
            ")";

    public static final String prodotti = "CREATE TABLE \"prodotti\" (" +
            "  \"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  \"nome\" text(30) NOT NULL," +
            "  \"descrizione\" text(100)," +
            "\"tipo\" integer," +
            "  \"prezzo\" real NOT NULL," +
            "\"qta\" integer," +
            "  \"icon\" text(50)," +
            "  \"f_annu\" text(1)" +
            ")";

    public static final String rifornimenti = "CREATE TABLE \"rifornimenti\" (" +
            "  \"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  \"utente\" integer NOT NULL," +
            "\"prodotto\" integer," +
            "\"qta\" integer," +
            "  \"prezzo\" real," +
            "  \"data\" text(20)," +
            "  \"data_ins\" text(20)," +
            "  \"descrizione\" text(200)," +
            "  \"scontrino\" text(50)," +
            "\"confermato\" integer," +
            "\"f_annu\" text(1)," +
            "  CONSTRAINT \"rifornimenti_utenti_fk\" FOREIGN KEY (\"utente\") REFERENCES \"utenti\" (\"id\")" +
            "CONSTRAINT \"rifornimenti_prodotti_fk\" FOREIGN KEY (\"prodotto\") REFERENCES \"prodotti\" (\"id\")" +
            ")";

    public static final String tipologie = "CREATE TABLE \"tipologie\" (" +
            "  \"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  \"tipo\" text(50) NOT NULL," +
            "  \"descrizione\" text(200)," +
            "  \"tabella\" text(20)," +
            "  \"f_annu\" text(1)" +
            ")";

    public static final String utenti = "CREATE TABLE \"utenti\" (" +
            "  \"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  \"nome\" text(30)," +
            "  \"cognome\" text(30)," +
            "  \"grado\" text(3)," +
            "  \"salto\" text(2)," +
            "  \"saldo\" real," +
            "  \"saldoticket\" integer," +
            "  \"f_annu\" text(1)," +
            "  \"posizione\" integer " +
            ")";

    public static final String versamenti = "CREATE TABLE \"versamenti\" (" +
            "  \"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  \"utente\" integer NOT NULL," +
            "  \"cifra\" real," +
            "  \"ticket\" integer," +
            "  \"valoreticket\" real," +
            "  \"data\" text(20)," +
            "\"f_annu\" text(1)," +
            "CONSTRAINT \"versamenti_utenti_fk\" FOREIGN KEY (\"utente\") REFERENCES \"utenti\" (\"id\")" +
            ")";

    public static final String utenze = "CREATE TABLE \"utenze\" (" +
            "  \"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "  \"username\" text(20)," +
            "  \"password\" text(20))";

    public static final String i1 = "CREATE INDEX idx_utenti_cognome ON utenti (cognome)";
    public static final String i2 = "CREATE INDEX idx_prodotti_nome ON prodotti (nome)";
    public static final String i3 = "CREATE INDEX idx_tipologie_tabella ON tipologie (tabella)";
    public static final String i4 = "CREATE INDEX idx_acquisti_utente ON acquisti (utente)";
    public static final String i5 = "CREATE INDEX idx_acquisti_prodotto ON acquisti (prodotto)";
    public static final String i6 = "CREATE INDEX idx_acquisti_data ON acquisti (data)";
    public static final String i7 = "CREATE INDEX idx_acquisti_utente_prodotto ON acquisti (utente,prodotto)";
    public static final String i8 = "CREATE INDEX idx_rifornimenti_utente ON rifornimenti (utente)";
    public static final String i9 = "CREATE INDEX idx_rifornimenti_prodotto ON rifornimenti (prodotto)";
    public static final String i10 = "CREATE INDEX idx_rifornimenti_utente_prodotto ON rifornimenti (utente,prodotto)";
    public static final String i11 = "CREATE INDEX idx_rifornimenti_data ON rifornimenti (data)";
    public static final String i12 = "CREATE INDEX idx_versamenti_utente ON versamenti (utente)";
    public static final String i13 = "CREATE INDEX idx_versamenti_data ON versamenti (data)";
    public static final String i14 = "CREATE INDEX idx_movimenticassa_tipo ON movimenticassa (tipo)";
    public static final String i15 = "CREATE INDEX idx_movimenticassa_data ON movimenticassa (data)";

    /*public static final String ut1 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('gianfranco','ornago','cse','A7',0,0,'0',1)";
    public static final String ut2 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('antonino','tilotta','cs','A4',0,0,'0',2)";
    public static final String ut3 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('alberto','guerini','vc','A1',0,0,'0',3)";
    public static final String ut4 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('giuseppe','campisi','vc','A4',0,0,'0',4)";
    public static final String ut5 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('vincenzo','brunno','vc','A1',0,0,'0',5)";
    public static final String ut6 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('francesco','stasi','ve','A4',0,0,'0',6)";
    public static final String ut7 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('giovanni','pisano','vq','A4',0,0,'0',7)";
    public static final String ut8 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('francesco','gaffuri','vq','A2',0,0,'0',8)";
    public static final String ut9 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('fabio','sala','vf','A4',0,0,'0',9)";
    public static final String ut10 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('luca','corsini','vf','A3',0,0,'0',10)";
    public static final String ut11 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('pietro','mancino','vc','A4',0,0,'0',11)";
    public static final String ut12 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('paolo','brugora','cr',null,0,0,'0',12)";
    public static final String ut13 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('','rinforzi','','null',0,0,'0',13)";
    public static final String ut14 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('','ospiti','','null',0,0,'0',14)";
    public static final String ut15 = "insert into utenti (nome,cognome,grado,saldo,f_annu) values ('','cassa','','null',0,0,'0',9999)";

    public static final String t1 = "insert into tipologie (tipo,descrizione,tabella,f_annu) values ('prodotto','prodotto acquistabile','prodotti','0')";
    public static final String t2 = "insert into tipologie (tipo,descrizione,tabella,f_annu) values ('tassa','tassa da applicare','prodotti','0')";
    public static final String t3 = "insert into tipologie (tipo,descrizione,tabella,f_annu) values ('acquisto','acquisto da utente','movimenticassa','0')";
    public static final String t4 = "insert into tipologie (tipo,descrizione,tabella,f_annu) values ('versamento','versamento da utente','movimenticassa','0')";
    public static final String t5 = "insert into tipologie (tipo,descrizione,tabella,f_annu) values ('prelievo','prelievo dalla cassa','movimenticassa','0')";
    public static final String t6 = "insert into tipologie (tipo,descrizione,tabella,f_annu) values ('inserimento','versamento nella cassa','movimenticassa','0')";
    public static final String t7 = "insert into tipologie (tipo,descrizione,tabella,f_annu) values ('nuovo','nuovo prodotto','prodotti','0')";

    public static final String pr1 = "insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('caffè','caffè dalla nostra macchinetta','1',0.30,400,'caffe','0')";
    public static final String pr2 = "insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('acqua 1,5lt','bottiglia grande di acqua','1',0.25,20,'acqua_15','0')";
    public static final String pr3 = "insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('acqua 1lt','bottiglia piccola di acqua','1',0.20,20,'acqua_1','0'";
    public static final String pr4 = "insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('bibita','bibita in lattina','1',0.35,10,'bibite','0')";
    public static final String pr5 = "insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('birra','birra in lattina o bottiglia','1',0.70,10,'birra','0')";
    public static final String pr6 = "insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('gelato','gelato','1',0.50,10,'gelato','0')";
    public static final String pr7 = "insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('ghiacciolo','ghiacciolo','1',0.30,10,'ghiacciolo','0')";
    public static final String pr8 = "insert into prodotti (nome,descrizione,tipo,prezzo,qta,icon,f_annu) values ('nuovo','nuovo prodotto','7',0,0,'','0')";

    /*public static final String ute1 = "insert into utenze (username,password) values ('bardesio','rosi')";
    public static final String ute2 = "insert into utenze (username,password) values ('cassa','d3s10')";*/

}
