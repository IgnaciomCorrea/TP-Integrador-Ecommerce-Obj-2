package reportes;

import Catalogo.Producto;
import Catalogo.Paquete;
import Catalogo.Vendible;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReporteProductosMasVendidos implements ReportVisitor {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Map<String, DatosVenta> ventas = new HashMap<>();

    public ReporteProductosMasVendidos(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }

    @Override
    public void visitProducto(Producto producto, int cantidadVendida, double precioTotal) {
        String key = producto.getSku();
        DatosVenta datos = ventas.getOrDefault(key, new DatosVenta(producto));
        datos.agregarVenta(cantidadVendida, precioTotal);
        ventas.put(key, datos);
    }

    @Override
    public void visitPaquete(Paquete paquete, int cantidadVendida, double precioTotal) {
        String key = paquete.getSku();
        DatosVenta datos = ventas.getOrDefault(key, new DatosVenta(paquete));
        datos.agregarVenta(cantidadVendida, precioTotal);
        ventas.put(key, datos);
    }

    public List<ItemVenta> getResultados() {
        return ventas.values().stream()
                .map(d -> new ItemVenta(
                        d.vendible.getSku(),
                        d.vendible.getNombre(),
                        d.cantidadTotal,
                        d.precioTotal / d.cantidadTotal
                ))
                .sorted((a, b) -> Integer.compare(b.getCantidad(), a.getCantidad()))
                .collect(Collectors.toList());
    }

    // Clase interna para acumular datos
    private static class DatosVenta {
        Vendible vendible;
        int cantidadTotal = 0;
        double precioTotal = 0;

        DatosVenta(Vendible vendible) {
            this.vendible = vendible;
        }

        void agregarVenta(int cantidad, double precio) {
            this.cantidadTotal += cantidad;
            this.precioTotal += precio;
        }
    }
}