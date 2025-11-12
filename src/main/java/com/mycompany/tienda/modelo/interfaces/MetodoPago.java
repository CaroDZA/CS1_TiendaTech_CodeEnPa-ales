/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.tienda.modelo.interfaces;

/**
 *
 * @author carod
 */
public interface MetodoPago {

    boolean validar();

    double calcularMontoFinal(double monto);

    String procesarPago(double monto);

    String getTipo();
}
