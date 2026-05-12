package com.example.receitasfirebase;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtNome, edtTempo, edtIngredientes;
    Spinner spCategoria, spDificuldade, spFiltro;
    CheckBox checkFavorita;
    Button btnSalvar;
    RecyclerView recycler;

    ArrayList<Receita> lista = new ArrayList<>();
    ReceitaAdapter adapter;

    DatabaseReference banco;

    String idEditar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNome = findViewById(R.id.edtNome);
        edtTempo = findViewById(R.id.edtTempo);
        edtIngredientes = findViewById(R.id.edtIngredientes);

        spCategoria = findViewById(R.id.spCategoria);
        spDificuldade = findViewById(R.id.spDificuldade);
        spFiltro = findViewById(R.id.spFiltro);

        checkFavorita = findViewById(R.id.checkFavorita);

        btnSalvar = findViewById(R.id.btnSalvar);

        recycler = findViewById(R.id.recycler);

        recycler.setLayoutManager(
                new LinearLayoutManager(this));

        adapter = new ReceitaAdapter(this, lista);

        recycler.setAdapter(adapter);

        banco = FirebaseDatabase.getInstance()
                .getReference("receitas");

        String[] categorias = {
                "Doce",
                "Salgada",
                "Bebida",
                "Massa",
                "Sobremesa"
        };

        String[] filtro = {
                "Todas",
                "Doce",
                "Salgada",
                "Bebida",
                "Massa",
                "Sobremesa"
        };

        String[] dificuldade = {
                "Fácil",
                "Médio",
                "Difícil"
        };

        spCategoria.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categorias
        ));

        spFiltro.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                filtro
        ));

        spDificuldade.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                dificuldade
        ));

        btnSalvar.setOnClickListener(v -> salvar());

        adapter.setOnItemClickListener(new ReceitaAdapter.OnItemClickListener() {
            @Override
            public void onClick(Receita r) {

                edtNome.setText(r.nome);
                edtTempo.setText(r.tempo);
                edtIngredientes.setText(r.ingredientes);

                checkFavorita.setChecked(r.favorita);

                idEditar = r.id;
            }

            @Override
            public void onLongClick(Receita r) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Excluir")
                        .setMessage("Deseja excluir?")
                        .setPositiveButton("Sim", (dialog, which) -> {

                            banco.child(r.id).removeValue();

                            Toast.makeText(
                                    MainActivity.this,
                                    "Excluído",
                                    Toast.LENGTH_SHORT
                            ).show();

                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });

        listar();
    }

    private void salvar() {

        String nome = edtNome.getText().toString();
        String tempo = edtTempo.getText().toString();
        String ingredientes = edtIngredientes.getText().toString();

        if(nome.isEmpty()){
            edtNome.setError("Digite o nome");
            return;
        }

        if(tempo.isEmpty()){
            edtTempo.setError("Digite o tempo");
            return;
        }

        if(ingredientes.isEmpty()){
            edtIngredientes.setError("Digite os ingredientes");
            return;
        }

        if(idEditar.isEmpty()){

            String id = banco.push().getKey();

            Receita r = new Receita(
                    id,
                    nome,
                    spCategoria.getSelectedItem().toString(),
                    tempo,
                    ingredientes,
                    spDificuldade.getSelectedItem().toString(),
                    checkFavorita.isChecked()
            );

            banco.child(id).setValue(r);

            Toast.makeText(this,
                    "Salvo",
                    Toast.LENGTH_SHORT).show();

        } else {

            Receita r = new Receita(
                    idEditar,
                    nome,
                    spCategoria.getSelectedItem().toString(),
                    tempo,
                    ingredientes,
                    spDificuldade.getSelectedItem().toString(),
                    checkFavorita.isChecked()
            );

            banco.child(idEditar).setValue(r);

            Toast.makeText(this,
                    "Atualizado",
                    Toast.LENGTH_SHORT).show();

            idEditar = "";
        }

        limpar();
    }

    private void listar(){

        banco.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                lista.clear();

                String categoriaFiltro =
                        spFiltro.getSelectedItem().toString();

                for(DataSnapshot dados : snapshot.getChildren()){

                    Receita r = dados.getValue(Receita.class);

                    if(categoriaFiltro.equals("Todas")){

                        lista.add(r);

                    } else {

                        if(r.categoria.equals(categoriaFiltro)){
                            lista.add(r);
                        }
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spFiltro.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            android.view.View view,
                            int position,
                            long id) {

                        listar();
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {

                    }
                });
    }

    private void limpar(){

        edtNome.setText("");
        edtTempo.setText("");
        edtIngredientes.setText("");

        checkFavorita.setChecked(false);
    }
}