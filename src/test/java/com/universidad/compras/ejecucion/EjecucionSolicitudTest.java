package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EjecucionSolicitudTest {

    @Test
    void ejecutarReservaPresupuestoYGeneraOrden() {
        Solicitud s = new Solicitud("S-010", "ana@udes.edu.co", 3000000, "SOFTWARE", "CC-100");
        s.setEstado("APROBADA");
        
        PresupuestoService presupuesto = new PresupuestoService();
        OrdenCompraService ordenes = new OrdenCompraService();
        GestorOperacionesEjecucion ejecutor = new GestorOperacionesEjecucion();

        ejecutor.ejecutarOperacion(new ReservarPresupuestoCommand(presupuesto, s));
        ejecutor.ejecutarOperacion(new GenerarOrdenCompraCommand(ordenes, s, "Proveedor Software SAS"));

        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void deshacerSoloLaUltimaOperacionNoAfectaLaAnterior() {
        Solicitud s = new Solicitud("S-011", "luis@udes.edu.co", 4000000, "MATERIAL_OFICINA", "CC-200");
        PresupuestoService presupuesto = new PresupuestoService();
        OrdenCompraService ordenes = new OrdenCompraService();
        GestorOperacionesEjecucion ejecutor = new GestorOperacionesEjecucion();

        assertDoesNotThrow(() -> {
            ejecutor.ejecutarOperacion(new ReservarPresupuestoCommand(presupuesto, s));
            ejecutor.ejecutarOperacion(new GenerarOrdenCompraCommand(ordenes, s, "Papelería UDES"));
            ejecutor.deshacerUltimaOperacion();
        });
    }

    @Test
    void elHistorialConservaTodasLasOperacionesNoSoloLaUltima() {
        Solicitud s = new Solicitud("S-012", "ana@udes.edu.co", 5000000, "SOFTWARE", "CC-100");
        PresupuestoService presupuesto = new PresupuestoService();
        OrdenCompraService ordenes = new OrdenCompraService();
        GestorOperacionesEjecucion ejecutor = new GestorOperacionesEjecucion();

        assertDoesNotThrow(() -> {
            ejecutor.ejecutarOperacion(new ReservarPresupuestoCommand(presupuesto, s));
            ejecutor.ejecutarOperacion(new GenerarOrdenCompraCommand(ordenes, s, "Tech Provider"));
            assertEquals(2, ejecutor.getHistorial().size());
        });
    }
}
