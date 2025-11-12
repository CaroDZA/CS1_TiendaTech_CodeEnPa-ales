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
public class PagoTarjetaDebito implements MetodoPago {

    private String numeroTarjeta;

    public PagoTarjetaDebito(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    @Override
    public boolean validar() {
        if (numeroTarjeta.length() != 16) {
            System.out.println("Número de tarjeta inválido (debe tener 16 dígitos)");
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
            return "Pago rechazado: tarjeta inválida";
        }

        if (monto < 1000) {
            return "Pago rechazado: monto mínimo $1000";
        }

        String tarjetaOculta = "****-****-****-" + numeroTarjeta.substring(12);

        return "Pago exitoso con Tarjeta Débito " + tarjetaOculta
                + " Total: $" + String.format("%.2f", monto);
    }

    @Override
    public String getTipo() {
        return "Tarjeta Debito";
    }
}
