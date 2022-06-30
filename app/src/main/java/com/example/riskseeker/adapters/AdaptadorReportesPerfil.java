package com.example.riskseeker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.riskseeker.R;
import com.example.riskseeker.models.ReportePerfil;

import java.util.ArrayList;

public class AdaptadorReportesPerfil extends RecyclerView.Adapter<AdaptadorReportesPerfil.ViewHolderReportesPerfil>{

    ArrayList<ReportePerfil> listaReportes;
    Context context;

    //Contructor de la lista de reportes
    public AdaptadorReportesPerfil(Context context, ArrayList<ReportePerfil> listaReportes) {
        this.context = context;
        this.listaReportes = listaReportes;
    }

    @NonNull
    @Override
    //Enlace del adaptador con archivo item_reporte_perfil
    public AdaptadorReportesPerfil.ViewHolderReportesPerfil onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reporte_perfil,parent,false);
        return new ViewHolderReportesPerfil(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorReportesPerfil.ViewHolderReportesPerfil holder, int position) {
        if(listaReportes.get(position).getFecha().equals("false")){
            holder.anonimo.setText(R.string.reporte_no_anonimo);
        }else{
            holder.anonimo.setText(R.string.reporte_si_anonimo);
        }
        holder.fecha.setText(listaReportes.get(position).getFecha());
        holder.tipo.setText(listaReportes.get(position).getTipo());
        holder.reporte.setText(listaReportes.get(position).getInf_reporte());
        holder.ubicacion.setText(listaReportes.get(position).getUbicacion());

        //------------------------------Carga de Imagenes de los reportes---------------------------------//

        //Se crea una instancia del adaptador
        AdaptadorImagenes adapter = new AdaptadorImagenes(listaReportes.get(position).getListaImagenes());
        holder.viewPager.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return listaReportes.size();
    }

    public class ViewHolderReportesPerfil extends RecyclerView.ViewHolder {

        TextView anonimo,fecha,tipo,reporte,ubicacion;
        ViewPager2 viewPager;

        public ViewHolderReportesPerfil(@NonNull View itemView) {
            super(itemView);
            anonimo = itemView.findViewById(R.id.anonimo);
            fecha = itemView.findViewById(R.id.FechaPublicacion_perfil);
            tipo = itemView.findViewById(R.id.tipoReporte_perfil);
            reporte = itemView.findViewById(R.id.reporte_perfil);
            ubicacion = itemView.findViewById(R.id.ubicacion_perfil);
            viewPager = itemView.findViewById(R.id.imagenes_reporte_perfil);

        }
    }
}
