/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.tienda.modelo.interfaces;

/**
 *
 * @author Hacking_man
 */
public interface EstadoVenta {

    boolean puedeAgregarProductos();

    boolean puedeConfirmarPago();

    boolean puedeCancelar();

    boolean puedeEntregar();

    String getNombre();

    EstadoVenta transicionar(String accion);
}