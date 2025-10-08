package it.uniroma2.pjdm.servletdb.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import it.uniroma2.pjdm.servletdb.entity.ListaSchede;
import it.uniroma2.pjdm.servletdb.entity.Razza;
import it.uniroma2.pjdm.servletdb.entity.Scheda;
import it.uniroma2.pjdm.servletdb.entity.Utente;

public class SchedeDAOJDBCImpl implements SchedeDAO{
	
	private Connection conn;
	
	public SchedeDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		
			// SU WINDOWS
			conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + dbName, userName, pwd);
			System.out.println("SchedeDAOJDBCImpl connected!");
			// SU LINUX
			//		conn = DriverManager.getConnection(
			//				"jdbc:mysql://" + ip + ":" + port + "/" + dbName
			//						+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
			//				userName, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*public Scheda loadScheda(String nome, String proprietario) {
		String query1 = "SELECT nome,proprietario,nome_razza,sesso,eta,visibilita,exp,agi,forz,inte,spi,vig FROM scheda WHERE nome = '" +nome+ "' and proprietario = '"+proprietario+"'";
		System.out.println(query1+"\n");
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query1);
			Scheda res = null;
			while (rset.next()) {
				
				String nom = rset.getString(1);
				String prop = rset.getString(2);
				String nomeRazza = rset.getString(3);
				String sesso = rset.getString(4);
				int eta = rset.getInt(5);
				int visibilita = rset.getInt(6);
				int exp = rset.getInt(7);
				int agi = rset.getInt(8);
				int forz = rset.getInt(9);
				int inte = rset.getInt(10);
				int spi = rset.getInt(11);
				int vig = rset.getInt(12);
				//res = new Scheda(nom,prop,nomeRazza,sesso,eta,visibilita,exp,agi,forz,inte,spi,vig);
				String query2 = "SELECT username,password from utente where username = '"+prop+"'";
				System.out.println(query2+"\n");
				String query3 = "SELECT nome_razza,descr from razza where nome_razza = '"+nomeRazza+"'";
				System.out.println(query3+"\n");
				ResultSet rset2 = stmt.executeQuery(query2);
				Utente propr = null;
				while(rset2.next()) {
					String username = rset2.getString(1);
					String password = rset2.getString(2);
					propr = new Utente(username,password);
				}
				rset2.close();
				Razza razza = null;
				ResultSet rset3 = stmt.executeQuery(query3);
				while(rset3.next()) {
					String nomeRaz = rset3.getString(1);
					String descr = rset3.getString(2);
					razza = new Razza(nomeRaz,descr);
				}
				rset3.close();
				res = new Scheda(nom,propr,razza,sesso,eta,visibilita,exp,agi,forz,inte,spi,vig);
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
	
	public Scheda loadScheda(String nome, String proprietario) {
		String query = "select scheda.nome, scheda.proprietario, utente.password, scheda.nome_razza, razza.descr, scheda.sesso, scheda.eta, scheda.visibilita, scheda.exp, scheda.agi, scheda.forz, scheda.inte, scheda.spi, scheda.vig from scheda, utente, razza where scheda.proprietario = utente.username and scheda.nome_razza = razza.nome_razza and scheda.nome ="+nome+" and scheda.proprietario = "+proprietario;
		System.out.println(query+"\n");
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			Scheda res = null;
			while(rset.next()) {
				String nomeScheda = rset.getString(1);
				String nomeProp = rset.getString(2);
				String pswProp = rset.getString(3);
				String nomeRazz = rset.getString(4);
				String descrRazz = rset.getString(5);
				String sesso = rset.getString(6);
				int eta = rset.getInt(7);
				int vis = rset.getInt(8);
				int exp = rset.getInt(9);
				int agi = rset.getInt(10);
				int forz = rset.getInt(11);
				int inte = rset.getInt(12);
				int spi = rset.getInt(13);
				int vig = rset.getInt(14);
				Razza razza = new Razza(nomeRazz,descrRazz);
				Utente utente = new Utente(nomeProp,pswProp);
				res = new Scheda(nomeScheda,utente,razza,sesso,eta,vis,exp,agi,forz,inte,spi,vig);
			}
			rset.close();
			stmt.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*public ListaSchede loadSchedeByProprietario(String username) {
		String query1 = "select scheda.nome,scheda.proprietario from scheda where proprietario = "+username;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query1);
			ListaSchede res = new ListaSchede();
			ArrayList<String> listaNomiSchede = new ArrayList<String>();
			ArrayList<String> listaProprietari = new ArrayList<String>();
			while(rset.next()) {
				String nomeScheda = rset.getString(1);
				String prop = rset.getString(2);
				listaNomiSchede.add(nomeScheda);
				listaProprietari.add(prop);
			}
			rset.close();
			for(int i=0; i<listaNomiSchede.size(); i++) {
				Scheda scheda = loadScheda(listaNomiSchede.get(i),listaProprietari.get(i));
				res.add(scheda);
			}
			stmt.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	public ListaSchede loadSchedeByProprietario(String username) {
		String query = "select scheda.nome, scheda.proprietario, utente.password, scheda.nome_razza, razza.descr, scheda.sesso, scheda.eta, scheda.visibilita, scheda.exp, scheda.agi, scheda.forz, scheda.inte, scheda.spi, scheda.vig from scheda, razza, utente where scheda.nome_razza = razza.nome_razza and scheda.proprietario = utente.username and utente.username = "+username;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaSchede res = new ListaSchede();
			while(rset.next()) {
				String nomeScheda = rset.getString(1);
				String nomeProp = rset.getString(2);
				String pswProp = rset.getString(3);
				String nomeRazz = rset.getString(4);
				String descrRazz = rset.getString(5);
				String sesso = rset.getString(6);
				int eta = rset.getInt(7);
				int vis = rset.getInt(8);
				int exp = rset.getInt(9);
				int agi = rset.getInt(10);
				int forz = rset.getInt(11);
				int inte = rset.getInt(12);
				int spi = rset.getInt(13);
				int vig = rset.getInt(14);
				Razza razza = new Razza(nomeRazz,descrRazz);
				Utente utente = new Utente(nomeProp,pswProp);
				Scheda sched = new Scheda(nomeScheda,utente,razza,sesso,eta,vis,exp,agi,forz,inte,spi,vig);
				res.add(sched);
			}
			rset.close();
			stmt.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*public ListaSchede loadSchedeByCampagnaVis(String nomeCampagna, String userCreatore) {
		String query = "select scheda.nome, scheda.proprietario from scheda, partecipa where partecipa.nome_campagna = "+nomeCampagna+" and partecipa.user_creatore = "+userCreatore+" and partecipa.nome_scheda = scheda.nome";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaSchede res = new ListaSchede();
			while(rset.next()) {
				String nomeScheda = rset.getString(1);
				String proprietario = rset.getString(2);
				Scheda scheda = loadScheda(nomeScheda,proprietario);
				res.add(scheda);
			}
			rset.close();
			stmt.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	public ListaSchede loadSchedeByCampagnaVis(String nomeCampagna, String userCreatore) {
		String query = "select scheda.nome, scheda.proprietario, utente.password, scheda.nome_razza, razza.descr, scheda.sesso, scheda.eta, scheda.visibilita, scheda.exp, scheda.agi, scheda.forz, scheda.inte, scheda.spi, scheda.vig from scheda, razza, utente, partecipa where scheda.nome_razza = razza.nome_razza and scheda.proprietario = utente.username and partecipa.nome_campagna = "+nomeCampagna+" and partecipa.user_creatore = "+userCreatore+" and partecipa.nome_scheda = scheda.nome";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaSchede res = new ListaSchede();
			while(rset.next()) {
				String nomeScheda = rset.getString(1);
				String nomeProp = rset.getString(2);
				String pswProp = rset.getString(3);
				String nomeRazz = rset.getString(4);
				String descrRazz = rset.getString(5);
				String sesso = rset.getString(6);
				int eta = rset.getInt(7);
				int vis = rset.getInt(8);
				int exp = rset.getInt(9);
				int agi = rset.getInt(10);
				int forz = rset.getInt(11);
				int inte = rset.getInt(12);
				int spi = rset.getInt(13);
				int vig = rset.getInt(14);
				Razza razza = new Razza(nomeRazz,descrRazz);
				Utente utente = new Utente(nomeProp,pswProp);
				Scheda sched = new Scheda(nomeScheda,utente,razza,sesso,eta,vis,exp,agi,forz,inte,spi,vig);
				res.add(sched);
			}
			rset.close();
			stmt.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*public ListaSchede loadSchedeByCampagnaNoVis(String nomeCampagna, String userCreatore) {
		String query = "select scheda.nome, scheda.proprietario from scheda, partecipa where partecipa.nome_campagna = "+nomeCampagna+" and partecipa.user_creatore = "+userCreatore+" and partecipa.nome_scheda = scheda.nome and scheda.visibilita = 1";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaSchede res = new ListaSchede();
			while(rset.next()) {
				String nomeScheda = rset.getString(1);
				String proprietario = rset.getString(2);
				Scheda scheda = loadScheda(nomeScheda,proprietario);
				res.add(scheda);
			}
			rset.close();
			stmt.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	public ListaSchede loadSchedeByCampagnaNoVis(String nomeCampagna, String userCreatore) {
		String query = "select scheda.nome, scheda.proprietario, utente.password, scheda.nome_razza, razza.descr, scheda.sesso, scheda.eta, scheda.visibilita, scheda.exp, scheda.agi, scheda.forz, scheda.inte, scheda.spi, scheda.vig from scheda, razza, utente, partecipa where scheda.nome_razza = razza.nome_razza and scheda.proprietario = utente.username and partecipa.nome_campagna = "+nomeCampagna+" and partecipa.user_creatore = "+userCreatore+" and partecipa.nome_scheda = scheda.nome and scheda.visibilita=1";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaSchede res = new ListaSchede();
			while(rset.next()) {
				String nomeScheda = rset.getString(1);
				String nomeProp = rset.getString(2);
				String pswProp = rset.getString(3);
				String nomeRazz = rset.getString(4);
				String descrRazz = rset.getString(5);
				String sesso = rset.getString(6);
				int eta = rset.getInt(7);
				int vis = rset.getInt(8);
				int exp = rset.getInt(9);
				int agi = rset.getInt(10);
				int forz = rset.getInt(11);
				int inte = rset.getInt(12);
				int spi = rset.getInt(13);
				int vig = rset.getInt(14);
				Razza razza = new Razza(nomeRazz,descrRazz);
				Utente utente = new Utente(nomeProp,pswProp);
				Scheda sched = new Scheda(nomeScheda,utente,razza,sesso,eta,vis,exp,agi,forz,inte,spi,vig);
				res.add(sched);
			}
			rset.close();
			stmt.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*public ListaSchede loadAllSchede(){
		String query = "SELECT nome,proprietario from scheda";
		System.out.println(query);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaSchede res = new ListaSchede();
			while (rset.next()) {
				String nome = rset.getString(1);
				String proprietario = rset.getString(2);
				Scheda m = loadScheda(nome,proprietario);
				res.add(m);
			}
			rset.close();
			stmt.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	public ListaSchede loadAllSchede(){
		String query = "select scheda.nome, scheda.proprietario, utente.password, scheda.nome_razza, razza.descr, scheda.sesso, scheda.eta, scheda.visibilita, scheda.exp, scheda.agi, scheda.forz, scheda.inte, scheda.spi, scheda.vig from scheda, utente, razza where scheda.proprietario = utente.username and scheda.nome_razza = razza.nome_razza";
		System.out.println(query);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);
			ListaSchede res = new ListaSchede();
			while(rset.next()) {
				String nomeScheda = rset.getString(1);
				String nomeProp = rset.getString(2);
				String pswProp = rset.getString(3);
				String nomeRazz = rset.getString(4);
				String descrRazz = rset.getString(5);
				String sesso = rset.getString(6);
				int eta = rset.getInt(7);
				int vis = rset.getInt(8);
				int exp = rset.getInt(9);
				int agi = rset.getInt(10);
				int forz = rset.getInt(11);
				int inte = rset.getInt(12);
				int spi = rset.getInt(13);
				int vig = rset.getInt(14);
				Razza razza = new Razza(nomeRazz,descrRazz);
				Utente utente = new Utente(nomeProp,pswProp);
				Scheda sched = new Scheda(nomeScheda,utente,razza,sesso,eta,vis,exp,agi,forz,inte,spi,vig);
				res.add(sched);
			}
			rset.close();
			stmt.close();
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int insertScheda(Scheda scheda) {
		System.out.println(scheda.toString());
		String SQL = "INSERT INTO scheda (nome,proprietario,nome_razza,sesso,eta,visibilita,exp,agi,forz,inte,spi,vig) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, scheda.getNome());
			pstmt.setString(2, scheda.getProprietario().getUsername());
			pstmt.setString(3, scheda.getRazza().getNomeRazza());
			pstmt.setString(4, scheda.getSesso());
			pstmt.setInt(5, scheda.getEta());
			pstmt.setInt(6, scheda.getVisibilita());
			pstmt.setInt(7, scheda.getExp());
			pstmt.setInt(8, scheda.getAgi());
			pstmt.setInt(9, scheda.getForz());
			pstmt.setInt(10, scheda.getInte());
			pstmt.setInt(11, scheda.getSpi());
			pstmt.setInt(12, scheda.getVig());
			int affectedRows = pstmt.executeUpdate();
			return affectedRows;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int updateScheda(Scheda scheda, String oldName, String proprietario) {
		String SQL = "UPDATE scheda SET nome=?, proprietario=?, nome_razza=?, sesso=?, eta=?, visibilita=?, exp=?, agi=?, forz=?, inte=?, spi=?, vig=? WHERE nome= '"+oldName+"' and proprietario = '"+proprietario+"'";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, scheda.getNome());
			pstmt.setString(2, scheda.getProprietario().getUsername());
			pstmt.setString(3, scheda.getRazza().getNomeRazza());
			pstmt.setString(4, scheda.getSesso());
			pstmt.setInt(5, scheda.getEta());
			pstmt.setInt(6, scheda.getVisibilita());
			pstmt.setInt(7, scheda.getExp());
			pstmt.setInt(8, scheda.getAgi());
			pstmt.setInt(9, scheda.getForz());
			pstmt.setInt(10, scheda.getInte());
			pstmt.setInt(11, scheda.getSpi());
			pstmt.setInt(12, scheda.getVig());
			int affectedRows = pstmt.executeUpdate();
			return affectedRows;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public boolean deleteScheda(String nome, String proprietario) {
		String SQL = "DELETE FROM scheda WHERE nome=" + nome + " and proprietario = "+proprietario;
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
		System.out.println("SchedeDAOJDBCImpl disconnected!");
	}
}