package mdls.ventas_desa;

import java.math.BigDecimal;

public class Cliente 
{	
	public Cliente()
	{
	}
	
	int idempresa;   
    int idejecutivo;   
    int idvendedor;    
    String idctacte;  
    String codlegal;    
    String nombre;    
    String sigla;    
    String telefono;  
    String correo;    
    String nombreencargado;    
    String direccion;   
    String idcanal;  
    String descuento_base;   
    String tipo_factor;   
    String idflete;  
    String soconsumo;
    BigDecimal idcondpago;   
    BigDecimal limitecredito; 
    BigDecimal idlisprecio; 
    BigDecimal monto_vencido;   
    BigDecimal monto_avencer;    
    BigDecimal monto_chcartera; 
    
	public int getIdempresa() 
	{
		return idempresa;
	}
	
	public void setIdempresa(int idempresa) 
	{
		this.idempresa = idempresa;
	}
	
	public String getIdctacte() 
	{
		return idctacte;
	}
	
	public void setIdctacte(String idctacte) 
	{
		this.idctacte = idctacte;
	}
	
	public String getCodlegal() 
	{
		return codlegal;
	}
	
	public void setCodlegal(String codlegal) 
	{
		this.codlegal = codlegal;
	}
	
	public String getNombre() 
	{
		return nombre;
	}
	
	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}
	
	public String getSigla() 
	{
		return sigla;
	}
	
	public void setSigla(String sigla) 
	{
		this.sigla = sigla;
	}
	
	public int getIdejecutivo() 
	{
		return idejecutivo;
	}
	
	public void setIdejecutivo(int idejecutivo) 
	{
		this.idejecutivo = idejecutivo;
	}
	
	public BigDecimal getIdcondpago() 
	{
		return idcondpago;
	}
	
	public void setIdcondpago(BigDecimal idcondpago) 
	{
		this.idcondpago = idcondpago;
	}
	
	public BigDecimal getLimitecredito() 
	{
		return limitecredito;
	}
	
	public void setLimitecredito(BigDecimal limitecredito) 
	{
		this.limitecredito = limitecredito;
	}
	
	public String getTelefono() 
	{
		return telefono;
	}
	public void setTelefono(String telefono) 
	{
		this.telefono = telefono;
	}
	
	public String getCorreo() 
	{
		return correo;
	}
	
	public void setCorreo(String correo) 
	{
		this.correo = correo;
	}
	
	public String getNombreencargado() 
	{
		return nombreencargado;
	}
	
	public void setNombreencargado(String nombreencargado) 
	{
		this.nombreencargado = nombreencargado;
	}
	
	public String getDireccion() 
	{
		return direccion;
	}
	
	public void setDireccion(String direccion) 
	{
		this.direccion = direccion;
	}
	
	public BigDecimal getIdlisprecio() 
	{
		return idlisprecio;
	}
	
	public void setIdlisprecio(BigDecimal idlisprecio) 
	{
		this.idlisprecio = idlisprecio;
	}
	
	public String getIdcanal() 
	{
		return idcanal;
	}
	
	public void setIdcanal(String idcanal) 
	{
		this.idcanal = idcanal;
	}
	
	public String getDescuento_base() 
	{
		return descuento_base;
	}
	
	public void setDescuento_base(String descuento_base) 
	{
		this.descuento_base = descuento_base;
	}
	
	public BigDecimal getMonto_vencido() 
	{
		return monto_vencido;
	}
	
	public void setMonto_vencido(BigDecimal monto_vencido) 
	{
		this.monto_vencido = monto_vencido;
	}
	
	public BigDecimal getMonto_avencer() 
	{
		return monto_avencer;
	}
	
	public void setMonto_avencer(BigDecimal monto_avencer) 
	{
		this.monto_avencer = monto_avencer;
	}
	
	public BigDecimal getMonto_chcartera() 
	{
		return monto_chcartera;
	}
	
	public void setMonto_chcartera(BigDecimal monto_chcartera) 
	{
		this.monto_chcartera = monto_chcartera;
	}
	
	public String getTipo_factor() 
	{
		return tipo_factor;
	}
	
	public void setTipo_factor(String tipo_factor) 
	{
		this.tipo_factor = tipo_factor;
	}
	
	public String getIdflete() 
	{
		return idflete;
	}
	
	public void setIdflete(String idflete) 
	{
		this.idflete = idflete;
	}
	
	public int getIdvendedor() 
	{
		return idvendedor;
	}
	
	public void setIdvendedor(int idvendedor) 
	{
		this.idvendedor = idvendedor;
	}
	
	public String getSoconsumo() 
	{
		return soconsumo;
	}
	
	public void setSoconsumo(String soconsumo) 
	{
		this.soconsumo = soconsumo;
	}
}

