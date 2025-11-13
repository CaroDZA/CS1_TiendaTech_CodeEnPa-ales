/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda;

import com.mycompany.tienda.control.EstadoTarea;
import java.time.LocalDateTime;

/**
 *
 * @author carod
 */
public class Tarea {

    private static int contador = 1;
    private String idTarea;
    private ServiciosDigitales servicio;
    private Tecnico tecnicoAsignado;
    private Cliente cliente;
    private LocalDateTime fechaHora;
    private EstadoTarea estado;

    public Tarea(String idTarea, ServiciosDigitales servicio, Cliente cliente, LocalDateTime fechaHora) {

        this.idTarea = "ORD-" + contador;
        contador++;
        this.servicio = servicio;
        this.tecnicoAsignado = tecnicoAsignado;
        this.cliente = cliente;
        this.fechaHora = LocalDateTime.now();
        this.estado = EstadoTarea.PENDIENTE;
    }

    public void asignarTecnico(Tecnico tecnico) {
        if (!estado.puedeAsignarTecnico()) {
            throw new IllegalStateException(
                    "No se puede asignar técnico en el estado actual: " + estado.getNombre()
            );
        }

        if (!tecnico.estaDisponible()) {
            throw new IllegalStateException(
                    "El tecnico " + tecnico.getNombre() + " no esta disponible"
            );
        }

        this.tecnicoAsignado = tecnico;

        this.estado = this.estado.transicionar("ASIGNAR_TECNICO");

        System.out.println(" Tecnico " + tecnico.getNombre()
                + " asignado a la tarea " + idTarea
                + " Estado: " + estado.getNombre());
    }

    public void completar() {
        if (!estado.puedeCompletar()) {
            throw new IllegalStateException(
                    "No se puede completar la tarea en el estado actual: " + estado.getNombre()
            );
        }

        this.estado = this.estado.transicionar("COMPLETAR");

        if (tecnicoAsignado != null) {
            tecnicoAsignado.completarServicio();
        }

        System.out.println(" Tarea " + idTarea + " completada  Estado: " + estado.getNombre());
    }

    // Getters
    public String getIdOrden() {
        return idTarea;
    }

    public Tecnico getTecnicoAsignado() {
        return tecnicoAsignado;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public String getIdTarea() {
        return idTarea;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }
}
