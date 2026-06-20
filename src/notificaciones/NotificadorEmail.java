package notificaciones;

import pedido.*;

public class NotificadorEmail implements ObservadorPedido {
    private MailSender mailSender;
    private String direccionCliente;

    public NotificadorEmail(MailSender mailSender, String direccionCliente) {
        this.mailSender = mailSender;
        this.direccionCliente = direccionCliente;
    }

    @Override
    public void onCambioEstado(CambioEstadoEvento evento, Pedido pedido) {
        EstadoPedido nuevo = evento.getEstadoNuevo();
        if (nuevo instanceof Confirmado || nuevo instanceof Enviado || nuevo instanceof Entregado) {
            String titulo = "Cambio de estado de tu pedido";
            String mensaje = "Tu pedido ha cambiado a: " + nuevo.getClass().getSimpleName();
            mailSender.enviarMail(direccionCliente, titulo, mensaje, null);
        }
    }
}