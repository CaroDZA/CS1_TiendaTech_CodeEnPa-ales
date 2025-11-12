/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.estados;

import com.mycompany.tienda.modelo.interfaces.EstadoTarea;

/**
 *
 * @author carod
 */
public class TareaCancelada implements EstadoTarea {

    @Override
    public boolean puedeAsignarTecnico() {
        return false;
    }

    @Override
    public boolean puedeIniciar() {
        return false;
    }

    @Override
    public boolean puedeCompletar() {
        return false;
    }

    @Override
    public boolean puedeCancelar() {
        return false;
    }

    @Override
    public String getNombre() {
        return "CANCELADA";
    }

    @Override
    public EstadoTarea transicionar(String accion) {

        return this;
    }
}
