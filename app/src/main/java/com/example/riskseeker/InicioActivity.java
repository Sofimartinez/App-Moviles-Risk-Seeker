package com.example.riskseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InicioActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    private Boolean invitado = true;
    private String usuario;

    private static final String TAG = "InicioActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        obtenerSesion();

    }

    public void obtenerSesion(){
        //FirebaseAuth.getInstance().signOut();
        //Obtener el usuario que tiene la sesión activa
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent inicio = new Intent(getApplicationContext(), MainActivity.class);

        if (user != null) {
            //Sesion iniciada
            String uid = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Usuario")
                    .child(uid);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String nombre = snapshot.child("nombre").getValue().toString();
                        String apellido = snapshot.child("apellido").getValue().toString();
                        invitado = false;
                        usuario = nombre + " " + apellido;
                        inicio.putExtra("invitado",invitado);
                        inicio.putExtra("nombre",usuario);
                        startActivity(inicio);
                        finish();

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Error al buscar el usuario", error.toException());
                }
            });

        } else {
            // Sesión no iniciada
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    inicio.putExtra("invitado",invitado);
                    startActivity(inicio);
                    finish();
                }
            },2000);

        }
    }
}