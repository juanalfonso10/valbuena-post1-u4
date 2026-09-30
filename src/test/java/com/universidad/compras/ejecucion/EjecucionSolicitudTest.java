package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.GestorNotificacionesEstado;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EjecucionSolicitudTest {

    // Espias: heredan de los servicios dados (sin modificarlos) solo para contar las llamadas
    static class PresupuestoEspia extends PresupuestoService {
        int liberaciones = 0;
        @Override
        public void liberar(String centroCosto, double monto) {
            liberaciones++;
            super.liberar(centroCosto, monto);
        }
    }

    static class OrdenCompraEspia extends OrdenCompraService {
        int cancelaciones = 0;
        @Override
        public void cancelar(String numeroOrden) {
            cancelaciones++;
            super.cancelar(numeroOrden);
        }
    }

    private final GestorNotificacionesEstado notificaciones = new GestorNotificacionesEstado();

    @Test
    void ejecutarReservaPresupuestoYGeneraOrden() {
        Solicitud s = new Solicitud("S-010", "ana@udes.edu.co", 3000000, "SOFTWARE", "CC-100");
        s.setEstado("APROBADA");
        GestorOperacionesEjecucion ejecutor = new GestorOperacionesEjecucion();

        ejecutor.ejecutarOperacion(new ReservarPresupuestoCommand(new PresupuestoService(), s));
        ejecutor.ejecutarOperacion(new GenerarOrdenCompraCommand(new OrdenCompraService(), s, "Proveedor Software SAS", notificaciones));

        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void deshacerSoloLaUltimaOperacionNoAfectaLaAnterior() {
        Solicitud s = new Solicitud("S-011", "luis@udes.edu.co", 4000000, "MATERIAL_OFICINA", "CC-200");
        s.setEstado("APROBADA");
        PresupuestoEspia presupuesto = new PresupuestoEspia();
        OrdenCompraEspia ordenes = new OrdenCompraEspia();
        GestorOperacionesEjecucion ejecutor = new GestorOperacionesEjecucion();

        ejecutor.ejecutarOperacion(new ReservarPresupuestoCommand(presupuesto, s));
        ejecutor.ejecutarOperacion(new GenerarOrdenCompraCommand(ordenes, s, "Papelería UDES", notificaciones));
        ejecutor.deshacerUltimaOperacion();

        assertEquals(1, ordenes.cancelaciones, "se debe cancelar la orden");
        assertEquals(0, presupuesto.liberaciones, "la reserva de presupuesto no se toca");
        assertEquals("APROBADA", s.getEstado(), "el undo restaura el estado anterior");
    }

    @Test
    void elHistorialConservaTodasLasOperacionesNoSoloLaUltima() {
        Solicitud s = new Solicitud("S-012", "ana@udes.edu.co", 5000000, "SOFTWARE", "CC-100");
        s.setEstado("APROBADA");
        GestorOperacionesEjecucion ejecutor = new GestorOperacionesEjecucion();

        ejecutor.ejecutarOperacion(new ReservarPresupuestoCommand(new PresupuestoService(), s));
        ejecutor.ejecutarOperacion(new GenerarOrdenCompraCommand(new OrdenCompraService(), s, "Tech Provider", notificaciones));

        assertEquals(2, ejecutor.getHistorial().size());
        assertTrue(ejecutor.getHistorial().get(0).getNombre().startsWith("Reserva de Presupuesto"));
        assertTrue(ejecutor.getHistorial().get(1).getNombre().startsWith("Generación de Orden"));
    }

    @Test
    void ejecutarYDeshacerDisparanNotificaciones() {
        List<String> cambios = new ArrayList<>();
        notificaciones.suscribir((solicitud, anterior, nuevo) -> cambios.add(anterior + "->" + nuevo));
        Solicitud s = new Solicitud("S-013", "ana@udes.edu.co", 1000000, "SOFTWARE", "CC-100");
        s.setEstado("APROBADA");
        GestorOperacionesEjecucion ejecutor = new GestorOperacionesEjecucion();

        ejecutor.ejecutarOperacion(new GenerarOrdenCompraCommand(new OrdenCompraService(), s, "Proveedor", notificaciones));
        ejecutor.deshacerUltimaOperacion();

        assertEquals(List.of("APROBADA->EJECUTADA", "EJECUTADA->APROBADA"), cambios);
    }
}
