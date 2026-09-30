package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.GestorNotificacionesEstado;

public class GenerarOrdenCompraCommand implements OperacionCompraCommand {
    private final OrdenCompraService ordenCompraService;
    private final Solicitud solicitud;
    private final String proveedor;
    private final GestorNotificacionesEstado notificaciones;
    private String numeroOrden;
    private String estadoAnterior;

    public GenerarOrdenCompraCommand(OrdenCompraService ordenCompraService, Solicitud solicitud,
                                     String proveedor, GestorNotificacionesEstado notificaciones) {
        this.ordenCompraService = ordenCompraService;
        this.solicitud = solicitud;
        this.proveedor = proveedor;
        this.notificaciones = notificaciones;
    }

    @Override
    public void ejecutar() {
        this.numeroOrden = ordenCompraService.generar(solicitud.getId(), proveedor);
        this.estadoAnterior = solicitud.getEstado();
        notificaciones.cambiarEstado(solicitud, "EJECUTADA");
    }

    @Override
    public void deshacer() {
        if (numeroOrden != null) {
            ordenCompraService.cancelar(numeroOrden);
            this.numeroOrden = null;
            // El undo tambien restaura el estado que tenia la solicitud antes de ejecutar
            notificaciones.cambiarEstado(solicitud, estadoAnterior);
        }
    }

    @Override
    public String getNombre() {
        return "Generación de Orden de Compra para " + proveedor;
    }
}
