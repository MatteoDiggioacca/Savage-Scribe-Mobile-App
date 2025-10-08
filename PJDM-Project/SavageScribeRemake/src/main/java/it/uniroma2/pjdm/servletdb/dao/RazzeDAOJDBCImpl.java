package it.uniroma2.pjdm.servletdb.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import it.uniroma2.pjdm.servletdb.entity.ListaRazze;
import it.uniroma2.pjdm.servletdb.entity.Razza;

public class RazzeDAOJDBCImpl implements RazzeDAO{
	
	private Connection conn;
	
	public RazzeDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		
			// SU WINDOWS
			conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + dbName, userName, pwd);
			System.out.println("RazzeDAOJDBCImpl connected!");
			// SU LINUX
			//		conn = DriverManager.getConnection(
			//				"jdbc:mysql://" + ip + ":" + port + "/" + dbName
			//						+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
			//				userName, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Razza loadRazzaByNome(String nome) {
		String query = "SELECT nome_razza,descr FROM razza WHERE nome_razza = " +nome;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			Razza res = null;
			while (rset.next()) {
				String nomeRazza = rset.getString(1);
				String descr = rset.getString(2);
				res = new Razza(nomeRazza,descr);
				break;
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
	
	public Razza loadRazzaByScheda(String nome, String userCreatore) {
		String query = "select razza.nome_razza, razza.descr from razza, scheda where scheda.nome = "+nome+" and scheda.proprietario = "+userCreatore+" and scheda.nome_razza = razza.nome_razza";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			Razza res = null;
			while (rset.next()) {
				String nomeRazza = rset.getString(1);
				String descr = rset.getString(2);
				res = new Razza(nomeRazza,descr);
				break;
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
	
	public ListaRazze loadAllRazze() {
		String query = "SELECT nome_razza,descr FROM razza ";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaRazze res = new ListaRazze();
			while (rset.next()) {
				String nomeRazza = rset.getString(1);
				String descr = rset.getString(2);
				Razza m = null;
				m = new Razza(nomeRazza,descr);
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
	
	public int insertRazza(Razza razza) {
		String SQL = "INSERT INTO razza (nome_razza,descr) " + "VALUES (?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, razza.getNomeRazza());
			pstmt.setString(2, razza.getDescrizione());
			int affectedRows = pstmt.executeUpdate();
			return affectedRows;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int updateRazza(Razza razza) {
		String SQL = "UPDATE razza SET descr = ? WHERE nome_razza = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, razza.getDescrizione());
			pstmt.setString(2, razza.getNomeRazza());
			int affectedRows = pstmt.executeUpdate();
			return affectedRows;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean deleteRazza(String nomeRazza) {
		String SQL = "DELETE FROM razza WHERE nome_razza=" + nomeRazza;
		try {
			Statement statement = conn.createStatement();
			statement.execute(SQL);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void closeConnection(){
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
		System.out.println("RazzeDAOJDBCImpl disconnected!");
	}
	
}