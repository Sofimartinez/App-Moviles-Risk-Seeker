package com.example.riskseeker.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.riskseeker.R;
import com.example.riskseeker.models.Reporte;

import java.util.ArrayList;

public class AdaptadorReportes extends RecyclerView.Adapter<AdaptadorReportes.ViewHolderReportes> {

    ArrayList<Reporte> listaReportes;
    Context context;

    //Contructor de la lista de reportes
    public AdaptadorReportes(Context context, ArrayList<Reporte> listaReportes) {
        this.context = context;
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

        //------------------------------Carga de Imagenes de los reportes---------------------------------//

        //Se crea una instancia del adaptador
        AdaptadorImagenes adapter = new AdaptadorImagenes(listaReportes.get(position).getListaImagenes());
        holder.recyclerImagenes.setAdapter(adapter);

    }

    //Retorna el tamaño de la lista que se le entrega
    @Override
    public int getItemCount() {
        return listaReportes.size();
    }

    //Referencia a los item graficos del recyclerView de reportes
    public class ViewHolderReportes extends RecyclerView.ViewHolder {

        TextView nombre,fecha,tipo,reporte;
        ImageView foto_perfil,foto;
        RecyclerView recyclerImagenes;

        public ViewHolderReportes(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.NombreUsuario);
            fecha = itemView.findViewById(R.id.FechaPublicacion);
            tipo = itemView.findViewById(R.id.tipoReporte);
            reporte = itemView.findViewById(R.id.reporte);
            foto = itemView.findViewById(R.id.imagenreporte);
            foto_perfil = itemView.findViewById(R.id.idImagenPerfil);
            recyclerImagenes = itemView.findViewById(R.id.imagenes_reporte);

            //Lista de tipo horizontal
            recyclerImagenes.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
        }
    }
}