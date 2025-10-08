package it.uniroma2.pjdm.servletdb.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniroma2.pjdm.servletdb.dao.CampagneDAO;
import it.uniroma2.pjdm.servletdb.dao.CampagneDAOJDBCImpl;
import it.uniroma2.pjdm.servletdb.entity.ListaCampagne;
import it.uniroma2.pjdm.servletdb.entity.Campagna;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class CampagneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CampagneDAO dao;

	public CampagneServlet() {
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

		System.out.println("CampagneServlet. Opening DB connection...");

		dao = new CampagneDAOJDBCImpl(ip, port, dbName, userName, password);
		
		System.out.println("CampagneServlet: ONLINE!");
	}

	@Override
	public void destroy() {
		System.out.print("CampagneServlet. Closing DB connection...");
		dao.closeConnection();
		System.out.println("CampagneServlet: OUT!");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("CampagneServlet. Invoking a doGet method.");
		PrintWriter out = response.getWriter();
		if (request.getParameter("nomeCampagna") == null) {
			if (request.getParameter("userCreatore")==null) {
				if(request.getParameter("user")==null) {
					ListaCampagne allCamps = dao.loadAllCampagne();
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(200); 
					out.print(allCamps.toJSONString());
					out.flush();
					return;
				} else {
					String user = request.getParameter("user");
					ListaCampagne allCamps = dao.loadCampagneByPartecipazione(user);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(200); 
					out.print(allCamps.toJSONString());
					out.flush();
					return;
				}
			} else {
				String userCreatore = request.getParameter("userCreatore");
				ListaCampagne allCamps = dao.loadCampagneByCreatore(userCreatore);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(200);
				out.print(allCamps.toJSONString());
				out.flush();
				return;
			}
		} else if (request.getParameter("userCreatore")!=null) {
			if(request.getParameter("like")==null) {
				String nomeCampagna = request.getParameter("nomeCampagna");
				String userCreatore = request.getParameter("userCreatore");
				Campagna camp = dao.loadCampagna(nomeCampagna, userCreatore);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(200); 
				out.print(camp.toJsonString());
				out.flush();
				return;
			} else {
				String nomeCampagna = request.getParameter("nomeCampagna");
				String userCreatore = request.getParameter("userCreatore");
				ListaCampagne allCamps = dao.loadCampagneByLike(nomeCampagna, userCreatore);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(200); 
				out.print(allCamps.toJSONString());
				out.flush();
				return;
			}
		} else {
			response.setStatus(422);
			return;
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("CampagneServlet. Invoking a doPost method...");
		response.setContentType("text/plain");
		Campagna newCampagna = null;
		if (request.getParameter("json") != null) {
			String jsonString = request.getParameter("json");
			try {
				JSONObject jsonObj = new JSONObject(jsonString);
				newCampagna = Campagna.fromJsonObject(jsonObj);
			} catch (JSONException e) {
				e.printStackTrace();
				response.setStatus(422); 
				return;
			}
		} else {
			response.setStatus(422);
			return;
		}

		int res = dao.insertCampagna(newCampagna);

		if (res > 0) {
			response.setStatus(201);
			return;
		} else {
			response.setStatus(500);
			return;
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("CampagneServlet. Invoking a doPost method...");
		response.setContentType("text/plain");
		String nomeCampagna = null;
		String userCreatore = null;
		String descr = null;
		String oldName = null;
		if (request.getParameter("nomeCampagna") != null && request.getParameter("userCreatore") != null && request.getParameter("descr") != null && request.getParameter("oldName") != null) {
			nomeCampagna = request.getParameter("nomeCampagna");
			userCreatore = request.getParameter("userCreatore");
			descr = request.getParameter("descr");
			oldName = request.getParameter("oldName");
		} else {
			response.setStatus(422); 
			return;
		}

		int res = dao.updateCampagna(nomeCampagna,userCreatore,descr,oldName);

		if (res > 0) {
			response.setStatus(201);
			return;
		} else {
			response.setStatus(500);
			return;
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("CampagneServlet. Invoking a doDelete method.");

		if (request.getParameter("nomeCampagna") == null && request.getParameter("userCreatore") == null) {
			response.setStatus(422); 
			return;
		}
		String nomeCampagna = request.getParameter("nomeCampagna");
		String userCreatore = request.getParameter("userCreatore");

		boolean isOk = dao.deleteCampagna(nomeCampagna,userCreatore);

		if (isOk) {
			response.setStatus(204); 
		} else {
			response.setStatus(500); 
		}
	}
}