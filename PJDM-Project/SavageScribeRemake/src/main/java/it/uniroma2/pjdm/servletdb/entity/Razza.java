package it.uniroma2.pjdm.servletdb.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Razza{
	private String nomeRazza;
	private String descrizione;
	
	public Razza(String nomeRazza, String descrizione) {
		this.nomeRazza = nomeRazza;
		this.descrizione = descrizione;
	}
	
	public String getNomeRazza() {
		return nomeRazza;
	}
	
	public void setNomeRazza(String newNomeRazza) {
		this.nomeRazza = newNomeRazza;
	}
	
	public String getDescrizione() {
		return descrizione;
	}
	
	public void setDescrizione(String newDescrizione) {
		this.descrizione = newDescrizione;
	}
	
	public static Razza fromJsonObject(JSONObject jsonObject)  {
		try {
			String nomeRazza = jsonObject.getString("nomeRazza");
			String descr = jsonObject.getString("descr");
			Razza res = new Razza(nomeRazza,descr);
			return res;
		} catch(JSONException | IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String toJsonString() {
		return new JSONObject(this).toString();	
	}
	
	public String toString() {
		return "Razza [nome=\""+this.nomeRazza+"\", descrizione= \""+this.descrizione+"\"]";
	}
}