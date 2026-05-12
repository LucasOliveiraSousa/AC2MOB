package com.example.receitasfirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReceitaAdapter extends
        RecyclerView.Adapter<ReceitaAdapter.MyViewHolder> {

    Context context;
    ArrayList<Receita> lista;

    OnItemClickListener listener;

    public interface OnItemClickListener{
        void onClick(Receita r);
        void onLongClick(Receita r);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ReceitaAdapter(Context context,
                          ArrayList<Receita> lista) {

        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_receita,
                        parent,
                        false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MyViewHolder holder,
            int position) {

        Receita r = lista.get(position);

        holder.txtNome.setText(r.nome);

        holder.txtInfo.setText(
                "Categoria: " + r.categoria +
                        "\nTempo: " + r.tempo + " min" +
                        "\nIngredientes: " + r.ingredientes +
                        "\nDificuldade: " + r.dificuldade
        );

        holder.itemView.setOnClickListener(v -> {
            listener.onClick(r);
        });

        holder.itemView.setOnLongClickListener(v -> {

            listener.onLongClick(r);

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MyViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtNome, txtInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNome = itemView.findViewById(R.id.txtNome);
            txtInfo = itemView.findViewById(R.id.txtInfo);
        }
    }
}