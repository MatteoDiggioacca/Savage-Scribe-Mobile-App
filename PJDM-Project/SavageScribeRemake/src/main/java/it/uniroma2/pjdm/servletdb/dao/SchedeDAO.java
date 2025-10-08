package it.uniroma2.pjdm.servletdb.dao;

import it.uniroma2.pjdm.servletdb.entity.ListaSchede;
import it.uniroma2.pjdm.servletdb.entity.Scheda;

public interface SchedeDAO{
	
	public Scheda loadScheda(String nome, String proprietario);
	
	public ListaSchede loadSchedeByProprietario(String proprietario);
	
	public ListaSchede loadSchedeByCampagnaVis(String nomeCampagna, String userCreatore);
	
	public ListaSchede loadSchedeByCampagnaNoVis(String nomeCampagna, String userCreatore);
	
	public ListaSchede loadAllSchede();
	
	public int insertScheda(Scheda scheda);
	
	public int updateScheda(Scheda scheda, String oldName, String proprietario);

	public boolean deleteScheda(String nome, String proprietario);

	public void closeConnection();

}