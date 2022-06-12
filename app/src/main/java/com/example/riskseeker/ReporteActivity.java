package com.example.riskseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
    RecyclerView recyclerReportes;

    private DatabaseReference databaseReference;
    private String idReporte;
    private Location location_report_selec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        idReporte = getIntent().getExtras().getString("idReporte");
        location_report_selec = new Location("Reporte seleccionado");
        location_report_selec.setLatitude(getIntent().getExtras().getDouble("latitud"));
        location_report_selec.setLongitude(getIntent().getExtras().getDouble("longitud"));

        databaseReference = FirebaseDatabase.getInstance().getReference();

        listaReportes = new ArrayList<>();

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
                        //Radio de la información de los reportes que se mostrará (500 metros)
                        Double radio = Double.valueOf(500);

                        Location location_reporte = new Location("Reporte cercano");
                        location_reporte.setLatitude((Double) snapsho.child("latitud").getValue());
                        location_reporte.setLongitude((Double) snapsho.child("longitud").getValue());

                        //Al momento de mostrar la información de los reportes cercanos y el seleccionando se posicionará primero el seleccionado
                        if(snapsho.child("idReporte").getValue().toString().equals(idReporte)){
                            listaReportes.add(0, new Reporte(snapsho.child("idUsuario").getValue().toString(),
                                    snapsho.child("fecha").getValue().toString(),
                                    snapsho.child("descripcion").getValue().toString(),
                                    snapsho.child("cantidadImg").getValue().toString(),
                                    R.drawable.ic_baseline_person_24_gris,
                                    snapsho.child("tipo").getValue().toString(),
                                    snapsho.child("idReporte").getValue().toString(),
                                    snapsho.child("anonimo").getValue().toString(),
                                    new ArrayList()));
                        }else if(location_reporte.distanceTo(location_report_selec)<= radio){
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Error al buscar reportes", error.toException());
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