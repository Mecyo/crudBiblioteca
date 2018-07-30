package br.com.mecyo.biblioteca.models;

import java.io.Serializable;

/**
 * Created by Emerson Santos on 30/07/18.
 */

public class Aluno implements Serializable {

    private int id;
    private String nome;
    private String sexo;
    private String uf;
    private String matricula;
    private boolean vip;

    public Aluno(int id, String nome, String sexo, String matricula, String uf, boolean vip){
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.matricula = matricula;
        this.uf = uf;
        this.vip = vip;
    }

    public int getId(){ return this.id; }
    public String getNome(){ return this.nome; }
    public String getSexo(){ return this.sexo; }
    public String getMatricula(){ return this.matricula; }
    public boolean getVip(){ return this.vip; }
    public String getUf(){ return this.uf; }

    @Override
    public boolean equals(Object o){
        return this.id == ((Aluno)o).id;
    }

    @Override
    public int hashCode(){
        return this.id;
    }
}
