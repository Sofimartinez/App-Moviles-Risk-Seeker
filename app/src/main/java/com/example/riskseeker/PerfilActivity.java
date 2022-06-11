package com.example.riskseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riskseeker.adapters.AdaptadorReportesPerfil;
import com.example.riskseeker.models.ReportePerfil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PerfilActivity extends AppCompatActivity {

    private TextView nombre_usuario, correo_usuario, cantidad_reportes;
    private DatabaseReference databaseReference;
    ArrayList<ReportePerfil> listaReportes;
    RecyclerView recyclerReportes;
    String uid;

    private static final String TAG = "PerfilActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        nombre_usuario = findViewById(R.id.nombre_usuario);
        correo_usuario = findViewById(R.id.correo);
        cantidad_reportes = findViewById(R.id.cantidad_reportes);
        recyclerReportes = findViewById(R.id.reportes_perfil);

        listaReportes = new ArrayList<>();

        //Lista de tipo vertical
        recyclerReportes.setLayoutManager(new LinearLayoutManager(this));

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setOnMenuItemClickListener(new MaterialToolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cerrar_sesion:
                        CerrarSesion();
                        return true;
                    default:
                        return false;
                }
            }
        });

        CargarDatosUsuario();
        CargarReportesPerfil();
    }

    private void CargarReportesPerfil() {
        databaseReference.child("Reporte").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snapsho: snapshot.getChildren()){
                        if(snapsho.child("idUsuario").getValue().equals(uid)){
                            listaReportes.add(new ReportePerfil(snapsho.child("fecha").getValue().toString(),
                                    snapsho.child("descripcion").getValue().toString(),
                                    snapsho.child("cantidadImg").getValue().toString(),
                                    snapsho.child("tipo").getValue().toString(),
                                    snapsho.child("idReporte").getValue().toString(),
                                    snapsho.child("anonimo").getValue().toString(),
                                    snapsho.child("ubicacion").getValue().toString(),
                                    new ArrayList()));
                        }
                    }
                    cantidad_reportes.setText(String.valueOf(listaReportes.size()));
                    for(int i= 0;i< listaReportes.size();i++){
                        ReportePerfil r = listaReportes.get(i);
                        if(Integer.parseInt(r.getCantidadImg())>0){
                            for(int pos_imagen = 1; pos_imagen<=Integer.parseInt(r.getCantidadImg());pos_imagen++){
                                r.setListaImagenes("Images"+listaReportes.get(i).getIdreporte()+pos_imagen);
                            }
                        }
                        listaReportes.set(i,r);
                    }
                    //Se crea una instancia del adaptador
                    AdaptadorReportesPerfil adapter = new AdaptadorReportesPerfil(getApplicationContext(),listaReportes);
                    recyclerReportes.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CargarDatosUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent inicio = new Intent(getApplicationContext(), MainActivity.class);

        if (user != null) {
            //Sesion iniciada
            uid = user.getUid();

            databaseReference.child("Usuario").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String nombre = snapshot.child("nombre").getValue().toString();
                        String apellido = snapshot.child("apellido").getValue().toString();
                        String correo = user.getEmail();
                        nombre_usuario.setText(nombre + " " + apellido);
                        correo_usuario.setText(correo);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Error al buscar el usuario", error.toException());
                }
            });

        } else {
            Toast.makeText(this, R.string.error_datos_usuario, Toast.LENGTH_LONG).show();
        }
    }


    private void CerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.Cerrar_sesion)
                .setMessage(R.string.mensaje_cerrar_sesion)
                .setCancelable(false)
                .setPositiveButton(R.string.boton_cerrar, (dialogInterface, i) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent inicioapp = new Intent(getApplicationContext(),MainActivity.class);
                    inicioapp.putExtra("invitado",true);
                    inicioapp.putExtra("nombre", "");
                    inicioapp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(inicioapp);
                    finish();
                })
                .setNegativeButton(R.string.boton_nocerrar, (dialogInterface, i) -> Log.e(TAG,"Cancelar"))
                .show();
    }

}