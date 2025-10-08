package it.uniroma2.pjdm.servletdb.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import it.uniroma2.pjdm.servletdb.entity.ListaCampagne;
import it.uniroma2.pjdm.servletdb.entity.Utente;
import it.uniroma2.pjdm.servletdb.entity.Campagna;

public class CampagneDAOJDBCImpl implements CampagneDAO{
	
	private Connection conn;
	
	public CampagneDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {
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
	
    /*public Campagna loadCampagna(String nomeCampagna, String userCreatore) {
    	String query = "SELECT nome_campagna,descr,user_creatore FROM campagna WHERE nome_campagna = "+nomeCampagna+" and user_creatore = "+userCreatore;
    	try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			Campagna res = null;
			while (rset.next()) {
				String nomeCam = rset.getString(1);
				String descr = rset.getString(2);
				String userCre = rset.getString(3);
				String query2 = "SELECT username,password from utente where username = '"+userCre+"'";
				ResultSet rset2 = stmt.executeQuery(query2);
				Utente utente = null;
				while (rset2.next()) {
					utente = new Utente(rset2.getString(1),rset2.getString(2));
					break;
				}
				rset2.close();
				res = new Campagna(nomeCam,utente,descr);
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
    }*/
	
	public Campagna loadCampagna(String nomeCampagna, String userCreatore) {
    	String query = "select campagna.nome_campagna, campagna.user_creatore, utente.password, campagna.descr from campagna, utente where campagna.user_creatore = utente.username and utente.username = "+userCreatore+" and campagna.nome_campagna = "+nomeCampagna;
    	try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			Campagna res = null;
			while (rset.next()) {
				String nomeCam = rset.getString(1);
				String userCre = rset.getString(2);
				String pswCre = rset.getString(3);
				String descr = rset.getString(4);
				Utente utente = new Utente(userCre,pswCre);
				res = new Campagna(nomeCam,utente,descr);
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
	
	/*public ListaCampagne loadCampagneByCreatore(String utente) {
		String query = "select campagna.nome_campagna, campagna.user_creatore, campagna.descr from campagna where campagna.user_creatore ="+utente;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaCampagne res = new ListaCampagne();
			while (rset.next()) {

				String nomeCampagna = rset.getString(1);
				String userCreatore = rset.getString(2);
				String descr = rset.getString(3);
				String query2 = "SELECT username,password from utente where username = '"+userCreatore+"'";
				Statement stmt2 = conn.createStatement();
				ResultSet rset2 = stmt2.executeQuery(query2);
				Utente ut = null;
				while (rset2.next()) {
					ut = new Utente(rset2.getString(1),rset2.getString(2));
					break;
				}
				rset2.close();
				stmt2.close();
				Campagna m = null;
				m = new Campagna(nomeCampagna,ut,descr);
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
	}*/
	
	public ListaCampagne loadCampagneByCreatore(String utente) {
		String query = "select campagna.nome_campagna, campagna.user_creatore, utente.password, campagna.descr from campagna,utente where utente.username = campagna.user_creatore and campagna.user_creatore ="+utente;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaCampagne res = new ListaCampagne();
			while (rset.next()) {

				String nomeCampagna = rset.getString(1);
				String userCreatore = rset.getString(2);
				String pswCreatore = rset.getString(3);
				String descr = rset.getString(4);
				
				Utente ut = new Utente(userCreatore,pswCreatore);
				Campagna m = new Campagna(nomeCampagna,ut,descr);
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
	
	
	
	/*public ListaCampagne loadCampagneByLike(String nomeCampagna, String userCreatore) {
		String query = "select campagna.nome_campagna, campagna.user_creatore, campagna.descr from campagna where locate("+userCreatore+",user_creatore) and locate("+nomeCampagna+",nome_campagna)";
		System.out.println(query);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaCampagne res = new ListaCampagne();
			while (rset.next()) {

				String nomeCamp = rset.getString(1);
				String userCrea = rset.getString(2);
				String descr = rset.getString(3);
				String query2 = "SELECT username,password from utente where username = '"+userCrea+"'";
				Statement stmt2 = conn.createStatement();
				ResultSet rset2 = stmt2.executeQuery(query2);
				Utente ut = null;
				while (rset2.next()) {
					ut = new Utente(rset2.getString(1),rset2.getString(2));
					break;
				}
				rset2.close();
				stmt2.close();
				Campagna m = null;
				m = new Campagna(nomeCamp,ut,descr);
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
	}*/
	
	public ListaCampagne loadCampagneByLike(String nomeCampagna, String userCreatore) {
		String query = "select campagna.nome_campagna, campagna.user_creatore, utente.password, campagna.descr from campagna,utente where locate("+userCreatore+",campagna.user_creatore) and locate("+nomeCampagna+",campagna.nome_campagna) and campagna.user_creatore = utente.username";
		System.out.println(query);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaCampagne res = new ListaCampagne();
			while (rset.next()) {
				String nomeCamp = rset.getString(1);
				String userCrea = rset.getString(2);
				String pswCrea = rset.getString(3);
				String descr = rset.getString(4);
				Utente ut = new Utente(userCrea,pswCrea);
				Campagna m = new Campagna(nomeCamp,ut,descr);
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
	
	/*public ListaCampagne loadCampagneByPartecipazione(String utente){
		String query = "select campagna.nome_campagna, campagna.user_creatore, campagna.descr from campagna, partecipa where partecipa.user = "+utente+" and partecipa.nome_campagna = campagna.nome_campagna and partecipa.user_creatore = campagna.user_creatore";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaCampagne res = new ListaCampagne();
			while (rset.next()) {
				String nomeCampagna = rset.getString(1);
				String userCreatore = rset.getString(2);
				String descr = rset.getString(3);
				String query2 = "SELECT username,password from utente where username = '"+userCreatore+"'";
				Statement stmt2 = conn.createStatement();
				ResultSet rset2 = stmt2.executeQuery(query2);
				Utente ut = null;
				while (rset2.next()) {
					ut = new Utente(rset2.getString(1),rset2.getString(2));
					break;
				}
				rset2.close();
				stmt2.close();
				Campagna m = null;
				m = new Campagna(nomeCampagna,ut,descr);
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
	}*/
	
	public ListaCampagne loadCampagneByPartecipazione(String utente){
		String query = "select campagna.nome_campagna, campagna.user_creatore, utente.password, campagna.descr from campagna, partecipa, utente where partecipa.user = "+utente+" and partecipa.nome_campagna = campagna.nome_campagna and partecipa.user_creatore = campagna.user_creatore and partecipa.user = utente.username";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaCampagne res = new ListaCampagne();
			while (rset.next()) {
				String nomeCampagna = rset.getString(1);
				String userCreatore = rset.getString(2);
				String pswCreatore = rset.getString(3);
				String descr = rset.getString(4);
				Utente ut = new Utente(userCreatore,pswCreatore);
				Campagna m = new Campagna(nomeCampagna,ut,descr);
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
	
	/*public ListaCampagne loadAllCampagne(){
		String query = "select campagna.nome_campagna, campagna.user_creatore, campagna.descr from campagna";
		System.out.println(query);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaCampagne res = new ListaCampagne();
			while (rset.next()) {
				String nomeCampagna = rset.getString(1);
				String userCreatore = rset.getString(2);
				String descr = rset.getString(3);
				String query2 = "SELECT username,password from utente where username = '"+userCreatore+"'";
				System.out.println(query2);
				Statement stmt2 = conn.createStatement();
				ResultSet rset2 = stmt2.executeQuery(query2);
				Utente ut = null;
				while (rset2.next()) {
					ut = new Utente(rset2.getString(1),rset2.getString(2));
					break;
				}
				rset2.close();
				stmt2.close();
				Campagna m = null;
				m = new Campagna(nomeCampagna,ut,descr);
				res.add(m);
				System.out.println(res.toJSONString());
			}
			
			rset.close();
			stmt.close();
			
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	public ListaCampagne loadAllCampagne(){
		String query = "select campagna.nome_campagna, campagna.user_creatore, utente.password, campagna.descr from campagna, utente where campagna.user_creatore = utente.username";
		System.out.println(query);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaCampagne res = new ListaCampagne();
			while (rset.next()) {
				String nomeCampagna = rset.getString(1);
				String userCreatore = rset.getString(2);
				String pswCreatore = rset.getString(3);
				String descr = rset.getString(4);
				Utente ut = new Utente(userCreatore,pswCreatore);
				Campagna m = new Campagna(nomeCampagna,ut,descr);
				res.add(m);
				System.out.println(res.toJSONString());
			}
			
			rset.close();
			stmt.close();
			
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int insertCampagna(Campagna campagna) {
		String SQL = "INSERT INTO campagna (nome_campagna,descr,user_creatore) " + "VALUES (?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, campagna.getNomeCampagna());
			pstmt.setString(2, campagna.getDescrizione());
			pstmt.setString(3, campagna.getCreatore().getUsername());
			int affectedRows = pstmt.executeUpdate();
			return affectedRows;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int updateCampagna(String nomeCampagna, String userCreatore, String descr, String oldName) {
		String SQL = "UPDATE campagna SET descr = "+descr+", nome_campagna = "+nomeCampagna+" WHERE nome_campagna = "+oldName+" and user_creatore = "+userCreatore;
		System.out.println(SQL);
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			System.out.println(SQL);
			int affectedRows = pstmt.executeUpdate();
			return affectedRows;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	

	public boolean deleteCampagna(String nomeCampagna, String userCreatore) {
		String SQL = "DELETE FROM campagna WHERE nome_campagna = " + nomeCampagna + " and user_creatore = "+userCreatore;
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