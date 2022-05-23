package com.example.riskseeker;

public class Reporte {

    private String nombre;
    private String fecha;
    private String inf_reporte;
    private int foto_perfil;
    private String tipo;
    private String fotos;

    public void reporte(){
    }

    public Reporte(String nombre, String fecha, String inf_reporte, String fotos, int foto_perfil, String tipo) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.inf_reporte = inf_reporte;
        this.fotos = fotos;
        this.foto_perfil = foto_perfil;
        this.tipo = tipo;
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
        return fotos;
    }

    public void setFotos(String fotos) {
        this.fotos = fotos;
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
}
