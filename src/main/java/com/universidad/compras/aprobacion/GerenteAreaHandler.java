package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class GerenteAreaHandler extends NivelAprobacionHandler {
    private static final double LIMITE = 10_000_000;

    @Override
    public ResultadoAprobacion procesar(Solicitud solicitud) {
        if (solicitud.getMonto() <= LIMITE) {
            return resolver(solicitud, true, "Gerente de Área", "Aprobado por monto menor o igual a $10.000.000");
        }
        return delegarAlSiguiente(solicitud);
    }
}
