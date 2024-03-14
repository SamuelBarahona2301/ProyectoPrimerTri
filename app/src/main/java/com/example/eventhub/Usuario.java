package com.example.eventhub;

public class Usuario {
    public static String userId, nombre, apellidos, fechaNac;

    public Usuario() {
    }

    public Usuario(String id, String nombre, String apellidos, String fechaNac) {
        this.nombre = nombre;
        this.userId = id;
        this.apellidos = apellidos;
        this.fechaNac = fechaNac;
    }

    public static String getId() {
        return userId;
    }

    public static void setId(String id) {
        Usuario.userId = id;
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

    public static String getFechaNac() {
        return fechaNac;
    }

    public static void setFechaNac(String fechaNac) {
        Usuario.fechaNac = fechaNac;
    }
}
