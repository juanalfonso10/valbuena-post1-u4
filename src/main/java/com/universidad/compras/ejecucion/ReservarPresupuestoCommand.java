package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;

public class ReservarPresupuestoCommand implements OperacionCompraCommand {
    private final PresupuestoService presupuestoService;
    private final Solicitud solicitud;
    private boolean ejecutado = false;

    public ReservarPresupuestoCommand(PresupuestoService presupuestoService, Solicitud solicitud) {
        this.presupuestoService = presupuestoService;
        this.solicitud = solicitud;
    }

    @Override
    public void ejecutar() {
        if (presupuestoService.reservar(solicitud.getCentroCosto(), solicitud.getMonto())) {
            this.ejecutado = true;
        }
    }

    @Override
    public void deshacer() {
        if (ejecutado) {
            presupuestoService.liberar(solicitud.getCentroCosto(), solicitud.getMonto());
            this.ejecutado = false;
        }
    }

    @Override
    public String getNombre() {
        return "Reserva de Presupuesto ($" + solicitud.getMonto() + " en " + solicitud.getCentroCosto() + ")";
    }
}
