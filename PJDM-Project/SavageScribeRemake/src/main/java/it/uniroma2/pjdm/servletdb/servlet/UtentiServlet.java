package it.uniroma2.pjdm.servletdb.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniroma2.pjdm.servletdb.dao.UtentiDAO;
import it.uniroma2.pjdm.servletdb.dao.UtentiDAOJDBCImpl;
import it.uniroma2.pjdm.servletdb.entity.Utente;
import it.uniroma2.pjdm.servletdb.entity.ListaUtenti;

public class UtentiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UtentiDAO dao;
	
	public UtentiServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		String ip = getInitParameter("ip");
		String port = getInitParameter("port");
		String dbName = getInitParameter("dbName");
		String userName = getInitParameter("userName");
		String password = getInitParameter("password");

		System.out.println("UtentiServlet. Opening DB connection...");

		dao = new UtentiDAOJDBCImpl(ip, port, dbName, userName, password);
		
		System.out.println("UtentiServlet: ONLINE!");
	}

	@Override
	public void destroy() {
		System.out.print("UtentiServlet. Closing DB connection...");
		dao.closeConnection();
		System.out.println("UtentiServlet: OFFLINE!");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("UtentiServlet. Invoking a doGet method.");
		PrintWriter out = response.getWriter();
		if(request.getParameter("like")==null) {
			if (request.getParameter("username") == null && request.getParameter("password") == null) {
				if (request.getParameter("nomeCampagna") == null && request.getParameter("userCreatore")==null) {
					ListaUtenti allUtenti = dao.loadAllUtenti();
					if (allUtenti == null) {
						response.setStatus(404); 
						return;
					}
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(200); 
					out.print(allUtenti.toJSONString());
					out.flush();
					return;
				} else {
						String nomeCampagna = request.getParameter("nomeCampagna");
						String userCreatore = request.getParameter("userCreatore");
						ListaUtenti utentiByCampagna = dao.loadUtentiByCampagna(nomeCampagna,userCreatore);
						if (utentiByCampagna == null) {
							response.setStatus(404); 
							return;
						}
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.setStatus(200); 
						out.print(utentiByCampagna.toJSONString());
						out.flush(); 
						return;
					}
			} else if (request.getParameter("username")!= null && request.getParameter("password")==null){
				String username = request.getParameter("username"); 
				Utente utente = dao.loadUtenteByUsername(username);
				if (utente == null) {
					response.setStatus(404); 
					return;
				}
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(200); 
				System.out.println(utente.toString());
				out.print(utente.toJsonString());
				out.flush();
				return;
			} else if (request.getParameter("username")!= null && request.getParameter("password")!=null){
				String username = request.getParameter("username"); 
				String password = request.getParameter("password");
				Utente utente = dao.loadUtente(username,password);
				if (utente == null) {
					response.setStatus(404); 
					return;
				}
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(200); 
				System.out.println(utente.toString());
				out.print(utente.toJsonString());
				out.flush();
				return;
			}
		} else {
			String username = request.getParameter("username");
			ListaUtenti ls = dao.loadUtentiByLike(username);
			if (ls == null) {
				response.setStatus(404);
				return;
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200); 
			System.out.println(username.toString());
			out.print(ls.toJSONString());
			out.flush();
			return;
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("UtentiServlet. Invoking a doPost method...");
		response.setContentType("text/plain");
		Utente newUtente = null;
		if (request.getParameter("json") != null) {
			String jsonString = request.getParameter("json");
			try {
				JSONObject jsonObject = new JSONObject(jsonString);
				newUtente = Utente.fromJsonObject(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
				response.setStatus(422); 
				return;
			}
		} else {
			response.setStatus(422); 
			return;
		}

		int res = dao.insertUtente(newUtente);
		

		if (res > 0) {
			response.setStatus(201);
			return;
		} else {
			response.setStatus(500);
			return;
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("UtentiServlet. Invoking a doPut method...");
		response.setContentType("text/plain");
		Utente utenteToUpdate = null;
		if (request.getParameter("json") != null) {
			String jsonString = request.getParameter("json");
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonString);
				utenteToUpdate = Utente.fromJsonObject(jsonObject);
				System.out.println(utenteToUpdate.toJsonString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.setStatus(422); 
				return;
			}
			
			int res = dao.updatePasswordUtente(utenteToUpdate);
			if(res > 0) {
				response.setStatus(200); 
				return;
			} else {
				response.setStatus(500); 
				return;
			}
			
		} else {
			response.setStatus(422); 
			return;
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("UtentiServlet. Invoking a doDelete method.");
		if(request.getParameter("nomeCampagna")==null && request.getParameter("userCreatore")==null && request.getParameter("username") != null) {
			String username = request.getParameter("username");
			boolean isOk = dao.deleteUtente(username);
			if (isOk) {
				response.setStatus(204); 
			} else {
				response.setStatus(500); 
			}
		} else if (request.getParameter("nomeCampagna") != null && request.getParameter("userCreatore") != null && request.getParameter("username") != null) {
			String nomeCampagna = request.getParameter("nomeCampagna");
			String userCreatore = request.getParameter("userCreatore");
			String username = request.getParameter("username");
			boolean isOk = dao.deleteUtenteFromCampagna(username,nomeCampagna,userCreatore);
			if (isOk) {
				response.setStatus(204); 
			} else {
				response.setStatus(500); 
			}
		} else {
			response.setStatus(422); 
			return;
		}
	}
}
