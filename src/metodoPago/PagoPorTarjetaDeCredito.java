package metodoPago;

import java.util.ArrayList;
import java.util.List;

public class PagoPorTarjetaDeCredito extends MetodoPago<TarjetaDeCredito>{
    private String cupon;

    protected void validarDatos(Double monto, TarjetaDeCredito medio) {
    	ApiTarjeta.validarDatos(medio.getNroTarjeta(), medio.getCvv(), medio.getFechaVencimiento());
    }
    
    protected void reservarFondos(Double monto, TarjetaDeCredito medio) {
    	ApiTarjeta.reservarFondos(medio.getNroTarjeta(), medio.getCvv(), medio.getFechaVencimiento());
    }
    
    protected void ejecutarTransaccion(Double monto, TarjetaDeCredito medio) {
    	ApiTarjeta.ejecutarTransaccion(medio.getNroTarjeta(), medio.getCvv(), medio.getFechaVencimiento());
    }

    @Override
    protected void notificarResultado(Double monto, TarjetaDeCredito medio) {
        this.cupon = "Registramos pago por: "+monto+"\ndesde la tarjeta: "+medio.getNroTarjeta();
    }

    public String getCupon() {
        return cupon;
    }
}
