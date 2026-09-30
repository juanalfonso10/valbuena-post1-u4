package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

class NotificacionEstadoTest {

    @Test
    void cambiarEstadoDisparaLasTresReaccionesSinLanzarExcepcion() {
        Solicitud s = new Solicitud("S-020", "ana@udes.edu.co", 2500000, "SOFTWARE", "CC-100");
        GestorNotificacionesEstado mecanismo = new GestorNotificacionesEstado();
        assertDoesNotThrow(() -> {
            mecanismo.cambiarEstado(s, "APROBADA");
            assertEquals("APROBADA", s.getEstado());
        });
    }

    @Test
    void agregarUnCuartoSuscriptorDePruebaNoRequiereModificarElMecanismo() {
        Solicitud s = new Solicitud("S-021", "luis@udes.edu.co", 4000000, "MATERIAL_OFICINA", "CC-200");
        GestorNotificacionesEstado mecanismo = new GestorNotificacionesEstado();
        AtomicBoolean cuartoReacciono = new AtomicBoolean(false);

        mecanismo.suscribir((solicitud, anterior, nuevo) -> cuartoReacciono.set(true));

        assertDoesNotThrow(() -> {
            mecanismo.cambiarEstado(s, "EJECUTADA");
            assertTrue(cuartoReacciono.get());
        });
    }
}
