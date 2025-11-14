/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.archivos;

import com.mycompany.tienda.ProductoFisico;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author carod
 */
public class GuardarProductos {

    public static void guardarEnCSV(ArrayList<ProductoFisico> productos, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Productos.csv"))) {

            bw.write("CodigoBarras,Nombre,Precio,Categoria,Stock,Ubicacion,Descuento");
            bw.newLine();

            for (int i = 0; i < productos.size(); i++) {
                ProductoFisico p = productos.get(i);

                String linea = p.getCodigoBarras() + ","
                        + p.getNombre() + ","
                        + p.getPrecio() + ","
                        + p.getCategoria() + ","
                        + p.getStockEnTienda() + ","
                        + p.getUbicacion() + ","
                        + p.getDescuentoOferta();

                bw.write(linea);
                bw.newLine();
            }

            System.out.println("Productos guardados en CSV: " + productos.size());

        } catch (IOException e) {
            System.err.println("Error al guardar CSV: " + e.getMessage());
        }
    }

}
