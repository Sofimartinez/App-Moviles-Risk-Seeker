package com.example.riskseeker;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.riskseeker.databinding.ActivityMapsBinding;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private UiSettings mUiSettings;
    private Marker marcador;
    public LocationManager locationManager;
    DatabaseReference mDatabase;
    public String provider;
    double latitud;
    double longitud;
    private FloatingActionButton perfil, reporte;
    private FloatingActionsMenu menu_fab;
    private com.google.android.material.floatingactionbutton.FloatingActionButton cancelar;

    EditText address;
    ImageButton lupa;
    TextInputLayout Inputbusqueda;

    private static final String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Boolean isInvitado = getIntent().getExtras().getBoolean("invitado");
        perfil = findViewById(R.id.perfil);
        reporte = findViewById(R.id.reporte);

        address = (EditText) findViewById(R.id.busqueda);
        lupa = (ImageButton) findViewById(R.id.busquedaBoton);
        Inputbusqueda = (TextInputLayout) findViewById(R.id.busquedaInput);
        menu_fab = findViewById(R.id.menu_fab);
        cancelar = findViewById(R.id.cancelar);

        cancelar.setVisibility(View.INVISIBLE);

        if(isInvitado){
            perfil.setVisibility(View.INVISIBLE);
            reporte.setVisibility(View.INVISIBLE);
        }else{
            perfil.setVisibility(View.VISIBLE);
            reporte.setVisibility(View.VISIBLE);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        obtenerLocalizacion();
    }


    private void obtenerLocalizacion() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            actualizarUbicacion();
            }
        else{
            activarUbicacion();
        }
    }

    private void activarUbicacion(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.activar)
                .setMessage(R.string.mensaje_ubicacion)
                .setCancelable(false)

                //Si se selecciona la opción de activar la ubicación se ejecutara la ventana  para activarlo
                .setPositiveButton(R.string.boton_activar, (dialogInterface, i) -> {
                    Intent activarUbicacion = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(activarUbicacion);
                    finish();
                })

                //----------------------Falta determina que se hará sino se activa la ubicación----------------------------------//
                .setNegativeButton(R.string.boton_noactivar, (dialogInterface, i) -> finish())
                .show();

    }
    @SuppressLint("MissingPermission")
    protected void actualizarUbicacion(){
        //Criterio de aplicación para seleccionar un proveedor de ubicación
        Criteria criterio = new Criteria();
        provider = String.valueOf(locationManager.getBestProvider(criterio, true));

        Location localizacion = locationManager.getLastKnownLocation(provider);
        if (localizacion != null) {
            latitud = localizacion.getLatitude();
            longitud = localizacion.getLongitude();
        }
        else {
            locationManager.requestLocationUpdates(provider, 1000, 0, this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(Location location) {

        //remove location callback:
        locationManager.removeUpdates(this);

        //open the map:
        latitud = location.getLatitude();
        longitud = location.getLongitude();
        onMapReady(map);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng ubicacion = new LatLng(latitud, longitud);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        marcador = googleMap.addMarker(new MarkerOptions()
                .position(ubicacion)
                .title("Tú")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

        googleMap.setOnMarkerClickListener(this);
        map.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
        map.moveCamera(CameraUpdateFactory.zoomTo(15));

        mUiSettings = map.getUiSettings();
        mUiSettings.setMapToolbarEnabled(false);

        //Cargar heatmap
        heatMap(map);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if(!marker.equals(marcador)) {
            String idReporte = String.valueOf(marker.getTag());
            mDatabase.child("Reporte").child(idReporte).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Intent cargar_reportes = new Intent(getApplicationContext(), ReporteActivity.class);
                        cargar_reportes.putExtra("idReporte", snapshot.child("idReporte").getValue().toString());
                        cargar_reportes.putExtra("latitud", (Double) snapshot.child("latitud").getValue());
                        cargar_reportes.putExtra("longitud", (Double) snapshot.child("longitud").getValue());
                        startActivity(cargar_reportes);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Error al leer los datos", error.toException());
                }
            });
        }
        return false;
    }

    public void CargarFormulario(View view) {
        Intent cargarMapa = new Intent(getApplicationContext(),FormularioReporteActivity.class);
        startActivity(cargarMapa);
    }

    public void CargarPerfil(View view) {
        Intent cargarPerfil = new Intent(getApplicationContext(),PerfilActivity.class);
        startActivity(cargarPerfil);
    }

    public void activarBuscar(View view) {
        if(address.getVisibility() == View.GONE){
            address.setVisibility(View.VISIBLE);
            lupa.setVisibility(View.VISIBLE);
            Inputbusqueda.setVisibility(View.VISIBLE);
            map.clear();
            menu_fab.collapse();
            menu_fab.setVisibility(View.GONE);
            cancelar.setVisibility(View.VISIBLE);
        }
        else {
            address.setVisibility(View.GONE);
            lupa.setVisibility(View.GONE);
            Inputbusqueda.setVisibility(View.GONE);
            onMapReady(map);
            menu_fab.setVisibility(View.VISIBLE);
            cancelar.setVisibility(View.GONE);
        }

    }

    //Heatmap
    private void heatMap(GoogleMap googleMap) {
        map = googleMap;
        
        List<LatLng> reports = new ArrayList<>();

        String TAG = "readData";
        // Referencia a reportes
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("Reporte");
        // TODO: falta agregar un geohash para traer de firebase solo los reportes mas cercanos a x radio https://firebaseopensource.com/projects/firebase/geofire-android/
        // Por ahora la query trae todos los reportes
        // Leer de firebase
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Obtiene datos y se actualiza 
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    String type = ds.child("tipo").getValue(String.class);
                    Double lat = ds.child("latitud").getValue(Double.class);
                    Double lon = ds.child("longitud").getValue(Double.class);
                    String idReporte = ds.child("idReporte").getValue(String.class);

                    reports.add(new LatLng(lat, lon));

                    //Añadir Marcadores
                    switch (type) {
                        case "Hurto":
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title(type)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                                    .setTag(idReporte);
                            break;
                        case "Actividad sospechosa":
                            map.addMarker( new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title(type)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                                    .setTag(idReporte);

                            break;
                        case "Asalto":
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title(type)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                                    .setTag(idReporte);
                            break;
                        case "Acoso":
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title(type)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)))
                                    .setTag(idReporte);
                            break;
                        case "Secuestro":
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title(type)
                                    .snippet("and snippet")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                                    .setTag(idReporte);

                            break;
                        case "Tráfico de drogas":
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title(type)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
                                    .setTag(idReporte);
                            break;
                        case "Tráfico de armas":
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title(type)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                                    .setTag(idReporte);
                            break;
                        case "Disturbios":
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat, lon))
                                    .title(type)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                                    .setTag(idReporte);
                            break;
                    }
                }

                if(!reports.isEmpty()){
                    // Crea el heatmap
                    HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                            .data(reports)
                            .radius(30)
                            .build();
                    // Añadir heatmap overlay al mapa
                    TileOverlay overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Error
                Log.w(TAG, "Error al leer los datos", error.toException());
            }
        });
    }
    public void buscarCalle(View view) {
        // Se usa para cerrar el teclado.
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        EditText direccionEditText = (EditText) findViewById(R.id.busqueda);
        String direccion = direccionEditText.getText().toString();

        List<Address> direccionList = null;
        MarkerOptions opcionesDeDirecciones = new MarkerOptions();

        if (!TextUtils.isEmpty(direccion)){
            Geocoder geocoder = new Geocoder(this);
            try {
                direccionList = geocoder.getFromLocationName(direccion,6);

                if (direccionList != null){
                    for (int i=0; i< direccionList.size();i++){
                        Address direccionUsuario = direccionList.get(i);
                        LatLng latLng = new LatLng(direccionUsuario.getLatitude(), direccionUsuario.getLongitude());

                        opcionesDeDirecciones.position(latLng);
                        opcionesDeDirecciones.title(direccion);
                        opcionesDeDirecciones.icon(BitmapDescriptorFactory.
                                defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                        map.addMarker(opcionesDeDirecciones);
                        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        map.animateCamera(CameraUpdateFactory.zoomTo(15));

                        heatMap(map);

                        // Se usa para cerrar el teclado.
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    }
                } else
                {
                    Toast.makeText(this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(this, "Escriba una dirección", Toast.LENGTH_SHORT).show();

        }
    }
}