package metodoPago;

public class PagoTransferenciaBancaria extends MetodoPago<CuentaCorriente>{

    protected void validarDatos(Double monto, CuentaCorriente medio) {
    	ApiTransferenciaBancaria.validarDatos(medio.getCbu(), medio.getAlias());
    }
    
    protected void reservarFondos(Double monto, CuentaCorriente medio) {
    	ApiTransferenciaBancaria.reservarFondos(medio.getCbu(), medio.getAlias());
    }
    
    protected void ejecutarTransaccion(Double monto, CuentaCorriente medio) {
    	ApiTransferenciaBancaria.ejecutarTransaccion(medio.getCbu(), medio.getAlias());
    }
	
}
