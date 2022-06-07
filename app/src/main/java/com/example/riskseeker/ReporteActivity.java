package com.example.riskseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.riskseeker.adapters.AdaptadorReportes;
import com.example.riskseeker.models.Reporte;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReporteActivity extends AppCompatActivity {
    private static final String TAG = "ReporteActivity";


    ArrayList<Reporte> listaReportes;
    ArrayList<String> listaImagenes;
    ArrayList<String> uids;
    RecyclerView recyclerReportes;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        listaReportes = new ArrayList<>();
        listaImagenes = new ArrayList<>();
        uids = new ArrayList<>();

        recyclerReportes = findViewById(R.id.reportes);

        //Lista de tipo vertical
        recyclerReportes.setLayoutManager(new LinearLayoutManager(this));

        CargarReportes();
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
                                R.drawable.ic_baseline_person_24_gris,
                                snapsho.child("tipo").getValue().toString(),
                                snapsho.child("idReporte").getValue().toString(),
                                snapsho.child("anonimo").getValue().toString(),
                                new ArrayList()));
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
                        if(r.isAnonimo().equals("true")){
                            r.setNombre("Anónimo");
                        }else{
                            String nombre = snapshot.child(r.getNombre()).child("nombre").getValue().toString();
                            String apellido = snapshot.child(r.getNombre()).child("apellido").getValue().toString();
                            r.setNombre(nombre + " " + apellido);

                        }
                        if(Integer.parseInt(r.getFotos())>0){
                            listaImagenes.clear();
                            for(int pos_imagen = 1; pos_imagen<=Integer.parseInt(r.getFotos());pos_imagen++){
                                r.setListaImagenes("Images"+listaReportes.get(i).getIdreporte()+pos_imagen);

                            }
                        }
                        listaReportes.set(i,r);
                    }
                    //Se crea una instancia del adaptador
                    AdaptadorReportes adapter = new AdaptadorReportes(getApplicationContext(),listaReportes);
                    recyclerReportes.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Error al buscar el usuario", error.toException());
            }
        });


    }
}