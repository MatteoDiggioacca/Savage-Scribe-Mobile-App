package it.uniroma2.pjdm.servletdb.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniroma2.pjdm.servletdb.dao.RazzeDAO;
import it.uniroma2.pjdm.servletdb.dao.RazzeDAOJDBCImpl;
import it.uniroma2.pjdm.servletdb.entity.Razza;
import it.uniroma2.pjdm.servletdb.entity.ListaRazze;


public class RazzeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private RazzeDAO dao;
	
	public RazzeServlet() {
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

		System.out.print("RazzeServlet. Opening DB connection...");

		dao = new RazzeDAOJDBCImpl(ip, port, dbName, userName, password);
		
		System.out.println("RazzeServlet: ONLINE!");
	}

	@Override
	public void destroy() {
		System.out.print("RazzeServlet. Closing DB connection...");
		dao.closeConnection();
		System.out.println("RazzeServlet: OFFLINE!");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("RazzeServlet. Invoking a doGet method.");
		PrintWriter out = response.getWriter();
		if (request.getParameter("nomeRazza") == null && request.getParameter("nomeScheda") == null && request.getParameter("userCreatore")==null) {
			ListaRazze allRazze = dao.loadAllRazze();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200); 
			out.print(allRazze.toJSONString());
			out.flush();
		return;
		} else if (request.getParameter("nomeRazza") == null && request.getParameter("nomeScheda") != null && request.getParameter("userCreatore") != null) {
			String nomeScheda = request.getParameter("nomeScheda");
			String userCreatore = request.getParameter("userCreatore");
			Razza razza = dao.loadRazzaByScheda(nomeScheda,userCreatore);
			if (razza==null) {
				response.setStatus(404);
				return;
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200); 
			out.print(razza.toJsonString());
			out.flush();
			return;	
		} else if (request.getParameter("nomeRazza") != null && request.getParameter("nomeScheda") == null && request.getParameter("userCreatore")==null) {
			String nomeRazza = request.getParameter("nomeRazza");
			Razza razza = dao.loadRazzaByNome(nomeRazza);
			if (razza==null) {
				response.setStatus(404);
				return;
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200); 
			out.print(razza.toJsonString());
			out.flush();
			return;
		} else {
			response.setStatus(422);
			return;
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("RazzeServlet. Invoking a doPost method...");
		response.setContentType("text/plain");
		Razza newRazza = null;
		if (request.getParameter("json") != null) {
			String jsonString = request.getParameter("json");
			try {
				JSONObject jsonObj = new JSONObject(jsonString);
				newRazza = Razza.fromJsonObject(jsonObj);
			} catch (JSONException e) {
				response.setStatus(422); 
				return;
			}
		} else {
			response.setStatus(422); 
			return;
		}

		int res = dao.insertRazza(newRazza);
		

		if (res > 0) {
			response.setStatus(201);
			return;
		} else {
			response.setStatus(500);
			return;
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("RazzeServlet. Invoking a doPut method...");
		response.setContentType("text/plain");
		Razza razzaToUpdate = null;
		if (request.getParameter("json") != null) {
			String jsonString = request.getParameter("json");
			try {
				JSONObject jsonObj = new JSONObject(jsonString);
				razzaToUpdate = Razza.fromJsonObject(jsonObj);
			} catch (JSONException e) {
				response.setStatus(422); 
				return;
			}
		} else {
			response.setStatus(422); 
			return;
		}
			
		int res = dao.updateRazza(razzaToUpdate);
		
		
		if(res > 0) {
			response.setStatus(200); 
			return;
		} else {
			response.setStatus(500); 
			return;
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("RazzeServlet. Invoking a doDelete method.");
		
		String nomeRazza = request.getParameter("nomeRazza");
		if (nomeRazza == null) {
			response.setStatus(422); 
			return;
		}

		boolean isOk = dao.deleteRazza(nomeRazza);

		if (isOk) {
			response.setStatus(204); 
		} else {
			response.setStatus(500); 
		}
	}
	
}