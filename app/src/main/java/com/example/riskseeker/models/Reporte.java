package com.example.riskseeker.models;

import java.util.ArrayList;

public class Reporte {


    private String nombre;
    private String fecha;
    private String inf_reporte;
    private int foto_perfil;
    private String tipo;
    private String cantidadImg;
    private String idreporte;
    private String anonimo;
    private ArrayList<String> listaImagenes;

    public Reporte(String nombre, String fecha, String inf_reporte,String cantidadImg, int foto_perfil, String tipo,String idreporte, String anonimo, ArrayList<String> listaImagenes) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.inf_reporte = inf_reporte;
        this.cantidadImg = cantidadImg;
        this.foto_perfil = foto_perfil;
        this.tipo = tipo;
        this.idreporte = idreporte;
        this.anonimo = anonimo;
        this.listaImagenes = listaImagenes;
    }

    public String getInf_reporte() {
        return inf_reporte;
    }

    public void setInf_reporte(String inf_reporte) {
        this.inf_reporte = inf_reporte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFotos() {
        return cantidadImg;
    }

    public void setFotos(String cantidadImg) {
        this.cantidadImg= cantidadImg;
    }

    public int getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(int foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setIdreporte(String uid) {
        this.idreporte = idreporte;
    }

    public String getIdreporte() {return  idreporte;}

    public String isAnonimo() {
        return anonimo;
    }

    public void setAnonimo(String anonimo) {
        this.anonimo = anonimo;
    }

    public ArrayList<String> getListaImagenes() {
        return listaImagenes;
    }

    public void setListaImagenes(String nombreImagen) {
        listaImagenes.add(nombreImagen);
    }
}
