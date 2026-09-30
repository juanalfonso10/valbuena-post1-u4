package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

// Eslabon de la cadena: cada nivel resuelve la solicitud o la delega al siguiente
public abstract class NivelAprobacionHandler {
    protected NivelAprobacionHandler siguiente;

    public NivelAprobacionHandler enlazarSiguiente(NivelAprobacionHandler siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public abstract ResultadoAprobacion procesar(Solicitud solicitud);

    protected ResultadoAprobacion delegarAlSiguiente(Solicitud solicitud) {
        if (siguiente != null) {
            return siguiente.procesar(solicitud);
        }
        return new ResultadoAprobacion(false, "Sistema", "Ningún nivel pudo resolver la solicitud");
    }

    // El nivel que resuelve solo registra su nombre; el cambio de estado lo hace ServicioAprobacionImpl
    // a traves del GestorNotificacionesEstado, para que se disparen las notificaciones.
    protected ResultadoAprobacion resolver(Solicitud solicitud, boolean aprobada, String nivel, String detalle) {
        solicitud.setNivelResolutor(nivel);
        return new ResultadoAprobacion(aprobada, nivel, detalle);
    }
}
