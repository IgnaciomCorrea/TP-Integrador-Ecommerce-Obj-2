package envio;

import direccion.Direccion;
import pedido.Pedido;
import sucursal.Sucursal;

public class EnvioEstandar implements MetodoEnvio {
    private final CorreoArgentina correo;

    public EnvioEstandar(CorreoArgentina correo) {
        this.correo = correo;
    }

    @Override
    public double calcularCosto(Pedido pedido, Direccion direccion, Sucursal sucursal) {
        double peso = 0.7f;//pedido.calcularPesoTotal(); ToDo
        return correo.estimarEnvio((float) peso, direccion);
    }

    @Override
    public int calcularTiempo(Pedido pedido, Direccion direccion, Sucursal sucursal) {
        return 5;
    }
}