public interface EstadoPedido {
    void agregarVendible(Pedido pedido, Vendible vendible);
    void quitarVendible(Pedido pedido, Vendible vendible);
    void confirmar(Pedido pedido);
    void cancelar(Pedido pedido);
    void pasarAEnPreparacion(Pedido pedido);
    void pasarAEnviado(Pedido pedido);
    void pasarAEntregado(Pedido pedido);
}