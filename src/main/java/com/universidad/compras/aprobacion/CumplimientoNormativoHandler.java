package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

// Primer eslabon: solo actua sobre solicitudes INTERNACIONAL; las demas pasan intactas al nivel por monto
public class CumplimientoNormativoHandler extends NivelAprobacionHandler {
    @Override
    public ResultadoAprobacion procesar(Solicitud solicitud) {
        if ("INTERNACIONAL".equalsIgnoreCase(solicitud.getCategoria())) {
            return resolver(solicitud, true, "Revisor de Cumplimiento Normativo",
                "Aprobado por Cumplimiento Normativo Internacional");
        }
        return delegarAlSiguiente(solicitud);
    }
}
