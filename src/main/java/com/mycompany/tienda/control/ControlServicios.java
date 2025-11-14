package com.mycompany.tienda.control;

import com.mycompany.tienda.archivos.GuardarTareas;
import com.mycompany.tienda.Cliente;
import com.mycompany.tienda.ServiciosDigitales;
import com.mycompany.tienda.Tarea;
import com.mycompany.tienda.Tecnico;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ControlServicios {

    private ArrayList<Tarea> tareas;

    public ControlServicios() {
        this.tareas = new ArrayList<>();
    }


    public Tarea crearTarea(ServiciosDigitales servicio, Cliente cliente) {
        System.out.println("Creando nueva tarea para el servicio: " + servicio.obtenerInformacion());

        Tecnico tecnico = SistemaVentas.getGestorPersonal().buscarTecnicoDisponible();

        if (tecnico == null) {
            System.out.println("No hay técnicos disponibles en este momento.");
            return null;
        }

        Tarea tarea = new Tarea(null, servicio, cliente, LocalDateTime.now());

        tarea.asignarTecnico(tecnico);
        tecnico.asignarServicio(servicio);

        tareas.add(tarea);

        guardarTareas();

        System.out.println("   Tarea creada con éxito:");
        System.out.println("   - ID: " + tarea.getIdTarea());
        System.out.println("   - Técnico: " + tecnico.getNombre());
        System.out.println("   - Estado técnico: " + (tecnico.estaDisponible() ? "DISPONIBLE" : "OCUPADO"));

        return tarea;
    }

    public void reasignarTecnico(Tarea tarea, Tecnico nuevoTecnico) {
        if (!tarea.getEstado().puedeAsignarTecnico()) {
            throw new IllegalStateException("No se puede reasignar técnico. La tarea está en estado: "
                    + tarea.getEstado().getNombre());
        }

        if (!nuevoTecnico.estaDisponible()) {
            throw new IllegalStateException("El técnico no está disponible");
        }

        Tecnico tecnicoAnterior = tarea.getTecnicoAsignado();

        if (tecnicoAnterior != null) {
            tecnicoAnterior.completarServicio();
        }

        tarea.asignarTecnico(nuevoTecnico);
        nuevoTecnico.asignarServicio(tarea.getServicio());

        System.out.println("Técnico reasignado: " + nuevoTecnico.getNombre());

        guardarTareas();
    }


    public ArrayList<Tarea> getTareas() {
        return tareas;
    }


    public void cargarTareas(ArrayList<Tarea> tareasCSV) {
        this.tareas = tareasCSV;
        System.out.println("Tareas cargadas en ControlServicios: " + tareas.size());
    }

    public void guardarTareas() {
        // YA NO se eliminan antes de guardar
        // Esto resolvió el problema de que no aparecían hasta reiniciar el programa

        GuardarTareas.guardarEnCSV(this.tareas, "Tareas.csv");
        System.out.println("Tareas guardadas: " + tareas.size());
    }

    public void limpiarTareasFinalizadas() {
        ArrayList<Tarea> tareasActivas = new ArrayList<>();

        for (Tarea tarea : tareas) {
            EstadoTarea estado = tarea.getEstado();

            if (estado != EstadoTarea.COMPLETADA && estado != EstadoTarea.CANCELADA) {
                tareasActivas.add(tarea);
            } else {
                System.out.println("Eliminando tarea finalizada: " + tarea.getIdTarea()
                        + " (Estado: " + estado.getNombre() + ")");
            }
        }

        this.tareas = tareasActivas;
        System.out.println("Tareas activas: " + tareasActivas.size());

        GuardarTareas.guardarEnCSV(this.tareas, "Tareas.csv");
    }
}

