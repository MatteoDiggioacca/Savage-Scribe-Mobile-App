package it.uniroma2.pjdm.servletdb.dao;

import it.uniroma2.pjdm.servletdb.entity.ListaUtenti;
import it.uniroma2.pjdm.servletdb.entity.Utente;

public interface UtentiDAO{
	
	public Utente loadUtente(String username, String password);
	
	public Utente loadUtenteByUsername(String username);
	
	public ListaUtenti loadUtentiByCampagna(String nomeCampagna, String userCreatore);
	
	public ListaUtenti loadUtentiByLike(String username);
	
	public ListaUtenti loadAllUtenti();
	
	public int insertUtente(Utente utente);
	
	public int updatePasswordUtente(Utente utente);

	public boolean deleteUtente(String username);
	
	public boolean deleteUtenteFromCampagna(String nomeCampagna, String userCreatore, String usernameUtente);

	public void closeConnection();

}