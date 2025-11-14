/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda.control;

import com.mycompany.tienda.archivos.CargadorClientes;
import com.mycompany.tienda.archivos.CargadorEmpleados;
import com.mycompany.tienda.archivos.CargadorProductos;
import com.mycompany.tienda.archivos.CargadorTareas;
import com.mycompany.tienda.Cliente;
import com.mycompany.tienda.Empleado;
import com.mycompany.tienda.ProductoFisico;
import com.mycompany.tienda.Tarea;
import com.mycompany.tienda.Venta;
import java.util.ArrayList;

/**
 * @author samue
 */
public class SistemaVentas {

    private static ControlClientes gestorClientes;
    private static ControlInventario inventario;
    private static ControlPersonal gestorPersonal;
    private static Empleado empleadoActual;
    private static ArrayList<Venta> historialVentas = new ArrayList<>();
    private static ControlServicios gestorServicios;

    public static void inicializar() {

        try {
            gestorClientes = new ControlClientes();
            inventario = new ControlInventario();
            gestorPersonal = new ControlPersonal();
            gestorServicios = new ControlServicios();

            cargarDatosIniciales();
        } catch (Exception e) {
            System.err.println("Error al inicializar el sistema: " + e.getMessage());
        }
    }

    public static ControlServicios getGestorServicios() {
        if (gestorServicios == null) {
            System.err.println("Sistema no inicializado. Inicializando...");
            inicializar();
        }
        return gestorServicios;
    }

    private static void cargarDatosIniciales() {
        System.out.println("Cargando datos iniciales...");

        try {
            ArrayList<Empleado> EmpleadosCSV = CargadorEmpleados.cargarDesdeCSV("Empleados.csv");

            for (Empleado empleado : EmpleadosCSV) {
                gestorPersonal.agregarEmpleadoSilencioso(empleado);
            }
            System.out.println("Empleados cargados desde CSV: " + EmpleadosCSV.size());

        } catch (Exception e) {
            System.err.println(" Error al cargar empleados desde CSV: " + e.getMessage());
        }

        try {
            ArrayList<Cliente> ClientesCSV = CargadorClientes.cargarDesdeCSV("Clientes.csv");

            for (Cliente cliente : ClientesCSV) {
                gestorClientes.getClientes().put(cliente.getCedula(), cliente);
            }
            System.out.println("Clientes cargados desde CSV: " + ClientesCSV.size());

        } catch (Exception e) {
            System.err.println(" Error al cargar clientes desde CSV: " + e.getMessage());
        }

        try {
            ArrayList<ProductoFisico> ProductosCSV = CargadorProductos.cargarDesdeCSV("Productos.csv");

            for (ProductoFisico producto : ProductosCSV) {
                inventario.agregarProducto(producto);
            }

            System.out.println("Productos cargados desde CSV: " + ProductosCSV.size());

        } catch (Exception e) {
            System.err.println(" Error al cargar productos desde CSV: " + e.getMessage());
        }

        try {
            ArrayList<Tarea> TareasCSV = CargadorTareas.cargarDesdeCSV(
                    "Tareas.csv",
                    gestorPersonal,
                    gestorClientes
            );

            gestorServicios.cargarTareas(TareasCSV);
            System.out.println("Tareas cargadas desde CSV: " + TareasCSV.size());

        } catch (Exception e) {
            System.err.println("Error al cargar tareas desde CSV: " + e.getMessage());
        }
    }

    public static ControlClientes getGestorClientes() {
        if (gestorClientes == null) {
            System.err.println(" Sistema no inicializado. Inicializando...");
            inicializar();
        }
        return gestorClientes;
    }

    public static ControlInventario getInventario() {
        if (inventario == null) {
            System.err.println(" Sistema no inicializado. Inicializando...");
            inicializar();
        }
        return inventario;
    }

    public static ControlPersonal getGestorPersonal() {
        if (gestorPersonal == null) {
            System.err.println(" Sistema no inicializado. Inicializando...");
            inicializar();
        }
        return gestorPersonal;
    }

    public static Empleado getEmpleadoActual() {
        return empleadoActual;
    }

    public static void setEmpleadoActual(Empleado empleado) {
        empleadoActual = empleado;
        if (empleado != null) {
            System.out.println(" Empleado actual: " + empleado.getNombre()
                    + " (" + empleado.getClass().getSimpleName() + ")");
        }
    }

    public static ArrayList<Venta> getHistorialVentas() {
        return historialVentas;
    }

    public static void registrarVenta(Venta venta) {
        historialVentas.add(venta);
    }
}
