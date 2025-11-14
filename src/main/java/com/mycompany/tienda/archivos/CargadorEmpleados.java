/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.archivos;

import com.mycompany.tienda.Cajero;
import com.mycompany.tienda.Empleado;
import com.mycompany.tienda.Supervisor;
import com.mycompany.tienda.Tecnico;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author carod
 */
public class CargadorEmpleados {

    public static ArrayList<Empleado> cargarDesdeCSV(String rutaArchivo) {
        ArrayList<Empleado> empleados = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean primeraLinea = true;
            int numeroLinea = 0;

            System.out.println("Leyendo archivo: " + rutaArchivo);

            while ((linea = br.readLine()) != null) {
                numeroLinea++;

                if (primeraLinea) {
                    primeraLinea = false;
                    System.out.println("Encabezado: " + linea);
                    continue;
                }

                String[] datos = linea.split(",");

                if (datos.length < 6) {
                    System.out.println("Linea " + numeroLinea + " incompleta: " + linea);
                    continue;
                }

                String id = datos[0].trim();
                String nombre = datos[1].trim();
                String usuario = datos[2].trim();
                String contrasena = datos[3].trim();
                boolean activo = Boolean.parseBoolean(datos[4].trim());
                String tipo = datos[5].trim();

                Empleado empleado = null;

                if (tipo.equalsIgnoreCase("Supervisor")) {
                    empleado = new Supervisor(id, nombre, usuario, contrasena);
                } else if (tipo.equalsIgnoreCase("Cajero")) {
                    empleado = new Cajero(id, nombre, usuario, contrasena);
                } else if (tipo.equalsIgnoreCase("Tecnico")) {
                    empleado = new Tecnico(id, nombre, usuario, contrasena);
                } else {
                    System.out.println("Tipo desconocido en linea " + numeroLinea + ": " + tipo);
                }

                if (empleado != null) {
                    empleado.setActivo(activo);
                    empleados.add(empleado);
                    System.out.println("Empleado cargado: " + nombre + " (" + tipo + ")");
                }
            }

            System.out.println("Total empleados cargados: " + empleados.size());

        } catch (IOException e) {
            System.err.println("ERROR al leer el archivo: " + e.getMessage());
            System.err.println("Verifica que el archivo 'Empleados.csv' exista en la raiz del proyecto");
        }

        return empleados;
    }
}
