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
public class TareaEnProceso implements EstadoTarea {

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
        return true;
    }

    @Override
    public boolean puedeCancelar() {
        return true;
    }

    @Override
    public String getNombre() {
        return "EN PROCESO";
    }

    @Override
    public EstadoTarea transicionar(String accion) {
        switch (accion) {
            case "COMPLETAR":
                return new TareaCompletada();
            case "CANCELAR":
                return new TareaCancelada();
            default:
                return this;
        }
    }
}
