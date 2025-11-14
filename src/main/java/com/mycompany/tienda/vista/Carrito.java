/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.tienda.vista;

import com.mycompany.tienda.CarritoDeCompras;
import com.mycompany.tienda.Cliente;
import com.mycompany.tienda.ItemVenta;
import com.mycompany.tienda.ProductoFisico;
import com.mycompany.tienda.Supervisor;
import com.mycompany.tienda.Venta;
import com.mycompany.tienda.control.ControlClientes;
import com.mycompany.tienda.control.ControlPersonal;
import com.mycompany.tienda.control.GuardarClientes;
import com.mycompany.tienda.control.SistemaVentas;
import com.mycompany.tienda.modelo.interfaces.MetodoPago;
import com.mycompany.tienda.pagos.PagoTransferencia;
import com.mycompany.tienda.pagos.PagoEfectivo;
import com.mycompany.tienda.pagos.PagoTarjetaDebito;
import com.mycompany.tienda.pagos.PagoTarjetadeCredito;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author samue
 */
public class Carrito extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Carrito.class.getName());
    private CarritoDeCompras carritoCompras;
    private DefaultTableModel modeloTabla;
    private javax.swing.JPanel panelTotales;
    private javax.swing.JButton btnDescuentoEspecial;

    /**
     * Creates new form Carrito
     */
    public Carrito() {
        initComponents();
        inicializarCarrito();
        cargarDatosCarrito();
    }

    private void inicializarCarrito() {
        carritoCompras = MainConSesion.getCarritoGlobal();

        if (carritoCompras == null) {
            System.out.println("Error: carrito no inicializado");
            return;
        }

        modeloTabla = new DefaultTableModel(
                new Object[]{"Producto", "Cantidad", "Precio Unit.", "Subtotal"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCarrito.setModel(modeloTabla);
    }

    private void aplicarDescuentoEspecial() {
        if (carritoCompras.estaVacio()) {
            JOptionPane.showMessageDialog(this,
                    "El carrito está vacío",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Solicitar credenciales de supervisor
        String usuario = JOptionPane.showInputDialog(this,
                "Ingrese usuario de SUPERVISOR:",
                "Autorización Requerida",
                JOptionPane.QUESTION_MESSAGE);

        if (usuario == null || usuario.trim().isEmpty()) {
            return;
        }

        String contrasena = JOptionPane.showInputDialog(this,
                "Ingrese contraseña:",
                "Autorización Requerida",
                JOptionPane.QUESTION_MESSAGE);

        if (contrasena == null || contrasena.trim().isEmpty()) {
            return;
        }

        // Validar credenciales
        ControlPersonal controlPersonal = SistemaVentas.getGestorPersonal();
        com.mycompany.tienda.Empleado empleado = controlPersonal.iniciarSesion(usuario, contrasena);

        if (empleado == null) {
            JOptionPane.showMessageDialog(this,
                    "Credenciales inválidas",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!(empleado instanceof Supervisor)) {
            JOptionPane.showMessageDialog(this,
                    "Solo supervisores pueden autorizar descuentos especiales",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Supervisor supervisor = (Supervisor) empleado;

        // Solicitar porcentaje de descuento
        String input = JOptionPane.showInputDialog(this,
                "Ingrese el porcentaje de descuento (máximo 20%):",
                "Descuento Especial",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            double porcentaje = Double.parseDouble(input);

            if (porcentaje <= 0 || porcentaje > 20) {
                JOptionPane.showMessageDialog(this,
                        "El descuento debe estar entre 0 y 20%",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Autorizar descuento
            if (supervisor.autorizarDescuentoEspecial(porcentaje)) {
                carritoCompras.aplicarDescuentoEspecial(porcentaje / 100.0);
                JOptionPane.showMessageDialog(this,
                        "Descuento especial de " + porcentaje + "% aplicado\n"
                        + "Autorizado por: " + supervisor.getNombre(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                actualizarTotales();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Descuento no autorizado",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosCarrito() {
        modeloTabla.setRowCount(0);

        for (ItemVenta item : carritoCompras.getItems()) {
            String descuentoInfo = "";

            if (item.getProducto() instanceof ProductoFisico) {
                ProductoFisico pf = (ProductoFisico) item.getProducto();
                if (pf.getDescuentoOferta() > 0) {
                    descuentoInfo = String.format("%.0f%%", pf.getDescuentoOferta() * 100);
                } else {
                    descuentoInfo = "0%";
                }
            } else {
                descuentoInfo = "N/A";
            }

            modeloTabla.addRow(new Object[]{
                item.getProducto().getNombre(),
                item.getCantidad(),
                String.format("$%.2f", item.getPrecioUnitario()),
                descuentoInfo,
                String.format("$%.2f", item.getSubtotal())
            });
        }
        actualizarTotales();
    }

    private void actualizarTotales() {
        double subtotal = carritoCompras.calcularSubtotal();
        double descuentoProductos = carritoCompras.calcularDescuentosProductos();
        double descuentoFidelidad = 0.0;
        double descuentoPuntos = carritoCompras.getDescuentoPorPuntos();
        double descuentoEspecial = carritoCompras.getDescuentoEspecial();
        double totalDescuentos = carritoCompras.calcularDescuentos();
        double iva = carritoCompras.calcularIVA();
        double total = carritoCompras.calcularTotalFinal();

        // Calcular descuento de fidelidad si hay cliente
        if (carritoCompras.getCliente() != null) {
            descuentoFidelidad = subtotal * carritoCompras.getCliente().DescuentoFidelidad();
        }

        // Actualizar labels de valores
        lblSubtotalValor.setText(String.format("$%.2f", subtotal));
        lblDescuentosValor.setText(String.format("-$%.2f", totalDescuentos));
        lblIVAValor.setText(String.format("$%.2f", iva));
        lblTotalValor.setText(String.format("$%.2f", total));

        // Crear detalle de descuentos
        StringBuilder detalleDescuentos = new StringBuilder();
        detalleDescuentos.append(" DETALLE DE DESCUENTOS \n");

        boolean hayDescuentos = false;

        if (descuentoProductos > 0) {
            detalleDescuentos.append(String.format(" Descuentos en productos: -$%.2f\n", descuentoProductos));
            hayDescuentos = true;
        }

        if (descuentoFidelidad > 0) {
            int porcentaje = (int) (carritoCompras.getCliente().DescuentoFidelidad() * 100);
            detalleDescuentos.append(String.format(" Descuento fidelidad (%d%%): -$%.2f\n",
                    porcentaje, descuentoFidelidad));
            hayDescuentos = true;
        }

        if (descuentoPuntos > 0) {
            detalleDescuentos.append(String.format(" Descuento por puntos canjeados: -$%.2f\n", descuentoPuntos));
            hayDescuentos = true;
        }

        if (descuentoEspecial > 0) {
            detalleDescuentos.append(String.format(" Descuento especial autorizado: -$%.2f\n", descuentoEspecial));
            hayDescuentos = true;
        }

        if (!hayDescuentos) {
            detalleDescuentos.append("No hay descuentos aplicados\n");
        }

        detalleDescuentos.append(String.format("TOTAL DESCUENTOS: -$%.2f", totalDescuentos));

        txtAreaDetalleDescuentos.setText(detalleDescuentos.toString());
    }

    private void procesarPago() {
        Clientes.resetearDescuentoPuntos();

        if (carritoCompras.estaVacio()) {
            JOptionPane.showMessageDialog(this,
                    "El carrito está vacío",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder resumen = new StringBuilder();
        resumen.append("  RESUMEN DE COMPRA  \n\n");
        for (ItemVenta item : carritoCompras.getItems()) {
            resumen.append(item.getCantidad()).append("x ")
                    .append(item.getProducto().getNombre())
                    .append(" - $").append(String.format("%.2f", item.getSubtotal()))
                    .append("\n");
        }
        resumen.append("\nSubtotal: $").append(String.format("%.2f", carritoCompras.calcularSubtotal()));
        resumen.append("\nDescuentos: $").append(String.format("%.2f", carritoCompras.calcularDescuentos()));
        resumen.append("\nIVA: $").append(String.format("%.2f", carritoCompras.calcularIVA()));
        resumen.append("\nTOTAL: $").append(String.format("%.2f", carritoCompras.calcularTotalFinal()));

        int confirmar = JOptionPane.showConfirmDialog(this,
                resumen.toString() + "\n\n¿Continuar con el pago?",
                "Confirmar Compra",
                JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        Cliente clienteSeleccionado = seleccionarCliente();
        
        if (clienteSeleccionado != null) {
        double descuentoPuntos = Clientes.getDescuentoPuntosCanjeados();
        if (descuentoPuntos > 0) {
            carritoCompras.aplicarDescuentoPorPuntos(descuentoPuntos);
            JOptionPane.showMessageDialog(this,
                    "Descuento por puntos aplicado: $" + String.format("%.2f", descuentoPuntos),
                    "Descuento Aplicado",
                    JOptionPane.INFORMATION_MESSAGE);
            actualizarTotales();
        }
    }

        MetodoPago metodoPago = seleccionarMetodoPago();
        if (metodoPago == null) {
            return;
        }

        if (clienteSeleccionado != null) {
            carritoCompras.setCliente(clienteSeleccionado);
        }

        for (ItemVenta item : carritoCompras.getItems()) {
            if (item.getProducto() instanceof ProductoFisico) {
                ProductoFisico pf = (ProductoFisico) item.getProducto();
                if (!pf.hayStockDisponible(item.getCantidad())) {
                    JOptionPane.showMessageDialog(this,
                            "Stock insuficiente de " + pf.getNombre(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        try {
            Venta venta = new Venta(carritoCompras.getCajero(), clienteSeleccionado);

            for (ItemVenta item : carritoCompras.getItems()) {
                venta.agregarItem(item.getProducto(), item.getCantidad(), SistemaVentas.getInventario());
            }

            boolean pagoExitoso = venta.procesarPago(metodoPago, SistemaVentas.getInventario());

            if (pagoExitoso) {
                reducirStockInventario();

                mostrarResumenVenta(venta);

                carritoCompras.vaciarCarrito();
                cargarDatosCarrito();

                int confirmarEntrega = JOptionPane.showConfirmDialog(this,
                        "¡Venta realizada con éxito!\nFactura: " + venta.getNumeroFactura()
                        + "\n\n¿Entregar productos al cliente?",
                        "Éxito",
                        JOptionPane.YES_NO_OPTION);

                if (confirmarEntrega == JOptionPane.YES_OPTION) {
                    // Marcar como entregada (acumula puntos al cliente)
                    venta.marcarComoEntregada();

                    // Registrar venta en el sistema (para reportes)
                    SistemaVentas.registrarVenta(venta);

                    // Guardar clientes actualizados en CSV
                    GuardarClientes.guardarEnCSV(
                            SistemaVentas.getGestorClientes().getClientes(),
                            "Clientes.csv"
                    );

                    JOptionPane.showMessageDialog(this,
                            "Productos entregados\nCliente obtuvo sus puntos",
                            "Completado",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Registrar venta sin entregar (también va a reportes)
                    SistemaVentas.registrarVenta(venta);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al procesar la venta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Cliente seleccionarCliente() {
        String[] opciones = {"Seleccionar Cliente Existente", "Venta Sin Cliente", "Crear Nuevo Cliente"};
        int seleccion = JOptionPane.showOptionDialog(this,
                "¿Desea asignar un cliente a esta venta?",
                "Cliente",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == 0) {
            return seleccionarClienteExistente();
        } else if (seleccion == 2) {
            JOptionPane.showMessageDialog(this,
                    "Por favor cree el cliente desde el menú principal antes de procesar la venta",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        return null;
    }

    private Cliente seleccionarClienteExistente() {
        ControlClientes control = SistemaVentas.getGestorClientes();

        if (control.getClientes().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay clientes registrados en el sistema",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        ArrayList<String> nombresClientes = new ArrayList<>();
        ArrayList<Cliente> listaClientes = new ArrayList<>();

        for (Cliente c : control.getClientes().values()) {
            nombresClientes.add(c.getNombre() + " - " + c.getCedula());
            listaClientes.add(c);
        }

        String[] opciones = nombresClientes.toArray(new String[0]);

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione un cliente:",
                "Clientes",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion == null) {
            return null;
        }

        int indice = nombresClientes.indexOf(seleccion);
        return listaClientes.get(indice);
    }

    private MetodoPago seleccionarMetodoPago() {
        String[] metodos = {"Efectivo", "Tarjeta Débito", "Tarjeta Crédito", "Transferencia"};
        String seleccion = (String) JOptionPane.showInputDialog(this,
                "Seleccione el método de pago:",
                "Método de Pago",
                JOptionPane.QUESTION_MESSAGE,
                null,
                metodos,
                metodos[0]);

        if (seleccion == null) {
            return null; // Usuario canceló
        }

        try {
            switch (seleccion) {
                case "Efectivo":
                    return crearPagoEfectivo();
                case "Tarjeta Débito":
                    return crearPagoTarjetaDebito();
                case "Tarjeta Crédito":
                    return crearPagoTarjetaCredito();
                case "Transferencia":
                    return crearPagoTransferencia();
                default:
                    return null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear método de pago: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private MetodoPago crearPagoEfectivo() {
        String input = JOptionPane.showInputDialog(this,
                "Total a pagar: $" + String.format("%.2f", carritoCompras.calcularTotalFinal())
                + "\n\nIngrese el efectivo recibido:",
                "Pago en Efectivo",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null) {
            return null;
        }

        try {
            double efectivo = Double.parseDouble(input);
            return new PagoEfectivo(efectivo);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Monto inválido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private MetodoPago crearPagoTarjetaDebito() {
        String numeroTarjeta = JOptionPane.showInputDialog(this,
                "Ingrese el número de tarjeta (16 dígitos):",
                "Pago con Tarjeta Débito",
                JOptionPane.QUESTION_MESSAGE);

        if (numeroTarjeta == null) {
            return null;
        }

        return new PagoTarjetaDebito(numeroTarjeta);
    }

    private MetodoPago crearPagoTarjetaCredito() {
        String numeroTarjeta = JOptionPane.showInputDialog(this,
                "Ingrese el número de tarjeta (16 dígitos):",
                "Pago con Tarjeta Crédito",
                JOptionPane.QUESTION_MESSAGE);

        if (numeroTarjeta == null) {
            return null;
        }

        String cuotasStr = JOptionPane.showInputDialog(this,
                "Ingrese el número de cuotas (1-36):",
                "Cuotas",
                JOptionPane.QUESTION_MESSAGE);

        if (cuotasStr == null) {
            return null;
        }

        try {
            int cuotas = Integer.parseInt(cuotasStr);
            return new PagoTarjetadeCredito(numeroTarjeta, cuotas);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Número de cuotas inválido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private MetodoPago crearPagoTransferencia() {
        String referencia = JOptionPane.showInputDialog(this,
                "Ingrese el número de referencia de la transferencia:",
                "Pago por Transferencia",
                JOptionPane.QUESTION_MESSAGE);

        if (referencia == null) {
            return null;
        }

        return new PagoTransferencia(referencia);
    }

    private void reducirStockInventario() {
        for (ItemVenta item : carritoCompras.getItems()) {
            if (item.getProducto() instanceof com.mycompany.tienda.ProductoFisico) {
                com.mycompany.tienda.ProductoFisico pf = (com.mycompany.tienda.ProductoFisico) item.getProducto();
                SistemaVentas.getInventario().reducirStock(pf.getCodigoBarras(), item.getCantidad());
            }
        }
    }

    private void mostrarResumenVenta(Venta venta) {
        StringBuilder resumen = new StringBuilder();
        resumen.append(" FACTURA \n\n");
        resumen.append("Numero: ").append(venta.getNumeroFactura()).append("\n");

        if (venta.getCliente() != null) {
            resumen.append("Cliente: ").append(venta.getCliente().getNombre()).append("\n");
            resumen.append("Cédula: ").append(venta.getCliente().getCedula()).append("\n");
        } else {
            resumen.append("Cliente: Sin cliente registrado\n");
        }

        resumen.append("\n PRODUCTOS \n");
        for (ItemVenta item : venta.getItems()) {
            resumen.append("• ").append(item.getCantidad()).append("x ")
                    .append(item.getProducto().getNombre())
                    .append("\n  $").append(String.format("%.2f", item.getPrecioUnitario()))
                    .append(" c/u = $").append(String.format("%.2f", item.getSubtotal()))
                    .append("\n");
        }

        resumen.append("\n TOTALES \n");
        resumen.append("Subtotal:     $").append(String.format("%.2f", carritoCompras.calcularSubtotal())).append("\n");

        double descProd = carritoCompras.calcularDescuentosProductos();
        if (descProd > 0) {
            resumen.append("Desc. Productos: -$").append(String.format("%.2f", descProd)).append("\n");
        }

        if (carritoCompras.getCliente() != null) {
            double descFidelidad = carritoCompras.calcularSubtotal() * carritoCompras.getCliente().DescuentoFidelidad();
            if (descFidelidad > 0) {
                resumen.append("Desc. Fidelidad: -$").append(String.format("%.2f", descFidelidad)).append("\n");
            }
        }

        if (carritoCompras.getDescuentoEspecial() > 0) {
            resumen.append("Desc. Especial: -$").append(String.format("%.2f", carritoCompras.getDescuentoEspecial())).append("\n");
        }

        if (carritoCompras.getDescuentoPorPuntos() > 0) {
            resumen.append("Desc. por Puntos: -$").append(String.format("%.2f", carritoCompras.getDescuentoPorPuntos())).append("\n");
        }

        resumen.append("IVA (19%):    $").append(String.format("%.2f", carritoCompras.calcularIVA())).append("\n");
        resumen.append("\nTOTAL FINAL:  $").append(String.format("%.2f", venta.getTotal())).append("\n");

        JOptionPane.showMessageDialog(this,
                resumen.toString(),
                "Resumen de Venta",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCarrito = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        btnVaciar = new javax.swing.JButton();
        btnProcesarPago = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        labelSubtotal = new javax.swing.JLabel();
        labelDescuentos = new javax.swing.JLabel();
        labelIVA = new javax.swing.JLabel();
        labealTotal = new javax.swing.JLabel();
        scrollDetalleDescuentos = new java.awt.ScrollPane();
        txtAreaDetalleDescuentos = new java.awt.TextArea();
        lblSubtotalValor = new javax.swing.JLabel();
        lblDescuentosValor = new javax.swing.JLabel();
        lblIVAValor = new javax.swing.JLabel();
        lblTotalValor = new javax.swing.JLabel();
        btnDescuentoEspeciall = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Carrito");

        jButton1.setText("Volver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tablaCarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaCarrito);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnVaciar.setText("Vaciar");
        btnVaciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVaciarActionPerformed(evt);
            }
        });

        btnProcesarPago.setText("Procesar Pago");
        btnProcesarPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcesarPagoActionPerformed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridLayout(4, 2));

        labelSubtotal.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        labelSubtotal.setForeground(new java.awt.Color(0, 204, 51));
        labelSubtotal.setText("SUB TOTAL: ");

        labelDescuentos.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        labelDescuentos.setForeground(new java.awt.Color(0, 204, 51));
        labelDescuentos.setText("DESCUENTOS:");

        labelIVA.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        labelIVA.setForeground(new java.awt.Color(0, 204, 51));
        labelIVA.setText("IVA:");

        labealTotal.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        labealTotal.setForeground(new java.awt.Color(0, 204, 51));
        labealTotal.setText("TOTAL:");

        scrollDetalleDescuentos.add(txtAreaDetalleDescuentos);

        lblSubtotalValor.setFont(new java.awt.Font("Microsoft Tai Le", 0, 14)); // NOI18N
        lblSubtotalValor.setText("$0.00");

        lblDescuentosValor.setFont(new java.awt.Font("Microsoft Tai Le", 0, 14)); // NOI18N
        lblDescuentosValor.setText("$0.00");

        lblIVAValor.setFont(new java.awt.Font("Microsoft Tai Le", 0, 14)); // NOI18N
        lblIVAValor.setText("$0.00");

        lblTotalValor.setFont(new java.awt.Font("Microsoft Tai Le", 0, 14)); // NOI18N
        lblTotalValor.setText("$0.00");

        btnDescuentoEspeciall.setText("Descuento Especial");
        btnDescuentoEspeciall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescuentoEspeciallActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addGap(18, 18, 18)
                .addComponent(btnVaciar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnProcesarPago)
                .addGap(18, 18, 18)
                .addComponent(btnDescuentoEspeciall, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(scrollDetalleDescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblDescuentosValor, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(labealTotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(labelIVA, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(labelDescuentos, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addComponent(labelSubtotal))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(25, 25, 25)
                                            .addComponent(lblSubtotalValor, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lblIVAValor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lblTotalValor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(scrollDetalleDescuentos, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSubtotal)
                    .addComponent(lblSubtotalValor))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDescuentos)
                    .addComponent(lblDescuentosValor))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelIVA)
                    .addComponent(lblIVAValor))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labealTotal)
                    .addComponent(lblTotalValor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnEliminar)
                    .addComponent(btnVaciar)
                    .addComponent(btnProcesarPago)
                    .addComponent(btnDescuentoEspeciall))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        MainConSesion cuf = new MainConSesion();
        cuf.setVisible(true);
        Carrito.this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int filaSeleccionada = tablaCarrito.getSelectedRow();
        if (filaSeleccionada >= 0) {
            carritoCompras.eliminarItem(filaSeleccionada);
            cargarDatosCarrito();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Selecciona un producto para eliminar",
                    "Advertencia",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnVaciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVaciarActionPerformed
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de cancelar esta venta?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            carritoCompras.vaciarCarrito();
            cargarDatosCarrito();
            JOptionPane.showMessageDialog(this,
                    "Venta cancelada",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnVaciarActionPerformed

    private void btnProcesarPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesarPagoActionPerformed
        procesarPago();
    }//GEN-LAST:event_btnProcesarPagoActionPerformed

    private void btnDescuentoEspeciallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescuentoEspeciallActionPerformed
        // TODO add your handling code here:
        aplicarDescuentoEspecial();
    }//GEN-LAST:event_btnDescuentoEspeciallActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Carrito().setVisible(true));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDescuentoEspeciall;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnProcesarPago;
    private javax.swing.JButton btnVaciar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labealTotal;
    private javax.swing.JLabel labelDescuentos;
    private javax.swing.JLabel labelIVA;
    private javax.swing.JLabel labelSubtotal;
    private javax.swing.JLabel lblDescuentosValor;
    private javax.swing.JLabel lblIVAValor;
    private javax.swing.JLabel lblSubtotalValor;
    private javax.swing.JLabel lblTotalValor;
    private java.awt.ScrollPane scrollDetalleDescuentos;
    private javax.swing.JTable tablaCarrito;
    private java.awt.TextArea txtAreaDetalleDescuentos;
    // End of variables declaration//GEN-END:variables
}
