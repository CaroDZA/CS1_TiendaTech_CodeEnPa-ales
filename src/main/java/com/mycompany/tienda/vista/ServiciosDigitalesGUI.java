/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.tienda.vista;

import com.mycompany.tienda.Tecnico;
import com.mycompany.tienda.control.SistemaVentas;
import com.mycompany.tienda.modelo.interfaces.Vendido;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JOptionPane;
import com.mycompany.tienda.ServiciosDigitales;

/**
 *
 * @author carod
 */
public class ServiciosDigitalesGUI extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ServiciosDigitales.class.getName());

    /**
     * Creates new form ServiciosDigitales
     */
    public ServiciosDigitalesGUI() {
        initComponents();
        cargarServiciosDisponibles();
        actualizarTecnicosDisponibles();
    }

    private void cargarServiciosDisponibles() {
        panelServicios.removeAll();

        ServiciosDigitales[] servicios = {
            new ServiciosDigitales("Instalación de Windows", 50000, "SERVICIOS TÉCNICOS", 120, "Instalación completa de Windows 10/11"),
            new ServiciosDigitales("Configuración de Equipo", 30000, "SERVICIOS TÉCNICOS", 60, "Configuración inicial de PC nuevo"),
            new ServiciosDigitales("Limpieza de Virus", 40000, "SERVICIOS TÉCNICOS", 90, "Eliminación de malware y optimización"),
            new ServiciosDigitales("Instalación Office", 25000, "SERVICIOS TÉCNICOS", 45, "Instalación y activación de Office"),
            new ServiciosDigitales("Backup de Datos", 35000, "SERVICIOS TÉCNICOS", 75, "Respaldo completo de información"),
            new ServiciosDigitales("Actualización Hardware", 45000, "SERVICIOS TÉCNICOS", 100, "Instalación de componentes nuevos")
        };

        int columnas = 2;
        int filas = (int) Math.ceil(servicios.length / (double) columnas);

        panelServicios.setLayout(new GridLayout(filas, columnas, 15, 15));

        for (ServiciosDigitales servicio : servicios) {
            javax.swing.JPanel cardServicio = crearCardServicio(servicio);
            panelServicios.add(cardServicio);
        }

        panelServicios.revalidate();
        panelServicios.repaint();
    }

    private javax.swing.JPanel crearCardServicio(ServiciosDigitales servicio) {
        javax.swing.JPanel card = new javax.swing.JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY, 2));
        card.setBackground(new Color(240, 248, 255));

        javax.swing.JPanel panelInfo = new javax.swing.JPanel();
        panelInfo.setLayout(new java.awt.GridLayout(5, 1, 5, 5));
        panelInfo.setBackground(new Color(240, 248, 255));

        javax.swing.JLabel lblNombre = new javax.swing.JLabel(servicio.getNombre());
        lblNombre.setFont(new java.awt.Font("Microsoft Tai Le", 1, 16));
        lblNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.JLabel lblPrecio = new javax.swing.JLabel("$" + String.format("%.2f", servicio.getPrecio()));
        lblPrecio.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblPrecio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPrecio.setForeground(new Color(0, 102, 0));

        javax.swing.JLabel lblDuracion = new javax.swing.JLabel("Duración: " + servicio.getDuracionEstimadaMinutos() + " min");
        lblDuracion.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblDuracion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.JLabel lblDescripcion = new javax.swing.JLabel("<html><center>" + servicio.getDescripcion() + "</center></html>");
        lblDescripcion.setFont(new java.awt.Font("Segoe UI", 0, 11));
        lblDescripcion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.JLabel lblTecnico = new javax.swing.JLabel("⚙ Requiere técnico especializado");
        lblTecnico.setFont(new java.awt.Font("Segoe UI", 2, 10));
        lblTecnico.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTecnico.setForeground(new Color(255, 140, 0));

        panelInfo.add(lblNombre);
        panelInfo.add(lblPrecio);
        panelInfo.add(lblDuracion);
        panelInfo.add(lblDescripcion);
        panelInfo.add(lblTecnico);

        javax.swing.JButton btnAgregar = new javax.swing.JButton("Agregar al Carrito");
        btnAgregar.setBackground(new Color(0, 153, 76));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new java.awt.Font("Microsoft Tai Le", 1, 12));
        btnAgregar.addActionListener(e -> agregarServicioAlCarrito(servicio));

        card.add(panelInfo, BorderLayout.CENTER);
        card.add(btnAgregar, BorderLayout.SOUTH);

        return card;
    }

    private void agregarServicioAlCarrito(ServiciosDigitales servicio) {
        Tecnico tecnicoDisponible = SistemaVentas.getGestorPersonal().buscarTecnicoDisponible();

        if (tecnicoDisponible == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay técnicos disponibles en este momento.\n"
                    + "Por favor, intente más tarde.",
                    "Sin Técnicos Disponibles",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this,
                "Ingrese la cantidad de servicios:\n"
                + "(Los servicios no tienen límite de cantidad)",
                "Cantidad",
                JOptionPane.QUESTION_MESSAGE);

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            int cantidad = Integer.parseInt(input);

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this,
                        "La cantidad debe ser mayor a 0",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            MainConSesion.getCarritoGlobal().agregarItem((Vendido) servicio, cantidad);

            JOptionPane.showMessageDialog(this,
                    "Servicio agregado al carrito\n\n"
                    + "Servicio: " + servicio.getNombre() + "\n"
                    + "Cantidad: " + cantidad + "\n"
                    + "Precio unitario: $" + String.format("%.2f", servicio.getPrecio()) + "\n"
                    + "Total: $" + String.format("%.2f", servicio.getPrecio() * cantidad) + "\n\n"
                    + "Técnico asignado: " + tecnicoDisponible.getNombre() + "\n"
                    + "Duración estimada: " + (servicio.getDuracionEstimadaMinutos() * cantidad) + " minutos",
                    "Confirmación",
                    JOptionPane.INFORMATION_MESSAGE);

            actualizarTecnicosDisponibles();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTecnicosDisponibles() {
        Tecnico tecnico = SistemaVentas.getGestorPersonal().buscarTecnicoDisponible();

        if (tecnico != null) {
            lblTecnicosDisponibles.setText("Técnicos Disponibles");
            lblTecnicosDisponibles.setForeground(new Color(0, 153, 0));
        } else {
            lblTecnicosDisponibles.setText("Sin Técnicos Disponibles");
            lblTecnicosDisponibles.setForeground(Color.RED);
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
        scrollPane1 = new java.awt.ScrollPane();
        panelServicios = new javax.swing.JPanel();
        lblTecnicosDisponibles = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();
        btnVerTareas = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 24)); // NOI18N
        jLabel1.setText("Servicios Digitales Disponibles");

        scrollPane1.add(panelServicios);

        lblTecnicosDisponibles.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        lblTecnicosDisponibles.setText("Técnicos Disponibles: 0");

        btnVolver.setText("Volver");

        btnVerTareas.setText("Ver Tareas Asignadas");
        btnVerTareas.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTecnicosDisponibles)
                            .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnVerTareas)
                            .addComponent(btnVolver))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(lblTecnicosDisponibles)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                .addComponent(btnVerTareas)
                .addGap(48, 48, 48)
                .addComponent(btnVolver)
                .addGap(33, 33, 33))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServiciosDigitales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServiciosDigitales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServiciosDigitales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServiciosDigitales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServiciosDigitalesGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVerTareas;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblTecnicosDisponibles;
    private javax.swing.JPanel panelServicios;
    private java.awt.ScrollPane scrollPane1;
    // End of variables declaration//GEN-END:variables
}
