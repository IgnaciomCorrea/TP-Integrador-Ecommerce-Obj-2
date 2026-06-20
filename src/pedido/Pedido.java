package pedido;

import Catalogo.Vendible;
import notificaciones.CambioEstadoEvento;
import notificaciones.ObservadorPedido;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private EstadoPedido estado;
    private List<Vendible> vendibles;
    private List<ObservadorPedido> observadores;

    public Pedido() {
        this.estado = new Borrador();
        this.vendibles = new ArrayList<>();
        this.observadores = new ArrayList<>();
    }

    public void agregarObservador(ObservadorPedido observador) {
        observadores.add(observador);
    }

    public void setEstado(EstadoPedido nuevoEstado) {
        EstadoPedido estadoAnterior = this.estado;
        this.estado = nuevoEstado;
        CambioEstadoEvento evento = new CambioEstadoEvento(estadoAnterior, nuevoEstado);
        for (ObservadorPedido obs : observadores) {
            obs.onCambioEstado(evento, this);
        }
    }

    public void agregarVendible(Vendible vendible) {
        estado.agregarVendible(this, vendible);
    }

    public void quitarVendible(Vendible vendible) {
        estado.quitarVendible(this, vendible);
    }

    public void confirmarPedido() {
        estado.confirmar(this);
    }

    public void cancelarPedido() {
        estado.cancelar(this);
    }

    public void pasarAEnPreparacion() {
        estado.pasarAEnPreparacion(this);
    }

    public void pasarAEnviado() {
        estado.pasarAEnviado(this);
    }

    public void pasarAEntregado() {
        estado.pasarAEntregado(this);
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public List<Vendible> getVendibles() {
        return vendibles;
    }
}