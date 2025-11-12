/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.control;

import com.mycompany.tienda.Cliente;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author carod
 */
public class GuardarClientes {

    public static void guardarEnCSV(Map<String, Cliente> clientes, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {

            bw.write("Nombre,Cedula,Telefono,Direccion,PuntosFidelidad,TotalCompras,IdCliente");
            bw.newLine();

            for (Cliente cliente : clientes.values()) {
                String linea = cliente.getNombre() + ","
                        + cliente.getCedula() + ","
                        + cliente.getTelefono() + ","
                        + cliente.getDireccion() + ","
                        + cliente.getPuntosFidelidad() + ","
                        + cliente.getTotalCompras() + ","
                        + cliente.getIdCliente();

                bw.write(linea);
                bw.newLine();
            }

            System.out.println("Clientes guardados en CSV: " + clientes.size());

        } catch (IOException e) {
            System.err.println("Error al guardar clientes: " + e.getMessage());
        }
    }
}
