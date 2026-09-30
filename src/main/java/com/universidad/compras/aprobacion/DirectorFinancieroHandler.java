package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

// Ultimo eslabon: autoridad sin limite superior, siempre resuelve
public class DirectorFinancieroHandler extends NivelAprobacionHandler {
    @Override
    public ResultadoAprobacion procesar(Solicitud solicitud) {
        return resolver(solicitud, true, "Director Financiero", "Aprobado por Dirección Financiera");
    }
}
