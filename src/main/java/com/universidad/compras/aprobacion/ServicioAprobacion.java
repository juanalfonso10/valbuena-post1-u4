package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public interface ServicioAprobacion {
    ResultadoAprobacion evaluar(Solicitud solicitud);
}
