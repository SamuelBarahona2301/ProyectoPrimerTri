package com.example.eventhub;

public class Usuario {
    public static String nombre, apellidos, telefono, rol, fechaNac, nickname;

    public Usuario(String nombre, String apellidos, String telefono, String rol, String fechaNac, String nickname) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.rol = rol;
        this.fechaNac = fechaNac;
        this.nickname = nickname;
    }
}
