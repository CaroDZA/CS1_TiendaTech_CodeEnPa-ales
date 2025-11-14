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
public abstract class ProductoBase implements Vendido {

    protected String nombre;
    private double precio;
    protected String categoria;

    private static ArrayList<String> categoriasProductos = new ArrayList<>();
    
    private static ArrayList<String> categoriasServicios = new ArrayList<>();

    static {
        categoriasProductos.add("COMPUTADORAS");
        categoriasProductos.add("SMARTPHONES");
        categoriasProductos.add("ACCESORIOS");
        categoriasProductos.add("PERIFÉRICOS");
        categoriasProductos.add("COMPONENTES");
        
        categoriasServicios.add("SERVICIOS TÉCNICOS");
    }

    public ProductoBase(String nombre, double precio, String categoria) {
        setNombre(nombre);
        setPrecio(precio);
        setCategoria(categoria);
    }

    public abstract double calcularPrecioFinal();

    @Override
    public abstract String obtenerInformacion();

    //Getters
    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacio");
        }
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        if (precio < 0.01) {
            throw new IllegalArgumentException("El precio mínimo es $0.01");
        }
        if (precio > 99999.99) {
            throw new IllegalArgumentException("El precio máximo es $99,999.99");
        }
        this.precio = precio;
    }

    private boolean esCategoriaValida(String categoria) {
        if (categoria == null) {
            return false;
        }

        for (int i = 0; i < categoriasProductos.size(); i++) {
            if (categoriasProductos.get(i).equals(categoria)) {
                return true;
            }
        }
        
        for (int i = 0; i < categoriasServicios.size(); i++) {
            if (categoriasServicios.get(i).equals(categoria)) {
                return true;
            }
        }
        
        return false;
    }

    public void setCategoria(String categoria) {
        if (!esCategoriaValida(categoria)) {
            throw new IllegalArgumentException("Categoría "+ categoria +" no válida");
        }
        this.categoria = categoria;
    }

    public static void agregarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }

        String categoriaMayuscula = categoria.trim().toUpperCase();

        for (int i = 0; i < categoriasProductos.size(); i++) {
            if (categoriasProductos.get(i).equals(categoriaMayuscula)) {
                throw new IllegalArgumentException("La categoría ya existe en productos");
            }
        }
        
        for (int i = 0; i < categoriasServicios.size(); i++) {
            if (categoriasServicios.get(i).equals(categoriaMayuscula)) {
                throw new IllegalArgumentException("La categoría ya existe en servicios");
            }
        }

        categoriasProductos.add(categoriaMayuscula);
        System.out.println("Categoría agregada a productos: " + categoriaMayuscula);
    }
    
    public static void eliminarCategoria(String categoria) {
        String categoriaMayuscula = categoria.trim().toUpperCase();
        boolean eliminada = false;

        
        for (int i = 0; i < categoriasProductos.size(); i++) {
            if (categoriasProductos.get(i).equals(categoriaMayuscula)) {
                categoriasProductos.remove(i);
                eliminada = true;
                System.out.println("Categoría eliminada de productos: " + categoriaMayuscula);
                return;
            }
        }
        
       
        for (int i = 0; i < categoriasServicios.size(); i++) {
            if (categoriasServicios.get(i).equals(categoriaMayuscula)) {
                categoriasServicios.remove(i);
                eliminada = true;
                System.out.println("Categoría eliminada de servicios: " + categoriaMayuscula);
                return;
            }
        }

        if (!eliminada) {
            System.out.println("Categoría no encontrada");
        }
    }
    
    public static ArrayList<String> obtenerCategoriasProductos() {
        return new ArrayList<>(categoriasProductos);
    }
    
    public static ArrayList<String> obtenerCategoriasServicios() {
        return new ArrayList<>(categoriasServicios);
    }
    
    public static ArrayList<String> obtenerCategorias() {
        ArrayList<String> todasCategorias = new ArrayList<>();
        todasCategorias.addAll(categoriasProductos);
        todasCategorias.addAll(categoriasServicios);
        return todasCategorias;
    }
   

    public static void mostrarCategorias() {
        System.out.println("\nCATEGORÍAS DISPONIBLES");
        
        System.out.println("\nPRODUCTOS:");
        for (int i = 0; i < categoriasProductos.size(); i++) {
            System.out.println((i + 1) + ". " + categoriasProductos.get(i));
        }
        
        System.out.println("\nSERVICIOS:");
        for (int i = 0; i < categoriasServicios.size(); i++) {
            System.out.println((i + 1) + ". " + categoriasServicios.get(i));
        }
        
        System.out.println("\nTotal: " + (categoriasProductos.size() + categoriasServicios.size()) + " categorías");
    }
}