package modelo;

import java.util.List;

public class DatosConsulta {
	private String idFuncion;
	private String tipoSilleteria;
	private String cantidadBoletas; 
	private List<String> sillas;
	private int idUsuario; 
	private int idTeatro;
	private int valorTotalVenta;
	private int idVenta;
	private String pelicula;
	private String valorBoleta;
	private String fecha;
	private String sala;
	
	public String getIdFuncion() {
		return idFuncion;
	}

	public void setIdFuncion(String idFuncion) {
		this.idFuncion = idFuncion;
	}

	public String getTipoSilleteria() {
		return tipoSilleteria;
	}

	public void setTipoSilleteria(String tipoSilleteria) {
		this.tipoSilleteria = tipoSilleteria;
		this.valorBoleta = tipoSilleteria == "VIP" ? "7000" : "3500";
	}
	
	public String getValorBoleta() {
		return this.valorBoleta;
	}

	public String getCantidadBoletas() {
		return cantidadBoletas;
	}

	public void setCantidadBoletas(String cantidadBoletas) {
		this.cantidadBoletas = cantidadBoletas;
	}

	public List<String> getSillas() {
		return sillas;
	}

	public void setSillas(List<String> sillas) {
		this.sillas = sillas;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getIdTeatro() {
		return idTeatro;
	}

	public void setIdTeatro(int idTeatro) {
		this.idTeatro = idTeatro;
	}

	public DatosConsulta() {
		
	}
	
	public void setValorTotalVenta(int venta) {
		this.valorTotalVenta = venta;
	}
	
	public int getValorTotalVenta() {
		return this.valorTotalVenta;
	}
	
	public void setidVenta(int venta) {
		this.idVenta= venta;
	}
	
	public int getidVenta() {
		return this.idVenta;
	}
	
	public void setPelicula(String pel) {
		this.pelicula = pel;
	}

	public String getPelicula() {
		return this.pelicula;
	}

	public String getFecha() {
		// TODO Auto-generated method stub
		return this.fecha;
	}

	public String getSala() {
		// TODO Auto-generated method stub
		return this.sala;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}
	
	
}
