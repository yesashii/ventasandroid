package mdls.ventas_desa;

public class ESCDESC {
	
	public ESCDESC()
	{
		
	}
	
	int idcanal;
	String idproducto;
	int cantidad_desde;
	int cantidad_hasta;
	float descuento;
	String sw_trato;
	
	public int getIdcanal() {
		return idcanal;
	}
	public void setIdcanal(int idcanal) {
		this.idcanal = idcanal;
	}
	public String getIdproducto() {
		return idproducto;
	}
	public void setIdproducto(String idproducto) {
		this.idproducto = idproducto;
	}
	public int getCantidad_desde() {
		return cantidad_desde;
	}
	public void setCantidad_desde(int cantidad_desde) {
		this.cantidad_desde = cantidad_desde;
	}
	public int getCantidad_hasta() {
		return cantidad_hasta;
	}
	public void setCantidad_hasta(int cantidad_hasta) {
		this.cantidad_hasta = cantidad_hasta;
	}
	public float getDescuento() {
		return descuento;
	}
	public void setDescuento(float descuento) {
		this.descuento = descuento;
	}
	public String getSw_trato() {
		return sw_trato;
	}
	public void setSw_trato(String sw_trato) {
		this.sw_trato = sw_trato;
	}
	

}
