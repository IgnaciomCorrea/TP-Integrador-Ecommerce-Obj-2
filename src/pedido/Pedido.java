package pedido;

import Catalogo.ItemVendible;
import notificaciones.CambioEstadoEvento;
import notificaciones.ObservadorPedido;

import java.util.ArrayList;
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

    public void decrementarStock() {
        // acá implementar lógica de decremento de stock
        System.out.println("Stock decrementado para el pedido.");
    }

    /**
     * Repone el stock de todos los productos del pedido.
     * Implementación pendiente.
     */
    public void reponerStock() {
        // acá implementar lógica de reposición de stock
        System.out.println("Stock repuesto para el pedido.");
    }

    /**
     * Genera una nota de crédito por el monto especificado.
     * Implementación pendiente.
     */
    public void generarNotaCredito(double monto, String motivo) {
        if (this.notaCredito != null) {
            throw new IllegalStateException("El pedido ya tiene una nota de crédito asociada");
        }
        this.notaCredito = NotaCredito.crear(this, monto, motivo);
        // Aquí se podría registrar la nota en un repositorio o sistema contable
        System.out.println("Nota de crédito generada: " + this.notaCredito);
    }

    /**
     * Devuelve la nota de crédito asociada (puede ser null si no se ha generado).
     */
    public NotaCredito getNotaCredito() {
        return notaCredito;
    }
    /**
     * Calcula el costo de envío según la estrategia seleccionada.
     * Este método debería delegar en el módulo de envíos (Strategy).
     * Por ahora devuelve un valor fijo como placeholder.
     */
    public double calcularCostoEnvio() {
        return 0; // placeholder
    }
}