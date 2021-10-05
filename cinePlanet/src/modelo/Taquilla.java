package modelo;

import cineplanet.CinePlanetBD;
import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Taquilla {
    
        private CinePlanetBD conexionBD;
        private List<Usuario> usuarios;
        
        public Taquilla() {
            conexionBD = new CinePlanetBD();
        }
        
        public  void agregarUsuario(Usuario usuario) {
            conexionBD.addUsuario(usuario);
        }

        public boolean validarUsuario(String userName, String clave) {
            return  conexionBD.validarUsuario(userName, clave);
        }
        
        public ArrayList<String[]> mostrarPeliculas(int idTeatro) {
        	return conexionBD.obtenerPeliculas(idTeatro, 4);
        }
        
        public void realizarCompra(DatosConsulta datos) {
        	conexionBD.realizarCompra(datos);
        }
        
        public ArrayList<String[]>mostrarPeliculasAntiguas() {
            return conexionBD.getPeliculasAntiguas("", 4);
        }
        
        public ArrayList<String[]>mostrarPeliBuscada(String pelicula, String fecha) {
            return conexionBD.buscarPelicula(pelicula, fecha, 4);
        }
        
        public List<String> mostrarSalaVenta(String mes) {
            return conexionBD.buscarSala(mes, 2);
        }
        
        public ArrayList<String[]> mostrarTeatrosVenta() {
        	return conexionBD.buscarTeatrosVenta();
        }
}
