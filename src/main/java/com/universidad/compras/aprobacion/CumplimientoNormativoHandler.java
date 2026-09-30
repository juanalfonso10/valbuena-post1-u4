package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class CumplimientoNormativoHandler extends NivelAprobacionHandler {
    @Override
    public ResultadoAprobacion procesar(Solicitud solicitud) {
        if ("INTERNACIONAL".equalsIgnoreCase(solicitud.getCategoria())) {
            solicitud.setEstado("APROBADA");
            solicitud.setNivelResolutor("Revisor de Cumplimiento Normativo");
            return new ResultadoAprobacion(true, "Revisor de Cumplimiento Normativo", "Aprobado por Cumplimiento Normativo Internacional");
        }
        return delegarAlSiguiente(solicitud);
    }
}
