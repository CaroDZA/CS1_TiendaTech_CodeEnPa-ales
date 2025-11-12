/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda;

/**
 *
 * @author carod
 */
public class Supervisor extends Empleado {

    public Supervisor(String id, String nombre, String usuario, String contraseña) {
        super(id, nombre, usuario, contraseña);
    }

    @Override
    public boolean tienePermiso(String accion) {
        // Supervisores tienen todos los permisos
        return true;
    }

    public boolean autorizarDescuentoEspecial(double porcentaje) {
        if (porcentaje > 20.0) {
            System.out.println(" Descuento de " + porcentaje + "% excede el límite 20%");
            return false;
        }
        System.out.println("Descuento especial de " + porcentaje + "% autorizado por: " + nombre);
        return true;
    }

    public boolean autorizarVentaMayor(double monto) {
        if (monto > 50000) {
            System.out.println(" Venta mayor ($" + monto + ") autorizada por: " + nombre);
            return true;
        }
        return true; // Ventas menores no requieren autorización
    }

    public void desactivarEmpleado(Empleado empleado) {
        empleado.setActivo(false);
        System.out.println(" Empleado desactivado por supervisor: " + empleado.getNombre());
    }

    public void activarEmpleado(Empleado empleado) {
        empleado.setActivo(true);
        System.out.println(" Empleado activado por supervisor: " + empleado.getNombre());
    }

    public void crearCategoria(String nombreCategoria) {
        try {
            ProductoBase.agregarCategoria(nombreCategoria);
            System.out.println(" Categoría creada por supervisor: " + nombre);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void eliminarCategoria(String nombreCategoria) {
        ProductoBase.eliminarCategoria(nombreCategoria);
        System.out.println(" Acción realizada por supervisor: " + nombre);
    }

    public void verCategorias() {
        ProductoBase.mostrarCategorias();
    }

    @Override
    public String toString() {
        return "Supervisor - " + super.toString();
    }
}
