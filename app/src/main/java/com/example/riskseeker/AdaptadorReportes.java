package com.example.riskseeker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorReportes extends RecyclerView.Adapter<AdaptadorReportes.ViewHolderReportes> {

    ArrayList<Reporte> listaReportes;

    //Contructor de la lista de reportes
    public AdaptadorReportes(ArrayList<Reporte> listaReportes) {
        this.listaReportes = listaReportes;
    }

    //Enlace del adaptador con archivo item_reporte
    @Override
    public ViewHolderReportes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reporte,parent,false);
        return new ViewHolderReportes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderReportes holder, int position) {
        holder.nombre.setText(listaReportes.get(position).getNombre());
        holder.fecha.setText(listaReportes.get(position).getFecha());
        holder.tipo.setText(listaReportes.get(position).getTipo());
        holder.reporte.setText(listaReportes.get(position).getInf_reporte());
        holder.foto_perfil.setImageResource(listaReportes.get(position).getFoto_perfil());

        //------------------------------Es un dato opción (ver como manejarlo)----------------------//
        holder.foto.setText(listaReportes.get(position).getFotos());

    }

    //Retorna el tamaño de la lista que se le entrega
    @Override
    public int getItemCount() {
        return listaReportes.size();
    }

    //Referencia a los item graficos del recyclerView de reportes
    public class ViewHolderReportes extends RecyclerView.ViewHolder {

        TextView nombre,fecha,tipo,reporte,foto;
        ImageView foto_perfil;

        public ViewHolderReportes(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.NombreUsuario);
            fecha = itemView.findViewById(R.id.FechaPublicacion);
            tipo = itemView.findViewById(R.id.tipoReporte);
            reporte = itemView.findViewById(R.id.reporte);
            foto = itemView.findViewById(R.id.foto);
            foto_perfil = itemView.findViewById(R.id.idImagenPerfil);
        }
    }
}