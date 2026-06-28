package MetodoDeEnvio;

public class EnvioEstandar  extends MetodoDeEnvio{

    public EnvioEstandar(){
        super();
    }

    public float calcularCosto(Pedido pedido){
        return 0;
    }
    public int estimarDiasDeEntrega(Pedido pedido){
        return correoArgentino.estimarEnvio(pedido.peso() , pedido.direccionDestino());
    }




}
