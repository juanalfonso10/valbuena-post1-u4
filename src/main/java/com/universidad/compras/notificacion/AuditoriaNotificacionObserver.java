package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class AuditoriaNotificacionObserver implements SolicitudEstadoObserver {
    @Override
    public void alCambiarEstado(Solicitud solicitud, String estadoAnterior, String nuevoEstado) {
        ClientesNotificacion.registrarAuditoria(
            solicitud.getId(),
            nuevoEstado,
            "Transición de " + estadoAnterior + " a " + nuevoEstado
        );
    }
}
