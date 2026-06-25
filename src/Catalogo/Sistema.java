package Catalogo;

import java.util.List;
import java.util.stream.Collectors;

import Catalogo.Catalogo;
import Catalogo.StockVendible;
import CriterioBusqueda.Criterio;

public class Sistema {

	private Catalogo catalogo;
	
	public Sistema(Catalogo catalogo) {
		this.catalogo = catalogo;
	}
	
	public List<StockVendible> filtrarCon(Criterio criterio){
		return catalogo.getStock().stream().filter(stock -> criterio.validar(stock.getVendible())).collect(Collectors.toList());
	}

	// crea la interfaz

	//

}