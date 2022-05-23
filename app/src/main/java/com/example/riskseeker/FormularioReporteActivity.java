package com.example.riskseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FormularioReporteActivity extends AppCompatActivity {

    EditText ubicacion, descripcion;
    AutoCompleteTextView tipo;
    ImageView imagen;
    boolean anonimo;
    Switch switchE;

    private static final String[] lista_tipo = new String[]{"Tipo 1","Tipo 2","Tipo 3","Tipo 4","Tipo 5","Tipo 6","Tipo 7","Tipo 8"};

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_reporte);

        ubicacion = findViewById(R.id.idUbicacion);
        descripcion = findViewById(R.id.idDescripcion);
        tipo = findViewById(R.id.idSelec_tipo);
        switchE = findViewById(R.id.idAnonimo);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista_tipo);
        tipo.setAdapter(adapter);

        inicializarFirebase();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void CargarImagenes(View view) {
    }

    public void AgregarReporte(View view) {
        String desc_reporte = descripcion.getText().toString();
        String ubic_reporte = ubicacion.getText().toString();
        String tipo_reporte = tipo.getText().toString();

        if(desc_reporte.equals("")||ubic_reporte.equals("")||tipo_reporte.equals("")) {
            Validacion(desc_reporte,ubic_reporte,tipo_reporte);
        }else{
            InfoReporte reporte = new InfoReporte();

            //Fecha actual en el momento que se agrega el reporte
            String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()));

            //Genera un id ramdon
            reporte.setIdReporte(UUID.randomUUID().toString());
            reporte.setFecha(fecha);
            reporte.setDescripcion(desc_reporte);
            reporte.setAnonimo(anonimo);
            reporte.setUbicacion(ubic_reporte);
            reporte.setTipo(tipo_reporte);

            //Falta obtener la latitud de la ubicacion entregada
            reporte.setLatitud(-33.0503315);
            //Falta generar la longitud de la ubicacion entregada
            reporte.setLongitud(-71.3595593);
            //Falta obtener el id del usuario (rut)
            reporte.setIdUsuario("1");


            databaseReference.child("Reporte").child(reporte.getIdReporte()).setValue(reporte);
            Toast.makeText(this,"Agregado",Toast.LENGTH_LONG).show();
            Limpiar();
        }
    }

    private void Limpiar() {
        descripcion.setText("");
        ubicacion.setText("");
        descripcion.setText("");
        tipo.setText("");
        switchE.setChecked(false);
    }

    private void Validacion(String desc_reporte, String ubic_reporte, String tipo_reporte) {
        if(desc_reporte.equals("")) {
            descripcion.setError(getString(R.string.requerido));
        }
        if(ubic_reporte.equals("")) {
            ubicacion.setError(getString(R.string.requerido));
        }
        if(tipo_reporte.equals("")) {
            Toast.makeText(this,"Debe seleccionar un tipo de reporte",Toast.LENGTH_LONG).show();
        }
    }

    public void activarAnonimo(View view) {
        if(view.getId()==R.id.idAnonimo){
            if(switchE.isChecked()){
                anonimo=true;
            }else{
                anonimo=false;
            }
        }
    }
}