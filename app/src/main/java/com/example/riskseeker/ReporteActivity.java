package com.example.riskseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReporteActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";


    ArrayList<Reporte> listaReportes;
    ArrayList<String> uids;
    RecyclerView recyclerReportes;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);
        databaseReference = FirebaseDatabase.getInstance().getReference();


        listaReportes = new ArrayList<>();
        uids = new ArrayList<>();

        recyclerReportes = findViewById(R.id.reportes);


        //Lista de tipo vertical
        recyclerReportes.setLayoutManager(new LinearLayoutManager(this));

        CargarReportes();
        //Se crea una instancia del adaptador


    }


    private void CargarReportes() {
        databaseReference.child("Reporte/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snapsho: snapshot.getChildren()){
                        uids.add(snapsho.child("idUsuario").getValue().toString());
                        listaReportes.add(new Reporte(snapsho.child("idUsuario").getValue().toString(),
                                snapsho.child("fecha").getValue().toString(),
                                snapsho.child("descripcion").getValue().toString(),
                                snapsho.child("cantidadImg").getValue().toString(),
                                R.mipmap.ic_launcher,
                                snapsho.child("tipo").getValue().toString(),
                                snapsho.child("idReporte").getValue().toString()));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("Usuario/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(int i= 0;i< listaReportes.size();i++){
                        Reporte r = listaReportes.get(i);
                        r.setNombre(snapshot.child(r.getNombre()).child("nombre").getValue().toString());
                        listaReportes.set(i,r);
                    }
                    AdaptadorReportes adapter = new AdaptadorReportes(listaReportes);
                    recyclerReportes.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}