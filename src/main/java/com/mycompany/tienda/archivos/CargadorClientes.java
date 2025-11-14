/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.archivos;

import com.mycompany.tienda.Cliente;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author carod
 */
public class CargadorClientes {

    public static ArrayList<Cliente> cargarDesdeCSV(String rutaArchivo) {
        ArrayList<Cliente> clientes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean primeraLinea = true;

            System.out.println("Leyendo archivo de clientes: " + rutaArchivo);

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    System.out.println("Encabezado: " + linea);
                    continue;
                }

                String[] datos = linea.split(",");
                if (datos.length >= 7) {
                    String nombre = datos[0].trim();
                    String cedula = datos[1].trim();
                    String telefono = datos[2].trim();
                    String direccion = datos[3].trim();
                    int puntos = Integer.parseInt(datos[4].trim());
                    int totalCompras = Integer.parseInt(datos[5].trim());

                    Cliente cliente = new Cliente(nombre, cedula, telefono, direccion);

                    cliente.setPuntosFidelidad(puntos);
                    cliente.setTotalCompras(totalCompras);

                    clientes.add(cliente);
                    System.out.println("Cliente cargado: " + nombre + " - Puntos: " + puntos + " - Compras: " + totalCompras);
                }
            }

            System.out.println("Total clientes cargados: " + clientes.size());

        } catch (IOException e) {
            System.err.println("Error al cargar clientes: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error procesando clientes: " + e.getMessage());
            e.printStackTrace();
        }

        return clientes;
    }
}
