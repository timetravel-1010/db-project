package cineplanet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import modelo.Usuario;
import modelo.DatosConsulta;
import vista.Login;
import vista.Registro;

public class CinePlanetBD {

    private String username = "postgres";
    private String password = "123";
    private String dbname = "Cineplanet_BD";
    private String host = "localhost";
    private int puerto = 5432;
    private Connection conexion;
    Statement s;//danilo
    List<String> idBoletas = new ArrayList<String>();
    
    /* constructor se conecta a la db */
    public CinePlanetBD() {        
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            String url = "jdbc:postgresql://"+host+":"+puerto+"/"+dbname;
            
            conexion = DriverManager.getConnection(url, username, password);
            s =  conexion.createStatement(); 
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    public boolean validarUsuario(String userName, String clave) {
         try {
                PreparedStatement statement = conexion.prepareStatement("SELECT idUsuario FROM Usuario WHERE nombreUsuario = ? AND clave = ?");
                statement.setString(1, userName);
                statement.setString(2, clave);
                
                ResultSet numFilas = statement.executeQuery();
                 // Verificar si el numFilas ha retornado algo o si la consulta es vacia.
                return numFilas.next();
                
         } catch (SQLException e) {
        	 e.printStackTrace();
        	 return false;
         }
    }
    
    public ArrayList<String[]> obtenerPeliculas(int idTeatro, int numCol) {
    	ArrayList<String[]> tabla = new ArrayList<>();
        try {
        	PreparedStatement sql = conexion.prepareStatement("SELECT idFuncion, pelicula, fecha, f.idSala FROM Funciones f, Sala s WHERE fecha = current_date AND s.idTeatro = ? AND f.idSala = s.idSala");
        	sql.setInt(1, idTeatro);
            ResultSet rs = sql.executeQuery();
            
            while (rs.next()){
                String[] fila = new String[numCol];
                for (int i = 0; i < numCol; i++) {
                    fila[i] = rs.getString(i+1);
                }
                
                tabla.add(fila);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return tabla;
    }
    
    public ArrayList<String[]> getPeliculasAntiguas(String sql, int numCol) {
        ArrayList<String[]> tabla = new ArrayList<>();
        
        try {
        	String sqls = "SELECT idFuncion, pelicula, fecha, idSala FROM Funciones WHERE fecha BETWEEN '2000-01-01' AND '2021-10-1'"; // WHERE fecha = current_date";
        	Statement s =  conexion.createStatement();
            ResultSet rs = s.executeQuery(sqls);
            
            while (rs.next()){
                String[] fila = new String[numCol];
                for (int i = 0; i < numCol; i++) {
                    fila[i] = rs.getString(i+1);
                }
                tabla.add(fila);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return tabla;
    }
        
    public ArrayList<String[]> buscarPelicula(String pelicula, String fecha, int numCol) {
        ArrayList<String[]> tabla = new ArrayList<>();
        
        try {
        	PreparedStatement sql = conexion.prepareStatement("SELECT * FROM buscarPelicula(?, TO_DATE(?, 'DD MM YYYY'))");
        	sql.setString(1, pelicula);
                sql.setString(2, fecha);
                ResultSet rs = sql.executeQuery();
                
            while (rs.next()){
                String[] fila = new String[numCol];
                for (int i = 0; i < numCol; i++) {
                    fila[i] = rs.getString(i+1);
                }
                tabla.add(fila);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return tabla;
    }
    
    public List<String> buscarSala(String mes, int numCol) {
        ArrayList<String[]> tabla = new ArrayList<>();
        List<String> datos = new ArrayList<>();
        
        try {
        	PreparedStatement sql = conexion.prepareStatement("SELECT * FROM salaVentas(TO_DATE(?, 'DD MM YYYY'))");
                 
                String fecha = "01/"+mes+"/2021";
                sql.setString(1, fecha);
                ResultSet rs = sql.executeQuery();
                
             if (rs.next()) {
                  String idSala = rs.getString("idSala");
                  String cantidadBoletas = rs.getString("cantidadBoletas");
                  datos.add(idSala);
                  datos.add(cantidadBoletas);
             }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return datos;
    }
    
    public void addUsuario(Usuario u) {   
        int resultado = 0;
        try {
        	PreparedStatement statement = conexion.prepareStatement("INSERT INTO Usuario (nombreUsuario,email,clave) VALUES  (?,?,?)");
        	statement.setString(1, u.getNombreUsuario());
        	statement.setString(2, u.getEmail());
        	statement.setString(3, u.getClave());
        	
        	Registro registrar= new Registro();
                if (validarUsuario(u.getNombreUsuario(), u.getClave())==false) {
                	resultado = statement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Usuario registrado con exito");
                    Login acceso = new Login();
                    acceso.setVisible(true); 
                } else {  
                    JOptionPane.showMessageDialog(null, "¡No se pudo hacer el registro por que el usuario ya existe!", "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
                    registrar.setVisible(true);
                    registrar.setLocationRelativeTo(null);
                }
                statement.close();
    	} catch (SQLException e) {
    		e.printStackTrace();
            System.out.println(e);
    	}
    }

    public void realizarCompra(DatosConsulta datos) {
		try {
			int precioBoleta = datos.getTipoSilleteria() == "VIP" ? 7000 : 3500;
			int cantidadBoletas = Integer.parseInt(datos.getCantidadBoletas());
			int valorTotalVenta = cantidadBoletas*precioBoleta;
			
			for (int i = 0; i<cantidadBoletas; i++) {
				PreparedStatement statement = conexion.prepareStatement("INSERT INTO Boleta (precioBoleta, silla, idUsuario, tipoSilleteria, idFuncion) VALUES  (?,?,?,?,?)");
	            statement.setInt(1, precioBoleta);
	            statement.setString(2, datos.getSillas().get(i));
	            statement.setInt(3, datos.getIdUsuario());
	            statement.setString(4, datos.getTipoSilleteria());
	            statement.setString(5, datos.getIdFuncion());
	            int rs = statement.executeUpdate();
			}
        	PreparedStatement statement = conexion.prepareStatement("INSERT INTO Ventas (valorVenta, valorBoleta, cantidadBoleta, idTeatro) VALUES  (?, ?,?,?) returning idVenta");
        	statement.setInt(1, valorTotalVenta);
        	statement.setInt(2, precioBoleta);
            statement.setInt(3, cantidadBoletas);
            statement.setInt(4, datos.getIdTeatro());
            
            ResultSet rVentas = statement.executeQuery();
            rVentas.next();
            int idVenta = Integer.parseInt(rVentas.getString("idVenta"));
            System.out.println("Ventas ha retornado: " + idVenta);
            
            PreparedStatement obtenerPelicula = conexion.prepareStatement("SELECT pelicula, fecha, idSala from funciones WHERE idFuncion = ?");
            obtenerPelicula.setString(1, datos.getIdFuncion());
            ResultSet pel = obtenerPelicula.executeQuery();
            pel.next();
            datos.setPelicula(pel.getString("pelicula"));
            datos.setSala(pel.getString("idSala"));
            datos.setFecha(pel.getString("fecha"));
            datos.setidVenta(idVenta);
            datos.setValorTotalVenta(valorTotalVenta);
            
            PreparedStatement statementUpdate = conexion.prepareStatement("UPDATE Boleta SET idVenta = ? WHERE idVenta IS NULL");
            statementUpdate.setInt(1, idVenta);
            int r = statementUpdate.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    
	public ArrayList<String[]> buscarTeatrosVenta() {
		ArrayList<String[]> tabla = new ArrayList<>();
		int numCol = 3;
		
        try {
        	PreparedStatement sql = conexion.prepareStatement("SELECT * FROM ventasTeatro");
            ResultSet rs = sql.executeQuery();
            
            while (rs.next()){
                String[] fila = new String[numCol];
                for (int i = 0; i < numCol; i++) {
                    fila[i] = rs.getString(i+1);
                }
                tabla.add(fila);
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return tabla;
	}
}