package mdls.ventas_desa;

public class Vendedor {
	
	private int idvendedor;
	private int idempresa;
	private String nombre;
	private int idruta;
	private String psw_pedidos;
	private String psw_avance;
	private String usuario;
	private int idlocal;
	private int minimo;
	private String version;

	public int getIdempresa() {
		return idempresa;
	}

	public void setIdempresa(int idempresa) {
		this.idempresa = idempresa;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getIdruta() {
		return idruta;
	}

	public void setIdruta(int idruta) {
		this.idruta = idruta;
	}

	public String getPsw_pedidos() {
		return psw_pedidos;
	}

	public void setPsw_pedidos(String psw_pedidos) {
		this.psw_pedidos = psw_pedidos;
	}

	public String getPsw_avance() {
		return psw_avance;
	}

	public void setPsw_avance(String psw_avance) {
		this.psw_avance = psw_avance;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getIdlocal() {
		return idlocal;
	}

	public void setIdlocal(int idlocal) {
		this.idlocal = idlocal;
	}
	
	public int getMinimo() {
		return minimo;
	}

	public void setMinimo(int minimo) {
		this.minimo = minimo;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	public Vendedor()
	{
		
	}

	public Vendedor(int idvendedor, int idempresa, String nombre, int idruta,
			String psw_pedidos, String psw_avance, String usuario) 
	{
		super();
		this.idvendedor = idvendedor;
		this.idempresa = idempresa;
		this.nombre = nombre;
		this.idruta = idruta;
		this.psw_pedidos = psw_pedidos;
		this.psw_avance = psw_avance;
		this.usuario = usuario;
		this.idlocal=idlocal;
	}

	public void setIdvendedor(int idvendedor )
	{
	this.idvendedor=idvendedor;
	}
	
	public int getIdvendedor()
	{
		return idvendedor;
	}
	
	

}
