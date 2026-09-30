package com.universidad.compras.ejecucion;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GestorOperacionesEjecucion {
    private final Deque<OperacionCompraCommand> pilaDeshacer = new ArrayDeque<>();
    private final List<OperacionCompraCommand> historial = new ArrayList<>();

    public void ejecutarOperacion(OperacionCompraCommand comando) {
        comando.ejecutar();
        pilaDeshacer.push(comando);
        historial.add(comando);
    }

    public void deshacerUltimaOperacion() {
        if (!pilaDeshacer.isEmpty()) {
            OperacionCompraCommand comando = pilaDeshacer.pop();
            comando.deshacer();
        }
    }

    public List<OperacionCompraCommand> getHistorial() {
        return new ArrayList<>(historial);
    }
}
