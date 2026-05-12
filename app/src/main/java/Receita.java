package com.example.receitasfirebase;

public class Receita {

    public String id;
    public String nome;
    public String categoria;
    public String tempo;
    public String ingredientes;
    public String dificuldade;
    public boolean favorita;

    public Receita() {
    }

    public Receita(String id,
                   String nome,
                   String categoria,
                   String tempo,
                   String ingredientes,
                   String dificuldade,
                   boolean favorita) {

        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.tempo = tempo;
        this.ingredientes = ingredientes;
        this.dificuldade = dificuldade;
        this.favorita = favorita;
    }
}