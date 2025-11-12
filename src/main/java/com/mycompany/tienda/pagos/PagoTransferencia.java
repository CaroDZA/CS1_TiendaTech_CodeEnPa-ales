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
public class PagoTransferencia implements MetodoPago {

    private String numeroReferencia;
    private boolean confirmada;

    public PagoTransferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
        this.confirmada = false;
    }

    @Override
    public boolean validar() {
        if (numeroReferencia == null || numeroReferencia.trim().isEmpty()) {
            System.out.println("Número de referencia requerido");
            return false;
        }

        if (numeroReferencia.length() < 6) {
            System.out.println("Número de referencia inválido");
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
        
        System.out.println("Monto recibido: " + monto); 
        
        if (!validar()) {
            return "Pago rechazado: referencia inválida";
        }

        if (monto < 1000) {
            return "Pago rechazado: monto mínimo $1000";
        }

        // Simular confirmación
        this.confirmada = true;

        return "Pago exitoso por Transferencia Referencia: " + numeroReferencia
                + " Total: $" + String.format("%.2f", monto);
    }

    @Override
    public String getTipo() {
        return "Transferencia";
    }

    public boolean isConfirmada() {
        return confirmada;
    }
}
