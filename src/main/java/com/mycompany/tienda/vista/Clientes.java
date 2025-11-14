/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.tienda.vista;

import com.mycompany.tienda.Cliente;
import com.mycompany.tienda.control.ControlClientes;
import com.mycompany.tienda.archivos.GuardarClientes;
import com.mycompany.tienda.control.SistemaVentas;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author samue
 */
public class Clientes extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Clientes.class.getName());

    private ControlClientes controlClientes;
    private Cliente clienteActual;
    private static double descuentoPuntosCanjeados = 0.0;

    /**
     * Creates new form ListUserForm
     *
     * @param controlClientes
     */
    public Clientes(ControlClientes controlClientes) {
        initComponents();
        this.controlClientes = SistemaVentas.getGestorClientes();
        cargarClientes();
        limpiarInfoCliente();
    }

    public static double getDescuentoPuntosCanjeados() {
        return descuentoPuntosCanjeados;
    }

    public static void resetearDescuentoPuntos() {
        descuentoPuntosCanjeados = 0.0;
    }

    private void cargarClientes() {
        usersCombo.removeAllItems();

        Map<String, Cliente> clientes = controlClientes.getClientes();

        System.out.println("Total de clientes en el sistema: " + clientes.size());

        if (clientes.isEmpty()) {
            usersCombo.addItem("--- No hay clientes ---");
            System.out.println("No hay clientes para mostrar");
        } else {
            usersCombo.addItem("--- Seleccione un cliente ---");

            for (Cliente cliente : clientes.values()) {
                String itemTexto = cliente.getNombre() + " (" + cliente.getCedula() + ")";
                usersCombo.addItem(itemTexto);
                System.out.println("Agregado al combo: " + itemTexto);
            }
        }
    }

    private void limpiarInfoCliente() {
        lblNombreCliente.setText("-");
        lblCedulaCliente.setText("-");
        lblPuntosCliente.setText("-");
        lblTotalCompras.setText("-");
        txtAreaHistorial.setText("");
        clienteActual = null;
    }

    private void buscarCliente() {
        String busqueda = txtBusqueda.getText().trim();

        if (busqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese una cédula o nombre para buscar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Buscar por cédula primero
        Cliente cliente = controlClientes.getClientes().get(busqueda);

        // Si no encuentra por cédula, buscar por nombre
        if (cliente == null) {
            for (Cliente c : controlClientes.getClientes().values()) {
                if (c.getNombre().equalsIgnoreCase(busqueda)) {
                    cliente = c;
                    break;
                }
            }
        }

        if (cliente == null) {
            JOptionPane.showMessageDialog(this,
                    "Cliente no encontrado",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            limpiarInfoCliente();
        } else {
            mostrarInfoCliente(cliente);
        }
    }

    private void mostrarInfoCliente(Cliente cliente) {
        clienteActual = cliente;
        lblNombreCliente.setText(cliente.getNombre());
        lblCedulaCliente.setText(cliente.getCedula());
        lblPuntosCliente.setText("" + cliente.getPuntosFidelidad());

        double descuento = cliente.DescuentoFidelidad() * 100;
        lblTotalCompras.setText("Compras: " + cliente.getTotalCompras() + " | Descuento: " + (int) descuento + "%");
    }

    private void verHistorial() {
        if (clienteActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Primero busque un cliente",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        txtAreaHistorial.setText("");
        txtAreaHistorial.append("      HISTORIAL DE COMPRAS    \n\n");
        txtAreaHistorial.append("Cliente: " + clienteActual.getNombre() + "\n");
        txtAreaHistorial.append("Cédula: " + clienteActual.getCedula() + "\n");
        txtAreaHistorial.append("Puntos: " + clienteActual.getPuntosFidelidad() + "\n");
        txtAreaHistorial.append("\n");
    }

    private void canjearPuntos() {
        if (clienteActual == null) {
            JOptionPane.showMessageDialog(this,
                    "Primero busque un cliente",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int puntosDisponibles = clienteActual.getPuntosFidelidad();

        if (puntosDisponibles < 100) {
            JOptionPane.showMessageDialog(this,
                    "Puntos insuficientes. Mínimo 100 puntos.\n"
                    + "Puntos actuales: " + puntosDisponibles,
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this,
                "Puntos disponibles: " + puntosDisponibles + "\n"
                + "Conversión: 100 puntos = $10 de descuento\n\n"
                + "Ingrese cantidad de puntos a canjear (mínimo 100):",
                "Canjear Puntos",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            int puntosACanjear = Integer.parseInt(input);

            if (puntosACanjear < 100) {
                JOptionPane.showMessageDialog(this,
                        "Mínimo 100 puntos para canjear",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (puntosACanjear > puntosDisponibles) {
                JOptionPane.showMessageDialog(this,
                        "Puntos insuficientes. Disponible: " + puntosDisponibles,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calcular descuento
            double descuento = controlClientes.canjearPuntos(
                    clienteActual.getCedula(),
                    puntosACanjear
            );

            clienteActual.setDescuentoPuntos(descuento);

            descuentoPuntosCanjeados = descuento;

            JOptionPane.showMessageDialog(this,
                    "¡Canje exitoso!\n\n"
                    + "Puntos canjeados: " + puntosACanjear + "\n"
                    + "Descuento obtenido: $" + String.format("%.2f", descuento) + "\n"
                    + "Puntos restantes: " + clienteActual.getPuntosFidelidad() + "\n\n"
                    + "Este descuento se aplicará automáticamente\n"
                    + "en la próxima compra de este cliente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            mostrarInfoCliente(clienteActual);
            GuardarClientes.guardarEnCSV(
                    SistemaVentas.getGestorClientes().getClientes(),
                    "Clientes.csv"
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
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
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        lblNombreCliente1 = new java.awt.TextField();
        jLabel1 = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        usersCombo = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        btnBuscar = new java.awt.Button();
        panel1 = new java.awt.Panel();
        label1 = new java.awt.Label();
        tnombreCliente = new java.awt.Label();
        lblNombreCliente = new java.awt.TextField();
        tidentificacion = new javax.swing.JLabel();
        lblCedulaCliente = new java.awt.TextField();
        tpuntosCliente = new javax.swing.JLabel();
        lblPuntosCliente = new java.awt.TextField();
        jLabel3 = new javax.swing.JLabel();
        lblTotalCompras = new java.awt.TextField();
        btnVerHistorial = new javax.swing.JButton();
        btnCanjearPuntos = new javax.swing.JButton();
        scrollPane1 = new java.awt.ScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaHistorial = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        jMenu2.setText("File");
        jMenuBar2.add(jMenu2);

        jMenu3.setText("Edit");
        jMenuBar2.add(jMenu3);

        lblNombreCliente1.setEditable(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Lista de clientes");

        cancelBtn.setText("Volver");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        usersCombo.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                usersComboComponentMoved(evt);
            }
        });
        usersCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usersComboActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setText("Gestor de Clientes");

        btnBuscar.setLabel("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        panel1.setBackground(new java.awt.Color(153, 255, 204));

        label1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        label1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        label1.setText("Información Cliente");

        tnombreCliente.setText("Nombre:");

        lblNombreCliente.setEditable(false);

        tidentificacion.setText("Identificación:");

        lblCedulaCliente.setEditable(false);

        tpuntosCliente.setText("Puntos Cliente:");

        lblPuntosCliente.setEditable(false);

        jLabel3.setText("Total Compras:");

        lblTotalCompras.setEditable(false);

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(tnombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(tidentificacion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCedulaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(tpuntosCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblPuntosCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(lblTotalCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tnombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tidentificacion)
                    .addComponent(lblCedulaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tpuntosCliente)
                    .addComponent(lblPuntosCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(lblTotalCompras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        btnVerHistorial.setText("Ver Historial");
        btnVerHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerHistorialActionPerformed(evt);
            }
        });

        btnCanjearPuntos.setText("Canjear Puntos");
        btnCanjearPuntos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCanjearPuntosActionPerformed(evt);
            }
        });

        txtAreaHistorial.setColumns(20);
        txtAreaHistorial.setRows(5);
        jScrollPane1.setViewportView(txtAreaHistorial);

        scrollPane1.add(jScrollPane1);

        jMenu1.setText("Crear Cliente");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(usersCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(63, 63, 63)
                        .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 13, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCanjearPuntos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnVerHistorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usersCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(btnVerHistorial)
                        .addGap(33, 33, 33)
                        .addComponent(btnCanjearPuntos)
                        .addGap(34, 34, 34)
                        .addComponent(cancelBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        // TODO add your handling code here:
        MainConSesion cuf = new MainConSesion();
        cuf.setVisible(true);
        Clientes.this.dispose();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void usersComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usersComboActionPerformed
        String seleccion = (String) usersCombo.getSelectedItem();

        if (seleccion == null
                || seleccion.equals("No hay clientes registrados")
                || seleccion.equals("--- Seleccione un cliente ---")
                || seleccion.equals("--- No hay clientes ---")) {
            limpiarInfoCliente();
            return;
        }

        try {
            int inicio = seleccion.indexOf("(");
            int fin = seleccion.indexOf(")");

            if (inicio != -1 && fin != -1 && fin > inicio) {
                String cedula = seleccion.substring(inicio + 1, fin);

                Cliente cliente = controlClientes.getClientes().get(cedula);

                if (cliente != null) {
                    mostrarInfoCliente(cliente);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al seleccionar cliente: " + e.getMessage());
            limpiarInfoCliente();
        }
    }//GEN-LAST:event_usersComboActionPerformed

    private void usersComboComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_usersComboComponentMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_usersComboComponentMoved

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        CrearClientes cliente = new CrearClientes();
        cliente.setVisible(true);
        Clientes.this.dispose();
    }//GEN-LAST:event_jMenu1MouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarCliente();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnVerHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerHistorialActionPerformed
        verHistorial();
    }//GEN-LAST:event_btnVerHistorialActionPerformed

    private void btnCanjearPuntosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCanjearPuntosActionPerformed
        canjearPuntos();
    }//GEN-LAST:event_btnCanjearPuntosActionPerformed

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
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button btnBuscar;
    private javax.swing.JButton btnCanjearPuntos;
    private javax.swing.JButton btnVerHistorial;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Label label1;
    private java.awt.TextField lblCedulaCliente;
    private java.awt.TextField lblNombreCliente;
    private java.awt.TextField lblNombreCliente1;
    private java.awt.TextField lblPuntosCliente;
    private java.awt.TextField lblTotalCompras;
    private java.awt.Panel panel1;
    private java.awt.ScrollPane scrollPane1;
    private javax.swing.JLabel tidentificacion;
    private java.awt.Label tnombreCliente;
    private javax.swing.JLabel tpuntosCliente;
    private javax.swing.JTextArea txtAreaHistorial;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JComboBox<String> usersCombo;
    // End of variables declaration//GEN-END:variables
}
