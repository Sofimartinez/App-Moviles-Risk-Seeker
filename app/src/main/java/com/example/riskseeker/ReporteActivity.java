package com.example.riskseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ReporteActivity extends AppCompatActivity {

    ArrayList<Reporte> listaReportes;
    RecyclerView recyclerReportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        listaReportes = new ArrayList<>();
        recyclerReportes = findViewById(R.id.reportes);

        //Lista de tipo vertical
        recyclerReportes.setLayoutManager(new LinearLayoutManager(this));

        CargarReportes();
        //Se crea una instancia del adaptador
        AdaptadorReportes adapter = new AdaptadorReportes(listaReportes);
        recyclerReportes.setAdapter(adapter);
    }

    private void CargarReportes() {
        listaReportes.add(new Reporte("Nombre usuario 1","2-marzo-2022", "Reporte 1 bla bla bla", "foto aqui",R.mipmap.ic_launcher,"Tipo de reporte 1"));
        listaReportes.add(new Reporte("Nombre usuario 2","3-mayo-2022", "Reporte 2 bla bla bla", "foto aqui",R.mipmap.ic_launcher,"Tipo de reporte 2"));
        listaReportes.add(new Reporte("Nombre usuario 3","24-enero-2020", "Reporte 3 bla bla bla", "foto aqui",R.mipmap.ic_launcher,"Tipo de reporte 3"));
        listaReportes.add(new Reporte("Nombre usuario 4","9-febrero-2019", "Reporte 4 bla bla bla", "foto aqui",R.mipmap.ic_launcher,"Tipo de reporte 4"));
        listaReportes.add(new Reporte("Nombre usuario 5","31-abril-2022", "Reporte 5 bla bla bla", "foto aqui",R.mipmap.ic_launcher,"Tipo de reporte 5"));
        listaReportes.add(new Reporte("Nombre usuario 6","18-julio-2021", "Reporte 6 bla bla bla", "foto aqui",R.mipmap.ic_launcher,"Tipo de reporte 6"));

    }
}