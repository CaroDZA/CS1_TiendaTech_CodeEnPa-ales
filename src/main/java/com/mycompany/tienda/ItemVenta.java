package com.mycompany.tienda;

import com.mycompany.tienda.modelo.interfaces.Vendido;

/**
 *
 * @author carod
 */
public class ItemVenta {

    private Vendido producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public ItemVenta(Vendido producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
        recalcularSubtotal();
    }

    private void recalcularSubtotal() {
        if (producto instanceof ProductoFisico) {
            ProductoFisico pf = (ProductoFisico) producto;
            // Subtotal YA con descuento aplicado
            this.subtotal = pf.calcularPrecioFinal() * cantidad;
        } else {
            this.subtotal = producto.getPrecio() * cantidad;
        }
    }

    // Getters
    public Vendido getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        // Validar stock si es producto físico
        if (producto instanceof ProductoFisico) {
            ProductoFisico pf = (ProductoFisico) producto;
            if (cantidad > pf.getStockEnTienda()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente. Disponible: " + pf.getStockEnTienda()
                );
            }
        }

        this.cantidad = cantidad;
        recalcularSubtotal();
    }

    @Override
    public String toString() {
        return cantidad + "x " + producto.getNombre()
                + " - $" + String.format("%.2f", precioUnitario)
                + " c/u = $" + String.format("%.2f", subtotal);
    }
}
