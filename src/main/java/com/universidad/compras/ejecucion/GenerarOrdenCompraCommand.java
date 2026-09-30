package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;

public class GenerarOrdenCompraCommand implements OperacionCompraCommand {
    private final OrdenCompraService ordenCompraService;
    private final Solicitud solicitud;
    private final String proveedor;
    private String numeroOrden;

    public GenerarOrdenCompraCommand(OrdenCompraService ordenCompraService, Solicitud solicitud, String proveedor) {
        this.ordenCompraService = ordenCompraService;
        this.solicitud = solicitud;
        this.proveedor = proveedor;
    }

    @Override
    public void ejecutar() {
        this.numeroOrden = ordenCompraService.generar(solicitud.getId(), proveedor);
        solicitud.setEstado("EJECUTADA");
    }

    @Override
    public void deshacer() {
        if (numeroOrden != null) {
            ordenCompraService.cancelar(numeroOrden);
            this.numeroOrden = null;
        }
    }

    @Override
    public String getNombre() {
        return "Generación de Orden de Compra para " + proveedor;
    }
}
