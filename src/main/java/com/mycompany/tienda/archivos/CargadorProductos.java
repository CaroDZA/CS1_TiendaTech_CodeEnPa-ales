/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.archivos;

import com.mycompany.tienda.ProductoFisico;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author carod
 */
public class CargadorProductos {

    public static ArrayList<ProductoFisico> cargarDesdeCSV(String rutaArchivo) {
        ArrayList<ProductoFisico> productos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("Productos.csv"))) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] datos = linea.split(",");
                if (datos.length == 7) {
                    String codigoBarras = datos[0].trim();
                    String nombre = datos[1].trim();
                    double precio = Double.parseDouble(datos[2].trim());
                    String categoria = datos[3].trim();
                    int stock = Integer.parseInt(datos[4].trim());
                    String ubicacion = datos[5].trim();
                    double descuento = Double.parseDouble(datos[6].trim());

                    ProductoFisico producto = new ProductoFisico(
                            nombre, precio, categoria, codigoBarras,
                            stock, ubicacion, descuento
                    );
                    productos.add(producto);
                }
            }

        } catch (IOException e) {
            System.err.println("Error al cargar productos desde CSV: " + e.getMessage());
        }

        return productos;
    }
}
