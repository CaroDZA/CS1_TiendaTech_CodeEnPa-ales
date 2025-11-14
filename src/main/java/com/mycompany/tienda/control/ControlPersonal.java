/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.control;

import com.mycompany.tienda.Empleado;
import com.mycompany.tienda.Tecnico;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author carod
 */
public class ControlPersonal {

    private HashMap<String, Empleado> empleados; // key = ID
    private Empleado empleadoActual;

    public ControlPersonal() {
        this.empleados = new HashMap<>();
        this.empleadoActual = null;
    }

    public void agregarEmpleado(Empleado empleado) {
        if (empleados.containsKey(empleado.getId())) {
            throw new IllegalArgumentException("Ya existe un empleado con ese ID");
        }
        empleados.put(empleado.getId(), empleado);
        System.out.println("Empleado agregado: " + empleado.getNombre());
    }

    public void agregarEmpleadoSilencioso(Empleado empleado) {
        if (empleados.containsKey(empleado.getId())) {
            throw new IllegalArgumentException("Ya existe un empleado con ese ID");
        }
        empleados.put(empleado.getId(), empleado);
    }

    public Empleado iniciarSesion(String usuario, String contrasena) {
        Empleado empleado = null;
        for (Empleado e : empleados.values()) {
            if (e.getUsuario().equals(usuario)) {
                empleado = e;
                break;
            }
        }
        if (empleado == null) {
            System.out.println("Usuario no encontrado");
            return null;
        }
        if (!empleado.getContraseña().equals(contrasena)) {
            System.out.println("Contraseña incorrecta");
            return null;
        }
        if (!empleado.esActivo()) {
            System.out.println("Empleado inactivo");
            return null;
        }
        this.empleadoActual = empleado;
        System.out.println("Sesion iniciada: " + empleado.getNombre() + " (" + empleado.getClass().getSimpleName() + ")");
        return empleado;
    }

    public void cerrarSesion() {
        if (empleadoActual != null) {
            System.out.println("Sesion cerrada: " + empleadoActual.getNombre());
            this.empleadoActual = null;
        }
    }

    public Empleado getEmpleadoActual() {
        return empleadoActual;
    }

    public HashMap<String, Empleado> getEmpleados() {
        return empleados;
    }

    public Tecnico buscarTecnicoDisponible() {
        for (Empleado empleado : empleados.values()) {
            if (empleado instanceof Tecnico && empleado.esActivo()) {
                Tecnico tecnico = (Tecnico) empleado;
                if (tecnico.estaDisponible()) {
                    return tecnico;
                }
            }
        }
        return null;
    }

    public ArrayList<Tecnico> obtenerTecnicosDisponibles() {
        ArrayList<Tecnico> tecnicos = new ArrayList<>();
        for (Empleado empleado : empleados.values()) {
            if (empleado instanceof Tecnico && empleado.esActivo()) {
                Tecnico tecnico = (Tecnico) empleado;
                if (tecnico.estaDisponible()) {
                    tecnicos.add(tecnico);
                }
            }
        }
        return tecnicos;
    }
}
