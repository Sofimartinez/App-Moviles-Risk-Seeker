package com.example.riskseeker;

public class Usuario {
    private String Nombre;
    private String Apellido;
    private String Rut;
    private String Correo;
    private String Contraseña;

    public Usuario(String nombre, String apellido, String rut, String correo, String contraseña) {
        Nombre = nombre;
        Apellido = apellido;
        Rut = rut;
        Correo = correo;
        Contraseña = contraseña;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getRut() {
        return Rut;
    }

    public void setRut(String rut) {
        Rut = rut;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }
}
