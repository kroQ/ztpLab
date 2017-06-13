package pl.javastart.weekop.controller;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.javastart.weekop.dao.DiscoveryDAOImpl;
import pl.javastart.weekop.model.Discovery;
import pl.javastart.weekop.service.DiscoveryService;

@WebServlet("/admin")
public class AdminController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		saveDiscoveriesInRequest(request);
		request.getRequestDispatcher("WEB-INF/admin.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String option = request.getParameter("option");
		DiscoveryService discoveryService = new DiscoveryService();
		DiscoveryDAOImpl dao = new DiscoveryDAOImpl();
		String operation = null;
		Boolean result = false;
		System.out.println(request.getAttribute(option));
		if("delete".equals(option)){
			System.out.println("jestem w delete!");
			discoveryService.deleteDiscovery(Long.valueOf(request.getParameter("id")));
			System.out.println("tamto usunelo, teraz DAO");
			operation = "delete";
			result=true;
			dao.delete(Long.valueOf(request.getParameter("id")));	

		}
		if("edit".equals(option)){
			System.out.println("jestem w edit!");
			discoveryService.updateDiscovery2(request.getParameter("id"), 
					request.getParameter("inputName"), 
					request.getParameter("inputDescription"));
			System.out.println("teraz DAO edit");
			operation = "edit";
			result=true;
			dao.update2(request.getParameter("id"), 
					request.getParameter("inputName"), 
					request.getParameter("inputDescription"));
		}
		if(result){
		request.setAttribute("option", operation);
		System.out.println(request.getAttribute(operation));
		saveDiscoveriesInRequest(request);
		request.getRequestDispatcher("WEB-INF/admin.jsp").forward(request, response);
		}
	}

	private void saveDiscoveriesInRequest(HttpServletRequest request) {
		DiscoveryService discoveryService = new DiscoveryService();
		List<Discovery> allDiscoveries = discoveryService.getAllDiscoveries(new Comparator<Discovery>() {
			// more votes = higher
			@Override
			public int compare(Discovery d1, Discovery d2) {
				int d1Vote = d1.getUpVote() - d1.getDownVote();
				int d2Vote = d2.getUpVote() - d2.getDownVote();
				if (d1Vote < d2Vote) {
					return 1;
				} else if (d1Vote > d2Vote) {
					return -1;
				}
				return 0;
			}
		});
		request.setAttribute("discoveries", allDiscoveries);
	}
}