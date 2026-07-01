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
            throw new ExcepcionGeneral("No se confirmara este pedido porque no tiene vendibles");
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
        throw new ExcepcionGeneral("No se puede pasar al siguiente estado que es preaparacion");
    }

    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede pasar a enviado desde el estado actual ");
    }

    @Override
    public void pasarAEntregado(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede pasar a entregado desde el estado actual ");
    }
}