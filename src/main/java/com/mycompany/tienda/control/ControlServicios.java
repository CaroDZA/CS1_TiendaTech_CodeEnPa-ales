package com.mycompany.tienda.control;

import com.mycompany.tienda.Cliente;
import com.mycompany.tienda.ServiciosDigitales;
import com.mycompany.tienda.Tarea;
import com.mycompany.tienda.Tecnico;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ControlServicios {

    private ArrayList<Tarea> tareas;
    private ArrayList<Tecnico> tecnicos;

    public ControlServicios() {
        this.tareas = new ArrayList<>();
        this.tecnicos = new ArrayList<>();
    }

    public void registrarTecnico(Tecnico tecnico) {
        tecnicos.add(tecnico);
    }

    public Tarea crearTarea(ServiciosDigitales servicio, Cliente cliente) {
        System.out.println("Creando nueva tarea para el servicio: " + servicio.obtenerInformacion());

        Tecnico tecnico = buscarTecnicoDisponible();

        if (tecnico == null) {
            System.out.println("No hay técnicos disponibles en este momento.");
            return null;
        }

        Tarea tarea = new Tarea(null, servicio, cliente, LocalDateTime.now());
        tarea.asignarTecnico(tecnico);
        tecnico.asignarServicio(servicio);

        tareas.add(tarea);

        System.out.println("Tarea creada con éxito" + "Id tarea: " + tarea.getIdTarea() + " y asignada a " + tecnico.getNombre());
        return tarea;
    }

    public Tecnico buscarTecnicoDisponible() {
        for (Tecnico tecnico : tecnicos) {
            if (tecnico.estaDisponible()) {
                return tecnico;
            }
        }
        return null; //¿Aquí podría ir excepción?, preguntar
    }

}
