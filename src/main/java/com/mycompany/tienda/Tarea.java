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
        this.idTarea = "ORD-" + contador++;
        this.servicio = servicio;
        this.tecnicoAsignado = null;
        this.cliente = cliente;
        this.fechaHora = LocalDateTime.now();
        this.estado = EstadoTarea.PENDIENTE;
    }

    public Tarea(String idTareaExistente, ServiciosDigitales servicio, Cliente cliente, LocalDateTime fechaHora, boolean desdeCSV) {
        this.idTarea = idTareaExistente;
        this.servicio = servicio;
        this.tecnicoAsignado = null;
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.estado = EstadoTarea.PENDIENTE;

        if (desdeCSV) {
            actualizarContador(idTareaExistente);
        }
    }

    private static void actualizarContador(String idTarea) {
        try {
            int numero = Integer.parseInt(idTarea.replace("ORD-", "").trim());
            if (numero >= contador) {
                contador = numero + 1;
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar contador: " + e.getMessage());
        }
    }

    public void asignarTecnico(Tecnico tecnico) {
        if (!estado.puedeAsignarTecnico()) {
            throw new IllegalStateException("No se puede asignar técnico en estado: " + estado.getNombre());
        }
        this.tecnicoAsignado = tecnico;
        this.estado = this.estado.transicionar("ASIGNAR_TECNICO");
        tecnico.agregarTarea(this);
        System.out.println("Tecnico " + tecnico.getNombre() + " asignado a tarea " + idTarea);
    }

    public void iniciarTarea() {
        if (!estado.puedeIniciar()) {
            throw new IllegalStateException("No se puede iniciar tarea en estado: " + estado.getNombre());
        }
        this.estado = this.estado.transicionar("INICIAR");
        System.out.println("Tarea " + idTarea + " iniciada");
    }

    public void completar() {
        if (!estado.puedeCompletar()) {
            throw new IllegalStateException("No se puede completar tarea en estado: " + estado.getNombre());
        }
        if (tecnicoAsignado != null) {
            tecnicoAsignado.completarServicio();
        }
        this.estado = this.estado.transicionar("COMPLETAR");
        System.out.println("Tarea " + idTarea + " completada");
    }

    public String getIdTarea() {
        return idTarea;
    }

    public Tecnico getTecnicoAsignado() {
        return tecnicoAsignado;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public ServiciosDigitales getServicio() {
        return servicio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
        if ((estado == EstadoTarea.CANCELADA || estado == EstadoTarea.COMPLETADA) && tecnicoAsignado != null) {
            tecnicoAsignado.completarServicio();
        }
    }

    public void setTecnicoAsignado(Tecnico tecnico) {
        this.tecnicoAsignado = tecnico;
        if (tecnico != null) {
            tecnico.agregarTarea(this);
        }
    }
}
