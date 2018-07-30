package br.com.mecyo.biblioteca.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.mecyo.biblioteca.Util.DbGateway;
import br.com.mecyo.biblioteca.models.Livro;

/**
 * Created by Emerson Santos on 30/07/18.
 */

public class LivroDAO {

    private final String TABLE_LIVROS = "Livros";
    private DbGateway gw;

    public LivroDAO(Context ctx){
        gw = DbGateway.getInstance(ctx);
    }

    public List<Livro> retornarTodos(){
        List<Livro> Livros = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Alunos", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String titulo = cursor.getString(cursor.getColumnIndex("Titulo"));
            String editora = cursor.getString(cursor.getColumnIndex("Editora"));
            String escritor = cursor.getString(cursor.getColumnIndex("Escritor"));
            String local = cursor.getString(cursor.getColumnIndex("Local"));
            String publicacao = cursor.getString(cursor.getColumnIndexOrThrow("Publicacao"));
            boolean disponivel = cursor.getInt(cursor.getColumnIndex("Disponivel")) > 0;
            Date dataPublicacao = null;
            try {
                dataPublicacao = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(publicacao);
            } catch (ParseException e) {
                Log.e("Data de Publicação", "Parsing ISO8601 datetime failed", e);
            }
            Livros.add(new Livro(id, titulo, editora, escritor, dataPublicacao, local, disponivel));
        }
        cursor.close();
        return Livros;
    }

    public Livro retornarUltimo(){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Alunos ORDER BY ID DESC", null);
        if(cursor.moveToFirst()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String titulo = cursor.getString(cursor.getColumnIndex("Titulo"));
            String editora = cursor.getString(cursor.getColumnIndex("Editora"));
            String escritor = cursor.getString(cursor.getColumnIndex("Escritor"));
            String local = cursor.getString(cursor.getColumnIndex("Local"));
            String publicacao = cursor.getString(cursor.getColumnIndexOrThrow("Publicacao"));
            boolean disponivel = cursor.getInt(cursor.getColumnIndex("Disponivel")) > 0;
            Date dataPublicacao = null;
            try {
                dataPublicacao = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(publicacao);
            } catch (ParseException e) {
                Log.e("Data de Publicação", "Parsing ISO8601 datetime failed", e);
            }
            cursor.close();
            return new Livro(id, titulo, editora, escritor, dataPublicacao, local, disponivel);
        }

        return null;
    }

    public boolean salvar(String titulo, String editora, String escritor, Date dataPublicacao, String local, boolean disponivel){
        return salvar(0, titulo, editora, escritor, dataPublicacao, local, disponivel);
    }

    public boolean salvar(int id, String titulo, String editora, String escritor, Date dataPublicacao, String local, boolean disponivel){
        ContentValues cv = new ContentValues();
        cv.put("Titulo", titulo);
        cv.put("Editora", editora);
        cv.put("Escritor", escritor);
        cv.put("Local", local);
        cv.put("Publicacao", dataPublicacao.toString());
        cv.put("Disponivel", disponivel ? 1 : 0);
        if(id > 0)
            return gw.getDatabase().update(TABLE_LIVROS, cv, "ID=?", new String[]{ id + "" }) > 0;
        else
            return gw.getDatabase().insert(TABLE_LIVROS, null, cv) > 0;
    }

    public boolean excluir(int id){
        return gw.getDatabase().delete(TABLE_LIVROS, "ID=?", new String[]{ id + "" }) > 0;
    }
}
