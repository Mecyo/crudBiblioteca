package br.com.mecyo.biblioteca.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.mecyo.biblioteca.models.Aluno;
import br.com.mecyo.biblioteca.Util.DbGateway;

/**
 * Created by Emerson Santos on 30/07/18.
 */

public class AlunoDAO {

    private final String TABLE_ALUNOS = "Alunos";
    private DbGateway gw;

    public AlunoDAO(Context ctx){
        gw = DbGateway.getInstance(ctx);
    }

    public List<Aluno> retornarTodos(){
        List<Aluno> alunos = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Alunos", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String sexo = cursor.getString(cursor.getColumnIndex("Sexo"));
            String uf = cursor.getString(cursor.getColumnIndex("UF"));
            String matricula = cursor.getString(cursor.getColumnIndex("Matricula"));
            boolean vip = cursor.getInt(cursor.getColumnIndex("Vip")) > 0;
            alunos.add(new Aluno(id, nome, sexo, matricula, uf, vip));
        }
        cursor.close();
        return alunos;
    }

    public Aluno retornarUltimo(){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Alunos ORDER BY ID DESC", null);
        if(cursor.moveToFirst()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String sexo = cursor.getString(cursor.getColumnIndex("Sexo"));
            String matricula = cursor.getString(cursor.getColumnIndex("Matricula"));
            String uf = cursor.getString(cursor.getColumnIndex("UF"));
            boolean vip = cursor.getInt(cursor.getColumnIndex("Vip")) > 0;
            cursor.close();
            return new Aluno(id, nome, sexo, matricula, uf, vip);
        }

        return null;
    }

    public boolean salvar(String nome, String sexo, String matricula, String uf, boolean vip){
        return salvar(0, nome, sexo, matricula, uf, vip);
    }

    public boolean salvar(int id, String nome, String sexo, String matricula, String uf, boolean vip){
        ContentValues cv = new ContentValues();
        cv.put("Nome", nome);
        cv.put("Sexo", sexo);
        cv.put("Matricula", matricula);
        cv.put("UF", uf);
        cv.put("Vip", vip ? 1 : 0);
        if(id > 0)
            return gw.getDatabase().update(TABLE_ALUNOS, cv, "ID=?", new String[]{ id + "" }) > 0;
        else
            return gw.getDatabase().insert(TABLE_ALUNOS, null, cv) > 0;
    }

    public boolean excluir(int id){
        return gw.getDatabase().delete(TABLE_ALUNOS, "ID=?", new String[]{ id + "" }) > 0;
    }
}
