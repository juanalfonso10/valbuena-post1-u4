package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;

public class SolicitudContexto {
    private final Solicitud solicitud;
    private EstadoSolicitud estadoActual;

    public SolicitudContexto(Solicitud solicitud) {
        this.solicitud = solicitud;
        this.estadoActual = resolverEstadoInicial(solicitud.getEstado());
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

    public void cambiarEstado(EstadoSolicitud nuevoEstado) {
        this.estadoActual = nuevoEstado;
        this.solicitud.setEstado(nuevoEstado.getNombre());
    }

    public String aprobar() { return estadoActual.aprobar(this); }
    public String rechazar() { return estadoActual.rechazar(this); }
    public String ejecutar() { return estadoActual.ejecutar(this); }
    public String cancelar() { return estadoActual.cancelar(this); }

    public Solicitud getSolicitud() { return solicitud; }
    public EstadoSolicitud getEstadoActual() { return estadoActual; }
}
