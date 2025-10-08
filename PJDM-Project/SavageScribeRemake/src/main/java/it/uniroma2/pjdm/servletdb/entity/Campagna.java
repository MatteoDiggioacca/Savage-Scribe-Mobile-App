package it.uniroma2.pjdm.servletdb.entity;


import org.json.JSONException;
import org.json.JSONObject;

public class Campagna{
	private String nomeCampagna;
	private Utente creatore;
	private String descrizione;

	
	public Campagna(String nomeCampagna, Utente creatore, String descrizione) {
		this.nomeCampagna = nomeCampagna;
		this.creatore = creatore;
		this.descrizione = descrizione;
	}
	
	
	public String getNomeCampagna() {
		return this.nomeCampagna;
	}
	
	public void setNomeCampagna(String newNomeCampagna) {
		this.nomeCampagna = newNomeCampagna;
	}
	
	
	public String getDescrizione() {
		return this.descrizione;
	}
	
	public void setDescrizione(String newDescrizione) {
		this.descrizione = newDescrizione;
	}
	
	public Utente getCreatore() {
		return creatore;
	}


	public void setCreatore(Utente creatore) {
		this.creatore = creatore;
	}
	
	public static Campagna fromJsonObject(JSONObject jsonObject) {
		try {
			Campagna res = null;
			String nomeCampagna = jsonObject.getString("nomeCampagna");
			Utente creatore = Utente.fromJsonObject(jsonObject.getJSONObject("creatore"));
			String descrizione = jsonObject.getString("descrizione");
			res = new Campagna(nomeCampagna,creatore,descrizione);
			return res;
		} catch (JSONException | IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String toJsonString() {
		return new JSONObject(this).toString();	
	}
	
}