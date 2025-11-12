/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda;

import com.mycompany.tienda.modelo.interfaces.Vendido;
import java.util.ArrayList;

/**
 *
 * @author Hacking_man
 */
public class CarritoDeCompras {

    private ArrayList<ItemVenta> items;
    private Cliente cliente;
    private Cajero cajero;
    private double descuentoEspecial;
    private double descuentoPorPuntos;
    private static final double TASA_IVA = 0.19;

    public CarritoDeCompras(Cajero cajero, Cliente cliente) {
        this.items = new ArrayList<>();
        this.cajero = cajero;
        this.cliente = cliente;
        this.descuentoEspecial = 0.0;
        this.descuentoPorPuntos = 0.0;
    }

    public void agregarItem(Vendido producto, int cantidad) {
        // Validar cantidad
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        // Validar stock si es producto físico
        if (producto instanceof ProductoFisico) {
            ProductoFisico pf = (ProductoFisico) producto;
            if (cantidad > pf.getStockEnTienda()) {
                throw new IllegalArgumentException(
                        "No hay suficiente stock de " + pf.getNombre()
                        + ". Disponible: " + pf.getStockEnTienda()
                );
            }
        }

        // Buscar si ya existe el producto en el carrito
        boolean encontrado = false;
        for (int i = 0; i < items.size(); i++) {
            ItemVenta item = items.get(i);
            if (item.getProducto().getNombre().equals(producto.getNombre())) {
                // Ya existe, aumentar cantidad
                int nuevaCantidad = item.getCantidad() + cantidad;

                // Validar nuevo stock total
                if (producto instanceof ProductoFisico) {
                    ProductoFisico pf = (ProductoFisico) producto;
                    if (nuevaCantidad > pf.getStockEnTienda()) {
                        throw new IllegalArgumentException(
                                "Stock insuficiente. Disponible: " + pf.getStockEnTienda()
                                + ", En carrito: " + item.getCantidad()
                                + ", Intentando agregar: " + cantidad
                        );
                    }
                }

                item.setCantidad(nuevaCantidad);
                encontrado = true;
                System.out.println(cantidad + "x " + producto.getNombre()
                        + " agregado (total: " + nuevaCantidad + ")");
                break;
            }
        }

        // Si no existía, agregar nuevo item
        if (!encontrado) {
            items.add(new ItemVenta(producto, cantidad));
            System.out.println(cantidad + "x " + producto.getNombre() + " agregado al carrito");
        }
    }

    public void eliminarItem(int indice) {
        if (indice < 0 || indice >= items.size()) {
            throw new IllegalArgumentException("Índice inválido: " + indice);
        }

        ItemVenta eliminado = items.remove(indice);
        System.out.println("Eliminado del carrito: " + eliminado.getProducto().getNombre());
    }

    public void actualizarCantidad(int indice, int nuevaCantidad) {
        if (indice < 0 || indice >= items.size()) {
            throw new IllegalArgumentException("Índice inválido: " + indice);
        }
        // Si la cantidad es 0 o negativa, eliminar el item
        if (nuevaCantidad <= 0) {
            eliminarItem(indice);
            return;
        }

        ItemVenta item = items.get(indice);

        // El setter de ItemVenta valida el stock automáticamente
        // y recalcula el subtotal
        item.setCantidad(nuevaCantidad);

        System.out.println("Cantidad actualizada: " + item.getProducto().getNombre()
                + " -> " + nuevaCantidad + " unidades");
    }

    public void vaciarCarrito() {
        items.clear();
        descuentoEspecial = 0.0;
        descuentoPorPuntos = 0.0;
        System.out.println("Carrito vaciado");
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (ItemVenta item : items) {
            // Usar precio original sin descuento
            subtotal += item.getPrecioUnitario() * item.getCantidad();
        }
        return subtotal;
    }

    public double calcularDescuentosProductos() {
        double totalDescuentos = 0;
        for (ItemVenta item : items) {
            if (item.getProducto() instanceof ProductoFisico) {
                ProductoFisico pf = (ProductoFisico) item.getProducto();

                // Calcular el descuento comparando precio original vs precio final
                double precioOriginal = pf.getPrecio() * item.getCantidad();
                double precioConDescuento = pf.calcularPrecioFinal() * item.getCantidad();
                double descuento = precioOriginal - precioConDescuento;

                // Solo sumar si hay descuento (evitar errores de redondeo)
                if (descuento > 0.01) {
                    totalDescuentos += descuento;
                }
            }
        }
        return totalDescuentos;
    }

    public double calcularDescuentos() {
        double descuentoProductos = calcularDescuentosProductos();
        double descuentoFidelidad = 0;

        // Descuento por fidelidad del cliente
        if (cliente != null) {
            double subtotal = calcularSubtotal();
            descuentoFidelidad = subtotal * cliente.DescuentoFidelidad();
        }

        return descuentoProductos + descuentoFidelidad + this.descuentoEspecial + this.descuentoPorPuntos;
    }

    public double calcularIVA() {
        double baseImponible = calcularSubtotal() - calcularDescuentos();
        return baseImponible * TASA_IVA;
    }

    public double calcularTotalFinal() {
        double subtotal = calcularSubtotal();
        double descuentos = calcularDescuentos();
        double iva = calcularIVA();
        return subtotal - descuentos + iva;
    }

    public void aplicarDescuentoEspecial(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 1.0) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 1");
        }

        double subtotal = calcularSubtotal();
        this.descuentoEspecial = subtotal * porcentaje;
        System.out.println("Descuento especial aplicado: $"
                + String.format("%.2f", this.descuentoEspecial));
    }

    public void quitarDescuentoEspecial() {
        this.descuentoEspecial = 0.0;
        System.out.println("Descuento especial removido");
    }

    public void aplicarDescuentoPorPuntos(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        this.descuentoPorPuntos = monto;
        System.out.println("Descuento por puntos aplicado: $"
                + String.format("%.2f", this.descuentoPorPuntos));
    }

    public void quitarDescuentoPorPuntos() {
        this.descuentoPorPuntos = 0.0;
        System.out.println("Descuento por puntos removido");
    }

    public int buscarProducto(String nombreProducto) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProducto().getNombre().equals(nombreProducto)) {
                return i;
            }
        }
        return -1;
    }

    // Getters
    public ArrayList<ItemVenta> getItems() {
        return items;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public int getCantidadItems() {
        return items.size();
    }

    public int getCantidadTotalUnidades() {
        int total = 0;
        for (int i = 0; i < items.size(); i++) {
            total = total + items.get(i).getCantidad();
        }
        return total;
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public double getDescuentoEspecial() {
        return descuentoEspecial;
    }

    public double getDescuentoPorPuntos() {
        return descuentoPorPuntos;
    }

    public static double getTasaIva() {
        return TASA_IVA;
    }

    // Setters
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        System.out.println("Cliente asignado: "
                + (cliente != null ? cliente.getNombre() : "Sin cliente"));
    }
}