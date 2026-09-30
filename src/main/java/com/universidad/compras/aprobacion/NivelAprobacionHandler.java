package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public abstract class NivelAprobacionHandler {
    protected NivelAprobacionHandler siguiente;

    public NivelAprobacionHandler enlazarSiguiente(NivelAprobacionHandler siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public abstract ResultadoAprobacion procesar(Solicitud solicitud);

    protected ResultadoAprobacion delegarAlSiguiente(Solicitud solicitud) {
        if (siguiente != null) {
            return siguiente.procesar(solicitud);
        }
        return new ResultadoAprobacion(false, "Sistema", "Ningún nivel pudo resolver la solicitud");
    }
}
