package it.uniroma2.pjdm.servletdb.entity;

import java.util.ArrayList;

import org.json.JSONArray;

public class ListaRazze extends ArrayList<Razza>{
	private static final long serialVersionUID = 5017166673637496567L;

	public String toJSONString() {
		return new JSONArray(this).toString();
	}
}