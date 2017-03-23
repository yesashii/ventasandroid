package mdls.ventas_desa;

import java.math.BigDecimal;

public class ListaPrecio {
	
	public ListaPrecio()
	{
		
	}
	
	int idempresa;
	BigDecimal idlisprecio;
	String idsku;
	BigDecimal valor;
	
	public int getIdempresa() {
		return idempresa;
	}
	
	public void setIdempresa(int idempresa) {
		this.idempresa = idempresa;
	}
	public BigDecimal getIdlisprecio() {
		return idlisprecio;
	}
	public void setIdlisprecio(BigDecimal idlisprecio) {
		this.idlisprecio = idlisprecio;
	}
	public String getIdsku() {
		return idsku;
	}
	public void setIdsku(String idsku) {
		this.idsku = idsku;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	

}
