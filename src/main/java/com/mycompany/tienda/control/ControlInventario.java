/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.control;

import com.mycompany.tienda.ProductoFisico;
import java.util.ArrayList;

/**
 *
 * @author carod
 */
public class ControlInventario {

    public ArrayList<ProductoFisico> inventario; 

    public ControlInventario() {
        this.inventario = new ArrayList<>();
    }

    public void agregarProducto(ProductoFisico producto) {
        for (int i = 0; i < inventario.size(); i++) {
            if (inventario.get(i).getCodigoBarras().equals(producto.getCodigoBarras())) {
                throw new IllegalArgumentException("Ya existe un producto con este código de barras");
            }
        }

        inventario.add(producto);
        System.out.println("Producto agregado: " + producto.getNombre());
    }

    public void eliminarProducto(String codigoBarras) {
        ProductoFisico producto = null;
        int indiceEliminar = -1;

        for (int i = 0; i < inventario.size(); i++) {
            if (inventario.get(i).getCodigoBarras().equals(codigoBarras)) {
                producto = inventario.get(i);
                indiceEliminar = i;
                break;
            }
        }

        if (producto == null) {
            System.out.println("Producto no encontrado");
        } else {
            inventario.remove(indiceEliminar);
            System.out.println("Producto eliminado: " + producto.getNombre());
        }
    }

    public ProductoFisico buscarPorCodigo(String codigoBarras) {
        for (int i = 0; i < inventario.size(); i++) {
            if (inventario.get(i).getCodigoBarras().equals(codigoBarras)) {
                return inventario.get(i);
            }
        }
        return null;
    }

    public void actualizarStock(String codigoBarras, int cantidadAgregar) {
        ProductoFisico producto = buscarPorCodigo(codigoBarras);

        if (producto == null) {
            System.out.println("Producto no encontrado");
            return;
        }

        int nuevoStock = producto.getStockEnTienda() + cantidadAgregar;
        producto.setStockEnTienda(nuevoStock);

        System.out.println("Stock actualizado: " + producto.getNombre()
                + " - Nuevo stock: " + nuevoStock);
    }

    public void reducirStock(String codigoBarras, int cantidad) {
        ProductoFisico producto = buscarPorCodigo(codigoBarras);

        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado");
        }

        if (!producto.hayStockDisponible(cantidad)) {
            throw new IllegalArgumentException(
                    "Stock insuficiente. Disponible: " + producto.getStockEnTienda() + ", Solicitado: " + cantidad);
        }

        int nuevoStock = producto.getStockEnTienda() - cantidad;
        producto.setStockEnTienda(nuevoStock);

        if (nuevoStock == 0) {
            System.out.println("Agotado: " + producto.getNombre());
        } else if (nuevoStock < 5) {
            System.out.println("Stock bajo: " + producto.getNombre() + "(" + nuevoStock + " unidades)");
        }
    }
}
