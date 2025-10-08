package it.uniroma2.pjdm.servletdb.dao;

import it.uniroma2.pjdm.servletdb.entity.ListaNotifiche;
import it.uniroma2.pjdm.servletdb.entity.Notifica;

public interface NotificheDAO{
	
	public Notifica loadNotifica(String mittente, String destinatario, String nomeCampagna, int tipo);
	
	public ListaNotifiche loadNotificheByMittente(String mittente, int tipo);
	
	public ListaNotifiche loadNotificheByDestinatario(String destinatario, int tipo);
	
	public ListaNotifiche loadAllNotifiche();
	
	public int insertNotifica(Notifica notifica);
	
	public int joinCampagna(String user, String nomeCampagna, String userCreatore, String nomeScheda);
	
	public boolean deleteNotifica(String mittente, String destinatario, String nomeCampagna, int tipo);

	public void closeConnection();

}