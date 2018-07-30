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

import br.com.mecyo.biblioteca.holders.AlunoHolder;
import br.com.mecyo.biblioteca.DAO.AlunoDAO;
import br.com.mecyo.biblioteca.R;
import br.com.mecyo.biblioteca.models.Aluno;

/**
 * Created by Emerson Santos on 30/07/18.
 */

public class AlunoAdapter extends RecyclerView.Adapter<AlunoHolder> {

    private final List<Aluno> alunos;

    public AlunoAdapter(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public void atualizarAluno(Aluno aluno){
        alunos.set(alunos.indexOf(aluno), aluno);
        notifyItemChanged(alunos.indexOf(aluno));
    }

    public void adicionarAluno(Aluno aluno){
        alunos.add(aluno);
        notifyItemInserted(getItemCount());
    }

    public void removerAluno(Aluno aluno){
        int position = alunos.indexOf(aluno);
        alunos.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public AlunoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlunoHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_aluno, parent, false));
    }

    @Override
    public void onBindViewHolder(AlunoHolder holder, int position) {
        holder.nomeAluno.setText(alunos.get(position).getNome());
        final Aluno aluno = alunos.get(position);
        holder.btnExcluir.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmação")
                        .setMessage("Tem certeza que deseja excluir este aluno?")
                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlunoDAO dao = new AlunoDAO(view.getContext());
                                boolean sucesso = dao.excluir(aluno.getId());
                                if(sucesso) {
                                    removerAluno(aluno);
                                    Snackbar.make(view, "Excluiu!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }else{
                                    Snackbar.make(view, "Erro ao excluir o aluno!", Snackbar.LENGTH_LONG)
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
                intent.putExtra("aluno", aluno);
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
        return alunos != null ? alunos.size() : 0;
    }
}