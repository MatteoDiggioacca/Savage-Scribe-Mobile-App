package it.uniroma2.pjdm.servletdb.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import it.uniroma2.pjdm.servletdb.entity.ListaUtenti;
import it.uniroma2.pjdm.servletdb.entity.Utente;

public class UtentiDAOJDBCImpl implements UtentiDAO{
	
	private Connection conn;
	
	public UtentiDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		
			// SU WINDOWS
			conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + dbName, userName, pwd);
			System.out.println("jdbc:mysql://" + ip + ":" + port + "/" + dbName+"/"+userName+"/"+pwd);
			System.out.println("UtentiDAOJDBCImpl connected!");
			// SU LINUX
			//		conn = DriverManager.getConnection(
			//				"jdbc:mysql://" + ip + ":" + port + "/" + dbName
			//						+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
			//				userName, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Utente loadUtente(String username, String password) {
		String query = "SELECT username, password FROM utente WHERE username = " +username+ " and password = "+password;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			Utente res = null;
			while (rset.next()) {
				String user = rset.getString(1);
				String pass = rset.getString(2);
				res = new Utente(user,pass);
				break;
			}
			rset.close();
			stmt.close();
			//System.out.println(res.toString());
			System.out.println(query);
			return res;
		} catch (SQLException e) {
			System.out.println(query);
			e.printStackTrace();
			return null;
		}
	}
	
	public Utente loadUtenteByUsername(String username) {
		
		String query = "SELECT username, password FROM utente WHERE username = " +username;
		System.out.println(query);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			Utente res = null;
			while (rset.next()) {
				String user = rset.getString(1);
				String pass = rset.getString(2);
				res = new Utente(user,pass);
				break;
			}
			rset.close();
			stmt.close();
			//System.out.println(res.toString());
			System.out.println(query);
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ListaUtenti loadUtentiByLike(String username) {
		String query = "select utente.username,utente.password from utente where locate("+username+",username)";
		System.out.println(query);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaUtenti res = new ListaUtenti();
			while (rset.next()) {
				String usr = rset.getString(1);
				String psswrd = rset.getString(2);
				Utente utente = null;
				utente = new Utente(usr,psswrd);
				res.add(utente);
			}
			rset.close();
			stmt.close();
			//System.out.println(res.toString());
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ListaUtenti loadUtentiByCampagna(String nomeCampagna, String userCreatore){
		String query = "select utente.username,utente.password from utente, partecipa where partecipa.nome_campagna = "+nomeCampagna+" and user_creatore = "+userCreatore+" and utente.username = partecipa.user";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaUtenti res = new ListaUtenti();
			while (rset.next()) {
				String user = rset.getString(1);
				String password = rset.getString(2);
				Utente m = null;
				m = new Utente(user,password);
				res.add(m);
			}
			rset.close();
			stmt.close();
			if (res.size()!=0)
				System.out.println(res.toString());
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ListaUtenti loadAllUtenti()  {
		String query = "SELECT username,password FROM utente ";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaUtenti res = new ListaUtenti();
			while (rset.next()) {
				String user = rset.getString(1);
				String password = rset.getString(2);
				Utente m = null;
				m = new Utente(user,password);
				res.add(m);
			}
			rset.close();
			stmt.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int insertUtente(Utente utente) {
		String SQL = "INSERT INTO utente (username,password) " + "VALUES (?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, utente.getUsername());
			pstmt.setString(2, utente.getPassword());
			
			int affectedRows = pstmt.executeUpdate();
			return affectedRows;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int updatePasswordUtente(Utente utente) {
		String SQL = "UPDATE utente SET password = ? WHERE username = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, utente.getPassword());
			pstmt.setString(2, utente.getUsername());
			int affectedRows = pstmt.executeUpdate();
			return affectedRows;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean deleteUtente(String username) {
		String SQL = "DELETE FROM utente WHERE username=" + username;
		try {
			Statement statement = conn.createStatement();
			statement.execute(SQL);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteUtenteFromCampagna(String username, String nomeCampagna, String userCreatore) {
		String SQL = "DELETE FROM partecipa WHERE nome_campagna = " + nomeCampagna + " and user_creatore = "+userCreatore+" and user = "+username;
		try {
			Statement statement = conn.createStatement();
			statement.execute(SQL);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Enumeration<Driver> enumDrivers = DriverManager.getDrivers();
			while (enumDrivers.hasMoreElements()) {
				Driver driver = enumDrivers.nextElement();
				DriverManager.deregisterDriver(driver);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("UtentiDAOJDBCImpl disconnected!");
	}
	
}