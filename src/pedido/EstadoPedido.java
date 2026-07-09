package pedido;

import Catalogo.ItemVendible;
import envio.MetodoEnvio;
import exceptions.PedidoExcepcion;
import metodoPago.MedioDePago;
import metodoPago.MetodoPago;

public abstract class EstadoPedido {

    public void agregarVendible(Pedido pedido, ItemVendible vendible) {
        throw new PedidoExcepcion("Operación inválida en estado: " + getNombreEstado());
    }

    public void quitarVendible(Pedido pedido, ItemVendible vendible) {
        throw new PedidoExcepcion("Operación inválida en estado: " + getNombreEstado());
    }

    public void confirmar(Pedido pedido) {
        throw new PedidoExcepcion("Operación inválida en estado: " + getNombreEstado());
    }

    public void cancelar(Pedido pedido) {
        throw new PedidoExcepcion("Operación inválida en estado: " + getNombreEstado());
    }

    public void pasarAEnPreparacion(Pedido pedido) {
        throw new PedidoExcepcion("Operación inválida en estado: " + getNombreEstado());
    }

    public void pasarAEnviado(Pedido pedido) {
        throw new PedidoExcepcion("Operación inválida en estado: " + getNombreEstado());
    }

    public void pasarAEntregado(Pedido pedido) {
        throw new PedidoExcepcion("Operación inválida en estado: " + getNombreEstado());
    }

    public void setMetodoDePago(Pedido pedido, MetodoPago<?> metodoPago, MedioDePago medioDePago) {
        throw new PedidoExcepcion("Operación inválida en estado: " + getNombreEstado());
    }

    public void setMetodoDeEnvio(Pedido pedido, MetodoEnvio metodoEnvio) {
        throw new PedidoExcepcion("Operación inválida en estado: " + getNombreEstado());
    }

    public boolean debeEnviarEmail() {
        return false;
    }

    public boolean debeGenerarFactura() {
        return false;
    }

    public boolean debeEnviarCupon() {
        return false;
    }

    public boolean esConfirmado() {
        return false;
    }

    public boolean esCanceladoConReposicion(EstadoPedido anterior) {
        return false;
    }

    public boolean esEntregado() {
        return false;
    }

    protected String getNombreEstado() {
        return this.getClass().getSimpleName();
    }

    protected boolean debeReponerStockAlCancelar() {
        return false;
    }
}