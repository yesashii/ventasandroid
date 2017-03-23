package mdls.ventas_desa;

public class CENSO_DETALLE {
	
	public CENSO_DETALLE()
	{
		
	}
	
	int idactivacion;
	int idcensolocal;
	int idgeneralact;
	String nombre;
	int idcensogeneral;
	int calibres;
	int item;
	int marca;
	float desde;
	float hasta;
	
	public int getIdactivacion() 
	{
		return idactivacion;
	}
	
	public void setIdactivacion(int idactivacion) 
	{
		this.idactivacion = idactivacion;
	}
	
	public int getIdcensolocal() 
	{
		return idcensolocal;
	}
	
	public void setIdcensolocal(int idcensolocal) 
	{
		this.idcensolocal = idcensolocal;
	}
	
	public int getIdgeneralact() 
	{
		return idgeneralact;
	}
	
	public void setIdgeneralact(int idgeneralact) 
	{
		this.idgeneralact = idgeneralact;
	}
	
	public String getNombre() 
	{
		return nombre;
	}
	
	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}
	
	public int getIdcensogeneral() 
	{
		return idcensogeneral;
	}
	
	public void setIdcensogeneral(int idcensogeneral) 
	{
		this.idcensogeneral = idcensogeneral;
	}
	
	public int getCalibres() 
	{
		return calibres;
	}
	
	public void setCalibres(int calibres) 
	{
		this.calibres = calibres;
	}
	
	public int getItem() 
	{
		return item;
	}
	
	public void setItem(int item) 
	{
		this.item = item;
	}

	public int getMarca() 
	{
		return marca;
	}
	
	public void setMarca(int marca) 
	{
		this.marca = marca;
	}
	
	public float getDesde() 
	{
		return desde;
	}
	
	public void setDesde(float desde) 
	{
		this.desde = desde;
	}

	public float getHasta() 
	{
		return hasta;
	}
	
	public void setHasta(float hasta) 
	{
		this.hasta = hasta;
	}
}
