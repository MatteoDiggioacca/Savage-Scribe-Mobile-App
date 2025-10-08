package it.uniroma2.pjdm.servletdb.dao;

import it.uniroma2.pjdm.servletdb.entity.ListaRazze;
import it.uniroma2.pjdm.servletdb.entity.Razza;

public interface RazzeDAO{
	
	public Razza loadRazzaByNome(String nome);
	
	public Razza loadRazzaByScheda(String nome, String userCreatore);
	
	public ListaRazze loadAllRazze();
	
	public int insertRazza(Razza razza);
	
	public int updateRazza(Razza razza);

	public boolean deleteRazza(String nomeRazza);

	public void closeConnection();

}