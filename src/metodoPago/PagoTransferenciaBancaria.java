package metodoPago;

public class PagoTransferenciaBancaria extends MetodoPago<CuentaCorriente>{
    private String cupon;

    protected void validarDatos(Double monto, CuentaCorriente medio) {
    	ApiTransferenciaBancaria.validarDatos(medio.getCbu(), medio.getAlias());
    }
    
    protected void reservarFondos(Double monto, CuentaCorriente medio) {
    	ApiTransferenciaBancaria.reservarFondos(medio.getCbu(), medio.getAlias());
    }
    
    protected void ejecutarTransaccion(Double monto, CuentaCorriente medio) {
    	ApiTransferenciaBancaria.ejecutarTransaccion(medio.getCbu(), medio.getAlias());
    }

    @Override
    protected void notificarResultado(Double monto, CuentaCorriente medio) {
        this.cupon = "Registramos pago por: "+monto+
                     "\nDesde CBU: "+medio.getCbu()+
                     "\nCon nro de transacción: " + ApiTransferenciaBancaria.getNroUltimaTransaccion(medio.getCbu(), medio.getAlias()) ;
    }


}
