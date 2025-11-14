/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.tienda.vista;

import com.mycompany.tienda.Cajero;
import com.mycompany.tienda.ItemVenta;
import com.mycompany.tienda.ProductoFisico;
import com.mycompany.tienda.Venta;
import com.mycompany.tienda.control.SistemaVentas;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
/**
 *
 * @author samue
 */
public class ReportesG extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ReportesG.class.getName());
    private String periodoActual = "HOY";
    
    // Borde simple y básico para el estilo "aprendiz"
    private final Border bordeAprendiz = BorderFactory.createLineBorder(Color.BLACK);
    // Fuente más simple para el estilo "aprendiz"
    private final Font fuenteNormal = new Font("SansSerif", Font.PLAIN, 12);
    private final Font fuenteTitulo = new Font("SansSerif", Font.BOLD, 16);
    private final Font fuenteGrande = new Font("SansSerif", Font.BOLD, 24);

    /**
     * Creates new form ReportesG
     */
    public ReportesG() {
        initComponents();
        crearPanelReportes();
    }

    private void crearPanelReportes() {
        jPanel1.removeAll();
        // Usamos un layout simple de 2 columnas
        jPanel1.setLayout(new GridBagLayout()); 
        jPanel1.setBackground(new Color(230, 230, 230)); // Fondo gris claro
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Rellenar ambos para un aspecto más "cuadrado"
        gbc.insets = new Insets(8, 8, 8, 8); // Márgenes reducidos
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Hacemos que los paneles se estiren verticalmente

        // Filtros de período
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa 2 columnas
        jPanel1.add(crearPanelFiltros(), gbc);

        // Ventas por cajero
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Ocupa 1 columna
        jPanel1.add(crearPanelVentasCajero(), gbc);

        // Total ventas
        gbc.gridx = 1;
        jPanel1.add(crearPanelTotalVentas(), gbc);

        // Productos más vendidos
        gbc.gridx = 0;
        gbc.gridy = 2;
        jPanel1.add(crearPanelProductosMasVendidos(), gbc);

        // Stock bajo
        gbc.gridx = 1;
        jPanel1.add(crearPanelStockBajo(), gbc);

        // Botón exportar
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Ocupa 2 columnas
        JButton btnExportar = new JButton("EXPORTAR A TXT"); // Texto en mayúsculas
        btnExportar.setFont(fuenteTitulo); // Fuente más simple
        btnExportar.setBackground(new Color(150, 150, 150)); // Color gris simple
        btnExportar.setForeground(Color.BLACK);
        btnExportar.setBorder(bordeAprendiz); // Borde de "aprendiz"
        btnExportar.addActionListener(e -> exportarReporte());
        jPanel1.add(btnExportar, gbc);

        jPanel1.revalidate();
        jPanel1.repaint();
    }
    
    private JPanel crearPanelFiltros() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(200, 200, 200)); // Gris más oscuro y plano
        panel.setBorder(BorderFactory.createTitledBorder(bordeAprendiz, "PERIODO")); // Borde simple y título en mayúsculas

        String[] periodos = {"HOY", "SEMANA", "MES", "AÑO"}; // Textos más cortos
        for (String periodo : periodos) {
            JButton btn = new JButton(periodo);
            btn.setFont(fuenteNormal);
            btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY)); // Borde muy simple para los botones
            
            if (periodo.equals(periodoActual.replace("ESTA ", "").replace("ESTE ", ""))) { // Adaptar la comparación
                btn.setBackground(Color.DARK_GRAY); // Color oscuro simple para selección
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(Color.LIGHT_GRAY); // Color claro para no seleccionados
                btn.setForeground(Color.BLACK);
            }
            
            btn.addActionListener(e -> {
                periodoActual = "ESTA SEMANA".contains(periodo) || "ESTE MES".contains(periodo) || "ESTE AÑO".contains(periodo) ? "ESTA " + periodo : periodo;
                crearPanelReportes();
            });
            panel.add(btn);
        }

        return panel;
    }
    
    private JPanel crearPanelVentasCajero() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(bordeAprendiz, "Ventas x Cajero")); // Borde simple
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.weightx = 1.0;

        HashMap<String, Double> ventasPorCajero = obtenerVentasPorCajero();

        int fila = 0;
        for (Map.Entry<String, Double> entry : ventasPorCajero.entrySet()) {
            gbc.gridy = fila++;
            gbc.gridx = 0;
            JLabel lblNombre = new JLabel(entry.getKey());
            lblNombre.setFont(fuenteNormal);
            panel.add(lblNombre, gbc);

            gbc.gridx = 1;
            JLabel lblTotal = new JLabel(String.format("$%.2f", entry.getValue())); // Formato menos "elegante"
            lblTotal.setFont(fuenteNormal);
            lblTotal.setForeground(Color.BLACK); // Color negro simple
            panel.add(lblTotal, gbc);
        }

        if (ventasPorCajero.isEmpty()) {
            gbc.gridy = 0;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            JLabel lblSinDatos = new JLabel("No hay ventas aun."); // Mensaje simple
            lblSinDatos.setFont(fuenteNormal);
            lblSinDatos.setForeground(Color.RED); // Un color más obvio para la falta de datos
            panel.add(lblSinDatos, gbc);
        }

        return panel;
    }
    
    private JPanel crearPanelTotalVentas() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE); // Fondo blanco simple
        panel.setBorder(BorderFactory.createTitledBorder(bordeAprendiz, "Total VENTAS")); // Borde simple
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        double total = calcularTotalVentas();
        int numVentas = obtenerVentasFiltradas().size();

        JLabel lblTotal = new JLabel(String.format("$%.2f", total)); // Formato menos "elegante"
        lblTotal.setFont(fuenteGrande); // Fuente grande, pero simple
        lblTotal.setForeground(new Color(0, 0, 150)); // Azul oscuro simple, no verde brillante
        gbc.gridy = 0;
        panel.add(lblTotal, gbc);

        JLabel lblNumVentas = new JLabel(numVentas + " ventas hechas"); // Texto simple
        lblNumVentas.setFont(fuenteNormal);
        lblNumVentas.setForeground(Color.BLACK);
        gbc.gridy = 1;
        panel.add(lblNumVentas, gbc);

        return panel;
    }
    
    private JPanel crearPanelProductosMasVendidos() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(bordeAprendiz, "TOP Productos")); // Título corto y simple
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.weightx = 1.0;

        ArrayList<ProductoVendido> topProductos = obtenerProductosMasVendidos();

        int fila = 0;
        for (int i = 0; i < Math.min(10, topProductos.size()); i++) {
            ProductoVendido pv = topProductos.get(i);
            gbc.gridy = fila++;
            
            gbc.gridx = 0;
            gbc.weightx = 0.1;
            JLabel lblPosicion = new JLabel((i + 1) + ")"); // Indicador simple
            lblPosicion.setFont(fuenteNormal);
            panel.add(lblPosicion, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            JLabel lblNombre = new JLabel(pv.nombre);
            lblNombre.setFont(fuenteNormal);
            panel.add(lblNombre, gbc);

            gbc.gridx = 2;
            gbc.weightx = 0.2;
            JLabel lblCantidad = new JLabel(pv.cantidad + " U"); // Unidades abreviadas
            lblCantidad.setFont(fuenteNormal);
            lblCantidad.setForeground(Color.BLACK); // Color negro simple
            panel.add(lblCantidad, gbc);
        }

        if (topProductos.isEmpty()) {
            gbc.gridy = 0;
            gbc.gridx = 0;
            gbc.gridwidth = 3;
            JLabel lblSinDatos = new JLabel("No hay productos vendidos.");
            lblSinDatos.setFont(fuenteNormal);
            lblSinDatos.setForeground(Color.RED);
            panel.add(lblSinDatos, gbc);
        }

        return panel;
    }
    
    private JPanel crearPanelStockBajo() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE); // Fondo blanco simple
        panel.setBorder(BorderFactory.createTitledBorder(bordeAprendiz, "Stock BAJO")); // Título corto y simple
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.weightx = 1.0;

        ArrayList<ProductoFisico> stockBajo = obtenerProductosStockBajo();

        int fila = 0;
        for (ProductoFisico producto : stockBajo) {
            gbc.gridy = fila++;
            
            gbc.gridx = 0;
            gbc.weightx = 0.7;
            JLabel lblNombre = new JLabel("-> " + producto.getNombre()); // Prefijo simple
            lblNombre.setFont(fuenteNormal);
            panel.add(lblNombre, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.3;
            JLabel lblStock = new JLabel("Faltan: " + producto.getStockEnTienda()); // Texto más simple
            lblStock.setFont(fuenteNormal);
            lblStock.setForeground(new Color(200, 0, 0)); // Un rojo fuerte y simple
            panel.add(lblStock, gbc);
        }

        if (stockBajo.isEmpty()) {
            gbc.gridy = 0;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            JLabel lblSinDatos = new JLabel("Todo bien con el stock."); // Mensaje más simple
            lblSinDatos.setFont(fuenteNormal);
            lblSinDatos.setForeground(Color.BLUE); // Color simple para el éxito
            panel.add(lblSinDatos, gbc);
        }

        return panel;
    }
    
    // El resto de los métodos de lógica (obtenerVentasFiltradas, obtenerVentasPorCajero, etc.)
    // no necesitan cambios ya que solo manejan los datos, no la apariencia.
    // Solo se deja el resto de la clase para que el código sea completo y compilable.

    // Métodos de cálculo (sin cambios significativos en la lógica, solo para completar la clase)
    private ArrayList<Venta> obtenerVentasFiltradas() {
        ArrayList<Venta> ventas = SistemaVentas.getHistorialVentas();
        ArrayList<Venta> ventasFiltradas = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();

        for (Venta venta : ventas) {
            LocalDateTime fechaVenta = venta.getFechaHora();
            
            boolean incluir = false;
            switch (periodoActual) {
                case "HOY":
                    incluir = fechaVenta.toLocalDate().equals(ahora.toLocalDate());
                    break;
                case "ESTA SEMANA":
                    incluir = fechaVenta.isAfter(ahora.minusWeeks(1));
                    break;
                case "ESTE MES":
                    incluir = fechaVenta.getMonth() == ahora.getMonth() 
                            && fechaVenta.getYear() == ahora.getYear();
                    break;
                case "ESTE AÑO":
                    incluir = fechaVenta.getYear() == ahora.getYear();
                    break;
            }
            
            if (incluir) {
                ventasFiltradas.add(venta);
            }
        }

        return ventasFiltradas;
    }

    private HashMap<String, Double> obtenerVentasPorCajero() {
        HashMap<String, Double> ventasPorCajero = new HashMap<>();
        ArrayList<Venta> ventas = obtenerVentasFiltradas();

        for (Venta venta : ventas) {
            Cajero cajero = venta.getCajero();
            if (cajero != null) {
                String nombreCajero = cajero.getNombre();
                double totalActual = ventasPorCajero.getOrDefault(nombreCajero, 0.0);
                ventasPorCajero.put(nombreCajero, totalActual + venta.getTotal());
            }
        }

        return ventasPorCajero;
    }

    private double calcularTotalVentas() {
        double total = 0;
        ArrayList<Venta> ventas = obtenerVentasFiltradas();

        for (Venta venta : ventas) {
            total += venta.getTotal();
        }

        return total;
    }

    private ArrayList<ProductoVendido> obtenerProductosMasVendidos() {
        HashMap<String, Integer> conteoProductos = new HashMap<>();
        ArrayList<Venta> ventas = obtenerVentasFiltradas();

        for (Venta venta : ventas) {
            for (ItemVenta item : venta.getItems()) {
                String nombreProducto = item.getProducto().getNombre();
                int cantidadActual = conteoProductos.getOrDefault(nombreProducto, 0);
                conteoProductos.put(nombreProducto, cantidadActual + item.getCantidad());
            }
        }

        // Convertir a lista y ordenar
        ArrayList<ProductoVendido> productos = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : conteoProductos.entrySet()) {
            productos.add(new ProductoVendido(entry.getKey(), entry.getValue()));
        }

        // Ordenar por cantidad descendente
        productos.sort((p1, p2) -> Integer.compare(p2.cantidad, p1.cantidad));

        return productos;
    }

    private ArrayList<ProductoFisico> obtenerProductosStockBajo() {
        ArrayList<ProductoFisico> stockBajo = new ArrayList<>();
        int UMBRAL_STOCK_BAJO = 5;

        for (ProductoFisico producto : SistemaVentas.getInventario().inventario) {
            if (producto.getStockEnTienda() < UMBRAL_STOCK_BAJO) {
                stockBajo.add(producto);
            }
        }

        // Ordenar por stock ascendente
        stockBajo.sort((p1, p2) -> Integer.compare(p1.getStockEnTienda(), p2.getStockEnTienda()));

        return stockBajo;
    }

    private void exportarReporte() {
        try {
            String nombreArchivo = "Reporte_" + periodoActual.replace(" ", "_") + "_" 
                    + LocalDate.now() + ".txt";
            FileWriter writer = new FileWriter(nombreArchivo);

            writer.write("=".repeat(60) + "\n");
            writer.write("           REPORTE DE VENTAS - " + periodoActual + "\n");
            writer.write("           Generado: " + LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            ) + "\n");
            writer.write("=".repeat(60) + "\n\n");

            // Ventas por cajero
            writer.write("VENTAS POR CAJERO:\n");
            writer.write("-".repeat(60) + "\n");
            HashMap<String, Double> ventasCajero = obtenerVentasPorCajero();
            for (Map.Entry<String, Double> entry : ventasCajero.entrySet()) {
                writer.write(String.format("%-30s $%,15.2f\n", entry.getKey(), entry.getValue()));
            }

            // Total
            writer.write("\n" + "=".repeat(60) + "\n");
            writer.write(String.format("TOTAL VENTAS: $%,.2f\n", calcularTotalVentas()));
            writer.write(String.format("Número de ventas: %d\n", obtenerVentasFiltradas().size()));
            writer.write("=".repeat(60) + "\n\n");

            // Productos más vendidos
            writer.write("TOP 10 PRODUCTOS MÁS VENDIDOS:\n");
            writer.write("-".repeat(60) + "\n");
            ArrayList<ProductoVendido> topProductos = obtenerProductosMasVendidos();
            for (int i = 0; i < Math.min(10, topProductos.size()); i++) {
                ProductoVendido pv = topProductos.get(i);
                writer.write(String.format("%2d. %-40s %5d unid.\n", (i + 1), pv.nombre, pv.cantidad));
            }

            // Stock bajo
            writer.write("\n" + "=".repeat(60) + "\n");
            writer.write("PRODUCTOS CON STOCK BAJO:\n");
            writer.write("-".repeat(60) + "\n");
            ArrayList<ProductoFisico> stockBajo = obtenerProductosStockBajo();
            if (stockBajo.isEmpty()) {
                writer.write("✓ Todos los productos tienen stock suficiente\n");
            } else {
                for (ProductoFisico p : stockBajo) {
                    writer.write(String.format("%-40s Stock: %3d\n", p.getNombre(), p.getStockEnTienda()));
                }
            }

            writer.write("\n" + "=".repeat(60) + "\n");
            writer.write("Fin del reporte\n");
            writer.close();

            JOptionPane.showMessageDialog(this,
                    "Reporte exportado exitosamente:\n" + nombreArchivo,
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al exportar reporte: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clase auxiliar
    private static class ProductoVendido {
        String nombre;
        int cantidad;

        ProductoVendido(String nombre, int cantidad) {
            this.nombre = nombre;
            this.cantidad = cantidad;
        }
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
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Reportes Generales");

        jButton1.setText("Volver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 865, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 503, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 867, Short.MAX_VALUE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        MainConSesion cuf = new MainConSesion();
        cuf.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new ReportesG().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
