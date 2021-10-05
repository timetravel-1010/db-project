package modelo;

public class Funcion {
	private String idFuncion;
	private String pelicula;
	private String fecha;
	private String idSala;
	
	
	public Funcion(String idFuncion, String pelicula, String fecha, String idSala) {
		this.idFuncion = idFuncion;
		this.pelicula = pelicula;
		this.fecha = fecha;
		this.idSala = idSala;
	}


	public String getIdFuncion() {
		return idFuncion;
	}


	public void setIdFuncion(String idFuncion) {
		this.idFuncion = idFuncion;
	}


	public String getPelicula() {
		return pelicula;
	}


	public void setPelicula(String pelicula) {
		this.pelicula = pelicula;
	}


	public String getFecha() {
		return fecha;
	}


	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


	public String getIdSala() {
		return idSala;
	}


	public void setIdSala(String idSala) {
		this.idSala = idSala;
	}
}
