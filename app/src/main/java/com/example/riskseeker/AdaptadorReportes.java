package com.example.riskseeker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.TextView;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class AdaptadorReportes extends RecyclerView.Adapter<AdaptadorReportes.ViewHolderReportes> {
    private static final String TAG = "adaptador";


    ArrayList<Reporte> listaReportes;
    ArrayList<String> Urls;
    private int posicionn;



    private DatabaseReference databaseReference;




    //Contructor de la lista de reportes
    public AdaptadorReportes(ArrayList<Reporte> listaReportes) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        posicionn = 0;
        Urls = new ArrayList<String>();

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
        holder.imagenIS.setImageResource(0);

        if(Integer.parseInt(listaReportes.get(position).getFotos())>0){
            int b = Integer.parseInt(listaReportes.get(position).getFotos());
            holder.botonSig.setVisibility(View.VISIBLE);
            holder.botonAnt.setVisibility(View.VISIBLE);
            final int[] finalI1 = {0};

            for(int i = 0; i<Integer.parseInt(listaReportes.get(position).getFotos());i++){
                final int[] a = {i + 1};
                databaseReference.child("ImageUrl").child("Images"+listaReportes.get(position).getIdreporte()+ a[0]).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Urls.add(snapshot.getValue().toString());
                            Picasso.get().load(Urls.get(0)).into(holder.imagenIS);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
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
        ImageView foto_perfil,imagenIS;
        ImageButton botonAnt, botonSig;

        public ViewHolderReportes(View itemView) {
            super(itemView);
            //--------------------
            botonSig = itemView.findViewById(R.id.reboton_siguiente);
            botonAnt = itemView.findViewById(R.id.reboton_anterior);
            imagenIS = itemView.findViewById(R.id.reimagenes);
            //botonSig.setVisibility(View.INVISIBLE);
            //botonAnt.setVisibility(View.INVISIBLE);
            //---------------------
            nombre = itemView.findViewById(R.id.NombreUsuario);
            fecha = itemView.findViewById(R.id.FechaPublicacion);
            tipo = itemView.findViewById(R.id.tipoReporte);
            reporte = itemView.findViewById(R.id.reporte);
            foto = itemView.findViewById(R.id.foto);
            foto_perfil = itemView.findViewById(R.id.idImagenPerfil);
            botonSig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(posicionn < Urls.size()-1){
                        posicionn++;
                        Picasso.get().load(Urls.get(posicionn)).into(imagenIS);
                    }
                }
            });
            botonAnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(posicionn > 0){
                        posicionn--;
                        Picasso.get().load(Urls.get(posicionn)).into(imagenIS);
                    }
                }
            });


        }
    }
}