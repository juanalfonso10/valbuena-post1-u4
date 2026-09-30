package com.universidad.compras.estado;

public class EstadoEnAprobacion implements EstadoSolicitud {
    @Override
    public String aprobar(SolicitudContexto ctx) {
        ctx.cambiarEstado(new EstadoAprobada());
        return "Aprobada";
    }

    @Override
    public String rechazar(SolicitudContexto ctx) {
        ctx.cambiarEstado(new EstadoRechazada());
        return "Rechazada";
    }

    @Override
    public String ejecutar(SolicitudContexto ctx) {
        return "Error: la solicitud se encuentra en proceso de aprobación";
    }

    @Override
    public String cancelar(SolicitudContexto ctx) {
        ctx.cambiarEstado(new EstadoCancelada());
        return "Cancelada";
    }

    @Override
    public String getNombre() { return "EN_APROBACION"; }
}
