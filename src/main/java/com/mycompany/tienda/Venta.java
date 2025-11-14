package com.mycompany.tienda;

import com.mycompany.tienda.modelo.interfaces.MetodoPago;
import com.mycompany.tienda.modelo.interfaces.Vendido;
import com.mycompany.tienda.control.ControlInventario;
import com.mycompany.tienda.control.EstadoVenta;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Venta {

    private static int contadorVentas = 0;

    private List<String> productos;
    private String numeroFactura;
    private Cajero cajero;
    private Cliente cliente;
    private ArrayList<ItemVenta> items;
    private EstadoVenta estado;
    private LocalDateTime fechaHora;
    private MetodoPago metodoPago;
    private int id;

    private double subtotal;
    private double descuentos;
    private double iva;
    private double total;

    private static final double tasaIva = 0.19;

    public Venta(Cajero cajero, Cliente cliente) {
        this.cajero = cajero;
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.productos = new ArrayList<>();
        this.fechaHora = LocalDateTime.now();
        this.estado = EstadoVenta.EN_PROCESO;
        this.subtotal = 0.0;
        this.descuentos = 0.0;
        this.iva = 0.0;
        this.total = 0.0;
    }

    public boolean agregarProducto(String producto) {
        if (!estado.puedeAgregarProductos()) {
            return false;
        }
        productos.add(producto);
        return true;
    }

    public void agregarItem(Vendido producto, int cantidad, ControlInventario inventario) {
        if (!estado.puedeAgregarProductos()) {
            throw new IllegalStateException("No se pueden agregar items" + estado.getNombre());
        }

        boolean encontrado = false;
        for (ItemVenta item : items) {
            if (item.getProducto().getNombre().equals(producto.getNombre())) {
                item.setCantidad(item.getCantidad() + cantidad);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            ItemVenta item = new ItemVenta(producto, cantidad);
            items.add(item);
        }
    }
    
    public void eliminarItem(int indice) {
        if (indice < 0 || indice >= items.size()) {
            throw new IllegalArgumentException("Índice inválido");
        }

        if (!estado.puedeAgregarProductos()) {
            throw new IllegalStateException("No se pueden eliminar items en el estado actual de la venta: " + estado.getNombre());
        }

        ItemVenta eliminado = items.remove(indice);
        System.out.println("Eliminado: " + eliminado.getProducto().getNombre());
    }

    public void aplicarDescuentoEspecial(double porcentaje, Supervisor supervisor) {

        if (!supervisor.autorizarDescuentoEspecial(porcentaje)) {
            throw new IllegalArgumentException("Descuento no autorizado");
        }

        double descuentoAdicional = subtotal * porcentaje;
        this.descuentos += descuentoAdicional;
    }

    private double calcularSubtotal() {
        double subtotal = 0;
        for (ItemVenta item : items) {
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }

    private double calcularDescuentos() {
        double descuentoFidelidad = 0;

        if (cliente != null) {
            double subtotal = calcularSubtotal();
            descuentoFidelidad = subtotal * cliente.DescuentoFidelidad();
        }

        return descuentoFidelidad;
    }

    private double calcularIVA() {
        double baseImponible = calcularSubtotal() - calcularDescuentos();
        return baseImponible * tasaIva;
    }

    private double calcularTotalFinal() {
        double subtotal = calcularSubtotal();
        double descuentos = calcularDescuentos();
        double iva = calcularIVA();
        return subtotal - descuentos + iva;
    }

    public boolean procesarPago(MetodoPago pago, ControlInventario inventario) {
        if (items.isEmpty()) {
            System.out.println("El carrito está vacío");
            return false;
        }

        if (!estado.puedeConfirmarPago()) {
            System.out.println("No se puede procesar el pago en el estado actual: " + estado.getNombre());
            return false;
        }

        this.subtotal = calcularSubtotal();
        this.descuentos = calcularDescuentos();
        this.iva = calcularIVA();
        this.total = calcularTotalFinal();

        double montoFinal = pago.calcularMontoFinal(total);

        String resultado = pago.procesarPago(montoFinal);
        System.out.println(resultado);

        if (resultado.contains("exitoso")) {
            this.metodoPago = pago;
            this.estado = this.estado.transicionar("PAGAR");

            generarNumeroFactura();
        }
        return true;
    }

    public void marcarComoEntregada() {
        if (!estado.puedeEntregar()) {
            throw new IllegalStateException("No se puede entregar en este estado");
        }

        this.estado = this.estado.transicionar("ENTREGAR");

        if (cliente != null) {
            cliente.acumularPuntos(total);
            cliente.incrementarCompras();
        }

        System.out.println("Venta entregada: " + numeroFactura);
    }

    public boolean confirmarPago() {
        if (!estado.puedeConfirmarPago()) {
            return false;
        }
        estado = estado.transicionar("PAGAR");
        return true;
    }

    public boolean entregar() {
        if (!estado.puedeEntregar()) {
            return false;
        }
        estado = estado.transicionar("ENTREGAR");
        return true;
    }

    public boolean cancelar() {
        if (!estado.puedeCancelar()) {
            return false;
        }
        estado = estado.transicionar("CANCELAR");
        return true;
    }

    private void generarNumeroFactura() {
        contadorVentas++;
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fecha = ahora.format(formato);

        this.numeroFactura = "FACT-" + fecha + "-" + String.format("%03d", contadorVentas);
    }

    public void mostrarResumen() {
        if (numeroFactura != null) {
            System.out.println("Factura: " + numeroFactura);
        }
        if (cliente != null) {
            System.out.println("Cliente: " + cliente.getNombre());
        }
        System.out.println("\nFecha: " + fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        System.out.println("\n Totales ");
        System.out.println("Subtotal:    $" + String.format("%.2f", subtotal));
        System.out.println("Descuentos: -$" + String.format("%.2f", descuentos));
        System.out.println("IVA (19%):   $" + String.format("%.2f", iva));
        System.out.println("TOTAL:       $" + String.format("%.2f", total));

        if (metodoPago != null) {
            System.out.println("Método de pago: " + metodoPago.getTipo());
        }
        System.out.println("Estado: " + estado.getNombre());
    }

    // Getters
    public String getEstadoActual() {
        return estado.getNombre();
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public EstadoVenta getEstado() {
        return estado;
    }

    public double getTotal() {
        return total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public ArrayList<ItemVenta> getItems() {
        return items;
    }

    public Cajero getCajero() {
        return cajero;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public int getId() {
        return id;
    }

}