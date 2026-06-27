package Catalogo;

import pedido.Pedido;

import java.util.ArrayList;

import java.util.List;

public class Catalogo {

	private List<StockVendible> stock;
	
	public Catalogo() {
		stock = new ArrayList<StockVendible>();
	}
	 	
	public List<StockVendible> getStock(){
		return stock;
	}
	
	public void agregarVendible(StockVendible stockVendible) {
		stock.add(stockVendible);
	}

	// ===== OPERACIONES CON PEDIDOS =====

	// Se valida la existencia de stock para el pedido y si existe, se resta.
	public void armarPedido(Pedido pedido) {
		if (this.verificarStockPedido(pedido)) {
			this.restarStock(pedido.getVendibles());
		} else {
			// agregar cambio de estado del pedido a Cancelado
			throw new IllegalArgumentException("No hay Stock disponible para tu pedido.");
		}
	}

	// Se verifica la existencia de stock para todos los elementos (ItemVendible) del pedido.
	public boolean verificarStockPedido(Pedido pedido){
			return pedido.getVendibles().stream()
					.allMatch(vendible -> this.hayStockDisponible(vendible.getSku(), vendible.getCantidad()));
	}

	// Evalúa que el stock del producto con el sku dado sea mayor o igual que la cantidadPedida.
	public boolean hayStockDisponible(String sku, int cantidadPedida) {
		return this.buscarVendible(sku).getStock() >= cantidadPedida;
	}

	// Retorna el StockVendible en la lista stock, con el mismo sku dado.
	public StockVendible buscarVendible(String sku) {
		return stock.stream().filter(stockVendible -> stockVendible.getSku().equals(sku))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("El producto con ID: " + sku + " no existe en el stock."));
	}


	// Modifica el stock de los items vendidos.
	public void restarStock(List<ItemVendible> itemVendibles) {
		for (ItemVendible item : itemVendibles) {
			StockVendible stockVendible = buscarVendible(item.getSku());
			int nuevoStock = stockVendible.getStock() - item.getCantidad();
			stockVendible.setStock(nuevoStock);
		}
	}

	public void reponerStock(List<ItemVendible> itemVendibles) {
		for (ItemVendible item : itemVendibles) {
			StockVendible stockVendible = buscarVendible(item.getSku());
			int nuevoStock = stockVendible.getStock() + item.getCantidad();
			stockVendible.setStock(nuevoStock);
		}
	}

}
