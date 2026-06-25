package pedido;

import java.time.LocalDateTime;

/**
 * Representa una nota de crédito generada por un reembolso de pedido.
 * Cada pedido puede tener como máximo una nota de crédito.
 */
public class NotaCredito {
    private final String id;
    private final Pedido pedido;
    private final double monto;
    private final String motivo;
    private final LocalDateTime fechaEmision;

    // Constructor privado (se usa el builder o un método fábrica)
    private NotaCredito(String id, Pedido pedido, double monto, String motivo) {
        this.id = id;
        this.pedido = pedido;
        this.monto = monto;
        this.motivo = motivo;
        this.fechaEmision = LocalDateTime.now();
    }

    /**
     * Método fábrica para crear una nueva nota de crédito.
     */
    public static NotaCredito crear(Pedido pedido, double monto, String motivo) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("El motivo es obligatorio");
        }
        // Generar un ID único simple (puede ser un UUID o secuencia)
        String id = "NC-" + System.currentTimeMillis() + "-" + pedido.hashCode();
        return new NotaCredito(id, pedido, monto, motivo);
    }

    public String getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public double getMonto() {
        return monto;
    }

    public String getMotivo() {
        return motivo;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    @Override
    public String toString() {
        return "NotaCredito{" +
                "id='" + id + '\'' +
                ", monto=" + monto +
                ", motivo='" + motivo + '\'' +
                ", fechaEmision=" + fechaEmision +
                '}';
    }
}