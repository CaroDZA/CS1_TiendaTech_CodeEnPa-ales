/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda;

/**
 *
 * @author carod
 */
public abstract class Empleado {

    protected String id;
    protected String nombre;
    protected String usuario;
    protected String contrasena;
    protected boolean activo;

    public Empleado(String id, String nombre, String usuario, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasena = contraseña;
        this.activo = true;
    }

    public abstract boolean tienePermiso(String accion);

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContraseña() {
        return contrasena;
    }

    public boolean esActivo() {
        return activo;
    }

    // Setters
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " (" + usuario + ")";
    }
}
