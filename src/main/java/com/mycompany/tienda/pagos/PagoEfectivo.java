/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.pagos;

import com.mycompany.tienda.modelo.interfaces.MetodoPago;

/**
 *
 * @author carod
 */
public class PagoEfectivo implements MetodoPago {

    private double efectivoRecibido;
    private double cambio;

    public PagoEfectivo(double efectivoRecibido) {
        this.efectivoRecibido = efectivoRecibido;
    }

    @Override
    public boolean validar() {
        if (efectivoRecibido < 1000) {
            System.out.println("No se aceptan pagos menores a $1000");  //Podría ser excepción
            return false;
        }
        return true;
    }

    @Override
    public double calcularMontoFinal(double monto) {
        return monto; // Sin recargos
    }

    @Override
    public String procesarPago(double monto) {
        if (!validar()) {
            return "Pago rechazado: monto mínimo no alcanzado";
        }

        if (efectivoRecibido < monto) {
            return "Pago rechazado: efectivo insuficiente. Falta: $" + (monto - efectivoRecibido);
        }

        this.cambio = efectivoRecibido - monto;
        return "Pago exitoso - Cambio: $" + String.format("%.2f", cambio);
    }

    @Override
    public String getTipo() {
        return "Efectivo";
    }

    public double getCambio() {
        return cambio;
    }
}
