package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.GestorNotificacionesEstado;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransicionEstadoTest {

    @Test
    void ejecutarUnaSolicitudAprobadaLaDejaEjecutada() {
        Solicitud s = new Solicitud("S-030", "luis@udes.edu.co", 3000000, "MATERIAL_OFICINA", "CC-200");
        s.setEstado("APROBADA");
        SolicitudContexto contexto = new SolicitudContexto(s);

        assertEquals("Ejecutada", contexto.ejecutar());
        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void ejecutarUnaSolicitudPendienteSeRechazaSinCambiarElEstado() {
        Solicitud s = new Solicitud("S-031", "ana@udes.edu.co", 1000000, "SOFTWARE", "CC-100");
        SolicitudContexto contexto = new SolicitudContexto(s);

        assertTrue(contexto.ejecutar().startsWith("Error"));
        assertEquals("PENDIENTE", s.getEstado());
    }

    @Test
    void unaSolicitudEjecutadaNoPuedeVolverAEjecutarse() {
        Solicitud s = new Solicitud("S-032", "ana@udes.edu.co", 1000000, "SOFTWARE", "CC-100");
        s.setEstado("EJECUTADA");
        SolicitudContexto contexto = new SolicitudContexto(s);

        assertTrue(contexto.ejecutar().startsWith("Error"));
        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void cadaTransicionValidaNotificaYLaInvalidaNo() {
        GestorNotificacionesEstado notificaciones = new GestorNotificacionesEstado();
        List<String> cambios = new ArrayList<>();
        notificaciones.suscribir((solicitud, anterior, nuevo) -> cambios.add(anterior + "->" + nuevo));
        Solicitud s = new Solicitud("S-033", "ana@udes.edu.co", 1000000, "SOFTWARE", "CC-100");
        SolicitudContexto contexto = new SolicitudContexto(s, notificaciones);

        contexto.ejecutar();   // invalida en PENDIENTE: no cambia ni notifica
        contexto.aprobar();
        contexto.cancelar();

        assertEquals(List.of("PENDIENTE->APROBADA", "APROBADA->CANCELADA"), cambios);
        assertEquals("CANCELADA", s.getEstado());
    }
}
