/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda;

/**
 *
 * @author Hacking_man
 */
public class ServiciosDigitales extends ProductoBase {

    private int duracionEstimadaMinutos;
    private String descripcion;
    private boolean requiereTecnico;

    public ServiciosDigitales(String nombre, double precio, String categoria, int duracionEstimadaMinutos, String descripcion) {
        super(nombre, precio, categoria);

        if (duracionEstimadaMinutos <= 0) {  //¿Podemos hacer conversión a minutos? 
            throw new IllegalArgumentException("La duración debe ser mayor a 0 minutos");
        }

        this.duracionEstimadaMinutos = duracionEstimadaMinutos;
        this.descripcion = descripcion;
        this.requiereTecnico = true; // Siempre es requerido
    }

    @Override
    public String obtenerInformacion() {
        return nombre + " - Duración: " + duracionEstimadaMinutos + " min - " + descripcion;
    }

    @Override
    public double calcularPrecioFinal() {
        // No aplican dto, se retorna el calor del precio normal.
        return getPrecio();
    }

    // Getters
    public int getDuracionEstimadaMinutos() {
        return duracionEstimadaMinutos;
    }

    public double getPrecioConDescuento() {
        return getPrecio(); //No tiene descuentos
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean getRequiereTecnico() {
        return requiereTecnico;
    }
}
