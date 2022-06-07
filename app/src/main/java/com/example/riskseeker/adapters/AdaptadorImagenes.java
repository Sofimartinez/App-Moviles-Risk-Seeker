package com.example.riskseeker.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riskseeker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorImagenes extends RecyclerView.Adapter<AdaptadorImagenes.ViewHolderImagenes> {

    private static final String TAG = "AdaptadorImagenes";

    ArrayList<String> listaImagenes;

    private DatabaseReference databaseReference;

    public AdaptadorImagenes(ArrayList<String> listaImagenes) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.listaImagenes = listaImagenes;
    }

    @NonNull
    @Override
    //Enlace del adaptador con archivo item_reporte_imagenes
    public ViewHolderImagenes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reporte_imagen,parent,false);
        return new ViewHolderImagenes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImagenes holder, int position) {
        String nombreImagen = listaImagenes.get(position);
        databaseReference.child("ImageUrl").child(nombreImagen).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Picasso.get().load(snapshot.getValue().toString()).into(holder.imagen);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Error al buscar las imagenes", error.toException());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaImagenes.size();
    }

    //Referencia a los item graficos del recyclerView de imagenes del reporte
    public class ViewHolderImagenes extends RecyclerView.ViewHolder {
        ImageView imagen;

        public ViewHolderImagenes(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagenreporte);
        }
    }
}
