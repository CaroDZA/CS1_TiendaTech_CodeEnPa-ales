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
public class PagoCombinado implements MetodoPago {

    private MetodoPago metodoPago1;
    private double montoPago1;

    private MetodoPago metodoPago2;
    private double montoPago2;

    public PagoCombinado(MetodoPago metodoPago1, double montoPago1,
            MetodoPago metodoPago2, double montoPago2) {

        if (metodoPago1 == null || metodoPago2 == null) {
            throw new IllegalArgumentException("Los métodos de pago no pueden ser nulos");
        }

        if (montoPago1 <= 0 || montoPago2 <= 0) {
            throw new IllegalArgumentException("Los montos deben ser mayores a 0");
        }

        this.metodoPago1 = metodoPago1;
        this.montoPago1 = montoPago1;
        this.metodoPago2 = metodoPago2;
        this.montoPago2 = montoPago2;
    }

    @Override
    public boolean validar() {
        if (!metodoPago1.validar()) {
            System.out.println("Error: El primer método de pago no es válido");
            return false;
        }

        if (!metodoPago2.validar()) {
            System.out.println("Error: El segundo método de pago no es válido");
            return false;
        }

        return true;
    }

    @Override
    public double calcularMontoFinal(double montoTotal) {
        double totalIngresado = montoPago1 + montoPago2;

        if (totalIngresado < montoTotal) {
            throw new IllegalArgumentException(
                    "El total de los pagos ($" + totalIngresado
                    + ") es menor al monto de la compra ($" + montoTotal + ")"
            );
        }

        double montoFinal1 = metodoPago1.calcularMontoFinal(montoPago1);
        double montoFinal2 = metodoPago2.calcularMontoFinal(montoPago2);

        return montoFinal1 + montoFinal2;
    }

    @Override
    public String getTipo() {
        return "Pago Combinado";
    }

    // Getters
    public MetodoPago getMetodoPago1() {
        return metodoPago1;
    }

    public double getMontoPago1() {
        return montoPago1;
    }

    public MetodoPago getMetodoPago2() {
        return metodoPago2;
    }

    public double getMontoPago2() {
        return montoPago2;
    }

    @Override
    public String procesarPago(double monto) {
        throw new UnsupportedOperationException("Not supported yet."); //Aún no se ha terminado este método 
    }

}
