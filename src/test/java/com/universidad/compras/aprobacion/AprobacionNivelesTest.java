package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.GestorNotificacionesEstado;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AprobacionNivelesTest {

    private final GestorNotificacionesEstado notificaciones = new GestorNotificacionesEstado();
    private final ServicioAprobacion servicio = new ServicioAprobacionImpl(notificaciones);

    @Test
    void solicitudDentroDeAutoridadDelSupervisorSeAprueba() {
        Solicitud s = new Solicitud("S-001", "ana@udes.edu.co", 1500000, "MATERIAL_OFICINA", "CC-100");
        ResultadoAprobacion r = servicio.evaluar(s);
        assertTrue(r.isAprobada());
        assertEquals("Supervisor de Área", r.getNivelResolutor());
        assertEquals("Supervisor de Área", s.getNivelResolutor());
    }

    @Test
    void solicitudQueSuperaAlSupervisorEscalaAlGerente() {
        Solicitud s = new Solicitud("S-002", "luis@udes.edu.co", 6000000, "SOFTWARE", "CC-200");
        ResultadoAprobacion r = servicio.evaluar(s);
        assertTrue(r.isAprobada());
        assertEquals("Gerente de Área", r.getNivelResolutor());
    }

    @Test
    void solicitudQueSuperaAlGerenteLlegaAlDirectorFinanciero() {
        Solicitud s = new Solicitud("S-004", "ana@udes.edu.co", 25000000, "SOFTWARE", "CC-200");
        assertEquals("Director Financiero", servicio.evaluar(s).getNivelResolutor());
    }

    @Test
    void solicitudInternacionalPasaPorCumplimientoAntesDelNivelPorMonto() {
        // El mismo servicio que usa el endpoint: el nivel de Cumplimiento siempre esta en la cadena
        Solicitud s = new Solicitud("S-003", "gerencia@udes.edu.co", 1000000, "INTERNACIONAL", "CC-300");
        ResultadoAprobacion r = servicio.evaluar(s);
        assertEquals("Revisor de Cumplimiento Normativo", r.getNivelResolutor());
    }

    @Test
    void laEvaluacionCambiaElEstadoYDisparaLasNotificaciones() {
        List<String> cambios = new ArrayList<>();
        notificaciones.suscribir((solicitud, anterior, nuevo) -> cambios.add(anterior + "->" + nuevo));

        Solicitud s = new Solicitud("S-005", "ana@udes.edu.co", 1500000, "SOFTWARE", "CC-100");
        servicio.evaluar(s);

        assertEquals("APROBADA", s.getEstado());
        assertEquals(List.of("PENDIENTE->APROBADA"), cambios);
    }
}
