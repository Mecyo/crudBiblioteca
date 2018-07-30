package br.com.mecyo.biblioteca.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import br.com.mecyo.biblioteca.R;


/**
 * Created by Emerson Santos on 30/07/18.
 */

public class LivroHolder extends RecyclerView.ViewHolder {

    public TextView titulo;
    public ImageButton btnEditar;
    public ImageButton btnExcluir;

    public LivroHolder(View itemView) {
        super(itemView);
        titulo = (TextView) itemView.findViewById(R.id.titulo);
        btnEditar = (ImageButton) itemView.findViewById(R.id.btnEdit);
        btnExcluir = (ImageButton) itemView.findViewById(R.id.btnDelete);
    }
}