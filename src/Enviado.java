import exceptions.ExcepcionGeneral;

public class Enviado implements EstadoPedido {
    @Override
    public void agregarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("No se puede agregar productos a  un pedido enviado");
    }

    @Override
    public void quitarVendible(Pedido pedido, Vendible vendible) {
        throw new ExcepcionGeneral("No se puede quitar productos a  un pedido que esta  enviado");
    }

    @Override
    public void confirmar(Pedido pedido) {
        throw new ExcepcionGeneral("El pedido ya fue confirmado");
    }

    @Override
    public void cancelar(Pedido pedido) {
        // Solo reembolso productos (sin envío), no se repone stock porque ya salió?
        // Según el enunciado: "solo se reembolsa el costo del producto pero no del envío"
        // y no menciona reposición de stock. Asumimos que no se repone porque ya está en camino.
        //pedido.generarNotaCredito(pedido.calcularTotal());
        pedido.setEstado(new Cancelado());
    }

    @Override
    public void pasarAEnPreparacion(Pedido pedido) {
        throw new ExcepcionGeneral("No se puede volver a preparación desde el estado actual de  enviado");
    }

    @Override
    public void pasarAEnviado(Pedido pedido) {
        throw new ExcepcionGeneral("El pedido ya se encuentra en  enviado");
    }

    @Override
    public void pasarAEntregado(Pedido pedido) {
        pedido.setEstado(new Entregado());
    }
}