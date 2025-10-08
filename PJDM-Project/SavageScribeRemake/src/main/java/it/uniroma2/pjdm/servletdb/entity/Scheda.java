package it.uniroma2.pjdm.servletdb.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Scheda{
	private String nome;
	private Utente proprietario;
	private Razza razza;
	private String sesso;
	private int eta;
	private int visibilita;
	private int exp;
	private int agi;
	private int forz;
	private int inte;
	private int spi;
	private int vig;
	
	public Scheda(String nome, Utente proprietario, Razza razza, String sesso, int eta, int visibilita, int exp, int agi, int forz, int inte, int spi, int vig) {
		this.setNome(nome);
		this.setProprietario(proprietario);
		this.setRazza(razza);
		this.setSesso(sesso);
		this.setEta(eta);
		this.setVisibilita(visibilita);
		this.setExp(exp);
		this.setAgi(agi);
		this.setForz(forz);
		this.setInte(inte);
		this.setSpi(spi);
		this.setVig(vig);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String newNome) {
		this.nome = newNome;
	}


	public String getSesso() {
		return sesso;
	}

	public void setSesso(String newSesso) {
		this.sesso = newSesso;
	}

	public int getEta() {
		return eta;
	}

	public void setEta(int newEta) {
		this.eta = newEta;
	}

	public int getVisibilita() {
		return visibilita;
	}

	public void setVisibilita(int newVisibilita) {
		this.visibilita = newVisibilita;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int newExp) {
		this.exp = newExp;
	}

	public int getAgi() {
		return agi;
	}

	public void setAgi(int newAgi) {
		this.agi = newAgi;
	}

	public int getForz() {
		return forz;
	}

	public void setForz(int newForz) {
		this.forz = newForz;
	}

	public int getInte() {
		return inte;
	}

	public void setInte(int newInte) {
		this.inte = newInte;
	}

	public int getSpi() {
		return spi;
	}

	public void setSpi(int newSpi) {
		this.spi = newSpi;
	}

	public int getVig() {
		return vig;
	}

	public void setVig(int newVig) {
		this.vig = newVig;
	}
	
	public static Scheda fromJSONObject(JSONObject jsonObject) {
		try {
			String nome = jsonObject.getString("nome");
			Utente proprietario = Utente.fromJsonObject(jsonObject.getJSONObject("proprietario"));
			Razza razza = Razza.fromJsonObject(jsonObject.getJSONObject("razza"));
			String sesso = jsonObject.getString("sesso");;
			int eta = jsonObject.getInt("eta");
			int visibilita = jsonObject.getInt("visibilita");;
			int exp = jsonObject.getInt("exp");;
			int agi = jsonObject.getInt("agi");;
			int forz = jsonObject.getInt("forz");;
			int inte = jsonObject.getInt("inte");;
			int spi = jsonObject.getInt("spi");;
			int vig = jsonObject.getInt("vig");;
			Scheda res = new Scheda(nome,proprietario,razza,sesso,eta,visibilita,exp,agi,forz,inte,spi,vig);
			return res;
		} catch (JSONException | IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	public String toJsonString() {
		return new JSONObject(this).toString();	
	}

	public Utente getProprietario() {
		return proprietario;
	}

	public void setProprietario(Utente proprietario) {
		this.proprietario = proprietario;
	}

	public Razza getRazza() {
		return razza;
	}

	public void setRazza(Razza razza) {
		this.razza = razza;
	}
}
