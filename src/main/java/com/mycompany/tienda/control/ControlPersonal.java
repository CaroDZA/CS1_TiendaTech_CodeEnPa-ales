/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.control;

import com.mycompany.tienda.Empleado;
import com.mycompany.tienda.Tecnico;
import java.util.HashMap;

/**
 *
 * @author carod
 */
public class ControlPersonal {

    private HashMap<String, Empleado> empleados; // key = usuario
    private Empleado empleadoActual;

    public ControlPersonal() {
        this.empleados = new HashMap<>();
        this.empleadoActual = null;
    }

    // Agregar empleado
    public void agregarEmpleado(Empleado empleado) {
        if (empleados.containsKey(empleado.getUsuario())) {
            throw new IllegalArgumentException("Ya existe un empleado con ese usuario");
        }

        empleados.put(empleado.getUsuario(), empleado);
        System.out.println("Empleado agregado: " + empleado.getNombre());
    }

    public void agregarEmpleadoSilencioso(Empleado empleado) {
        if (empleados.containsKey(empleado.getUsuario())) {
            throw new IllegalArgumentException("Ya existe un empleado con ese usuario");
        }

        empleados.put(empleado.getUsuario(), empleado);
    }

    // Iniciar sesión
    public Empleado iniciarSesion(String usuario, String contrasena) {
        Empleado empleado = empleados.get(usuario);

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

    // Cerrar sesión
    public void cerrarSesion() {
        if (empleadoActual != null) {
            System.out.println("Sesion cerrada: " + empleadoActual.getNombre());
            this.empleadoActual = null;
        }
    }

    public Empleado getEmpleadoActual() {
        return empleadoActual;
    }

    // Buscar técnico disponible
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

}
