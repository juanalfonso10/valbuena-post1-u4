package com.universidad.compras.estado;

public class EstadoCancelada implements EstadoSolicitud {
    @Override
    public String aprobar(SolicitudContexto ctx) { return "Error: solicitud cancelada"; }
    @Override
    public String rechazar(SolicitudContexto ctx) { return "Error: solicitud cancelada"; }
    @Override
    public String ejecutar(SolicitudContexto ctx) { return "Error: no se puede ejecutar una solicitud CANCELADA"; }
    @Override
    public String cancelar(SolicitudContexto ctx) { return "Error: ya fue cancelada"; }
    @Override
    public String getNombre() { return "CANCELADA"; }
}
