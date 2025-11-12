/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda;

import java.time.LocalDate;

/**
 *
 * @author Hacking_man
 */
public class ProductoFisico extends ProductoBase {

    private String codigoBarras;
    private int stockEnTienda;
    private String ubicacion;
    private LocalDate fechaIngreso;
    private double descuentoOferta;

    public ProductoFisico(String nombre, double precio, String categoria,
            String codigoBarras, int stockEnTienda, String ubicacion, double descuentoOferta) {
        super(nombre, precio, categoria);

        this.codigoBarras = codigoBarras;
        this.stockEnTienda = stockEnTienda;
        this.ubicacion = ubicacion;
        this.fechaIngreso = LocalDate.now();
        this.descuentoOferta = descuentoOferta;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public int getStockEnTienda() {
        return stockEnTienda;
    }

    public void setStockEnTienda(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stockEnTienda = stock;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public double getDescuentoOferta() {
        return descuentoOferta;
    }

    public void setDescuentoOferta(double descuentoOferta) {
        this.descuentoOferta = descuentoOferta;
    }

    //Hay stock disponible para esta cantidad?
    public boolean hayStockDisponible(int cantidad) {
        return this.stockEnTienda >= cantidad;
    }

    @Override
    public double calcularPrecioFinal() {
        return getPrecio() * (1 - this.descuentoOferta);
    }

    @Override
    public String obtenerInformacion() {
        return nombre + " (" + codigoBarras + ") - Stock: " + stockEnTienda + " - Ubicacion: " + ubicacion;
    }

}
