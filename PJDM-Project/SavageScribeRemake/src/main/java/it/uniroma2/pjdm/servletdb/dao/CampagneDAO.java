package it.uniroma2.pjdm.servletdb.dao;

import it.uniroma2.pjdm.servletdb.entity.ListaCampagne;
import it.uniroma2.pjdm.servletdb.entity.Campagna;

public interface CampagneDAO{
	
	public Campagna loadCampagna(String nomeCampagna, String userCreatore);
	
	public ListaCampagne loadCampagneByCreatore(String utente);
	
	public ListaCampagne loadCampagneByPartecipazione(String utente);
	
	public ListaCampagne loadCampagneByLike(String nomeCampagna, String userCreatore);
	
	public ListaCampagne loadAllCampagne();
	
	public int insertCampagna(Campagna campagna);
	
	public int updateCampagna(String nomeCampagna, String userCreatore, String descr, String oldName);

	public boolean deleteCampagna(String nomeCampagna, String userCreatore);

	public void closeConnection();

}