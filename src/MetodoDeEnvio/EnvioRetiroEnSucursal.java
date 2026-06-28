package MetodoDeEnvio;

public class EnvioRetiroEnSucursal  extends MetodoDeEnvio{

    public EnvioRetiroEnSucursal(){
        super()
        ;
    }

    public float calcularCosto(Pedido pedido){
        return 0 ;
        }
    }
    public int estimarDiasDeEntrega(Pedido pedido) {
        if (pedido.tieneStock()) {
            return 0;
        }else{
            return 3;
            }
    ;
    }

}
