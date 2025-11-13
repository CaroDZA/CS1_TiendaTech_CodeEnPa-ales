/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.tienda.control;

/**
 *
 * @author carod
 */
public enum EstadoTarea {
    PENDIENTE("PENDIENTE") {
        @Override
        public boolean puedeAsignarTecnico() {
            return true;
        }

        @Override
        public boolean puedeIniciar() {
            return false;
        }

        @Override
        public boolean puedeCompletar() {
            return false;
        }

        @Override
        public boolean puedeCancelar() {
            return true;
        }

        @Override
        public EstadoTarea transicionar(String accion) {
            if (accion.equals("ASIGNAR_TECNICO")) {
                return EN_ESPERA;
            }
            if (accion.equals("CANCELAR")) {
                return CANCELADA;
            }
            return this;
        }
    },
    EN_ESPERA("EN ESPERA") {
        @Override
        public boolean puedeAsignarTecnico() {
            return true; // Se puede reasignar en espera
        }

        @Override
        public boolean puedeIniciar() {
            return true;
        }

        @Override
        public boolean puedeCompletar() {
            return false;
        }

        @Override
        public boolean puedeCancelar() {
            return true;
        }

        @Override
        public EstadoTarea transicionar(String accion) {
            if (accion.equals("INICIAR")) {
                return EN_PROCESO;
            }
            if (accion.equals("CANCELAR")) {
                return CANCELADA;
            }
            return this;
        }
    },
    EN_PROCESO("EN PROCESO") {
        @Override
        public boolean puedeAsignarTecnico() {
            return false;
        }

        @Override
        public boolean puedeIniciar() {
            return false;
        }

        @Override
        public boolean puedeCompletar() {
            return true;
        }

        @Override
        public boolean puedeCancelar() {
            return true;
        }

        @Override
        public EstadoTarea transicionar(String accion) {
            if (accion.equals("COMPLETAR")) {
                return COMPLETADA;
            }
            if (accion.equals("CANCELAR")) {
                return CANCELADA;
            }
            return this;
        }
    },
    COMPLETADA("COMPLETADA") {
        @Override
        public boolean puedeAsignarTecnico() {
            return false;
        }

        @Override
        public boolean puedeIniciar() {
            return false;
        }

        @Override
        public boolean puedeCompletar() {
            return false;
        }

        @Override
        public boolean puedeCancelar() {
            return false;
        }

        @Override
        public EstadoTarea transicionar(String accion) {
            return this;
        }
    },
    CANCELADA("CANCELADA") {
        @Override
        public boolean puedeAsignarTecnico() {
            return false;
        }

        @Override
        public boolean puedeIniciar() {
            return false;
        }

        @Override
        public boolean puedeCompletar() {
            return false;
        }

        @Override
        public boolean puedeCancelar() {
            return false;
        }

        @Override
        public EstadoTarea transicionar(String accion) {
            return this;
        }
    };

    private final String nombre;

    EstadoTarea(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract boolean puedeAsignarTecnico();

    public abstract boolean puedeIniciar();

    public abstract boolean puedeCompletar();

    public abstract boolean puedeCancelar();

    public abstract EstadoTarea transicionar(String accion);
}
