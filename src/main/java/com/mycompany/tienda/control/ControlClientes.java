/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.control;

import com.mycompany.tienda.Cliente;
import java.util.HashMap;

public class ControlClientes {

    private HashMap<String, Cliente> clientes; // key = cedula

    public ControlClientes() {
        this.clientes = new HashMap<>();
    }

    public void registrarCliente(Cliente cliente) {
        if (clientes.containsKey(cliente.getCedula())) {
            throw new IllegalArgumentException("Ya existe un cliente con esa cédula");
        }

        clientes.put(cliente.getCedula(), cliente);
        System.out.println("Cliente registrado: " + cliente.getNombre() + " (ID: " + cliente.getIdCliente() + ")");
    }

    // Actualizar puntos después de una compra
    public void actualizarPuntos(String cedula, double montoCompra) {
        Cliente cliente = clientes.get(cedula);

        if (cliente == null) {
            System.out.println("Cliente no encontrado");
            return;
        }

        cliente.acumularPuntos(montoCompra);
        System.out.println("Puntos acumulados: " + cliente.getPuntosFidelidad());
    }

    // Canjear puntos
    public double canjearPuntos(String cedula, int puntosACanjear) {
        Cliente cliente = clientes.get(cedula);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        if (puntosACanjear < 100) {
            throw new IllegalArgumentException("Mínimo 100 puntos para canjear");
        }

        if (cliente.getPuntosFidelidad() < puntosACanjear) {
            throw new IllegalArgumentException("Puntos insuficientes. Disponible: " + cliente.getPuntosFidelidad());
        }

        double descuento = puntosACanjear / 10.0; // 100 puntos = $10
        System.out.println("Descuento por puntos: $" + descuento);

        return descuento;
    }

    public HashMap<String, Cliente> getClientes() {
        return clientes;
    }
}
