package mdls.ventas_desa;


public class Censos {
	
	public Censos()
	{
		
	}

	String Idactivacion;
	String Ctacte;
	String Fecha;
	String Calibre;
	String Item;
	String Marca;
	String Respuesta;
	String Motivo;

	public Censos(String idactivacion, String ctacte, String fecha, String calibre, String item, String marca, String respuesta, String motivo) 
	{
		Idactivacion=idactivacion;
		Ctacte=ctacte;
		Fecha=fecha;
		Calibre=calibre;
		Item=item;
		Marca=marca;
		Respuesta=respuesta;
		Motivo=motivo;
	}

	public String getIdactivacion() 
	{
		return Idactivacion;
	}

	public void setIdactivacion(String idactivacion) 
	{
		Idactivacion = idactivacion;
	}

	public String getCtacte() 
	{
		return Ctacte;
	}
	
	public void setCtacte(String ctacte) 
	{
		Ctacte = ctacte;
	}

	public String getFecha() 
	{
		return Fecha;
	}

	public void setFecha(String fecha) 
	{
		Fecha = fecha;
	}

	public String getCalibre() 
	{
		return Calibre;
	}

	public void setCalibre(String calibre) 
	{
		Calibre = calibre;
	}

	public String getItem() 
	{
		return Item;
	}

	public void setItem(String item) 
	{
		Item = item;
	}

	public String getMarca() 
	{
		return Marca;
	}

	public void setMarca(String marca) 
	{
		Marca = marca;
	}

	public String getRespuesta() 
	{
		return Respuesta;
	}

	public void setRespuesta(String respuesta) 
	{
		Respuesta = respuesta;
	}
	
	public String getMotivo() 
	{
		return Motivo;
	}

	public void setMotivo(String motivo) 
	{
		Motivo = motivo;
	}
}