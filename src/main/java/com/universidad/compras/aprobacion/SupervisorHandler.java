package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class SupervisorHandler extends NivelAprobacionHandler {
    private static final double LIMITE = 2_000_000;

    @Override
    public ResultadoAprobacion procesar(Solicitud solicitud) {
        if (solicitud.getMonto() <= LIMITE) {
            return resolver(solicitud, true, "Supervisor de Área", "Aprobado por monto menor o igual a $2.000.000");
        }
        return delegarAlSiguiente(solicitud);
    }
}
