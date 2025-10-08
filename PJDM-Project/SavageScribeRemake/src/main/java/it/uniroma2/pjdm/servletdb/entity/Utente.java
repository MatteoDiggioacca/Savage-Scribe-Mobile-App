package it.uniroma2.pjdm.servletdb.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Utente{
	private String username;
	private String password;
	
	public Utente(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String newUsername) {
		this.username = newUsername;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String newPassword) {
		this.password = newPassword;
	}
	
	public static Utente fromJsonObject(JSONObject jsonObject)  {
		try {
			String username = jsonObject.getString("username");
			String password = jsonObject.getString("password");
			Utente res = new Utente(username,password);
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
		return "Utente [username=\""+this.username+"\", password= \""+this.password+"\"]";
	}
}