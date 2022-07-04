package com.example.riskseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.riskseeker.models.InfoReporte;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FormularioReporteActivity extends AppCompatActivity implements OnMapReadyCallback {


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
    private int contadorImg = 0;

    private Button openMap, closeMap, refreshMap;
    private View imagens;
    private MapView mapView;

    private GoogleMap map;

    double confirmLatitud = 0;
    double confirmLongitud = 0;

    public LocationManager locationManager;
    public String provider;

    private static final String[] lista_tipo = new String[]{"Hurto", "Actividad sospechosa", "Asalto", "Acoso", "Secuestro", "Tráfico de drogas", "Tráfico de armas", "Disturbios"};

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

        //confirmar mapa
        mapView = findViewById(R.id.confirmationMap);
        openMap = findViewById(R.id.idConfirmMap);
        closeMap = findViewById(R.id.idCerMap);
        refreshMap = findViewById(R.id.refresh);


        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        //Cambiar acción en reporte
        ubicacion.setOnKeyListener(new  View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int id, KeyEvent event) {
                if (id == KeyEvent.KEYCODE_ENTER) {
                    openMap.callOnClick();
                    return true;
                }
                return false;
            }
        });


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
        imagens = findViewById(R.id.imagesLayout);
        if(contadorImg != 0){
            imagens.setVisibility(View.VISIBLE);
            imagens.animate()
                    .alpha(1.0f)
                    .setDuration(600)
                    .setListener(null);
        }
        if (contadorImg < 5) {
            Intent selec_imagen = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            selec_imagen.setType("image/");
            startActivityForResult(selec_imagen.createChooser(selec_imagen, "Seleccione una aplicación"), SELECCIONAR_IMAGEN);
        } else {
            Toast.makeText(this, "Se ha alcanzado el máximo de imagenes", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int resquestCode, int resultCode, Intent data) {
        super.onActivityResult(resquestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imagens = findViewById(R.id.imagesLayout);
            imagens.setVisibility(View.VISIBLE);
            imagenUri = data.getData();
            listaimagenes.add(imagenUri);
            //Agregar primera imagen por defecto al ImageSwitcher
            botonSig.setVisibility(View.VISIBLE);
            botonAnt.setVisibility(View.VISIBLE);
            imagenIS.setImageURI(listaimagenes.get(0));
            contadorImg++;
            posicion = 0;
        }

    }

    private void confImageSwitcher() {
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


        if (desc_reporte.equals("") || ubic_reporte.equals("") || tipo_reporte.equals("")) {
            Validacion(desc_reporte, ubic_reporte, tipo_reporte);
        } else {
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

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            reporte.setIdUsuario(user.getUid());

            String TAG = "GeolocateReg";
            Geocoder geocoder = new Geocoder(this);
            List<Address> addressList;
            try {
                addressList = geocoder.getFromLocationName(ubicacion.getText().toString(), 1);

                if (!addressList.isEmpty()) {
                    double doublelat = addressList.get(0).getLatitude();
                    double doublelong = addressList.get(0).getLongitude();

                    reporte.setLatitud(doublelat);
                    reporte.setLongitud(doublelong);

                    Log.w(TAG, "\n \n \n Value Reached" + doublelat + doublelong);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            databaseReference.child("Reporte").child(reporte.getIdReporte()).setValue(reporte);

            if (contadorImg > 0) {
                subirFoto(listaimagenes.size(), reporte.getIdReporte());
            }
            Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
            Limpiar();
        }
    }



    private void subirFoto(int posicionImg, String nombreid) {
        StorageReference path = mStorage.child("Images" + nombreid + posicionImg);
        UploadTask uploadTask = path.putFile(listaimagenes.get(posicionImg - 1));
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return path.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String urlFoto = downloadUri.toString();
                    databaseReference.child("ImageUrl").child("Images" + nombreid + posicionImg).setValue(urlFoto);
                }
            }
        });
        if (posicionImg - 1 > 0) {
            subirFoto(posicionImg - 1, nombreid);
        }
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
        imagenIS.setVisibility(View.INVISIBLE);
    }

    private void Validacion(String desc_reporte, String ubic_reporte, String tipo_reporte) {
        if (desc_reporte.equals("")) {
            descripcion.setError(getString(R.string.requerido));
        }
        if (ubic_reporte.equals("")) {
            ubicacion.setError(getString(R.string.requerido));
        }
        if (tipo_reporte.equals("")) {
            Toast.makeText(this, "Debe seleccionar un tipo de reporte", Toast.LENGTH_LONG).show();
        }
    }

    public void activarAnonimo(View view) {
        if (view.getId() == R.id.idAnonimo) {
            if (switchE.isChecked()) {
                anonimo = true;
            } else {
                anonimo = false;
            }
        }
    }

    public void BotonSiguiente(View view) {
        if (posicion < listaimagenes.size() - 1) {
            posicion++;
            imagenIS.setImageURI(listaimagenes.get(posicion));
        }
    }

    public void BotonAnterior(View view) {
        if (posicion > 0) {
            posicion--;
            imagenIS.setImageURI(listaimagenes.get(posicion));
        }
    }

    //--------MAP-------------------------------------------------------------
    public void confirmarDirec(View view){
        final int animationDuration = 600;

        openMap.animate()
                .alpha(0.0f)
                .setDuration(animationDuration)
                .setListener(null);

        openMap.setVisibility(View.GONE);

        mapView.setVisibility(View.VISIBLE);
        closeMap.setVisibility(View.VISIBLE);
        refreshMap.setVisibility(View.VISIBLE);

        mapView.setAlpha(0.0f);
        closeMap.setAlpha(0.0f);
        refreshMap.setAlpha(0.0f);

        mapView.animate()
                .alpha(1.0f)
                .setDuration(animationDuration)
                .setListener(null);
        closeMap.animate()
                .alpha(1.0f)
                .setDuration(animationDuration)
                .setListener(null);
        refreshMap.animate()
                .alpha(1.0f)
                .setDuration(animationDuration)
                .setListener(null);

        buscarUbicacion();
    }

    public void CerrarMap(View view){
        final int animationDuration = 600;

        openMap.setVisibility(View.VISIBLE);
        openMap.animate()
                .alpha(1.0f)
                .setDuration(animationDuration)
                .setListener(null);

        mapView.animate()
                .alpha(1.0f)
                .setDuration(animationDuration)
                .setListener(null);

        closeMap.animate()
                .alpha(1.0f)
                .setDuration(animationDuration)
                .setListener(null);

        refreshMap.animate()
                .alpha(1.0f)
                .setDuration(animationDuration)
                .setListener(null);

        mapView.setVisibility(View.GONE);
        closeMap.setVisibility(View.GONE);
        refreshMap.setVisibility(View.GONE);
    }

    public  void RefreshMap(View view){
        openMap.callOnClick();
    }

    public void buscarUbicacion(){

        String direccion = ubicacion.getText().toString();

        if (!TextUtils.isEmpty(direccion)) {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addressList;
            try {
                addressList = geocoder.getFromLocationName(ubicacion.getText().toString(), 6);
                if (!addressList.isEmpty()) {

                    confirmLatitud = addressList.get(0).getLatitude();
                    confirmLongitud = addressList.get(0).getLongitude();

                } else {
                    Toast.makeText(this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "Escriba una Dirección", Toast.LENGTH_SHORT).show();
            confirmLatitud = 0;
            confirmLongitud = 0;
        }
        onMapReady(map);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.clear();

        if (confirmLongitud == 0 && confirmLatitud == 0){
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            Criteria criterio = new Criteria();
            provider = String.valueOf(locationManager.getBestProvider(criterio, true));

            @SuppressLint("MissingPermission")
            Location localizacion = locationManager.getLastKnownLocation(provider);
            if (localizacion != null) {
                confirmLatitud = localizacion.getLatitude();
                confirmLongitud = localizacion.getLongitude();
            }
        }
        map.addMarker(new MarkerOptions()
                    .position(new LatLng(confirmLatitud, confirmLongitud))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(confirmLatitud, confirmLongitud)));
        map.moveCamera(CameraUpdateFactory.zoomTo(14));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}