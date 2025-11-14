/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.archivos;

import com.mycompany.tienda.Tarea;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author carod
 */
public class GuardarTareas {

    public static void guardarEnCSV(ArrayList<Tarea> tareas, String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {

            bw.write("IdTarea,IdTecnico,NombreServicio,PrecioServicio,DuracionMinutos,CedulaCliente,Estado");
            bw.newLine();

            for (Tarea tarea : tareas) {
                String idTecnico = (tarea.getTecnicoAsignado() != null) ? tarea.getTecnicoAsignado().getId() : "SIN_TECNICO";
                String nombreServicio = tarea.getServicio().getNombre().replace(",", ";");
                String cedulaCliente = (tarea.getCliente() != null) ? tarea.getCliente().getCedula() : "SIN_CLIENTE";

                String linea = tarea.getIdTarea() + ","
                        + idTecnico + ","
                        + nombreServicio + ","
                        + tarea.getServicio().getPrecio() + ","
                        + tarea.getServicio().getDuracionEstimadaMinutos() + ","
                        + cedulaCliente + ","
                        + tarea.getEstado().name();

                bw.write(linea);
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error al guardar tareas: " + e.getMessage());
        }
    }
}
