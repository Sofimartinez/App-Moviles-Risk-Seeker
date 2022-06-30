package com.example.riskseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {

    private static final int CODIGO_PERMISO_UBICACION = 1;
    Button usuario, invitado, inicio_sesion, registro;
    private Boolean isInvitado = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isInvitado = getIntent().getExtras().getBoolean("invitado");

        usuario = findViewById(R.id.usuario);
        invitado = findViewById(R.id.invitado);
        inicio_sesion = findViewById(R.id.inicio_sesion);
        registro = findViewById(R.id.registro);

        if(isInvitado){
            usuario.setVisibility(View.INVISIBLE);
            invitado.setVisibility(View.VISIBLE);
        }else{
            usuario.setVisibility(View.VISIBLE);
            usuario.setText(getIntent().getExtras().getString("nombre"));
            invitado.setVisibility(View.INVISIBLE);
            inicio_sesion.setVisibility(View.INVISIBLE);
            registro.setVisibility(View.INVISIBLE);
        }
    }

    public void CargarMapa(View view) {
        verificarPermiso();
    }

    //Si se otorgo el permiso se carga el mapa
    private void permisoUbicacionConcedido() {
        Intent cargarMapa = new Intent(getApplicationContext(),MapsActivity.class);
        cargarMapa.putExtra("invitado",isInvitado );
        startActivity(cargarMapa);
    }

    private void verificarPermiso() {
        //Verificación de permiso de ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            permisoUbicacionConcedido();
        }else{
            //Si no se ha concedido el permiso anteriormente se pregunta
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PERMISO_UBICACION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        //En caso de tener mas permisos distintos a la ubicación
        switch (requestCode) {
            case CODIGO_PERMISO_UBICACION:
                if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                    permisoUbicacionConcedido();
                    Toast.makeText(this, R.string.Permiso_Ubicacion_concedido, Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, R.string.Permiso_Ubicacion_denegado, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void iniciarSesion(View view) {
        Intent i = new Intent(getApplicationContext(), IniciarSesionActivity.class);
        startActivity(i);
    }

    public void Registrar(View view) {
        Intent i = new Intent(getApplicationContext(), RegistroActivity.class);
        startActivity(i);
    }
}