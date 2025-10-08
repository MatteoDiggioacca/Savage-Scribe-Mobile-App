package it.uniroma2.pjdm.servletdb.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import it.uniroma2.pjdm.servletdb.entity.ListaNotifiche;
import it.uniroma2.pjdm.servletdb.entity.Notifica;

public class NotificheDAOJDBCImpl implements NotificheDAO{
	
	private Connection conn;
	
	public NotificheDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		
			// SU WINDOWS
			conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + dbName, userName, pwd);
			System.out.println("CampagneDAOJDBCImpl connected!");
			// SU LINUX
			//		conn = DriverManager.getConnection(
			//				"jdbc:mysql://" + ip + ":" + port + "/" + dbName
			//						+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
			//				userName, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public Notifica loadNotifica(String mittente, String destinatario, String nomeCampagna, int tipo) {
    	String query = "SELECT mittente,destinatario,nome_campagna,tipo FROM notifica WHERE mittente = "+mittente+" and destinatario = "+destinatario+" and nome_campagna = "+nomeCampagna+" and tipo = "+tipo;
    	try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			Notifica res = null;
			while (rset.next()) {
				String mitt = rset.getString(1);
				String dest = rset.getString(2);
				String nomeCamp = rset.getString(3);
				int tip = rset.getInt(4);
				res = new Notifica(mitt,dest,nomeCamp,tip);
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
    
	public ListaNotifiche loadNotificheByMittente(String mittente, int tipo) {
		String query = "SELECT mittente,destinatario,nome_campagna,tipo FROM notifica WHERE mittente = "+mittente+" and tipo="+tipo;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaNotifiche res = new ListaNotifiche();
			while (rset.next()) {
				String mitt = rset.getString(1);
				String destinatario = rset.getString(2);
				String nomeCampagna = rset.getString(3);
				int tip = rset.getInt(4);
				Notifica m = null;
				m = new Notifica(mitt,destinatario,nomeCampagna,tip);
				res.add(m);
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
	
	public ListaNotifiche loadNotificheByDestinatario(String destinatario, int tipo) {
		String query = "SELECT mittente,destinatario,nome_campagna,tipo FROM notifica WHERE destinatario = " +destinatario+" and tipo ="+tipo;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaNotifiche res = new ListaNotifiche();
			while (rset.next()) {
				String mitt = rset.getString(1);
				String dest = rset.getString(2);
				String nomeCampagna = rset.getString(3);
				int tip = rset.getInt(4);
				Notifica m = null;
				m = new Notifica(mitt,dest,nomeCampagna,tip);
				res.add(m);
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
	
	
	public ListaNotifiche loadAllNotifiche() {
		String query = "SELECT mittente,destinatario,nome_campagna,tipo FROM notifica";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaNotifiche res = new ListaNotifiche();
			while (rset.next()) {
				String mitt = rset.getString(1);
				String destinatario = rset.getString(2);
				String nomeCampagna = rset.getString(3);
				int tipo = rset.getInt(4);
				Notifica m = null;
				m = new Notifica(mitt,destinatario,nomeCampagna,tipo);
				res.add(m);
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
	
	public int insertNotifica(Notifica notifica) {
		String SQL = "INSERT INTO notifica (mittente,destinatario,nome_campagna,tipo) " + "VALUES (?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, notifica.getMittente());
			pstmt.setString(2, notifica.getDestinatario());
			pstmt.setString(3, notifica.getNomeCampagna());
			pstmt.setInt(4, notifica.getTipo());
			int affectedRows = pstmt.executeUpdate();
			return affectedRows;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int joinCampagna(String user, String nomeCampagna, String userCreatore, String nomeScheda) {
		String SQL = "INSERT INTO partecipa (user,nome_campagna,user_creatore,nome_scheda) " + "VALUES (?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user);
			pstmt.setString(2, nomeCampagna);
			pstmt.setString(3, userCreatore);
			pstmt.setString(4, nomeScheda);
			int affectedRows = pstmt.executeUpdate();
			return affectedRows;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean deleteNotifica(String mittente, String destinatario, String nomeCampagna, int tipo) {
		String SQL = "DELETE FROM notifica WHERE mittente= " + mittente + " and destinatario = "+destinatario+" and nome_campagna = "+nomeCampagna+" and tipo = "+tipo;
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
		System.out.println("CampagneDAOJDBCImpl disconnected!");
	}
	
}