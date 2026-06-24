package metodoPago;

public class PagoPorTarjetaDeCredito extends MetodoPago<TarjetaDeCredito>{

    protected void validarDatos(Double monto, TarjetaDeCredito medio) {
    	ApiTarjeta.validarDatos(medio.getNroTarjeta(), medio.getCvv(), medio.getFechaVencimiento());
    }
    
    protected void reservarFondos(Double monto, TarjetaDeCredito medio) {
    	ApiTarjeta.reservarFondos(medio.getNroTarjeta(), medio.getCvv(), medio.getFechaVencimiento());
    }
    
    protected void ejecutarTransaccion(Double monto, TarjetaDeCredito medio) {
    	ApiTarjeta.ejecutarTransaccion(medio.getNroTarjeta(), medio.getCvv(), medio.getFechaVencimiento());
    }
}
