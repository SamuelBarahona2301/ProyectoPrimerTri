package com.example.eventhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> {

    ArrayList<Evento> listDatos;

    public AdapterDatos(ArrayList<Evento> listDatos) {
        this.listDatos = listDatos;
    }

    @Override
    public AdapterDatos.ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDatos.ViewHolderDatos holder, int position) {
        holder.nombre.setText(listDatos.get(position).getNombre());
        holder.descripcion.setText(listDatos.get(position).getDescripcion());
        holder.foto.setImageResource(listDatos.get(position).getFoto());
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView nombre, descripcion, fecha, lugar, hora;
        ImageView foto;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombre);
            descripcion = itemView.findViewById(R.id.txtDescripcion);
            foto = itemView.findViewById(R.id.idImagen);
        }
    }
}
