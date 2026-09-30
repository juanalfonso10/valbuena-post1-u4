package com.universidad.compras.estado;

public class EstadoEjecutada implements EstadoSolicitud {
    @Override
    public String aprobar(SolicitudContexto ctx) { return "Error: ya fue ejecutada"; }
    @Override
    public String rechazar(SolicitudContexto ctx) { return "Error: no se puede rechazar una orden ejecutada"; }
    @Override
    public String ejecutar(SolicitudContexto ctx) { return "Error: ya fue ejecutada"; }
    @Override
    public String cancelar(SolicitudContexto ctx) { return "Error: no se puede cancelar directamente una orden ejecutada"; }
    @Override
    public String getNombre() { return "EJECUTADA"; }
}
