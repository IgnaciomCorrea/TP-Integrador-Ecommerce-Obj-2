package pedido;

import Catalogo.ItemVendible;
import notificaciones.CambioEstadoEvento;
import notificaciones.ObservadorPedido;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pedido {
    private EstadoPedido estado;
    private List<ItemVendible> vendibles;
    private List<ObservadorPedido> observadores;
    private NotaCredito notaCredito;

    public Pedido() {
        this.estado = new Borrador();
        this.vendibles = new ArrayList<>();
        this.observadores = new ArrayList<>();
        this.notaCredito = null;
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

    public double calcularPrecioTotal() {
        return vendibles.stream().mapToDouble(ItemVendible::getPrecioFinal).sum();
    }

    public double calcularPesoTotal() {
        return vendibles.stream().mapToDouble(ItemVendible::getPeso).sum();
    }

    public void agregarVendible(ItemVendible vendible) {
        estado.agregarVendible(this, vendible);
    }

    public void quitarVendible(ItemVendible vendible) {
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

    public List<ItemVendible> getVendibles() {
        return vendibles;
    }

    public void generarNotaCredito(double monto, String motivo) {
        if (this.notaCredito != null) {
            throw new IllegalStateException("El pedido ya tiene una nota de crédito asociada");
        }
        this.notaCredito = NotaCredito.crear(this, monto, motivo);
        System.out.println("Nota de crédito generada: " + this.notaCredito);
    }


    public NotaCredito getNotaCredito() {
        return notaCredito;
    }

    public double calcularCostoEnvio() {
        return 0; // placeholder
    }

    public List<ObservadorPedido> getObservadores() {
        return new ArrayList<>(observadores);
    }
}