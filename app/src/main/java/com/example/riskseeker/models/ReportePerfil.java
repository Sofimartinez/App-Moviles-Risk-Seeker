package com.example.riskseeker.models;

import java.util.ArrayList;

public class ReportePerfil {
    private String fecha;
    private String inf_reporte;
    private String tipo;
    private String cantidadImg;
    private String idreporte;
    private String anonimo;
    private String ubicacion;
    private ArrayList<String> listaImagenes;

    public ReportePerfil(String fecha, String inf_reporte, String cantidadImg, String tipo, String idreporte, String anonimo, String ubicacion, ArrayList<String> listaImagenes) {
        this.fecha = fecha;
        this.inf_reporte = inf_reporte;
        this.cantidadImg = cantidadImg;
        this.tipo = tipo;
        this.idreporte = idreporte;
        this.anonimo = anonimo;
        this.ubicacion = ubicacion;
        this.listaImagenes = listaImagenes;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getInf_reporte() {
        return inf_reporte;
    }

    public void setInf_reporte(String inf_reporte) {
        this.inf_reporte = inf_reporte;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCantidadImg() {
        return cantidadImg;
    }

    public void setCantidadImg(String cantidadImg) {
        this.cantidadImg = cantidadImg;
    }

    public String getIdreporte() {
        return idreporte;
    }

    public void setIdreporte(String idreporte) {
        this.idreporte = idreporte;
    }

    public String getAnonimo() {
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

}
