package testutils;

import direccion.Direccion;
import envio.MetodoEnvio;
import metodoPago.MedioDePago;
import metodoPago.MetodoPago;
import pedido.Pedido;
import sucursal.Sucursal;

public final class PedidoFactory {
    private PedidoFactory() {
    }

    public static Pedido pedido() {
        return new Pedido(new PagoDummy(), new EnvioDummy(), new MedioDePago());
    }

    public static Pedido pedidoConPago(PagoDummy pago) {
        return new Pedido(pago, new EnvioDummy(), new MedioDePago());
    }

    public static class PagoDummy extends MetodoPago<MedioDePago> {
        private int validaciones;
        private int reservas;
        private int transacciones;
        private int notificaciones;

        @Override
        protected void validarDatos(Double monto, MedioDePago medio) {
            validaciones++;
        }

        @Override
        protected void reservarFondos(Double monto, MedioDePago medio) {
            reservas++;
        }

        @Override
        protected void ejecutarTransaccion(Double monto, MedioDePago medio) {
            transacciones++;
        }

        @Override
        protected void notificarResultado(Double monto, MedioDePago medio) {
            notificaciones++;
        }

        public int getValidaciones() {
            return validaciones;
        }

        public int getReservas() {
            return reservas;
        }

        public int getTransacciones() {
            return transacciones;
        }

        public int getNotificaciones() {
            return notificaciones;
        }
    }

    private static class EnvioDummy implements MetodoEnvio {
        @Override
        public double calcularCosto(Pedido pedido, Direccion direccion, Sucursal sucursal) {
            return 0;
        }

        @Override
        public int calcularTiempo(Pedido pedido, Direccion direccion, Sucursal sucursal) {
            return 0;
        }
    }
}
