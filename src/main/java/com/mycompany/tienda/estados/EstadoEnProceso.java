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
public class EstadoEnProceso implements EstadoVenta {

    @Override
    public boolean puedeAgregarProductos() {
        return true;
    }

    @Override
    public boolean puedeConfirmarPago() {
        return true;
    }

    @Override
    public boolean puedeCancelar() {
        return true;
    }

    @Override
    public boolean puedeEntregar() {
        return false;
    }

    @Override
    public String getNombre() {
        return "EN PROCESO";
    }

    @Override
    public EstadoVenta transicionar(String accion) {
        if (accion.equals("PAGAR")) {
            return new EstadoPagada();
        }
        if (accion.equals("CANCELAR")) {
            return new EstadoCancelada();
        }
        return this;
    }
}
