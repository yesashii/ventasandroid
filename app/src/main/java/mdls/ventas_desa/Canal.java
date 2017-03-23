package mdls.ventas_desa;

import java.math.BigDecimal;

public class Canal {

	public Canal()
	{
		
	}
	
	int idcanal;
	int idmegacanal;
	String nombre;
	String sigla;
	BigDecimal descuento;
	
	public int getIdcanal() {
		return idcanal;
	}
	public void setIdcanal(int idcanal) {
		this.idcanal = idcanal;
	}
	public int getIdmegacanal() {
		return idmegacanal;
	}
	public void setIdmegacanal(int idmegacanal) {
		this.idmegacanal = idmegacanal;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public BigDecimal getDescuento() {
		return descuento;
	}
	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}
	
	
	
	
	
}
