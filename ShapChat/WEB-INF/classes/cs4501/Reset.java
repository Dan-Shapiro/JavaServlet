package cs4501;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;
import java.lang.*;
import org.xml.sax.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Reset extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);

		ServletContext context = req.getSession().getServletContext();
		
		ArrayList<String> names = (ArrayList<String>)context.getAttribute("names");
		ArrayList<String> claims = (ArrayList<String>)context.getAttribute("claims");
		ArrayList<String> evidences = (ArrayList<String>)context.getAttribute("evidences");

		ArrayList<Integer> convinces = (ArrayList<Integer>)context.getAttribute("convinces");
		ArrayList<Integer> unsures = (ArrayList<Integer>)context.getAttribute("unsures");
		ArrayList<Integer> disagrees = (ArrayList<Integer>)context.getAttribute("disagrees");

		//remove all context
		context.removeAttribute("names");
		context.removeAttribute("claims");
		context.removeAttribute("evidences");
		context.removeAttribute("convinces");
		context.removeAttribute("unsures");
		context.removeAttribute("disagrees");

		try {
			FileWriter datafile = new FileWriter("C:/Users/Daniel/Desktop/product.xml", false);
			datafile.write("");
			datafile.close();
		}
		catch(IOException e) {
			log ("Error occurred while writing to file", e);
		}

		res.sendRedirect("form.jsp");
	}
}