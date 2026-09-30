package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// Sujeto del Observer: unico punto por el que cambia el estado de una Solicitud.
// Quien cambia el estado solo conoce este gestor, nunca a los observadores concretos.
@Component
public class GestorNotificacionesEstado {
    private final List<SolicitudEstadoObserver> observadores = new ArrayList<>();

    public GestorNotificacionesEstado() {
        observadores.add(new CorreoNotificacionObserver());
        observadores.add(new ContabilidadDashboardObserver());
        observadores.add(new AuditoriaNotificacionObserver());
    }

    public void suscribir(SolicitudEstadoObserver observer) {
        observadores.add(observer);
    }

    public void desuscribir(SolicitudEstadoObserver observer) {
        observadores.remove(observer);
    }

    public void cambiarEstado(Solicitud solicitud, String nuevoEstado) {
        String estadoAnterior = solicitud.getEstado();
        solicitud.setEstado(nuevoEstado);
        for (SolicitudEstadoObserver observer : observadores) {
            observer.alCambiarEstado(solicitud, estadoAnterior, nuevoEstado);
        }
    }
}
