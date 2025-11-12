/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda;

import java.util.ArrayList;

/**
 *
 * @author carod
 */
public class Tecnico extends Empleado {

    private boolean disponible;
    private ServiciosDigitales servicioActual;
    private ArrayList<String> especialidades;

    public Tecnico(String id, String nombre, String usuario, String contraseña) {
        super(id, nombre, usuario, contraseña);
        this.disponible = true;
        this.servicioActual = null;
        this.especialidades = new ArrayList<>();
    }

    @Override
    public boolean tienePermiso(String accion) {
        return accion.equals("RealizarServicio")
                || accion.equals("Instalacion")
                || accion.equals("VerServicios");
    }

    public void asignarServicio(ServiciosDigitales servicio) {
        if (!disponible) {
            throw new IllegalStateException("El técnico no está disponible");
        }
        this.disponible = false;
        this.servicioActual = servicio;
        System.out.println("Servicio asignado a técnico: " + nombre);
    }

    public void completarServicio() {
        if (servicioActual == null) {
            throw new IllegalStateException("El técnico no tiene servicios asignados");
        }
        System.out.println("Servicio completado por: " + nombre);
        this.disponible = true;
        this.servicioActual = null;
    }

    // Getters
    public boolean estaDisponible() {
        return disponible;
    }

    public ServiciosDigitales getServicioActual() {
        return servicioActual;
    }

    @Override
    public String toString() {
        String estado = disponible ? "Disponible" : "Ocupado";
        return "Técnico - " + super.toString() + " - " + estado;
    }
}
