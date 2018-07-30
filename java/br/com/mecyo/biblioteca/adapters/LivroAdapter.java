package br.com.mecyo.biblioteca.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import br.com.mecyo.biblioteca.DAO.LivroDAO;
import br.com.mecyo.biblioteca.holders.LivroHolder;
import br.com.mecyo.biblioteca.R;
import br.com.mecyo.biblioteca.models.Livro;

/**
 * Created by Emerson Santos on 30/07/18.
 */

public class LivroAdapter extends RecyclerView.Adapter<LivroHolder> {

    private final List<Livro> livros;

    public LivroAdapter(List<Livro> livros) {
        this.livros = livros;
    }

    public void atualizarLivro(Livro livro){
        livros.set(livros.indexOf(livro), livro);
        notifyItemChanged(livros.indexOf(livro));
    }

    public void adicionarLivro(Livro livro){
        livros.add(livro);
        notifyItemInserted(getItemCount());
    }

    public void removerLivro(Livro livro){
        int position = livros.indexOf(livro);
        livros.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public LivroHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LivroHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_livros, parent, false));
    }

    @Override
    public void onBindViewHolder(LivroHolder holder, int position) {
        holder.titulo.setText(livros.get(position).getTitulo());
        final Livro livro = livros.get(position);
        holder.btnExcluir.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmação")
                        .setMessage("Tem certeza que deseja excluir este livro?")
                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LivroDAO dao = new LivroDAO(view.getContext());
                                boolean sucesso = dao.excluir(livro.getId());
                                if(sucesso) {
                                    removerLivro(livro);
                                    Snackbar.make(view, "Excluiu!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }else{
                                    Snackbar.make(view, "Erro ao excluir o livro!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create()
                        .show();
            }
        });

        holder.btnEditar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity(v);
                Intent intent = activity.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("livro", livro);
                activity.finish();
                activity.startActivity(intent);
            }
        });
    }

    private Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return livros != null ? livros.size() : 0;
    }
}