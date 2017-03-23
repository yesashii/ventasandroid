package mdls.ventas_desa;


public class Pedido {
	
	public Pedido()
	{
		
	}

	String	Vendedor;
	String	Fecha	;
	String	Hora	;
	String	Cliente	;
	String	Nota	;
	String	Oc	;
	String	Producto01	;
	String	Cantidad01	;
	String	Descuento01	;
	String	Producto02	;
	String	Cantidad02	;
	String	Descuento02	;
	String	Producto03	;
	String	Cantidad03	;
	String	Descuento03	;
	String	Producto04	;
	String	Cantidad04	;
	String	Descuento04	;
	String	Producto05	;
	String	Cantidad05	;
	String	Descuento05	;
	String	Producto06	;
	String	Cantidad06	;
	String	Descuento06	;
	String	Producto07	;
	String	Cantidad07	;
	String	Descuento07	;
	String	Producto08	;
	String	Cantidad08	;
	String	Descuento08	;
	String	Producto09	;
	String	Cantidad09	;
	String	Descuento09	;
	String	Producto10	;
	String	Cantidad10	;
	String	Descuento10	;
	String	Producto11	;
	String	Cantidad11	;
	String	Descuento11	;
	String	Producto12	;
	String	Cantidad12	;
	String	Descuento12	;
	String	OBS1	;
	String	Estado	;
	String	Opcion	;
	String	Fechaentrega	;
	String	OBS21	;
	String	Empresa	;
	String	Notamovil	;
	String GPS_latitud;
	String GPS_longitud;
	String GPS_tiempo;
	String Bodega;
	
	
		
	
	public Pedido(String vendedor, String fecha, String hora, String cliente,
			String nota, String oc, String producto01, String cantidad01,
			String descuento01, String producto02, String cantidad02,
			String descuento02, String producto03, String cantidad03,
			String descuento03, String producto04, String cantidad04,
			String descuento04, String producto05, String cantidad05,
			String descuento05, String producto06, String cantidad06,
			String descuento06, String producto07, String cantidad07,
			String descuento07, String producto08, String cantidad08,
			String descuento08, String producto09, String cantidad09,
			String descuento09, String producto10, String cantidad10,
			String descuento10, String producto11, String cantidad11,
			String descuento11, String producto12, String cantidad12,
			String descuento12, String oBS1, String estado, String opcion,
			String fechaentrega, String oBS21, String empresa, String notamovil,String gps_latitud,String gps_longitud,String gps_tiempo,String bodega) {
		
		Vendedor = vendedor;
		Fecha = fecha;
		Hora = hora;
		Cliente = cliente;
		Nota = nota;
		Oc = oc;
		Producto01 = producto01;
		Cantidad01 = cantidad01;
		Descuento01 = descuento01;
		Producto02 = producto02;
		Cantidad02 = cantidad02;
		Descuento02 = descuento02;
		Producto03 = producto03;
		Cantidad03 = cantidad03;
		Descuento03 = descuento03;
		Producto04 = producto04;
		Cantidad04 = cantidad04;
		Descuento04 = descuento04;
		Producto05 = producto05;
		Cantidad05 = cantidad05;
		Descuento05 = descuento05;
		Producto06 = producto06;
		Cantidad06 = cantidad06;
		Descuento06 = descuento06;
		Producto07 = producto07;
		Cantidad07 = cantidad07;
		Descuento07 = descuento07;
		Producto08 = producto08;
		Cantidad08 = cantidad08;
		Descuento08 = descuento08;
		Producto09 = producto09;
		Cantidad09 = cantidad09;
		Descuento09 = descuento09;
		Producto10 = producto10;
		Cantidad10 = cantidad10;
		Descuento10 = descuento10;
		Producto11 = producto11;
		Cantidad11 = cantidad11;
		Descuento11 = descuento11;
		Producto12 = producto12;
		Cantidad12 = cantidad12;
		Descuento12 = descuento12;
		OBS1 = oBS1;
		Estado = estado;
		Opcion = opcion;
		Fechaentrega = fechaentrega;
		OBS21 = oBS21;
		Empresa = empresa;
		Notamovil = notamovil;
		GPS_latitud=gps_latitud;
		GPS_longitud=gps_longitud;
		GPS_tiempo=gps_tiempo;
		Bodega=bodega;
	}
	
	
	
	public String getVendedor() {
		return Vendedor;
	}



	public void setVendedor(String vendedor) {
		Vendedor = vendedor;
	}



	public String getFecha() {
		return Fecha;
	}



	public void setFecha(String fecha) {
		Fecha = fecha;
	}



	public String getHora() {
		return Hora;
	}



	public void setHora(String hora) {
		Hora = hora;
	}



	public String getCliente() {
		return Cliente;
	}



	public void setCliente(String cliente) {
		Cliente = cliente;
	}



	public String getNota() {
		return Nota;
	}



	public void setNota(String nota) {
		Nota = nota;
	}



	public String getOc() {
		return Oc;
	}



	public void setOc(String oc) {
		Oc = oc;
	}



	public String getProducto01() {
		return Producto01;
	}



	public void setProducto01(String producto01) {
		Producto01 = producto01;
	}



	public String getCantidad01() {
		return Cantidad01;
	}



	public void setCantidad01(String cantidad01) {
		Cantidad01 = cantidad01;
	}



	public String getDescuento01() {
		return Descuento01;
	}



	public void setDescuento01(String descuento01) {
		Descuento01 = descuento01;
	}



	public String getProducto02() {
		return Producto02;
	}



	public void setProducto02(String producto02) {
		Producto02 = producto02;
	}



	public String getCantidad02() {
		return Cantidad02;
	}



	public void setCantidad02(String cantidad02) {
		Cantidad02 = cantidad02;
	}



	public String getDescuento02() {
		return Descuento02;
	}



	public void setDescuento02(String descuento02) {
		Descuento02 = descuento02;
	}



	public String getProducto03() {
		return Producto03;
	}



	public void setProducto03(String producto03) {
		Producto03 = producto03;
	}



	public String getCantidad03() {
		return Cantidad03;
	}



	public void setCantidad03(String cantidad03) {
		Cantidad03 = cantidad03;
	}



	public String getDescuento03() {
		return Descuento03;
	}



	public void setDescuento03(String descuento03) {
		Descuento03 = descuento03;
	}



	public String getProducto04() {
		return Producto04;
	}



	public void setProducto04(String producto04) {
		Producto04 = producto04;
	}



	public String getCantidad04() {
		return Cantidad04;
	}



	public void setCantidad04(String cantidad04) {
		Cantidad04 = cantidad04;
	}



	public String getDescuento04() {
		return Descuento04;
	}



	public void setDescuento04(String descuento04) {
		Descuento04 = descuento04;
	}



	public String getProducto05() {
		return Producto05;
	}



	public void setProducto05(String producto05) {
		Producto05 = producto05;
	}



	public String getCantidad05() {
		return Cantidad05;
	}



	public void setCantidad05(String cantidad05) {
		Cantidad05 = cantidad05;
	}



	public String getDescuento05() {
		return Descuento05;
	}



	public void setDescuento05(String descuento05) {
		Descuento05 = descuento05;
	}



	public String getProducto06() {
		return Producto06;
	}



	public void setProducto06(String producto06) {
		Producto06 = producto06;
	}



	public String getCantidad06() {
		return Cantidad06;
	}



	public void setCantidad06(String cantidad06) {
		Cantidad06 = cantidad06;
	}



	public String getDescuento06() {
		return Descuento06;
	}



	public void setDescuento06(String descuento06) {
		Descuento06 = descuento06;
	}



	public String getProducto07() {
		return Producto07;
	}



	public void setProducto07(String producto07) {
		Producto07 = producto07;
	}



	public String getCantidad07() {
		return Cantidad07;
	}



	public void setCantidad07(String cantidad07) {
		Cantidad07 = cantidad07;
	}



	public String getDescuento07() {
		return Descuento07;
	}



	public void setDescuento07(String descuento07) {
		Descuento07 = descuento07;
	}



	public String getProducto08() {
		return Producto08;
	}



	public void setProducto08(String producto08) {
		Producto08 = producto08;
	}



	public String getCantidad08() {
		return Cantidad08;
	}



	public void setCantidad08(String cantidad08) {
		Cantidad08 = cantidad08;
	}



	public String getDescuento08() {
		return Descuento08;
	}



	public void setDescuento08(String descuento08) {
		Descuento08 = descuento08;
	}



	public String getProducto09() {
		return Producto09;
	}



	public void setProducto09(String producto09) {
		Producto09 = producto09;
	}



	public String getCantidad09() {
		return Cantidad09;
	}



	public void setCantidad09(String cantidad09) {
		Cantidad09 = cantidad09;
	}



	public String getDescuento09() {
		return Descuento09;
	}



	public void setDescuento09(String descuento09) {
		Descuento09 = descuento09;
	}



	public String getProducto10() {
		return Producto10;
	}



	public void setProducto10(String producto10) {
		Producto10 = producto10;
	}



	public String getCantidad10() {
		return Cantidad10;
	}



	public void setCantidad10(String cantidad10) {
		Cantidad10 = cantidad10;
	}



	public String getDescuento10() {
		return Descuento10;
	}



	public void setDescuento10(String descuento10) {
		Descuento10 = descuento10;
	}



	public String getProducto11() {
		return Producto11;
	}



	public void setProducto11(String producto11) {
		Producto11 = producto11;
	}



	public String getCantidad11() {
		return Cantidad11;
	}



	public void setCantidad11(String cantidad11) {
		Cantidad11 = cantidad11;
	}



	public String getDescuento11() {
		return Descuento11;
	}



	public void setDescuento11(String descuento11) {
		Descuento11 = descuento11;
	}



	public String getProducto12() {
		return Producto12;
	}



	public void setProducto12(String producto12) {
		Producto12 = producto12;
	}



	public String getCantidad12() {
		return Cantidad12;
	}



	public void setCantidad12(String cantidad12) {
		Cantidad12 = cantidad12;
	}



	public String getDescuento12() {
		return Descuento12;
	}



	public void setDescuento12(String descuento12) {
		Descuento12 = descuento12;
	}



	public String getOBS1() {
		return OBS1;
	}



	public void setOBS1(String oBS1) {
		OBS1 = oBS1;
	}



	public String getEstado() {
		return Estado;
	}



	public void setEstado(String estado) {
		Estado = estado;
	}



	public String getOpcion() {
		return Opcion;
	}



	public void setOpcion(String opcion) {
		Opcion = opcion;
	}



	public String getFechaentrega() {
		return Fechaentrega;
	}



	public void setFechaentrega(String fechaentrega) {
		Fechaentrega = fechaentrega;
	}



	public String getOBS21() {
		return OBS21;
	}



	public void setOBS21(String oBS21) {
		OBS21 = oBS21;
	}



	public String getEmpresa() {
		return Empresa;
	}



	public void setEmpresa(String empresa) {
		Empresa = empresa;
	}



	public String getNotamovil() {
		return Notamovil;
	}



	public void setNotamovil(String notamovil) {
		Notamovil = notamovil;
	}



	public String getGPS_latitud() {
		return GPS_latitud;
	}



	public void setGPS_latitud(String gPS_latitud) {
		GPS_latitud = gPS_latitud;
	}



	public String getGPS_longitud() {
		return GPS_longitud;
	}



	public void setGPS_longitud(String gPS_longitud) {
		GPS_longitud = gPS_longitud;
	}



	public String getGPS_tiempo() {
		return GPS_tiempo;
	}



	public void setGPS_tiempo(String gPS_tiempo) {
		GPS_tiempo = gPS_tiempo;
	}
	
	
	public String getBodega() {
		return Bodega;
	}


	public void setBodega(String bodega) {
		Bodega = bodega;
	}

	
}
