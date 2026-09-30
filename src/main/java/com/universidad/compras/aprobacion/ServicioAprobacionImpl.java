package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.GestorNotificacionesEstado;
import org.springframework.stereotype.Service;

// Arma la cadena una sola vez. ControladorSolicitudes no sabe cuantos niveles hay, en que orden
// se consultan ni que la categoria INTERNACIONAL agrega un nivel: eso vive solo aqui.
@Service
public class ServicioAprobacionImpl implements ServicioAprobacion {
    private final NivelAprobacionHandler cadena;
    private final GestorNotificacionesEstado notificaciones;

    public ServicioAprobacionImpl(GestorNotificacionesEstado notificaciones) {
        this.notificaciones = notificaciones;
        NivelAprobacionHandler cumplimiento = new CumplimientoNormativoHandler();
        cumplimiento.enlazarSiguiente(new SupervisorHandler())
            .enlazarSiguiente(new GerenteAreaHandler())
            .enlazarSiguiente(new DirectorFinancieroHandler());
        this.cadena = cumplimiento;
    }

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        ResultadoAprobacion resultado = cadena.procesar(solicitud);
        notificaciones.cambiarEstado(solicitud, resultado.isAprobada() ? "APROBADA" : "RECHAZADA");
        return resultado;
    }
}
