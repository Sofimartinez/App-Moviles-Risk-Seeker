package com.example.riskseeker.adapters;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riskseeker.R;
import com.example.riskseeker.models.Reporte;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class AdaptadorReportes extends RecyclerView.Adapter<AdaptadorReportes.ViewHolderReportes> {

    ArrayList<Reporte> listaReportes;
    Context context;
    private static final String TAG = "Adatadoractivity";

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
        if(position == 0){
            String type = listaReportes.get(position).getTipo();
            //Falta definir mejor los colores (estan muy feos)
            switch (type) {
                case "Hurto":
                    holder.cardView.setCardBackgroundColor(Color.rgb(235,94,94));
                    holder.nombre.setTextColor(Color.rgb(255,255,255));
                    holder.fecha.setTextColor(Color.rgb(255,255,255));
                    break;
                case "Actividad sospechosa":
                    holder.cardView.setCardBackgroundColor(Color.rgb(173,87,231));
                    holder.nombre.setTextColor(Color.rgb(255,255,255));
                    holder.fecha.setTextColor(Color.rgb(255,255,255));
                    break;
                case "Asalto":
                    holder.cardView.setCardBackgroundColor(Color.rgb(97,97,236));
                    holder.nombre.setTextColor(Color.rgb(255,255,255));
                    holder.fecha.setTextColor(Color.rgb(255,255,255));
                    break;
                case "Acoso":
                    holder.cardView.setCardBackgroundColor(Color.rgb( 137,230,230));
                    break;
                case "Secuestro":
                    holder.cardView.setCardBackgroundColor(Color.rgb(113,178,243));
                    holder.nombre.setTextColor(Color.rgb(255,255,255));
                    holder.fecha.setTextColor(Color.rgb(255,255,255));
                    break;
                case "Tráfico de drogas":
                    holder.cardView.setCardBackgroundColor(Color.rgb(235,235,94));
                    break;
                case "Tráfico de armas":
                    holder.cardView.setCardBackgroundColor(Color.rgb(233,162,92));
                    holder.nombre.setTextColor(Color.rgb(255,255,255));
                    holder.fecha.setTextColor(Color.rgb(255,255,255));
                    break;
                case "disturbios":
                    holder.cardView.setCardBackgroundColor(Color.rgb(134,251,134));
                    break;
            }
        }else if(position == 1){
            holder.reportes_cerca.setText(R.string.reportes_cercanos);
            holder.cardView.setCardBackgroundColor(Color.rgb(255,255,255));
            holder.nombre.setTextColor(Color.rgb(164,164,164));
            holder.fecha.setTextColor(Color.rgb(21,21,21));
        }else{
            holder.reportes_cerca.setText("");
            holder.cardView.setCardBackgroundColor(Color.rgb(255,255,255));
            holder.nombre.setTextColor(Color.rgb(164,164,164));
            holder.fecha.setTextColor(Color.rgb(21,21,21));
        }

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
        CardView cardView;
        RecyclerView recyclerImagenes;
        TextView reportes_cerca;

        public ViewHolderReportes(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.NombreUsuario);
            fecha = itemView.findViewById(R.id.FechaPublicacion);
            tipo = itemView.findViewById(R.id.tipoReporte);
            reporte = itemView.findViewById(R.id.reporte);
            foto = itemView.findViewById(R.id.imagenreporte);
            foto_perfil = itemView.findViewById(R.id.idImagenPerfil);
            cardView = itemView.findViewById(R.id.card_view);
            reportes_cerca = itemView.findViewById(R.id.reportes_cercano);
            recyclerImagenes = itemView.findViewById(R.id.imagenes_reporte);

            //Lista de tipo horizontal
            recyclerImagenes.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
        }
    }
}