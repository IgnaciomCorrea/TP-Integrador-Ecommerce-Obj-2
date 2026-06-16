import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private EstadoPedido estado;
    private List<Vendible> vendibles;

    public Pedido() {
        this.estado = new Borrador();
        this.vendibles = new ArrayList<>();
    }

    public void agregarVendible(Vendible vendible) {
        estado.agregarVendible(this, vendible);
    }

    public void quitarVendible(Vendible vendible) {
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

    public void setEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public List<Vendible> getVendibles() {
        return vendibles;
    }
}