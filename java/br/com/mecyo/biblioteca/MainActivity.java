package br.com.mecyo.biblioteca;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Calendar;

import br.com.mecyo.biblioteca.DAO.AlunoDAO;
import br.com.mecyo.biblioteca.DAO.LivroDAO;
import br.com.mecyo.biblioteca.Util.Uteis;
import br.com.mecyo.biblioteca.adapters.AlunoAdapter;
import br.com.mecyo.biblioteca.adapters.LivroAdapter;
import br.com.mecyo.biblioteca.models.Aluno;
import br.com.mecyo.biblioteca.models.Livro;

/**
 * Created by Emerson Santos on 30/07/18.
 */

public class MainActivity extends AppCompatActivity {

    Aluno alunoEditado = null;
    Livro livroEditado = null;
    //CRIA POPUP COM O CALENDÁRIO
    DatePickerDialog datePickerDialogDataPublicacao;
    EditText editTextDataPublicacao;


    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextDataPublicacao = (EditText)this.findViewById(R.id.editTextDataPublicacao);
        CriarEventos();
        //verifica se começou agora ou se veio de uma edição
        Intent intent = getIntent();
        if(intent.hasExtra("aluno")){
            findViewById(R.id.includemain).setVisibility(View.INVISIBLE);
            findViewById(R.id.includecadastroaluno).setVisibility(View.VISIBLE);
            findViewById(R.id.includecadastrolivro).setVisibility(View.INVISIBLE);
            findViewById(R.id.fab).setVisibility(View.INVISIBLE);
            alunoEditado = (Aluno) intent.getSerializableExtra("aluno");
            EditText txtNome = (EditText)findViewById(R.id.txtNome);
            Spinner spnEstado = (Spinner)findViewById(R.id.spnEstado);
            CheckBox chkVip = (CheckBox)findViewById(R.id.chkVip);

            txtNome.setText(alunoEditado.getNome());
            chkVip.setChecked(alunoEditado.getVip());
            spnEstado.setSelection(getIndex(spnEstado, alunoEditado.getUf()));
            if(alunoEditado.getSexo() != null){
                RadioButton rb;
                if(alunoEditado.getSexo().equals("M"))
                    rb = (RadioButton)findViewById(R.id.rbMasculino);
                else
                    rb = (RadioButton)findViewById(R.id.rbFeminino);
                rb.setChecked(true);
            }
        }
        else if(intent.hasExtra("livro")){
            findViewById(R.id.includemain).setVisibility(View.INVISIBLE);
            findViewById(R.id.includecadastrolivro).setVisibility(View.VISIBLE);
            findViewById(R.id.includecadastroaluno).setVisibility(View.INVISIBLE);
            findViewById(R.id.fab).setVisibility(View.INVISIBLE);
            livroEditado = (Livro) intent.getSerializableExtra("livro");
            EditText txtTitulo = (EditText)findViewById(R.id.txtTitulo);
            EditText txtEditora = (EditText)findViewById(R.id.txtEditora);
            EditText txtEscritor = (EditText)findViewById(R.id.txtEscritor);
            EditText txtLocal = (EditText)findViewById(R.id.txtLocal);
            CheckBox chkDisponivel = (CheckBox)findViewById(R.id.chkDisponivel);
            EditText editTextDataPublicacao = (EditText)findViewById(R.id.editTextDataPublicacao);
            txtTitulo.setText(livroEditado.getTitulo());
            txtEditora.setText(livroEditado.getEditora());
            txtEscritor.setText(livroEditado.getEscritor());
            txtLocal.setText(livroEditado.getLocal());
            chkDisponivel.setChecked(livroEditado.getDisponivel());
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(findViewById(R.id.includecadastroaluno).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.includemain).setVisibility(View.INVISIBLE);
                    findViewById(R.id.includecadastrolivro).setVisibility(View.INVISIBLE);
                    findViewById(R.id.includecadastroaluno).setVisibility(View.VISIBLE);
                    findViewById(R.id.fab).setVisibility(View.INVISIBLE);
                }
                else if(findViewById(R.id.includecadastrolivro).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.includemain).setVisibility(View.INVISIBLE);
                    findViewById(R.id.includecadastroaluno).setVisibility(View.INVISIBLE);
                    findViewById(R.id.includecadastrolivro).setVisibility(View.VISIBLE);
                    findViewById(R.id.fab).setVisibility(View.INVISIBLE);
                }
            }
        });

        Button btnCancelarAluno = (Button)findViewById(R.id.btnCancelarAluno);
        btnCancelarAluno.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.includemain).setVisibility(View.VISIBLE);
                findViewById(R.id.includecadastroaluno).setVisibility(View.INVISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        });

        Button btnCancelarLivro = (Button)findViewById(R.id.btnCancelarLivro);
        btnCancelarLivro.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.includecadastrolivro).setVisibility(View.INVISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        });

        Button btnSalvarAluno = (Button)findViewById(R.id.btnSalvarAluno);
        btnSalvarAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //carregando os campos
                EditText txtNome = (EditText)findViewById(R.id.txtNome);
                EditText txtMatricula = (EditText)findViewById(R.id.txtMatricula);
                Spinner spnEstado = (Spinner)findViewById(R.id.spnEstado);
                RadioGroup rgSexo = (RadioGroup)findViewById(R.id.rgSexo);
                CheckBox chkVip = (CheckBox)findViewById(R.id.chkVip);

                //pegando os valores
                String nome = txtNome.getText().toString();
                String uf = spnEstado.getSelectedItem().toString();
                String matricula = txtMatricula.getText().toString();
                boolean vip = chkVip.isChecked();
                String sexo = rgSexo.getCheckedRadioButtonId() == R.id.rbMasculino ? "M" : "F";

                //salvando os dados
                AlunoDAO dao = new AlunoDAO(getBaseContext());
                boolean sucesso;
                if(alunoEditado != null)
                    sucesso = dao.salvar(alunoEditado.getId(), nome, sexo, matricula, uf, vip);
                else
                    sucesso = dao.salvar(nome, sexo, matricula, uf, vip);

                if(sucesso) {
                    Aluno aluno = dao.retornarUltimo();
                    if(alunoEditado != null)
                        adapterAluno.atualizarAluno(aluno);
                    else
                        adapterAluno.adicionarAluno(aluno);

                    //limpa os campos
                    alunoEditado = null;
                    txtNome.setText("");
                    rgSexo.setSelected(false);
                    spnEstado.setSelection(0);
                    chkVip.setChecked(false);

                    Snackbar.make(view, "Salvou!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    findViewById(R.id.includemain).setVisibility(View.VISIBLE);
                    findViewById(R.id.includecadastroaluno).setVisibility(View.INVISIBLE);
                    findViewById(R.id.fab).setVisibility(View.VISIBLE);
                }else{
                    Snackbar.make(view, "Erro ao salvar, consulte os logs!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        configurarRecycler("aluno");
    }

    RecyclerView recyclerView;
    private AlunoAdapter adapterAluno;
    private LivroAdapter adapterLivro;

    private void configurarRecycler(String tipo) {

        if(tipo == "aluno") {
            recyclerView = (RecyclerView) findViewById(R.id.recyclerViewAlunos);
            // Configurando o gerenciador de layout para ser uma lista.
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            // Adiciona o adapter que irá anexar os objetos à lista.
            AlunoDAO dao = new AlunoDAO(this);
            adapterAluno = new AlunoAdapter(dao.retornarTodos());
            recyclerView.setAdapter(adapterAluno);
        }
        else if(tipo == "livro") {
            recyclerView = (RecyclerView) findViewById(R.id.recyclerViewLivros);
            // Configurando o gerenciador de layout para ser uma lista.
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            // Adiciona o adapter que irá anexar os objetos à lista.
            LivroDAO dao = new LivroDAO(this);
            adapterLivro = new LivroAdapter(dao.retornarTodos());
            recyclerView.setAdapter(adapterLivro);
        }

        // Configurando um separador entre linhas, para uma melhor visualização.
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_alunos) {
            findViewById(R.id.includemain).setVisibility(View.VISIBLE);
            findViewById(R.id.includecadastroaluno).setVisibility(View.VISIBLE);
            findViewById(R.id.includecadastrolivro).setVisibility(View.INVISIBLE);
            findViewById(R.id.fab).setVisibility(View.VISIBLE);
            return true;
        }
        else if (id == R.id.action_livros) {
            findViewById(R.id.includemain).setVisibility(View.VISIBLE);
            findViewById(R.id.includecadastroaluno).setVisibility(View.INVISIBLE);
            findViewById(R.id.includecadastrolivro).setVisibility(View.VISIBLE);
            findViewById(R.id.fab).setVisibility(View.VISIBLE);
            return true;
        }
        else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //CRIA OS EVENTOS DOS COMPONENTES
    protected void CriarEventos() {
        final Calendar calendarDataAtual = Calendar.getInstance();
        int anoAtual = calendarDataAtual.get(Calendar.YEAR);
        int mesAtual = calendarDataAtual.get(Calendar.MONTH);
        int diaAtual = calendarDataAtual.get(Calendar.DAY_OF_MONTH);
        //MONTANDO O OBJETO DE DATA PARA PREENCHER O CAMPOS QUANDO FOR SELECIONADO
        //FORMATO DD/MM/YYYY
        datePickerDialogDataPublicacao = new DatePickerDialog(this, new
                DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int anoSelecionado, int
                            mesSelecionado, int diaSelecionado) {
                        //FORMATANDO O MÊS COM DOIS DÍGITOS
                        String mes = (String.valueOf((mesSelecionado + 1)).length() == 1 ?
                                "0" + (mesSelecionado + 1) : String.valueOf(mesSelecionado));
                        editTextDataPublicacao.setText(diaSelecionado + "/" + mes + "/" +
                                anoSelecionado);
                    }
                }, anoAtual, mesAtual, diaAtual);
        //CRIANDO EVENTO NO CAMPO DE DATA PARA ABRIR A POPUP
        editTextDataPublicacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogDataPublicacao.show();
            }
        });
        //CRIANDO EVENTO NO CAMPO DE DATA PARA ABRIR A POPUP
        editTextDataPublicacao.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        datePickerDialogDataPublicacao.show();
                    }
                }
        );
    }
}
