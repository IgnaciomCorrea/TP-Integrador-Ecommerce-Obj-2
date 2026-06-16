import exceptions.ExcepcionGeneral;

public class Borrador implements EstadoPedido {
    @Override
    public void agregarVendible(Pedido pedido, Vendible vendible) {
        pedido.getVendibles().add(vendible);
    }

    @Override
    public void quitarVendible(Pedido pedido, Vendible vendible) {
        pedido.getVendibles().remove(vendible);
    }

    @Override
    public void confirmar(Pedido pedido) {
        if (pedido.getVendibles().isEmpty()) {
            throw new ExcepcionGeneral("No se puede confirmar un pedido sin ítems");
        }
        pedido.setEstado(new Confirmado());
    }

    @Override
    public void cancelar(Pedido pedido) {
        // Cancelado desde borrador no requiere reembolso ni reposición de stock
        pedido.setEstado(new Cancelado());
    }

    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede pasar a preparación desde Borrador");
    }

    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede pasar a enviado desde Borrador");
    }

    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede pasar a entregado desde Borrador");
    }
}