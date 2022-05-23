package com.example.riskseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int CODIGO_PERMISO_UBICACION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void CargarMapa(View view) {
        verificarPermiso();
    }

    //Si se otorgo el permiso se carga el mapa
    private void permisoUbicacionConcedido() {
        Intent cargarMapa = new Intent(getApplicationContext(),MapsActivity.class);
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

    public void Formulario(View view) {
        Intent cargarMapa = new Intent(getApplicationContext(),FormularioReporteActivity.class);
        startActivity(cargarMapa);
    }
}