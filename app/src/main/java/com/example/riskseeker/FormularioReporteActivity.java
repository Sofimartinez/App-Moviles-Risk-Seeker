package com.example.riskseeker;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class FormularioReporteActivity extends AppCompatActivity {
    public final static String LOGTAG = "Pruebas imagenes";

    EditText ubicacion, descripcion;
    AutoCompleteTextView tipo;
    ImageView imagen;
    boolean anonimo;
    Switch switchE;
    boolean imagenCargada = false;
    private ArrayList<Uri> imageness;
    Uri image;
    ActivityResultLauncher<String> mTakePhoto;


    private static final String[] lista_tipo = new String[]{"Tipo 1","Tipo 2","Tipo 3","Tipo 4","Tipo 5","Tipo 6","Tipo 7","Tipo 8"};

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_reporte);

        ubicacion = findViewById(R.id.idUbicacion);
        descripcion = findViewById(R.id.idDescripcion);
        tipo = findViewById(R.id.idSelec_tipo);
        switchE = findViewById(R.id.idAnonimo);
        imageness = new ArrayList<Uri>();

        mTakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        image = result;
                        imageness.add(image);
                        imagen.setImageURI(result);
                        imagenCargada = true;

                    }
                }
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista_tipo);
        tipo.setAdapter(adapter);

        inicializarFirebase();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    public void CargarImagenes(View view) {
        Intent cargar = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cargar.setType("image/");
        String resID =  getResources().getResourceEntryName(view.getId());

        // No encontre manera para hacer el acceso dinamico.
        if(resID.equals("idAgregarImagen1")){
            imagen = (ImageView) findViewById(R.id.Imageid1);
        }else if(resID.equals("idAgregarImagen2")){
            imagen = (ImageView) findViewById(R.id.Imageid2);
        }else if (resID.equals("idAgregarImagen3")){
            imagen = (ImageView) findViewById(R.id.Imageid3);
        }else if (resID.equals("idAgregarImagen4")){
            imagen = (ImageView) findViewById(R.id.Imageid4);
        }else{
            imagen = (ImageView) findViewById(R.id.Imageid5);
        }
        mTakePhoto.launch("image/");

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
            if(imagenCargada){
                subirFoto(imageness.size());
            }
            Toast.makeText(this,"Agregado",Toast.LENGTH_LONG).show();
            Limpiar();
        }
    }

    private void subirFoto(int i){
        if(i>0){
            StorageReference path = mStorage.child("Images"+imageness.get(i-1).getLastPathSegment());
            path.putFile(imageness.get(i-1)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    subirFoto(i-1);
                }
            });
        }

    }

    private void Limpiar() {
        descripcion.setText("");
        ubicacion.setText("");
        descripcion.setText("");
        tipo.setText("");
        switchE.setChecked(false);
        imagenCargada = false;
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