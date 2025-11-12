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
public class TareaPendiente implements EstadoTarea {

    @Override
    public boolean puedeAsignarTecnico() {
        return true;
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
        return true;
    }

    @Override
    public String getNombre() {
        return "PENDIENTE";
    }

    @Override
    public EstadoTarea transicionar(String accion) {
        switch (accion) {
            case "ASIGNAR_TECNICO":
                return new TareaEnProceso();
            case "CANCELAR":
                return new TareaCancelada();
            default:
                return this;
        }
    }
}
