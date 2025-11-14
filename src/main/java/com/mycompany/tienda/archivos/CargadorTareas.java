/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.archivos;

import com.mycompany.tienda.Cliente;
import com.mycompany.tienda.Empleado;
import com.mycompany.tienda.ServiciosDigitales;
import com.mycompany.tienda.Tarea;
import com.mycompany.tienda.Tecnico;
import com.mycompany.tienda.control.ControlClientes;
import com.mycompany.tienda.control.ControlPersonal;
import com.mycompany.tienda.control.EstadoTarea;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author carod
 */
public class CargadorTareas {

    public static ArrayList<Tarea> cargarDesdeCSV(String rutaArchivo, ControlPersonal gestorPersonal, ControlClientes gestorClientes) {
        ArrayList<Tarea> tareas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                String[] datos = linea.split(",");
                if (datos.length < 7) {
                    continue;
                }

                String idTarea = datos[0].trim();
                String idTecnico = datos[1].trim();
                String nombreServicio = datos[2].trim().replace(";", ",");
                double precio = Double.parseDouble(datos[3].trim());
                int duracion = Integer.parseInt(datos[4].trim());
                String cedulaCliente = datos[5].trim();
                String nombreEstado = datos[6].trim();

                EstadoTarea estadoEnum;
                try {
                    estadoEnum = EstadoTarea.valueOf(nombreEstado.replace(" ", "_"));
                } catch (Exception e) {
                    estadoEnum = EstadoTarea.PENDIENTE;
                }

                if (estadoEnum == EstadoTarea.COMPLETADA || estadoEnum == EstadoTarea.CANCELADA) {
                    continue;
                }

                Tecnico tecnico = null;
                if (!idTecnico.equals("SIN_TECNICO")) {
                    Empleado emp = gestorPersonal.getEmpleados().get(idTecnico);
                    if (emp instanceof Tecnico) {
                        tecnico = (Tecnico) emp;
                    }
                }

                Cliente cliente = (cedulaCliente.equals("SIN_CLIENTE")) ? null : gestorClientes.getClientes().get(cedulaCliente);

                ServiciosDigitales servicio = new ServiciosDigitales(nombreServicio, precio, "SERVICIOS TÉCNICOS", duracion, "Servicio CSV");

                Tarea tarea = new Tarea(idTarea, servicio, cliente, LocalDateTime.now(), true);
                tarea.setEstado(estadoEnum);

                if (tecnico != null) {
                    tarea.setTecnicoAsignado(tecnico);
                    if (tecnico.estaDisponible()) {
                        tecnico.asignarServicio(servicio);
                    }
                }

                tareas.add(tarea);
            }

        } catch (IOException e) {
            System.err.println("Archivo Tareas.csv no encontrado. Se creará al guardar.");
        }

        return tareas;
    }
}
