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

public class FormHandler extends HttpServlet {
	public void sortLists(ServletContext context) {
   		ArrayList<String> names = (ArrayList<String>)context.getAttribute("names");
		ArrayList<String> claims = (ArrayList<String>)context.getAttribute("claims");
		ArrayList<String> evidences = (ArrayList<String>)context.getAttribute("evidences");
		ArrayList<Integer> convinces = (ArrayList<Integer>)context.getAttribute("convinces");
		ArrayList<Integer> unsures = (ArrayList<Integer>)context.getAttribute("unsures");
		ArrayList<Integer> disagrees = (ArrayList<Integer>)context.getAttribute("disagrees");

		ArrayList<String> newNames = new ArrayList<String>();
		ArrayList<String> newClaims = new ArrayList<String>();
		ArrayList<String> newEvidences = new ArrayList<String>();
		ArrayList<Integer> newConvinces = new ArrayList<Integer>();
		ArrayList<Integer> newUnsures = new ArrayList<Integer>();
		ArrayList<Integer> newDisagrees = new ArrayList<Integer>();

		int maxIndex = 0;
		int max = 0;
		while(names.size() > 0) {
			maxIndex = 0;
			max = 0;
			for(int i = 0; i < names.size(); i++) {
				if(convinces.get(i) > max) {
					maxIndex = i;
					max = convinces.get(i);
				}
			}

			newNames.add(names.get(maxIndex));
			newClaims.add(claims.get(maxIndex));
			newEvidences.add(evidences.get(maxIndex));
			newConvinces.add(convinces.get(maxIndex));
			newUnsures.add(unsures.get(maxIndex));
			newDisagrees.add(disagrees.get(maxIndex));

			names.remove(maxIndex);
			claims.remove(maxIndex);
			evidences.remove(maxIndex);
			convinces.remove(maxIndex);
			unsures.remove(maxIndex);
			disagrees.remove(maxIndex);
		}

		context.setAttribute("names", newNames);
		context.setAttribute("claims", newClaims);
		context.setAttribute("evidences", newEvidences);
		context.setAttribute("convinces", newConvinces);
		context.setAttribute("unsures", newUnsures);
		context.setAttribute("disagrees", newDisagrees);
	}

	public void readData(ServletContext context) throws IOException {
   		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> claims = new ArrayList<String>();
		ArrayList<String> evidences = new ArrayList<String>();
		ArrayList<Integer> convinces = new ArrayList<Integer>();
		ArrayList<Integer> unsures = new ArrayList<Integer>();
		ArrayList<Integer> disagrees = new ArrayList<Integer>();

		try {
         Document doc = create_DOM_from_file("C:/Users/Daniel/Desktop/product.xml");

         NodeList nList = doc.getElementsByTagName("block");
         for (int i = 0; i < nList.getLength(); i++) 
         {
            Node nd = nList.item(i);
            // check if nd is an XML element, get values of its attributes and children
            if (nd.getNodeType() == Node.ELEMENT_NODE) 
            {
               Element ele = (Element)nd;
                              
               // access element's value
               names.add(ele.getElementsByTagName("name").item(0).getTextContent());
               claims.add(ele.getElementsByTagName("claim").item(0).getTextContent());
               evidences.add(ele.getElementsByTagName("evidence").item(0).getTextContent());
               convinces.add(Integer.parseInt(ele.getElementsByTagName("convinces").item(0).getTextContent()));
               unsures.add(Integer.parseInt(ele.getElementsByTagName("unsures").item(0).getTextContent()));
               disagrees.add(Integer.parseInt(ele.getElementsByTagName("disagrees").item(0).getTextContent()));            
            }
         }
         context.setAttribute("names", names);
         context.setAttribute("claims", claims);
         context.setAttribute("evidences", evidences);
         context.setAttribute("convinces", convinces);
         context.setAttribute("unsures", unsures);
         context.setAttribute("disagrees", disagrees);
      } catch (Exception e) {
         e.printStackTrace();
      }
	}

	private Document create_DOM_from_file(String fname) throws Exception 
   {
      try {
         File datafile = new File(fname);
         DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();
         Document doc = dbuilder.parse(datafile);
         return doc;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   } 

	public void saveData(ServletContext context) throws IOException {
		sortLists(context);
		String message = getXML(context);
		writeToFile("C:/Users/Daniel/Desktop/product.xml", message);
	}

	private String getXML(ServletContext context) throws IOException
   {
   		ArrayList<String> names = (ArrayList<String>)context.getAttribute("names");
		ArrayList<String> claims = (ArrayList<String>)context.getAttribute("claims");
		ArrayList<String> evidences = (ArrayList<String>)context.getAttribute("evidences");
		ArrayList<Integer> convinces = (ArrayList<Integer>)context.getAttribute("convinces");
		ArrayList<Integer> unsures = (ArrayList<Integer>)context.getAttribute("unsures");
		ArrayList<Integer> disagrees = (ArrayList<Integer>)context.getAttribute("disagrees");

	    StringBuffer tempStringBuffer = new StringBuffer (4096);

	    tempStringBuffer.append("<data>\n");
	    for(int i = 0; i < names.size(); i++) {
	    	tempStringBuffer.append("  <block>\n");
	    	tempStringBuffer.append("    <name>" + names.get(i).toString() + "</name>\n");
	    	tempStringBuffer.append("    <claim>" + claims.get(i).toString() + "</claim>\n");
	    	tempStringBuffer.append("    <evidence>" + evidences.get(i).toString() + "</evidence>\n");
	    	tempStringBuffer.append("    <convinces>" + convinces.get(i).toString() + "</convinces>\n");
	    	tempStringBuffer.append("    <unsures>" + unsures.get(i).toString() + "</unsures>\n");
	    	tempStringBuffer.append("    <disagrees>" + disagrees.get(i).toString() + "</disagrees>\n");
	    	tempStringBuffer.append("  </block>\n");
	    }
	    tempStringBuffer.append("</data>");

	    return (tempStringBuffer.toString());
   }

   private void writeToFile (String fileName, String message)
   {
      try
      {
         FileWriter datafile = new FileWriter(fileName, false);
         datafile.write (message);
         datafile.write ("\n");
         datafile.close ();
      }
      catch (IOException e)
      {
         log ("Error occurred while writing to file", e);
      }
   }

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
		PrintWriter toClient = res.getWriter();

		//get form inputs
		String name;
		String pass;
		String claim = req.getParameter("claim");
		String evidence = req.getParameter("evidence");

		//get name from session if exists
		HttpSession session = req.getSession(true);
		if(session.getAttribute("name") == null) {
			name = req.getParameter("name");
			session.setAttribute("name", name);
			pass = req.getParameter("pass");
			session.setAttribute("pass", pass);
		}
		else {
			name = (String)session.getAttribute("name");
			pass = (String)session.getAttribute("pass");
		}
		//set session
		session.setAttribute("claim", claim);
		session.setAttribute("evidence", evidence);

		//start context
		ServletContext context = req.getSession().getServletContext();


		readData(context);

		//create new arraylists, or get current if exists
		//set strings to inputs
		//set votings to 0
		ArrayList<String> names;
		if(context.getAttribute("names") == null) {
			names = new ArrayList<String>();
		}
		else {
			names = (ArrayList<String>)context.getAttribute("names");
		}
		names.add(name);
		context.setAttribute("names", names);

		ArrayList<String> claims;
		if(context.getAttribute("claims") == null) {
			claims = new ArrayList<String>();
		}
		else {
			claims = (ArrayList<String>)context.getAttribute("claims");
		}
		claims.add(claim);
		context.setAttribute("claims", claims);

		ArrayList<String> evidences;
		if(context.getAttribute("evidences") == null) {
			evidences = new ArrayList<String>();
		}
		else {
			evidences = (ArrayList<String>)context.getAttribute("evidences");
		}
		evidences.add(evidence);
		context.setAttribute("evidences", evidences);

		ArrayList<Integer> convinces;
		if(context.getAttribute("convinces") == null) {
			convinces = new ArrayList<Integer>();
		}
		else {
			convinces = (ArrayList<Integer>)context.getAttribute("convinces");
		}
		convinces.add(0);
		context.setAttribute("convinces", convinces);

		ArrayList<Integer> unsures;
		if(context.getAttribute("unsures") == null) {
			unsures = new ArrayList<Integer>();
		}
		else {
			unsures = (ArrayList<Integer>)context.getAttribute("unsures");
		}
		unsures.add(0);
		context.setAttribute("unsures", unsures);

		ArrayList<Integer> disagrees;
		if(context.getAttribute("disagrees") == null) {
			disagrees = new ArrayList<Integer>();
		}
		else {
			disagrees = (ArrayList<Integer>)context.getAttribute("disagrees");
		}
		disagrees.add(0);
		context.setAttribute("disagrees", disagrees);

		

		toClient.println("<!DOCTYPE html>");
		toClient.println("  <html>");
		toClient.println("    <head>");
		toClient.println("      <title>Submission</title>");
		toClient.println("      <link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">");
		toClient.println("      <script type=\"text/javascript\">");
		toClient.println("        function callServlet(vote, index) {");
		toClient.println("          document.getElementsByName(\"vote\")[index].value = vote;");
		toClient.println("          document.getElementsByName(\"myForm\")[index].submit();");
		toClient.println("        }");
		toClient.println("        function back() {");
		toClient.println("          window.location = \"http://localhost:9999/assignment6/form.jsp\"");
		toClient.println("        }");
		toClient.println("        function del(index, pass) {");
		toClient.println("          var inputPass = prompt(\"Enter password:\");");
		toClient.println("          if(inputPass != pass) {");
		toClient.println("            alert(\"Password incorrect!\");");
		toClient.println("            return;");
		toClient.println("          }");
		toClient.println("          document.getElementsByName(\"indexDelete\")[index].value = index;");
		toClient.println("          document.getElementsByName(\"myFormDelete\")[index].submit();");
		toClient.println("        }");
		toClient.println("        function reset() {");
		toClient.println("          document.getElementsByName(\"myFormReset\")[0].submit();");
		toClient.println("        }");
		toClient.println("      </script>");
		toClient.println("    </head>");
		toClient.println("    <body>");
		toClient.println("      <h1>Submission</h1>");
		toClient.println("      <p>You are logged in as " + name + "</p>");
		toClient.println("      <p>Here are the claims and evidences.  Please vote what you think below.</p>");
		for(int i = 0; i < names.size(); i++) {
			toClient.println("      <h3>Claim: " + claims.get(i) + "</h3>");
			toClient.println("      <p>Stated by " + names.get(i) +"</p>");
			toClient.println("      <p>Evidence: " + evidences.get(i) + "</p>");
			toClient.println("      <br />");
			toClient.println("      <form name=\"myForm\" method=\"POST\" action=\"http://localhost:9999/assignment6/VoteHandler\">");
			toClient.println("        <input type=\"hidden\" name=\"vote\" />");
			toClient.println("        <input type=\"hidden\" name=\"index\" value=\"" + i +"\" />");
			toClient.println("      </form>");
			toClient.println("      <div id=\"vote\">");
			if(!names.get(i).equals(name)) {
				toClient.println("        <span class=\"col\">");
				toClient.println("          <button class=\"button\" onClick=\"callServlet('convinced', " + i + ")\" style=\"background-color:green;color=white;\">Convinced</button>");
				toClient.println("        </span>");
				toClient.println("        <span class=\"col\">");
				toClient.println("          <button class=\"button\" onClick=\"callServlet('unsure', " + i + ")\" style=\"background-color:yellow;color=black;\">Unsure</button>");
				toClient.println("        </span>");
				toClient.println("        <span class=\"col\">");
				toClient.println("          <button class=\"button\" onClick=\"callServlet('disagree', " + i + ")\" style=\"background-color:red;color=white;\">Disagree</button>");
				toClient.println("        </span>");
			}
			else {
				toClient.println("      <form name=\"myFormDelete\" method=\"POST\" action=\"http://localhost:9999/assignment6/Delete\">");
				toClient.println("        <input type=\"hidden\" name=\"indexDelete\" />");
				toClient.println("      </form>");
				toClient.println("        <button class=\"button\" onClick=\"del(" + i + ", '" + session.getAttribute("pass") + "')\">Delete</button>");
			}
			toClient.println("        <hr />");
		}
		toClient.println("      <br />");
		toClient.println("      <br />");
		toClient.println("      <button class=\"button\" onClick=\"back()\">Back</button>");
		toClient.println("      <br />");
		toClient.println("      <br />");
		toClient.println("      <form name=\"myFormReset\" method=\"POST\" action=\"http://localhost:9999/assignment6/Reset\">");
		toClient.println("      </form>");
		toClient.println("      <button class=\"button\" onClick=\"reset()\">Reset</button>");
		toClient.println("      </div>");
		toClient.println("    </body>");
		toClient.println("  </html>");

		toClient.close();

		saveData(context);
	}
}