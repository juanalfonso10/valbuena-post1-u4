package com.universidad.compras.estado;

public interface EstadoSolicitud {
    String aprobar(SolicitudContexto ctx);
    String rechazar(SolicitudContexto ctx);
    String ejecutar(SolicitudContexto ctx);
    String cancelar(SolicitudContexto ctx);
    String getNombre();
}
