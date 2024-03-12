package com.example.eventhub;

public class Usuario {
    public static String id, nombre, apellidos, rol, fechaNac;

    public Usuario() {
    }

    public Usuario(String id, String nombre, String apellidos, String rol, String fechaNac) {
        this.nombre = nombre;
        this.id = id;
        this.apellidos = apellidos;
        this.rol = rol;
        this.fechaNac = fechaNac;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Usuario.id = id;
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        Usuario.nombre = nombre;
    }

    public static String getApellidos() {
        return apellidos;
    }

    public static void setApellidos(String apellidos) {
        Usuario.apellidos = apellidos;
    }

    public static String getRol() {
        return rol;
    }

    public static void setRol(String rol) {
        Usuario.rol = rol;
    }

    public static String getFechaNac() {
        return fechaNac;
    }

    public static void setFechaNac(String fechaNac) {
        Usuario.fechaNac = fechaNac;
    }
}
