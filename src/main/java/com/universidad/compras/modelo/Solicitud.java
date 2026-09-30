package com.universidad.compras.modelo;

public class Solicitud {
    private final String id;
    private final String solicitanteEmail;
    private final double monto;
    private final String categoria;
    private final String centroCosto;
    private String estado;
    private String nivelResolutor;

    public Solicitud(String id, String solicitanteEmail, double monto, String categoria, String centroCosto) {
        this.id = id;
        this.solicitanteEmail = solicitanteEmail;
        this.monto = monto;
        this.categoria = categoria;
        this.centroCosto = centroCosto;
        this.estado = "PENDIENTE";
    }

    public String getId()               { return id; }
    public String getSolicitanteEmail() { return solicitanteEmail; }
    public double getMonto()            { return monto; }
    public String getCategoria()        { return categoria; }
    public String getCentroCosto()      { return centroCosto; }
    public String getEstado()           { return estado; }
    public String getNivelResolutor()   { return nivelResolutor; }

    public void setEstado(String estado)              { this.estado = estado; }
    public void setNivelResolutor(String nivel)        { this.nivelResolutor = nivel; }

    @Override
    public String toString() {
        return "Solicitud{id='" + id + "', estado='" + estado + "', monto=" + monto + "}";
    }
}
