/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.tienda.vista;

import com.mycompany.tienda.Cajero;
import com.mycompany.tienda.CarritoDeCompras;
import com.mycompany.tienda.Empleado;
import com.mycompany.tienda.Tecnico;
import com.mycompany.tienda.control.SistemaVentas;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JOptionPane;
import com.mycompany.tienda.ServiciosDigitales;
import com.mycompany.tienda.Supervisor;
import com.mycompany.tienda.Tarea;
import com.mycompany.tienda.control.ControlServicios;
import com.mycompany.tienda.control.EstadoTarea;
import java.util.ArrayList;

/**
 *
 * @author carod
 */
public class ServiciosDigitalesGUI extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ServiciosDigitales.class.getName());
    private javax.swing.table.DefaultTableModel modeloTablaTecnicos;
    private ControlServicios controlServicios;
    private javax.swing.JButton btnIniciarSesionTecnico;
    private javax.swing.JButton btnCerrarSesionTecnico;
    private javax.swing.JLabel lblTecnicoActual;
    private javax.swing.JPanel panelAutenticacion;

    /**
     * Creates new form ServiciosDigitales
     */
    public ServiciosDigitalesGUI() {
        initComponents();
        modeloTablaTecnicos = new javax.swing.table.DefaultTableModel(
                new Object[]{"Nombre", "Estado"}, // Columnas
                0 // 0 filas inicialmente
        );
        tablaTecnicos.setModel(modeloTablaTecnicos);

        controlServicios = new ControlServicios();
        verificarInicializarCarrito();
        cargarServiciosDisponibles();
        actualizarTecnicosDisponibles();
        configurarEventos();
        crearPanelAutenticacionTecnico();
    }

    private void verificarInicializarCarrito() {
        if (MainConSesion.getCarritoGlobal() == null) {
            System.out.println("⚠️ Carrito global no inicializado. Inicializando...");

            // Obtener el empleado actual
            Empleado empleado = SistemaVentas.getEmpleadoActual();
            Cajero cajero = null;

            if (empleado instanceof Cajero) {
                cajero = (Cajero) empleado;
            } else if (empleado instanceof Supervisor) {
                Supervisor sup = (Supervisor) empleado;
                cajero = new Cajero(sup.getId(), sup.getNombre(),
                        sup.getUsuario(), sup.getContraseña());
            } else if (empleado instanceof Tecnico) {
                Tecnico tec = (Tecnico) empleado;
                cajero = new Cajero(tec.getId(), tec.getNombre(),
                        tec.getUsuario(), tec.getContraseña());
            } else {
                cajero = new Cajero("TEMP-001", "Cajero Temporal",
                        "temp", "temp123");
            }

            CarritoDeCompras nuevoCarrito = new CarritoDeCompras(cajero, null);
            MainConSesion.setCarritoGlobal(nuevoCarrito);

            System.out.println("Carrito global inicializado correctamente");
        }
    }

    private void crearPanelAutenticacionTecnico() {
        // Panel superior para autenticación
        panelAutenticacion = new javax.swing.JPanel();
        panelAutenticacion.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5));
        panelAutenticacion.setBackground(new java.awt.Color(240, 248, 255));
        panelAutenticacion.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY, 1));

        // Label para mostrar técnico actual
        lblTecnicoActual = new javax.swing.JLabel();
        lblTecnicoActual.setFont(new java.awt.Font("Microsoft Tai Le", 1, 12));

        // Botón para iniciar sesión
        btnIniciarSesionTecnico = new javax.swing.JButton("Iniciar Sesión Técnico");
        btnIniciarSesionTecnico.setBackground(new java.awt.Color(51, 153, 255));
        btnIniciarSesionTecnico.setForeground(Color.WHITE);
        btnIniciarSesionTecnico.setFont(new java.awt.Font("Microsoft Tai Le", 1, 11));
        btnIniciarSesionTecnico.addActionListener(e -> irAInicioSesionTecnico());

        // Botón para cerrar sesión
        btnCerrarSesionTecnico = new javax.swing.JButton("Cerrar Sesión");
        btnCerrarSesionTecnico.setBackground(new java.awt.Color(204, 0, 0));
        btnCerrarSesionTecnico.setForeground(Color.WHITE);
        btnCerrarSesionTecnico.setFont(new java.awt.Font("Microsoft Tai Le", 1, 11));
        btnCerrarSesionTecnico.addActionListener(e -> cerrarSesionTecnico());
        btnCerrarSesionTecnico.setVisible(false); // Oculto inicialmente

        panelAutenticacion.add(lblTecnicoActual);
        panelAutenticacion.add(btnIniciarSesionTecnico);
        panelAutenticacion.add(btnCerrarSesionTecnico);

        // Agregar el panel al contenedor principal
        java.awt.Container contentPane = getContentPane();
        panelAutenticacion.setBounds(10, 640, 628, 40);
        contentPane.add(panelAutenticacion);

        actualizarEstadoAutenticacion();

        contentPane.revalidate();
        contentPane.repaint();
    }

    private void actualizarEstadoAutenticacion() {
        Empleado empleadoActual = SistemaVentas.getEmpleadoActual();

        if (empleadoActual instanceof Tecnico) {
            // Técnico autenticado
            lblTecnicoActual.setText("Sesión: " + empleadoActual.getNombre() + " (Técnico)");
            lblTecnicoActual.setForeground(new java.awt.Color(0, 153, 0));
            btnIniciarSesionTecnico.setVisible(false);
            btnCerrarSesionTecnico.setVisible(true);
        } else if (empleadoActual != null) {
            // Otro tipo de empleado logueado
            lblTecnicoActual.setText("Sesión: " + empleadoActual.getNombre()
                    + " (" + empleadoActual.getClass().getSimpleName() + ")");
            lblTecnicoActual.setForeground(new java.awt.Color(255, 140, 0)); // Naranja
            btnIniciarSesionTecnico.setVisible(true);
            btnCerrarSesionTecnico.setVisible(false);
        } else {
            // Sin sesión
            lblTecnicoActual.setText("Sesión: No iniciada");
            lblTecnicoActual.setForeground(Color.RED);
            btnIniciarSesionTecnico.setVisible(true);
            btnCerrarSesionTecnico.setVisible(false);
        }
    }

    private void cerrarSesionTecnico() {
        String[] opciones = {
            "Ir al Menú Principal",
            "Cambiar de Usuario",
            "Cancelar"
        };

        int seleccion = JOptionPane.showOptionDialog(this,
                "¿Qué desea hacer?\n\n"
                + "Sesión actual: " + SistemaVentas.getEmpleadoActual().getNombre(),
                "Opciones de Sesión",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        switch (seleccion) {
            case 0: 
                SistemaVentas.getGestorPersonal().cerrarSesion();
                MainConSesion main = new MainConSesion();
                main.setVisible(true);
                this.dispose();
                break;

            case 1: 
                SistemaVentas.getGestorPersonal().cerrarSesion();
                IniciarSesionT loginTec = new IniciarSesionT();
                loginTec.setVisible(true);
                this.dispose();
                break;

            case 2: 
            default:
               
                break;
        }
    }

    private boolean verificarAutenticacionTecnico() {
        Empleado empleadoActual = SistemaVentas.getEmpleadoActual();

        if (!(empleadoActual instanceof Tecnico)) {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "Esta función requiere que inicie sesión como Técnico.\n¿Desea ir a la pantalla de inicio de sesión?",
                    "Autenticación Requerida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                irAInicioSesionTecnico();
            }
            return false;
        }

        return true;
    }

    private void irAInicioSesionTecnico() {
        IniciarSesionT login = new IniciarSesionT();
        login.setVisible(true);
        this.dispose();
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
        String input = JOptionPane.showInputDialog(this,
                "¿Cuántos servicios desea agregar?",
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

            for (int i = 0; i < cantidad; i++) {
                Tarea nuevaTarea = controlServicios.crearTarea(servicio, null);

                if (nuevaTarea == null) {
                    JOptionPane.showMessageDialog(this,
                            "No hay técnicos disponibles para asignar",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Agregar al carrito (ya está garantizado que existe)
            MainConSesion.getCarritoGlobal().agregarItem(servicio, cantidad);
            actualizarTecnicosDisponibles();

            JOptionPane.showMessageDialog(this,
                    "Servicio agregado al carrito\n\n"
                    + "Servicio: " + servicio.getNombre() + "\n"
                    + "Cantidad: " + cantidad + "\n"
                    + "Técnicos asignados automáticamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese un número válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTecnicosDisponibles() {
        modeloTablaTecnicos.setRowCount(0);

        // Obtener técnicos
        ArrayList<Tecnico> tecnicos = SistemaVentas.getGestorPersonal().obtenerTecnicosDisponibles();

        // Llenar tabla
        for (int i = 0; i < tecnicos.size(); i++) {
            Tecnico tecnico = tecnicos.get(i);
            modeloTablaTecnicos.addRow(new Object[]{
                tecnico.getNombre(),
                "Disponible"
            });
        }

        // Actualizar label
        if (tecnicos.isEmpty()) {
            lblTecnicosDisponibles.setText("Sin Técnicos Disponibles");
            lblTecnicosDisponibles.setForeground(Color.RED);
        } else {
            lblTecnicosDisponibles.setText(" Técnicos Disponibles: " + tecnicos.size());
            lblTecnicosDisponibles.setForeground(new Color(0, 153, 0));
        }
    }

    private void configurarEventos() {
        btnVerTareas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerTareasActionPerformed(evt);
            }
        });

        bntAsignacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntAsignacionActionPerformed(evt);
            }
        });

        btnVerestadodetareas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerestadodetareasActionPerformed(evt);
            }
        });
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
        btnVolver = new javax.swing.JButton();
        btnVerTareas = new javax.swing.JButton();
        jScrollPaneTecnicos = new java.awt.ScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTecnicos = new javax.swing.JTable();
        lblTecnicosDisponibles = new javax.swing.JLabel();
        bntAsignacion = new javax.swing.JButton();
        btnVerestadodetareas = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Microsoft Tai Le", 1, 24)); // NOI18N
        jLabel1.setText("Servicios Digitales Disponibles");

        scrollPane1.add(panelServicios);

        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        btnVerTareas.setText("Ver Tareas Asignadas");
        btnVerTareas.setToolTipText("");
        btnVerTareas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerTareasActionPerformed(evt);
            }
        });

        jScrollPaneTecnicos.setMinimumSize(new java.awt.Dimension(400, 150));

        tablaTecnicos.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaTecnicos.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(tablaTecnicos);

        jScrollPaneTecnicos.add(jScrollPane1);

        lblTecnicosDisponibles.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        lblTecnicosDisponibles.setForeground(new java.awt.Color(0, 153, 51));
        lblTecnicosDisponibles.setText("Técnicos Disponibles");

        bntAsignacion.setText("Cambiar Asignación");
        bntAsignacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntAsignacionActionPerformed(evt);
            }
        });

        btnVerestadodetareas.setText("Ver estado de tareas");
        btnVerestadodetareas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerestadodetareasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(137, 137, 137)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnVerTareas, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnVerestadodetareas)
                                .addGap(18, 18, 18)
                                .addComponent(bntAsignacion))
                            .addComponent(jScrollPaneTecnicos, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lblTecnicosDisponibles)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addGap(22, 22, 22)
                .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTecnicosDisponibles)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(jScrollPaneTecnicos, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVerestadodetareas)
                    .addComponent(btnVerTareas)
                    .addComponent(bntAsignacion))
                .addGap(50, 50, 50)
                .addComponent(btnVolver)
                .addGap(17, 17, 17))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        // TODO add your handling code here:
        if (SistemaVentas.getEmpleadoActual() instanceof Tecnico) {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Desea cerrar su sesión antes de salir?",
                    "Cerrar Sesión",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (opcion == JOptionPane.CANCEL_OPTION) {
                return; // No salir
            }

            if (opcion == JOptionPane.YES_OPTION) {
                SistemaVentas.getGestorPersonal().cerrarSesion();
            }
        }

        Main main = new Main();
        main.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnVerTareasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerTareasActionPerformed
        // TODO add your handling code here:
        ArrayList<Tarea> tareas = controlServicios.getTareas();

        if (tareas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay tareas registradas",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder listaTareas = new StringBuilder();
        listaTareas.append("TAREAS ASIGNADAS:\n\n");

        for (int i = 0; i < tareas.size(); i++) {
            Tarea tarea = tareas.get(i);
            listaTareas.append((i + 1)).append(". ")
                    .append("ID: ").append(tarea.getIdTarea())
                    .append(" | Técnico: ").append(tarea.getTecnicoAsignado().getNombre())
                    .append(" | Estado: ").append(tarea.getEstado().getNombre())
                    .append("\n");
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                listaTareas.toString() + "\n¿Desea cambiar la asignación de algún técnico?",
                "Tareas Asignadas",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            cambiarAsignacionTecnico(tareas);
        }
    }

    private void cambiarAsignacionTecnico(ArrayList<Tarea> tareas) {
        String[] opcionesTareas = new String[tareas.size()];

        for (int i = 0; i < tareas.size(); i++) {
            Tarea t = tareas.get(i);
            opcionesTareas[i] = t.getIdTarea() + " - " + t.getTecnicoAsignado().getNombre();
        }

        String seleccionTarea = (String) JOptionPane.showInputDialog(this,
                "Seleccione la tarea a reasignar:",
                "Cambiar Asignación",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesTareas,
                opcionesTareas[0]);

        if (seleccionTarea == null) {
            return;
        }

        int indiceTarea = -1;
        for (int i = 0; i < opcionesTareas.length; i++) {
            if (opcionesTareas[i].equals(seleccionTarea)) {
                indiceTarea = i;
                break;
            }
        }

        Tarea tareaSeleccionada = tareas.get(indiceTarea);

        ArrayList<Tecnico> tecnicosDisponibles = SistemaVentas.getGestorPersonal().obtenerTecnicosDisponibles();

        if (tecnicosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay técnicos disponibles",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] opcionesTecnicos = new String[tecnicosDisponibles.size()];
        for (int i = 0; i < tecnicosDisponibles.size(); i++) {
            opcionesTecnicos[i] = tecnicosDisponibles.get(i).getNombre();
        }

        String seleccionTecnico = (String) JOptionPane.showInputDialog(this,
                "Seleccione el nuevo técnico:",
                "Nuevo Técnico",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesTecnicos,
                opcionesTecnicos[0]);

        if (seleccionTecnico == null) {
            return;
        }

        Tecnico nuevoTecnico = null;
        for (Tecnico t : tecnicosDisponibles) {
            if (t.getNombre().equals(seleccionTecnico)) {
                nuevoTecnico = t;
                break;
            }
        }

        try {
            controlServicios.reasignarTecnico(tareaSeleccionada, nuevoTecnico);
            actualizarTecnicosDisponibles();

            JOptionPane.showMessageDialog(this,
                    "Técnico reasignado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnVerTareasActionPerformed

    private void bntAsignacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntAsignacionActionPerformed
        // TODO add your handling code here:
        ArrayList<Tarea> tareas = controlServicios.getTareas();

        if (tareas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay tareas registradas",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        cambiarAsignacionTecnico(tareas);
    }//GEN-LAST:event_bntAsignacionActionPerformed

    private void btnVerestadodetareasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerestadodetareasActionPerformed
        // TODO add your handling code here:
        if (!verificarAutenticacionTecnico()) {
            return;
        }

        Tecnico tecnicoActual = (Tecnico) SistemaVentas.getEmpleadoActual();
        ArrayList<Tarea> tareas = controlServicios.getTareas();

        if (tareas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay tareas registradas",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Filtrar solo las tareas del técnico actual
        ArrayList<Tarea> tareasTecnico = new ArrayList<>();
        for (Tarea t : tareas) {
            if (t.getTecnicoAsignado().getId().equals(tecnicoActual.getId())) {
                tareasTecnico.add(t);
            }
        }

        if (tareasTecnico.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No tienes tareas asignadas",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Mostrar tareas
        StringBuilder listaTareas = new StringBuilder();
        listaTareas.append("TUS TAREAS:\n\n");

        for (int i = 0; i < tareasTecnico.size(); i++) {
            Tarea tarea = tareasTecnico.get(i);
            listaTareas.append((i + 1)).append(". ")
                    .append("ID: ").append(tarea.getIdTarea())
                    .append(" | Estado: ").append(tarea.getEstado().getNombre())
                    .append("\n");
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                listaTareas.toString() + "\n¿Desea cambiar el estado de alguna tarea?",
                "Mis Tareas - " + tecnicoActual.getNombre(),
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            cambiarEstadoTarea(tareasTecnico);
        }
    }

    private void cambiarEstadoTarea(ArrayList<Tarea> tareas) {
        String[] opcionesTareas = new String[tareas.size()];

        for (int i = 0; i < tareas.size(); i++) {
            Tarea t = tareas.get(i);
            opcionesTareas[i] = t.getIdTarea() + " - " + t.getEstado().getNombre();
        }

        String seleccionTarea = (String) JOptionPane.showInputDialog(this,
                "Seleccione la tarea:",
                "Cambiar Estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcionesTareas,
                opcionesTareas[0]);

        if (seleccionTarea == null) {
            return;
        }

        int indiceTarea = -1;
        for (int i = 0; i < opcionesTareas.length; i++) {
            if (opcionesTareas[i].equals(seleccionTarea)) {
                indiceTarea = i;
                break;
            }
        }

        Tarea tareaSeleccionada = tareas.get(indiceTarea);
        String estadoActual = tareaSeleccionada.getEstado().getNombre();

        ArrayList<String> opcionesEstado = new ArrayList<>();

        if (tareaSeleccionada.getEstado().puedeIniciar()) {
            opcionesEstado.add("INICIAR");
        }
        if (tareaSeleccionada.getEstado().puedeCompletar()) {
            opcionesEstado.add("COMPLETAR");
        }
        if (tareaSeleccionada.getEstado().puedeCancelar()) {
            opcionesEstado.add("CANCELAR");
        }

        if (opcionesEstado.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Esta tarea no puede cambiar de estado",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] arrayOpciones = opcionesEstado.toArray(new String[0]);

        String accion = (String) JOptionPane.showInputDialog(this,
                "Estado actual: " + estadoActual + "\nSeleccione la acción:",
                "Cambiar Estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                arrayOpciones,
                arrayOpciones[0]);

        if (accion == null) {
            return;
        }

        try {
            if (accion.equals("INICIAR")) {
                tareaSeleccionada.iniciarTarea();
            } else if (accion.equals("COMPLETAR")) {
                tareaSeleccionada.completar();
            } else if (accion.equals("CANCELAR")) {
                tareaSeleccionada.setEstado(EstadoTarea.CANCELADA);
            }

            JOptionPane.showMessageDialog(this,
                    "Estado cambiado exitosamente a: " + tareaSeleccionada.getEstado().getNombre(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnVerestadodetareasActionPerformed

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
    private javax.swing.JButton bntAsignacion;
    private javax.swing.JButton btnVerTareas;
    private javax.swing.JButton btnVerestadodetareas;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.ScrollPane jScrollPaneTecnicos;
    private javax.swing.JLabel lblTecnicosDisponibles;
    private javax.swing.JPanel panelServicios;
    private java.awt.ScrollPane scrollPane1;
    private javax.swing.JTable tablaTecnicos;
    // End of variables declaration//GEN-END:variables
}
