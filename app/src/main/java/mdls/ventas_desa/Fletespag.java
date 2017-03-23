package mdls.ventas_desa;

import java.math.BigDecimal;

public class Fletespag {

	public Fletespag()
	{
		
	}
	
	String idcliente;
	String idProducto;
	BigDecimal valorFlete;
	BigDecimal valor;
	
	public String getIdCliente() {
		return idcliente;
	}
	public void setIdCliente(String idcliente) {
		this.idcliente = idcliente;
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
	
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	
}
