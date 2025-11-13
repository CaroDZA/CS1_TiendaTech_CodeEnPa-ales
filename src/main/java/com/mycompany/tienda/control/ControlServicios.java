package com.mycompany.tienda.control;

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

        System.out.println("Tarea creada con éxito - Id tarea: " + tarea.getIdTarea() + " y asignada a " + tecnico.getNombre());
        return tarea;
    }

    public void reasignarTecnico(Tarea tarea, Tecnico nuevoTecnico) {
        if (!tarea.getEstado().puedeAsignarTecnico()) {
            throw new IllegalStateException("No se puede reasignar técnico. La tarea está en estado: " + tarea.getEstado().getNombre());
        }

        if (!nuevoTecnico.estaDisponible()) {
            throw new IllegalStateException("El técnico no está disponible");
        }

        Tecnico tecnicoAnterior = tarea.getTecnicoAsignado();

        if (tecnicoAnterior != null) {
            tecnicoAnterior.completarServicio();
        }

        tarea.asignarTecnico(nuevoTecnico);
        nuevoTecnico.asignarServicio(null);

        System.out.println("Técnico reasignado: " + nuevoTecnico.getNombre());
    }

    public ArrayList<Tarea> getTareas() {
        return tareas;
    }

}
