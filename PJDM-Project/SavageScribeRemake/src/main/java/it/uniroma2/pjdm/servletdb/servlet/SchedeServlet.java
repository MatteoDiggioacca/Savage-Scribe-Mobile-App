package it.uniroma2.pjdm.servletdb.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniroma2.pjdm.servletdb.dao.SchedeDAO;
import it.uniroma2.pjdm.servletdb.dao.SchedeDAOJDBCImpl;
import it.uniroma2.pjdm.servletdb.entity.ListaSchede;
import it.uniroma2.pjdm.servletdb.entity.Scheda;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class SchedeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private SchedeDAO dao;

	public SchedeServlet() {
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

		System.out.println("SchedeServlet. Opening DB connection...");

		dao = new SchedeDAOJDBCImpl(ip, port, dbName, userName, password);
		
		System.out.println("SchedeServlet: ONLINE!");
	}

	@Override
	public void destroy() {
		System.out.print("SchedeServlet. Closing DB connection...");
		dao.closeConnection();
		System.out.println("SchedeServlet: OUT!");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("SchedeServlet. Invoking a doGet method.");
		PrintWriter out = response.getWriter();
		if (request.getParameter("nome") == null && request.getParameter("proprietario")==null) {
			if (request.getParameter("nomeCampagna")==null && request.getParameter("userCreatore") == null) {
				ListaSchede allSched = dao.loadAllSchede();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(200); 
				out.print(allSched.toJSONString());
				out.flush();
				return;
			} else if (request.getParameter("nomeCampagna") !=null && request.getParameter("userCreatore") != null) {
				if (request.getParameter("visibilita")==null) {
					String nomeCampagna = request.getParameter("nomeCampagna");
					String userCreatore = request.getParameter("userCreatore");
					ListaSchede allSched = dao.loadSchedeByCampagnaVis(nomeCampagna,userCreatore);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(200); 
					out.print(allSched.toJSONString());
					out.flush();
					return;
				} else {
					String nomeCampagna = request.getParameter("nomeCampagna");
					String userCreatore = request.getParameter("userCreatore");
					ListaSchede allSched = dao.loadSchedeByCampagnaNoVis(nomeCampagna,userCreatore);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(200); 
					out.print(allSched.toJSONString());
					out.flush();
					return;
				}
			}
		} else if (request.getParameter("nome")!=null && request.getParameter("proprietario")!=null) {
			String nome = request.getParameter("nome");
			String proprietario = request.getParameter("proprietario");
			Scheda sched = dao.loadScheda(nome,proprietario);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200);
			out.print(sched.toJsonString());
			out.flush();
			return;
		} else if (request.getParameter("nome")==null && request.getParameter("proprietario")!=null){
			String proprietario = request.getParameter("proprietario");
			ListaSchede allSched = dao.loadSchedeByProprietario(proprietario);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200); 
			out.print(allSched.toJSONString());
			out.flush();
			return;
		} else {
			response.setStatus(422); 
			out.flush();
			return;
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("SchedeServlet. Invoking a doPost method...");
		response.setContentType("text/plain");
		Scheda newScheda = null;
		if (request.getParameter("json") != null) {
			String jsonString = request.getParameter("json");
			try {
				JSONObject jsonObj = new JSONObject(jsonString);
				newScheda = Scheda.fromJSONObject(jsonObj);
				System.out.println(newScheda.toString());
			} catch (JSONException e) {
				e.printStackTrace();
				response.setStatus(422); 
				return;
			}
		} else {
			response.setStatus(422); 
			return;
		}

		int res = dao.insertScheda(newScheda);

		if (res > 0) {
			response.setStatus(201);
			return;
		} else {
			response.setStatus(500);
			return;
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("SchedeServlet. Invoking a doPut method...");
		response.setContentType("text/plain");
		Scheda schedaToUpdate = null;
		String oldName = null;
		String proprietario = null;
		if (request.getParameter("json") != null) {
			String jsonString = request.getParameter("json");
			try {
				JSONObject jsonObj = new JSONObject(jsonString);
				schedaToUpdate = Scheda.fromJSONObject(jsonObj.getJSONObject("scheda"));
				oldName = jsonObj.getString("oldName");
				proprietario = jsonObj.getString("proprietario");
				System.out.println(schedaToUpdate.toString());
			} catch (JSONException e) {
				e.printStackTrace();
				response.setStatus(422); 
				return;
			}
		} else {
			response.setStatus(422); 
			return;
		}

		int res = dao.updateScheda(schedaToUpdate,oldName,proprietario);

		if (res > 0) {
			response.setStatus(201);
			return;
		} else {
			response.setStatus(500);
			return;
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("SchedeServlet. Invoking a doDelete method.");

		if (request.getParameter("nome") == null || request.getParameter("proprietario") == null) {
			response.setStatus(422); 
			return;
		}
		
		String nome = request.getParameter("nome");
		String proprietario = request.getParameter("proprietario");
		boolean isOk = dao.deleteScheda(nome,proprietario);

		if (isOk) {
			response.setStatus(204); 
		} else {
			response.setStatus(500); 
		}
	}
	
}