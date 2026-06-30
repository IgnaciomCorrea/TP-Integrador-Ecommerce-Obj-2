package pedido;

import Catalogo.ItemVendible;
import Catalogo.Vendible;
import envio.MetodoEnvio;
import metodoPago.MedioDePago;
import metodoPago.MetodoPago;

public interface EstadoPedido {
    void agregarVendible(Pedido pedido, ItemVendible vendible);
    void quitarVendible(Pedido pedido, ItemVendible vendible);
    void confirmar(Pedido pedido);
    void cancelar(Pedido pedido);
    void pasarAEnPreparacion(Pedido pedido);
    void pasarAEnviado(Pedido pedido);
    void pasarAEntregado(Pedido pedido);
    void setMetodoDePago(Pedido pedido, MetodoPago<?> metodoPago, MedioDePago medioDePago);
    void setMetodoDeEnvio(Pedido pedido, MetodoEnvio metodoEnvio);
}