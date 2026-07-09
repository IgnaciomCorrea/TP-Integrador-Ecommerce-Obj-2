package pedido;


public class Cancelado extends EstadoPedido {

    @Override
    public boolean esCanceladoConReposicion(EstadoPedido anterior) {
        return anterior.debeReponerStockAlCancelar();
    }

    @Override
    public boolean debeEnviarCupon() {
        return true;
    }

}