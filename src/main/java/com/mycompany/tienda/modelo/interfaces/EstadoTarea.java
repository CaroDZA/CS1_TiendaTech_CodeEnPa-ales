/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.tienda.modelo.interfaces;

/**
 *
 * @author carod
 */
public interface EstadoTarea {

    boolean puedeAsignarTecnico();

    boolean puedeIniciar();

    boolean puedeCompletar();

    boolean puedeCancelar();

    String getNombre();

    EstadoTarea transicionar(String accion);
}
