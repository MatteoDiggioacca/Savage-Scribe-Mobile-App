package it.uniroma2.pjdm.servletdb.entity;

import java.util.ArrayList;

import org.json.JSONArray;

public class ListaSchede extends ArrayList<Scheda>{
	private static final long serialVersionUID = 5016166673637496567L;

	public String toJSONString() {
		return new JSONArray(this).toString();
	}
}