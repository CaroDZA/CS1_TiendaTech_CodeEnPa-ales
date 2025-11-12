/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hacking_man
 */
public class Cliente {

    private static int contadorClientes = 0;
    private String idCliente;
    private String nombre;
    private String cedula;
    private String telefono;
    private String direccion;
    private int puntosFidelidad;
    private int totalCompras;

    public Cliente(String nombre, String cedula, String telefono, String direccion) {
        Cliente.contadorClientes++;
        this.idCliente = String.format("CLI-%03d", Cliente.contadorClientes);

        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.direccion = direccion;
        this.puntosFidelidad = 0;
        this.totalCompras = 0;
    }

    public void acumularPuntos(double montoCompra) {
        int nuevosPuntos = (int) (montoCompra / 10.0);
        this.puntosFidelidad += nuevosPuntos;
    }

    public double DescuentoFidelidad() {
        if (totalCompras >= 6) {
            return 0.10; //10%
        } else if (totalCompras >= 3) {
            return 0.05; // 5%
        } else {
            return 0.00; //0%
        }
    }

    public void restarPuntos(int puntos) {
        if (puntos > this.puntosFidelidad) {
            throw new IllegalArgumentException("Puntos insuficientes. Disponible: " + this.puntosFidelidad + ", Solicitado: " + puntos);
        }

        if (puntos < 0) {
            throw new IllegalArgumentException("La cantidad de puntos debe ser positiva");
        }

        this.puntosFidelidad -= puntos;
        System.out.println("Puntos descontados: " + puntos + " - Puntos restantes: " + this.puntosFidelidad);
    }

    public void incrementarCompras() {
        this.totalCompras++;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public int getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public String getIdCliente() {
        return idCliente;
    }

    @Override
    public String toString() {
        return nombre; // o lo que quieras mostrar
    }

    public String verHistorialDeCompras(List<Venta> todasLasVentas) {
        String texto = "";

        if (todasLasVentas == null || todasLasVentas.isEmpty()) {
            return "\nNo hay ventas registradas en el sistema";
        }

        List<Venta> historial = new ArrayList<>();

        for (Venta venta : todasLasVentas) {
            if (venta.getCliente() != null
                    && venta.getCliente().getIdCliente().equals(this.idCliente)) {
                historial.add(venta);
            }
        }

        texto += "   HISTORIAL DE COMPRAS  \n";
        texto += "Cliente: " + this.nombre + "\n";
        texto += "ID: " + this.idCliente + "\n";
        texto += "Cédula: " + this.cedula + "\n\n";

        if (historial.isEmpty()) {
            texto += "No se encontraron compras para este cliente\n";
            return texto;
        }

        texto += "Total de compras: " + historial.size() + "\n\n";

        double totalGastado = 0;
        int comprasEntregadas = 0;

        for (Venta v : historial) {
            texto += "Venta #" + v.getId() + "\n";
            texto += "  Estado: " + v.getEstado().getNombre() + "\n";
            texto += "  Total: $" + String.format("%.2f", v.getTotal()) + "\n\n";

            if (v.getEstado().getNombre().equals("ENTREGADA")) {
                totalGastado += v.getTotal();
                comprasEntregadas++;
            }
        }

        texto += "Compras entregadas: " + comprasEntregadas + "\n";
        texto += "Total gastado: $" + String.format("%.2f", totalGastado) + "\n";
        texto += "Puntos acumulados: " + this.puntosFidelidad + "\n";
        texto += "Descuento actual: " + (int) (DescuentoFidelidad() * 100) + "%\n";

        return texto;
    }

}
