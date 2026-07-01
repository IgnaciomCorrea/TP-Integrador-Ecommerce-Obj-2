package reportes;

import java.util.List;

public class ExportadorReporte {

    public static String exportarTexto(ReporteProductosMasVendidos reporte) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PRODUCTOS MÁS VENDIDOS ===\n");
        sb.append("Período: ").append(reporte.getFechaInicio()).append(" - ").append(reporte.getFechaFin()).append("\n\n");
        sb.append("SKU\tNombre\tCantidad\tPrecio Promedio\n");
        sb.append("---\t------\t--------\t---------------\n");
        for (ItemVenta item : reporte.getResultados()) {
            sb.append(item.getSku()).append("\t")
                    .append(item.getNombre()).append("\t")
                    .append(item.getCantidad()).append("\t")
                    .append(String.format("%.2f", item.getPrecioPromedio())).append("\n");
        }
        return sb.toString();
    }

    public static String exportarCSV(ReporteProductosMasVendidos reporte) {
        StringBuilder sb = new StringBuilder();
        sb.append("SKU,Nombre,Cantidad,PrecioPromedio\n");
        for (ItemVenta item : reporte.getResultados()) {
            sb.append(item.getSku()).append(",")
                    .append(item.getNombre()).append(",")
                    .append(item.getCantidad()).append(",")
                    .append(String.format("%.2f", item.getPrecioPromedio())).append("\n");
        }
        return sb.toString();
    }

    public static String exportarHTML(ReporteProductosMasVendidos reporte) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><title>Productos más vendidos</title></head><body>");
        sb.append("<h1>Productos más vendidos</h1>");
        sb.append("<p>Período: ").append(reporte.getFechaInicio()).append(" - ").append(reporte.getFechaFin()).append("</p>");
        sb.append("<table border='1'><tr><th>SKU</th><th>Nombre</th><th>Cantidad</th><th>Precio Promedio</th></tr>");
        for (ItemVenta item : reporte.getResultados()) {
            sb.append("<tr>")
                    .append("<td>").append(item.getSku()).append("</td>")
                    .append("<td>").append(item.getNombre()).append("</td>")
                    .append("<td>").append(item.getCantidad()).append("</td>")
                    .append("<td>").append(String.format("%.2f", item.getPrecioPromedio())).append("</td>")
                    .append("</tr>");
        }
        sb.append("</table></body></html>");
        return sb.toString();
    }
}