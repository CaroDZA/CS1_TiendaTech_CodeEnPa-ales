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

    private static ArrayList<String> categoriasValidas = new ArrayList<>();

    static {
        categoriasValidas.add("COMPUTADORAS");
        categoriasValidas.add("SMARTPHONES");
        categoriasValidas.add("ACCESORIOS");
        categoriasValidas.add("PERIFÉRICOS");
        categoriasValidas.add("COMPONENTES");
        categoriasValidas.add("SERVICIOS TÉCNICOS");
    }

    public ProductoBase(String nombre, double precio, String categoria) {
        setNombre(nombre);
        setPrecio(precio); //constructor tenía validación en set pero no validaba en constructor.
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

        for (int i = 0; i < categoriasValidas.size(); i++) {
            if (categoriasValidas.get(i).equals(categoria)) {
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

        for (int i = 0; i < categoriasValidas.size(); i++) {
            if (categoriasValidas.get(i).equals(categoriaMayuscula)) {
                throw new IllegalArgumentException("La categoría ya existe");
            }
        }

        categoriasValidas.add(categoriaMayuscula);
        System.out.println("Categoría agregada: " + categoriaMayuscula);
    }

    public static void eliminarCategoria(String categoria) {
        String categoriaMayuscula = categoria.trim().toUpperCase();

        boolean eliminada = false;
        for (int i = 0; i < categoriasValidas.size(); i++) {
            if (categoriasValidas.get(i).equals(categoriaMayuscula)) {
                categoriasValidas.remove(i);
                eliminada = true;
                System.out.println("Categoría eliminada: " + categoriaMayuscula);
                break;
            }
        }

        if (!eliminada) {
            System.out.println("Categoría no encontrada");
        }
    }

    public static ArrayList<String> obtenerCategorias() {
        return categoriasValidas;
    }

    public static void mostrarCategorias() {
        System.out.println("\n CATEGORÍAS DISPONIBLES ");
        for (int i = 0; i < categoriasValidas.size(); i++) {
            System.out.println((i + 1) + ". " + categoriasValidas.get(i));
        }
        System.out.println("Total: " + categoriasValidas.size() + " categorías");
    }
}
