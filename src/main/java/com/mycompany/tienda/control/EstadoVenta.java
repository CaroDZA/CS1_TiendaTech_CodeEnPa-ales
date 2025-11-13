/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.tienda.control;

/**
 *
 * @author carod
 */
public enum EstadoVenta {
    EN_PROCESO("EN PROCESO") {
        @Override
        public boolean puedeAgregarProductos() {
            return true;
        }

        @Override
        public boolean puedeConfirmarPago() {
            return true;
        }

        @Override
        public boolean puedeCancelar() {
            return true;
        }

        @Override
        public boolean puedeEntregar() {
            return false;
        }

        @Override
        public EstadoVenta transicionar(String accion) {
            if (accion.equals("PAGAR")) {
                return PAGADA;
            }
            if (accion.equals("CANCELAR")) {
                return CANCELADA;
            }
            return this;
        }
    },
    PAGADA("PAGADA") {
        @Override
        public boolean puedeAgregarProductos() {
            return false;
        }

        @Override
        public boolean puedeConfirmarPago() {
            return false;
        }

        @Override
        public boolean puedeCancelar() {
            return false;
        }

        @Override
        public boolean puedeEntregar() {
            return true;
        }

        @Override
        public EstadoVenta transicionar(String accion) {
            return accion.equals("ENTREGAR") ? ENTREGADA : this;
        }
    },
    ENTREGADA("ENTREGADA") {
        @Override
        public boolean puedeAgregarProductos() {
            return false;
        }

        @Override
        public boolean puedeConfirmarPago() {
            return false;
        }

        @Override
        public boolean puedeCancelar() {
            return false;
        }

        @Override
        public boolean puedeEntregar() {
            return false;
        }

        @Override
        public EstadoVenta transicionar(String accion) {
            return this;
        }
    },
    CANCELADA("CANCELADA") {
        @Override
        public boolean puedeAgregarProductos() {
            return false;
        }

        @Override
        public boolean puedeConfirmarPago() {
            return false;
        }

        @Override
        public boolean puedeCancelar() {
            return false;
        }

        @Override
        public boolean puedeEntregar() {
            return false;
        }

        @Override
        public EstadoVenta transicionar(String accion) {
            return this;
        }
    };

    private final String nombre;

    EstadoVenta(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract boolean puedeAgregarProductos();

    public abstract boolean puedeConfirmarPago();

    public abstract boolean puedeCancelar();

    public abstract boolean puedeEntregar();

    public abstract EstadoVenta transicionar(String accion);
}
