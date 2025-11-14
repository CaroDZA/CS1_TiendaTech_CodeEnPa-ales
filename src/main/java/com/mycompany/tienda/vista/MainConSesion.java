/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.tienda.vista;

import com.mycompany.tienda.Cajero;
import com.mycompany.tienda.CarritoDeCompras;
import com.mycompany.tienda.ProductoFisico;
import com.mycompany.tienda.Supervisor;
import com.mycompany.tienda.control.SistemaVentas;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author samue
 */
public class MainConSesion extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainConSesion.class.getName());
    private static CarritoDeCompras carritoGlobal;
    private javax.swing.JComboBox<String> comboFiltroCategoria;

    /**
     * Creates new form MainConSesion
     */
    public MainConSesion() {
        initComponents();
        inicializarFiltroCategoria();
        if (carritoGlobal == null) {
            inicializarCarritoGlobal();
        }
        cargarProductosDinamicamente();
    }

    private void inicializarCarritoGlobal() {
        Object empleado = SistemaVentas.getEmpleadoActual();
        Cajero cajero = null;

        if (empleado instanceof Cajero) {
            cajero = (Cajero) empleado;
        } else if (empleado instanceof Supervisor) {
            // Crear un cajero real con los datos del supervisor
            Supervisor sup = (Supervisor) empleado;
            cajero = new Cajero(sup.getId(), sup.getNombre(),
                    sup.getUsuario(), sup.getContraseña());
        }

        carritoGlobal = new CarritoDeCompras(cajero, null);
    }

    public static void setCarritoGlobal(CarritoDeCompras carrito) {
        carritoGlobal = carrito;
        System.out.println(" Carrito global establecido");
    }

    public static CarritoDeCompras getCarritoGlobal() {
        return carritoGlobal;
    }

    private void inicializarFiltroCategoria() {
        comboFiltroCategoria = new javax.swing.JComboBox<>();
        cargarCategoriasEnCombo();
        
        comboFiltroCategoria.setFont(new java.awt.Font("Microsoft Tai Le", 0, 14));
        comboFiltroCategoria.addActionListener(evt -> cargarProductosDinamicamente());

        // Crear panel para título y filtro
        javax.swing.JPanel panelSuperior = new javax.swing.JPanel();
        panelSuperior.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 10));
        panelSuperior.setBackground(java.awt.Color.WHITE);

        // Agregar label de "Categoría:"
        javax.swing.JLabel lblCategoria = new javax.swing.JLabel("Categoría:");
        lblCategoria.setFont(new java.awt.Font("Microsoft Tai Le", 0, 16));

        panelSuperior.add(jLabel1); // Título "Productos"
        panelSuperior.add(lblCategoria);
        panelSuperior.add(comboFiltroCategoria);

        // Reemplazar jLabel1 con el panel superior
        java.awt.Container contentPane = getContentPane();
        contentPane.remove(jLabel1);
        panelSuperior.setBounds(27, 19, 841, 50);
        contentPane.add(panelSuperior);
        contentPane.revalidate();
        contentPane.repaint();
    }

    private void cargarCategoriasEnCombo() {
        comboFiltroCategoria.removeAllItems();

        // Agregar opción "TODAS" primero
        comboFiltroCategoria.addItem("TODAS");

        // Cargar SOLO categorías de productos (no servicios)
        for (String categoria : com.mycompany.tienda.ProductoBase.obtenerCategoriasProductos()) {
            comboFiltroCategoria.addItem(categoria);
        }
    }
    private void cargarProductosDinamicamente() {
        inventario.removeAll();

        String categoriaSeleccionada = (String) comboFiltroCategoria.getSelectedItem();
        ArrayList<ProductoFisico> productos = SistemaVentas.getInventario().inventario;
        ArrayList<ProductoFisico> productosFiltrados = new ArrayList<>();

        // Filtrar por categoría
        for (ProductoFisico producto : productos) {
            if (categoriaSeleccionada.equals("TODAS") || producto.getCategoria().equals(categoriaSeleccionada)) {
                productosFiltrados.add(producto);
            }
        }

        int columnas = 3;
        int filas = (int) Math.ceil(productosFiltrados.size() / (double) columnas);

        inventario.setLayout(new java.awt.GridLayout(filas, columnas, 20, 20));

        for (ProductoFisico producto : productosFiltrados) {
            javax.swing.JPanel cardProducto = crearCardProducto(producto);
            inventario.add(cardProducto);
        }

        inventario.revalidate();
        inventario.repaint();
    }

    private javax.swing.JPanel crearCardProducto(ProductoFisico producto) {
        javax.swing.JPanel card = new javax.swing.JPanel();
        card.setLayout(new java.awt.BorderLayout(5, 5));
        card.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY, 1));

        // Destacar productos con oferta
        if (producto.getDescuentoOferta() > 0) {
            card.setBackground(new java.awt.Color(255, 240, 240));
            card.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
        } else {
            card.setBackground(java.awt.Color.WHITE);
        }

        // Nombre del producto
        javax.swing.JLabel lblNombre = new javax.swing.JLabel(producto.getNombre());
        lblNombre.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14));
        lblNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Precio
        javax.swing.JLabel lblPrecio;
        if (producto.getDescuentoOferta() > 0) {
            // Mostrar precio con descuento
            double precioOriginal = producto.getPrecio();
            double precioFinal = producto.calcularPrecioFinal();
            lblPrecio = new javax.swing.JLabel(
                    "<html><strike>$" + String.format("%.2f", precioOriginal) + "</strike> "
                    + "<font color='red'>$" + String.format("%.2f", precioFinal) + "</font></html>"
            );
        } else {
            lblPrecio = new javax.swing.JLabel("$" + String.format("%.2f", producto.getPrecio()));
        }
        lblPrecio.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblPrecio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrecio.setForeground(new java.awt.Color(0, 102, 0));

        // Stock
        javax.swing.JLabel lblStock = new javax.swing.JLabel("Stock: " + producto.getStockEnTienda());
        lblStock.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblStock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        // Etiqueta de oferta si aplica
        javax.swing.JPanel panelSuperior = new javax.swing.JPanel();
        panelSuperior.setLayout(new java.awt.BorderLayout());
        panelSuperior.setBackground(card.getBackground());

        if (producto.getDescuentoOferta() > 0) {
            javax.swing.JLabel lblOferta = new javax.swing.JLabel("¡OFERTA " + String.format("%.0f", producto.getDescuentoOferta() * 100) + "%!");
            lblOferta.setFont(new java.awt.Font("Arial", 1, 12));
            lblOferta.setForeground(java.awt.Color.RED);
            lblOferta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            panelSuperior.add(lblOferta, java.awt.BorderLayout.NORTH);
        }

        // Botón agregar
        javax.swing.JButton btnAgregar = new javax.swing.JButton("Agregar al carrito");
        btnAgregar.setBackground(new java.awt.Color(51, 153, 255));
        btnAgregar.setForeground(java.awt.Color.WHITE);
        btnAgregar.setFont(new java.awt.Font("Microsoft Tai Le", 1, 12));
        btnAgregar.addActionListener(e -> agregarAlCarrito(producto));

        // Panel de info
        javax.swing.JPanel panelInfo = new javax.swing.JPanel();
        panelInfo.setLayout(new java.awt.GridLayout(3, 1, 5, 5));
        panelInfo.setBackground(card.getBackground());
        panelInfo.add(lblNombre);
        panelInfo.add(lblPrecio);
        panelInfo.add(lblStock);

        card.add(panelSuperior, java.awt.BorderLayout.NORTH);
        card.add(panelInfo, java.awt.BorderLayout.CENTER);
        card.add(btnAgregar, java.awt.BorderLayout.SOUTH);

        return card;
    }

    private void agregarAlCarrito(ProductoFisico producto) {
        if (producto.getStockEnTienda() > 0) {
            carritoGlobal.agregarItem(producto, 1);
            javax.swing.JOptionPane.showMessageDialog(this,
                    producto.getNombre() + " agregado al carrito",
                    "Confirmación",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            cargarProductosDinamicamente(); // Actualizar vista
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Producto sin stock",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
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

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenuBar4 = new javax.swing.JMenuBar();
        jMenu8 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenuBar5 = new javax.swing.JMenuBar();
        jMenu10 = new javax.swing.JMenu();
        jMenu11 = new javax.swing.JMenu();
        jMenuBar6 = new javax.swing.JMenuBar();
        jMenu12 = new javax.swing.JMenu();
        jMenu13 = new javax.swing.JMenu();
        jMenuBar7 = new javax.swing.JMenuBar();
        jMenu14 = new javax.swing.JMenu();
        jMenu15 = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inventario = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuClientes = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuInventario = new javax.swing.JMenu();
        jMenuCategorias = new javax.swing.JMenu();
        jMenuServicios = new javax.swing.JMenu();
        Reportes = new javax.swing.JMenu();

        jMenu1.setText("File");
        jMenuBar2.add(jMenu1);

        jMenu5.setText("Edit");
        jMenuBar2.add(jMenu5);

        jMenuItem2.setText("jMenuItem2");

        jMenu6.setText("File");
        jMenuBar3.add(jMenu6);

        jMenu7.setText("Edit");
        jMenuBar3.add(jMenu7);

        jMenu8.setText("File");
        jMenuBar4.add(jMenu8);

        jMenu9.setText("Edit");
        jMenuBar4.add(jMenu9);

        jMenu10.setText("File");
        jMenuBar5.add(jMenu10);

        jMenu11.setText("Edit");
        jMenuBar5.add(jMenu11);

        jMenu12.setText("File");
        jMenuBar6.add(jMenu12);

        jMenu13.setText("Edit");
        jMenuBar6.add(jMenu13);

        jMenu14.setText("File");
        jMenuBar7.add(jMenu14);

        jMenu15.setText("Edit");
        jMenuBar7.add(jMenu15);

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        jMenuItem3.setText("jMenuItem3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Productos");

        javax.swing.GroupLayout inventarioLayout = new javax.swing.GroupLayout(inventario);
        inventario.setLayout(inventarioLayout);
        inventarioLayout.setHorizontalGroup(
            inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1064, Short.MAX_VALUE)
        );
        inventarioLayout.setVerticalGroup(
            inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 467, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(inventario);

        jMenuBar1.setToolTipText("");

        jMenuClientes.setText("Clientes");
        jMenuClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuClientesMouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenuClientes);

        jMenu3.setText("Carrito");
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        jMenuItem1.setText("Productos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuBar1.add(jMenu3);

        jMenuInventario.setText("Inventario");
        jMenuInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuInventarioMouseClicked(evt);
            }
        });
        jMenuInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuInventarioActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenuInventario);

        jMenuCategorias.setText("Categorias");
        jMenuCategorias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuCategoriasMouseClicked(evt);
            }
        });
        jMenuCategorias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCategoriasActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenuCategorias);

        jMenuServicios.setText("Servicios Digitales");
        jMenuServicios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuServiciosMouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenuServicios);

        Reportes.setText("Reportes");
        Reportes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ReportesMouseClicked(evt);
            }
        });
        jMenuBar1.add(Reportes);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        if (SistemaVentas.getEmpleadoActual() instanceof Cajero) {
            Carrito gc = new Carrito();
            gc.setVisible(true);
            this.dispose();
        } else if(SistemaVentas.getEmpleadoActual() instanceof Supervisor){
            Carrito gc = new Carrito();
            gc.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Solo cajeros y supervisores pueden acceder");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed
//aa
    private void jMenuClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuClientesMouseClicked
        // TODO add your handling code here:
        if (SistemaVentas.getEmpleadoActual() instanceof Cajero) {
            Clientes cuf = new Clientes(SistemaVentas.getGestorClientes());
            cuf.setVisible(true);
            MainConSesion.this.dispose();
        } else if(SistemaVentas.getEmpleadoActual() instanceof Supervisor){
            Clientes cuf = new Clientes(SistemaVentas.getGestorClientes());
            cuf.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Solo cajeros y supervisores pueden acceder");
        }
    }//GEN-LAST:event_jMenuClientesMouseClicked

    private void jMenuInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuInventarioActionPerformed

    }//GEN-LAST:event_jMenuInventarioActionPerformed

    private void jMenuInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuInventarioMouseClicked
        if (SistemaVentas.getEmpleadoActual() instanceof Cajero) {
            GestionInventario inv = new GestionInventario();
            inv.setVisible(true);
            MainConSesion.this.dispose();
        } else if (SistemaVentas.getEmpleadoActual() instanceof Supervisor) {
            GestionInventario inv = new GestionInventario();
            inv.setVisible(true);
            MainConSesion.this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Solo cajeros y supervisores pueden acceder");
        }
    }//GEN-LAST:event_jMenuInventarioMouseClicked

    private void jMenuCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCategoriasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuCategoriasActionPerformed

    private void jMenuCategoriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuCategoriasMouseClicked
        if (SistemaVentas.getEmpleadoActual() instanceof Supervisor) {
            GestionCategorias gc = new GestionCategorias();
            gc.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Solo supervisores pueden acceder");
        }
    }//GEN-LAST:event_jMenuCategoriasMouseClicked

    private void jMenuServiciosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuServiciosMouseClicked
        ServiciosDigitalesGUI servicios = new ServiciosDigitalesGUI();
        servicios.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuServiciosMouseClicked

    private void ReportesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReportesMouseClicked
        // TODO add your handling code here:
        if (SistemaVentas.getEmpleadoActual() instanceof Supervisor) {
            ReportesG gc = new ReportesG();
            gc.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Solo supervisores pueden acceder");
        }
    }//GEN-LAST:event_ReportesMouseClicked

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
        java.awt.EventQueue.invokeLater(() -> new MainConSesion().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Reportes;
    private javax.swing.JPanel inventario;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JMenuBar jMenuBar4;
    private javax.swing.JMenuBar jMenuBar5;
    private javax.swing.JMenuBar jMenuBar6;
    private javax.swing.JMenuBar jMenuBar7;
    private javax.swing.JMenu jMenuCategorias;
    private javax.swing.JMenu jMenuClientes;
    private javax.swing.JMenu jMenuInventario;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenu jMenuServicios;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
