package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Service;

@Service
public class ServicioAprobacionImpl implements ServicioAprobacion {
    private final NivelAprobacionHandler cadena;

    public ServicioAprobacionImpl() {
        this(false);
    }

    public ServicioAprobacionImpl(boolean incluirCumplimiento) {
        NivelAprobacionHandler supervisor = new SupervisorHandler();
        NivelAprobacionHandler gerente = new GerenteAreaHandler();
        NivelAprobacionHandler director = new DirectorFinancieroHandler();

        if (incluirCumplimiento) {
            NivelAprobacionHandler cumplimiento = new CumplimientoNormativoHandler();
            cumplimiento.enlazarSiguiente(supervisor);
            supervisor.enlazarSiguiente(gerente);
            gerente.enlazarSiguiente(director);
            this.cadena = cumplimiento;
        } else {
            supervisor.enlazarSiguiente(gerente);
            gerente.enlazarSiguiente(director);
            this.cadena = supervisor;
        }
    }

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        return cadena.procesar(solicitud);
    }
}
