package it.uniroma2.pjdm.servletdb.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniroma2.pjdm.servletdb.dao.NotificheDAO;
import it.uniroma2.pjdm.servletdb.dao.NotificheDAOJDBCImpl;
import it.uniroma2.pjdm.servletdb.entity.ListaNotifiche;
import it.uniroma2.pjdm.servletdb.entity.Notifica;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class NotificheServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private NotificheDAO dao;

	public NotificheServlet() {
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

		System.out.println("NotificheServlet. Opening DB connection...");

		dao = new NotificheDAOJDBCImpl(ip, port, dbName, userName, password);
		
		System.out.println("CampagneServlet: ONLINE!");
	}

	@Override
	public void destroy() {
		System.out.print("NotificheServlet. Closing DB connection...");
		dao.closeConnection();
		System.out.println("NotificheServlet: OUT!");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("NotificheServlet. Invoking a doGet method.");
		PrintWriter out = response.getWriter();
		if(request.getParameter("mittente")==null && request.getParameter("destinatario")==null && request.getParameter("nomeCampagna")==null && request.getParameter("tipo")==null) {
			ListaNotifiche allNot = dao.loadAllNotifiche();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200); 
			out.print(allNot.toJSONString());
			out.flush();
			return;
		} else if(request.getParameter("mittente")!=null && request.getParameter("destinatario")==null && request.getParameter("nomeCampagna")==null && request.getParameter("tipo")!=null) {
			String mittente = request.getParameter("mittente");
			int tipo = Integer.valueOf(request.getParameter("tipo"));
			ListaNotifiche allNot = dao.loadNotificheByMittente(mittente,tipo);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200); 
			out.print(allNot.toJSONString());
			out.flush();
			return;
		} else if(request.getParameter("mittente")==null && request.getParameter("destinatario")!=null && request.getParameter("nomeCampagna")==null && request.getParameter("tipo")!=null) {
			String destinatario = request.getParameter("destinatario");
			int tipo = Integer.valueOf(request.getParameter("tipo"));
			ListaNotifiche allNot = dao.loadNotificheByDestinatario(destinatario,tipo);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200); 
			out.print(allNot.toJSONString());
			out.flush();
			return;
		} else if(request.getParameter("mittente")!=null && request.getParameter("destinatario")!=null && request.getParameter("nomeCampagna")!=null && request.getParameter("tipo")!=null) {
			String mittente = request.getParameter("mittente");
			String destinatario = request.getParameter("destinatario");
			String nomeCampagna = request.getParameter("nomeCampagna");
			int tipo = Integer.valueOf(request.getParameter("tipo"));
			Notifica res = dao.loadNotifica(mittente, destinatario, nomeCampagna, tipo);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(200); 
			out.print(res.toJsonString());
			out.flush();
			return;
		} else {
			response.setStatus(422); 
			out.flush();
			return;
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("NotificheServlet. Invoking a doPost method...");
		response.setContentType("text/plain");
		Notifica newNot = null;
		if (request.getParameter("json") != null) {
			String jsonString = request.getParameter("json");
			try {
				JSONObject jsonObj = new JSONObject(jsonString);
				if(jsonString.contains("user")){
					try {
						String user = jsonObj.getString("user");
						String nomeCampagna = jsonObj.getString("nomeCampagna");
						String userCreatore = jsonObj.getString("userCreatore");
						String nomeScheda = jsonObj.getString("nomeScheda");
						int res = dao.joinCampagna(user,nomeCampagna,userCreatore,nomeScheda);
						
						if (res > 0) {
							response.setStatus(201);
							return;
						} else {
							response.setStatus(500);
							return;
						}
					} catch (JSONException es) {
						es.printStackTrace();
						response.setStatus(422);
						return;
					}
				} else {
					newNot = Notifica.fromJsonObject(jsonObj);
					int res = dao.insertNotifica(newNot);
					if (res > 0) {
						response.setStatus(201);
						return;
					} else {
						response.setStatus(500);
						return;
					}		
				} 
			} catch (JSONException e) {
				e.printStackTrace();
				response.setStatus(422);
				return;
			}
		} else {
			response.setStatus(422); 
			return;
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("NotificheServlet. Invoking a doDelete method.");
		if(request.getParameter("mittente")!=null && request.getParameter("destinatario")!=null && request.getParameter("nomeCampagna") != null && request.getParameter("tipo") != null) {
			String mittente = request.getParameter("mittente");
			String destinatario = request.getParameter("destinatario");
			String nomeCampagna = request.getParameter("nomeCampagna");
			int tipo = Integer.valueOf(request.getParameter("tipo"));
			boolean isOk = dao.deleteNotifica(mittente,destinatario,nomeCampagna,tipo);
			if (isOk) {
				response.setStatus(204); 
			} else {
				response.setStatus(500); 
			}
		} else {
			response.setStatus(422);
		}
	}
}