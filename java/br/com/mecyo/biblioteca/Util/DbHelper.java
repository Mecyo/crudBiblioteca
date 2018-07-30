package br.com.mecyo.biblioteca.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Emerson Santos on 30/07/18.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Biblioteca.db";
    private static final int DATABASE_VERSION = 1;
    private final String CREATE_TABLE_ALUNOS = "CREATE TABLE Alunos (ID INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT NOT NULL, Sexo TEXT, UF TEXT NOT NULL, Vip INTEGER NOT NULL);";
    private final String CREATE_TABLE_LIVROS = "CREATE TABLE Livros (ID INTEGER PRIMARY KEY AUTOINCREMENT, Titulo TEXT NOT NULL, Editora TEXT, Escritor TEXT NOT NULL, Publicacao DATE NOT NULL, Local TEXT NOT NULL, Disponivel INTEGER NOT NULL);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ALUNOS);
        db.execSQL(CREATE_TABLE_LIVROS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
