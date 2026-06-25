package pedido;

import Catalogo.ItemVendible;
import Catalogo.Vendible;

public interface EstadoPedido {
    void agregarVendible(Pedido pedido, ItemVendible vendible);
    void quitarVendible(Pedido pedido, ItemVendible vendible);
    void confirmar(Pedido pedido);
    void cancelar(Pedido pedido);
    void pasarAEnPreparacion(Pedido pedido);
    void pasarAEnviado(Pedido pedido);
    void pasarAEntregado(Pedido pedido);
}