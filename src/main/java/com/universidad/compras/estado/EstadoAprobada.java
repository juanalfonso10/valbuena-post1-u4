package com.universidad.compras.estado;

public class EstadoAprobada implements EstadoSolicitud {
    @Override
    public String aprobar(SolicitudContexto ctx) {
        return "Error: ya se encuentra aprobada";
    }

    @Override
    public String rechazar(SolicitudContexto ctx) {
        return "Error: no se puede rechazar una solicitud ya aprobada";
    }

    @Override
    public String ejecutar(SolicitudContexto ctx) {
        ctx.cambiarEstado(new EstadoEjecutada());
        return "Ejecutada";
    }

    @Override
    public String cancelar(SolicitudContexto ctx) {
        ctx.cambiarEstado(new EstadoCancelada());
        return "Cancelada";
    }

    @Override
    public String getNombre() { return "APROBADA"; }
}
