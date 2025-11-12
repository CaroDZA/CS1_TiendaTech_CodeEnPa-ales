/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.tienda;

import com.mycompany.tienda.control.SistemaVentas;
import com.mycompany.tienda.vista.Main;

/**
 *
 * @author carod
 */
public class TiendaPrueba {

    public static void main(String[] args) {
        SistemaVentas.inicializar();
        java.awt.EventQueue.invokeLater(() -> new Main().setVisible(true));
        
    }
}