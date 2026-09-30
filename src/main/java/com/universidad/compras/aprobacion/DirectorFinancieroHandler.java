package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class DirectorFinancieroHandler extends NivelAprobacionHandler {
    @Override
    public ResultadoAprobacion procesar(Solicitud solicitud) {
        solicitud.setEstado("APROBADA");
        solicitud.setNivelResolutor("Director Financiero");
        return new ResultadoAprobacion(true, "Director Financiero", "Aprobado por Dirección Financiera");
    }
}
