package MetodoDeEnvio;

public class EnvioExpress  extends MetodoDeEnvio{
    private float  precioBase;

    public EnvioExpress (float precioBase){
        super();
        this.precioBase = precioBase;
    }
    public float calcularCosto(Pedido pedido) {
        return pedido.valorTotal() + precioBase ;
    }

    public int estimarDiasDeEntrega(){
        return 1;
    }

}
