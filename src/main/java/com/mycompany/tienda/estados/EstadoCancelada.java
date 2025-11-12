/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.estados;

import com.mycompany.tienda.modelo.interfaces.EstadoVenta;

/**
 *
 * @author Hacking_man
 */
public class EstadoCancelada implements EstadoVenta {

    @Override
    public boolean puedeAgregarProductos() {
        return false;
    }

    @Override
    public boolean puedeConfirmarPago() {
        return false;
    }

    @Override
    public boolean puedeCancelar() {
        return false;
    }

    @Override
    public boolean puedeEntregar() {
        return false;
    }

    @Override
    public String getNombre() {
        return "CANCELADA";
    }

    @Override
    public EstadoVenta transicionar(String accion) {
        return this;
    }
}
