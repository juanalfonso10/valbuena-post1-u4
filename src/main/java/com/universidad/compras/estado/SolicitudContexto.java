package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.GestorNotificacionesEstado;

// Contexto del patron State: delega cada operacion en el objeto del estado actual
public class SolicitudContexto {
    private final Solicitud solicitud;
    private final GestorNotificacionesEstado notificaciones;
    private EstadoSolicitud estadoActual;

    public SolicitudContexto(Solicitud solicitud, GestorNotificacionesEstado notificaciones) {
        this.solicitud = solicitud;
        this.notificaciones = notificaciones;
        this.estadoActual = resolverEstadoInicial(solicitud.getEstado());
    }

    public SolicitudContexto(Solicitud solicitud) {
        this(solicitud, new GestorNotificacionesEstado());
    }

    private EstadoSolicitud resolverEstadoInicial(String estado) {
        if (estado == null) return new EstadoPendiente();
        return switch (estado) {
            case "PENDIENTE" -> new EstadoPendiente();
            case "EN_APROBACION" -> new EstadoEnAprobacion();
            case "APROBADA" -> new EstadoAprobada();
            case "RECHAZADA" -> new EstadoRechazada();
            case "EJECUTADA" -> new EstadoEjecutada();
            case "CANCELADA" -> new EstadoCancelada();
            default -> new EstadoPendiente();
        };
    }

    // Toda transicion valida pasa por el gestor de notificaciones (Necesidad 3)
    public void cambiarEstado(EstadoSolicitud nuevoEstado) {
        this.estadoActual = nuevoEstado;
        notificaciones.cambiarEstado(solicitud, nuevoEstado.getNombre());
    }

    public String aprobar() { return estadoActual.aprobar(this); }
    public String rechazar() { return estadoActual.rechazar(this); }
    public String ejecutar() { return estadoActual.ejecutar(this); }
    public String cancelar() { return estadoActual.cancelar(this); }

    public Solicitud getSolicitud() { return solicitud; }
    public EstadoSolicitud getEstadoActual() { return estadoActual; }
}
