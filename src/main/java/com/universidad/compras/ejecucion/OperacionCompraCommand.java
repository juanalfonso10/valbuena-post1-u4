package com.universidad.compras.ejecucion;

public interface OperacionCompraCommand {
    void ejecutar();
    void deshacer();
    String getNombre();
}
