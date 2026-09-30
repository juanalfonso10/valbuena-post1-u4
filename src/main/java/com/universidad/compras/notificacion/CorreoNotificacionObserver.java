package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class CorreoNotificacionObserver implements SolicitudEstadoObserver {
    @Override
    public void alCambiarEstado(Solicitud solicitud, String estadoAnterior, String nuevoEstado) {
        ClientesNotificacion.enviarCorreo(
            solicitud.getSolicitanteEmail(),
            "Actualización de Solicitud " + solicitud.getId(),
            "Su solicitud ha cambiado a estado " + nuevoEstado
        );
    }
}
