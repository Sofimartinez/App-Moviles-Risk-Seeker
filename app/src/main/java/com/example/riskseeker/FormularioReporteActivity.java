package com.example.riskseeker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class FormularioReporteActivity extends AppCompatActivity {

    private int SELECCIONAR_IMAGEN = 1;

    private EditText ubicacion, descripcion;
    private AutoCompleteTextView tipo;
    private Switch switchE;
    private boolean anonimo;
    private ImageButton botonAnt, botonSig;
    private ImageSwitcher imagenIS;
    private ArrayList<Uri> listaimagenes;
    private Uri imagenUri;
    private int posicion;
    private int contadorImg=0;

    private static final String[] lista_tipo = new String[]{"Tipo 1", "Tipo 2", "Tipo 3", "Tipo 4", "Tipo 5", "Tipo 6", "Tipo 7", "Tipo 8"};

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_reporte);

        inicializarFirebase();

        ubicacion = findViewById(R.id.idUbicacion);
        descripcion = findViewById(R.id.idDescripcion);
        tipo = findViewById(R.id.idSelec_tipo);
        switchE = findViewById(R.id.idAnonimo);

        //Imagenes
        botonSig = findViewById(R.id.boton_siguiente);
        botonAnt = findViewById(R.id.boton_anterior);
        imagenIS = findViewById(R.id.imagenes);

        //Ocultar botones mientras no se haya seleccionado una imagen
        botonSig.setVisibility(View.INVISIBLE);
        botonAnt.setVisibility(View.INVISIBLE);

        listaimagenes = new ArrayList<Uri>();

        confImageSwitcher();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista_tipo);
        tipo.setAdapter(adapter);

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    public void CargarImagenes(View view) {
        if(contadorImg < 5){
            Intent selec_imagen = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            selec_imagen.setType("image/");
            startActivityForResult(selec_imagen.createChooser(selec_imagen,"Seleccione una aplicación"),SELECCIONAR_IMAGEN);
        }else{
            Toast.makeText(this,"Se ha alcanzado el máximo de imagenes",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int resquestCode, int resultCode, Intent data) {
        super.onActivityResult(resquestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imagenUri = data.getData();
            listaimagenes.add(imagenUri);
        }
        //Agregar primera imagen por defecto al ImageSwitcher
        botonSig.setVisibility(View.VISIBLE);
        botonAnt.setVisibility(View.VISIBLE);
        imagenIS.setImageURI(listaimagenes.get(0));
        contadorImg ++;
        posicion = 0;
    }

    private void confImageSwitcher(){
        imagenIS.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(getApplicationContext());
            }
        });
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
            reporte.setCantidadImg(contadorImg);

            //Falta obtener la latitud de la ubicacion entregada
            reporte.setLatitud(-33.0503315);
            //Falta generar la longitud de la ubicacion entregada
            reporte.setLongitud(-71.3595593);
            //Falta obtener el id del usuario (rut)
            reporte.setIdUsuario("1");

            databaseReference.child("Reporte").child(reporte.getIdReporte()).setValue(reporte);

            if(contadorImg > 0){
                subirFoto(listaimagenes.size());
            }
            Toast.makeText(this,"Agregado",Toast.LENGTH_LONG).show();
            Limpiar();
        }
    }

    private void subirFoto(int posicionImg){
        StorageReference path = mStorage.child("Images"+listaimagenes.get(posicionImg-1).getLastPathSegment());
        path.putFile(listaimagenes.get(posicionImg-1)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                subirFoto(posicionImg-1);
            }
        });

    }

    private void Limpiar() {
        descripcion.setText("");
        ubicacion.setText("");
        descripcion.setText("");
        tipo.setText("");
        switchE.setChecked(false);
        listaimagenes.clear();
        botonSig.setVisibility(View.INVISIBLE);
        botonAnt.setVisibility(View.INVISIBLE);
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

    public void BotonSiguiente(View view) {
        if(posicion < listaimagenes.size()-1){
            posicion++;
            imagenIS.setImageURI(listaimagenes.get(posicion));
        }
    }

    public void BotonAnterior(View view) {
        if(posicion > 0){
            posicion--;
            imagenIS.setImageURI(listaimagenes.get(posicion));
        }
    }
}