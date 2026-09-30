package com.universidad.compras.estado;

public class EstadoRechazada implements EstadoSolicitud {
    @Override
    public String aprobar(SolicitudContexto ctx) { return "Error: solicitud rechazada"; }
    @Override
    public String rechazar(SolicitudContexto ctx) { return "Error: ya fue rechazada"; }
    @Override
    public String ejecutar(SolicitudContexto ctx) { return "Error: no se puede ejecutar una solicitud RECHAZADA"; }
    @Override
    public String cancelar(SolicitudContexto ctx) { return "Error: solicitud ya rechazada"; }
    @Override
    public String getNombre() { return "RECHAZADA"; }
}
