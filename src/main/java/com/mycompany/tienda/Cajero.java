/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda;

/**
 *
 * @author Hacking_man
 */
public class Cajero extends Empleado {

    private int ventasProcesadas;
    private double totalVendidoHoy;

    public Cajero(String id, String nombre, String usuario, String contraseña) {
        super(id, nombre, usuario, contraseña);
        this.ventasProcesadas = 0;
        this.totalVendidoHoy = 0.0;
    }

    @Override
    public boolean tienePermiso(String accion) {
        // Cajeros pueden: procesar ventas, emitir facturas, buscar productos
        return accion.equals("Procesar Venta")
                || accion.equals("Emitir Factura")
                || accion.equals("Buscar Prodcuto")
                || accion.equals("Ver Inventario");
    }

    public void registrarVenta(double monto) {
        this.ventasProcesadas++;
        this.totalVendidoHoy += monto;
    }

    public void reiniciarContadorDiario() {
        this.ventasProcesadas = 0;
        this.totalVendidoHoy = 0;
    }

    // Getters
    public int getVentasProcesadas() {
        return ventasProcesadas;
    }

    public double getTotalVendidoHoy() {
        return totalVendidoHoy;
    }

    @Override
    public String toString() {
        return "Cajero - " + super.toString()
                + " | Ventas: " + ventasProcesadas
                + " | Total: $" + totalVendidoHoy;
    }
}
