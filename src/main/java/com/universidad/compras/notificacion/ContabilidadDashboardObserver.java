package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class ContabilidadDashboardObserver implements SolicitudEstadoObserver {
    @Override
    public void alCambiarEstado(Solicitud solicitud, String estadoAnterior, String nuevoEstado) {
        ClientesNotificacion.actualizarDashboardContabilidad(
            solicitud.getId(),
            nuevoEstado,
            solicitud.getMonto()
        );
    }
}
