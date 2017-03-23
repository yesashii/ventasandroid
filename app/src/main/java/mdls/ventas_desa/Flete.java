package mdls.ventas_desa;

import java.math.BigDecimal;

public class Flete {

	public Flete()
	{
		
	}
	
	String tipoCliente;
	String idProducto;
	BigDecimal valorFlete;
	
	public String getTipoCliente() {
		return tipoCliente;
	}
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}
	public String getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}
	public BigDecimal getValorFlete() {
		return valorFlete;
	}
	public void setValorFlete(BigDecimal valorFlete) {
		this.valorFlete = valorFlete;
	}
	
	
	
}
