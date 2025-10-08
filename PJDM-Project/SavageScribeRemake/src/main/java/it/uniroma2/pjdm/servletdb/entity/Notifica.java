package it.uniroma2.pjdm.servletdb.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Notifica{
	
	private String mittente;
	private String destinatario;
	private String nomeCampagna;
	private int tipo;
	
	public Notifica(String mittente, String destinatario, String nomeCampagna, int tipo) {
		this.mittente = mittente;
		this.destinatario = destinatario;
		this.nomeCampagna = nomeCampagna;
		this.tipo = tipo;
	}

	public String getMittente() {
		return mittente;
	}

	public void setMittente(String mittente) {
		this.mittente = mittente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getNomeCampagna() {
		return nomeCampagna;
	}

	public void setNomeCampagna(String nomeCampagna) {
		this.nomeCampagna = nomeCampagna;
	}
	
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public int getTipo() {
		return this.tipo;
	}
	
	
	public static Notifica fromJsonObject(JSONObject jsonObject)  {
		try {
			String username = jsonObject.getString("mittente");
			String password = jsonObject.getString("destinatario");
			String nomeCampagna = jsonObject.getString("nomeCampagna");
			int tipo = jsonObject.getInt("tipo");
			Notifica res = new Notifica(username,password,nomeCampagna,tipo);
			return res;
		} catch(JSONException | IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String toJsonString() {
		return new JSONObject(this).toString();	
	}
}