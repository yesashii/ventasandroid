package mdls.ventas_desa;

import java.math.BigDecimal;

public class Producto {
	
	public Producto()
	{
		
	}
	
	int idempresa;
	String sku;
	String nombre;
	int idfamilia;
	int idsubfamilia;
	int idmarca;
	BigDecimal factoralt;
	BigDecimal flete;
	String ila;	
	
	
	public int getIdempresa() {
		return idempresa;
	}
	public void setIdempresa(int idempresa) {
		this.idempresa = idempresa;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getIdfamilia() {
		return idfamilia;
	}
	public void setIdfamilia(int idfamilia) {
		this.idfamilia = idfamilia;
	}
	public int getIdsubfamilia() {
		return idsubfamilia;
	}
	public void setIdsubfamilia(int idsubfamilia) {
		this.idsubfamilia = idsubfamilia;
	}
	public int getIdmarca() {
		return idmarca;
	}
	public void setIdmarca(int idmarca) {
		this.idmarca = idmarca;
	}
	public BigDecimal getFactoralt() {
		return factoralt;
	}
	public void setFactoralt(BigDecimal factoralt) {
		this.factoralt = factoralt;
	}
	public BigDecimal getFlete() {
		return flete;
	}
	public void setFlete(BigDecimal flete) {
		this.flete = flete;
	}
	public String getIla() {
		return ila;
	}
	public void setIla(String ila) {
		this.ila = ila;
	}
	
	

}



