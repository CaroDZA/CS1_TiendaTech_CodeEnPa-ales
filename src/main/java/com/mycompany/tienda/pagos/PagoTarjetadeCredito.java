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
public class PagoTarjetadeCredito implements MetodoPago {

    private String numeroTarjeta;
    private int numeroCuotas;
    private double RecargoCuota = 0.05; // 5%

    public PagoTarjetadeCredito(String numeroTarjeta, int numeroCuotas) {
        this.numeroTarjeta = numeroTarjeta;
        this.numeroCuotas = numeroCuotas;
    }

    @Override
    public boolean validar() {
        // Validar que tenga 16 dígitos
        if (numeroTarjeta.length() != 16) {
            System.out.println("Numero de tarjeta invalido (debe tener 16 digitos)");
            return false;
        }

        // Validar que solo sean números
        try {
            Long.valueOf(numeroTarjeta);
        } catch (NumberFormatException e) {
            System.out.println("Numero de tarjeta contiene caracteres no numericos");
            return false;
        }

        if (numeroCuotas < 1) {
            System.out.println("Numero de cuotas invalido");
            return false;
        }

        return true;
    }

    @Override
    public double calcularMontoFinal(double monto) {
        if (numeroCuotas == 1) {
            return monto; // Sin recargo si es en 1 cuota
        }
        double recargo = monto * RecargoCuota * (numeroCuotas - 1);
        return monto + recargo;
    }

    @Override
    public String procesarPago(double monto) {
        if (!validar()) {
            return "Pago rechazado: tarjeta invalida";
        }

        if (monto < 1000) {
            return "Pago rechazado: monto minimo $1000";
        }

        double montoFinal = calcularMontoFinal(monto);
        double valorCuota = montoFinal / numeroCuotas;

        String tarjetaOculta = "****-****-****-" + numeroTarjeta.substring(12);

        return "Pago exitoso con Tarjeta " + tarjetaOculta
                + " | " + numeroCuotas + " cuotas de $" + String.format("%.2f", valorCuota)
                + " | Total: $" + String.format("%.2f", montoFinal);
    }

    @Override
    public String getTipo() {
        return "Tarjeta Credito";
    }
}
