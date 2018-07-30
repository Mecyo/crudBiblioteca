package br.com.mecyo.biblioteca.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Emerson Santos on 30/07/18.
 */

public class Livro implements Serializable {

    private int id;
    private String titulo;
    private String editora;
    private String escritor;
    private String local;
    private Date publicacao;
    private boolean disponivel;

    public Livro(int id, String titulo, String editora, String escritor, Date publicacao, String local, boolean vip){
        this.id = id;
        this.titulo = titulo;
        this.editora = editora;
        this.escritor = escritor;
        this.local = local;
        this.publicacao = publicacao;
        this.disponivel = disponivel;
    }

    public int getId(){ return this.id; }
    public String getTitulo(){ return this.titulo; }
    public String getEditora(){ return this.editora; }
    public String getEscritor(){ return this.escritor; }
    public Date getPublicacao(){ return this.publicacao; }
    public String getLocal(){ return this.local; }
    public boolean getDisponivel(){ return this.disponivel; }


    @Override
    public boolean equals(Object o){
        return this.id == ((Livro)o).id;
    }

    @Override
    public int hashCode(){
        return this.id;
    }
}
