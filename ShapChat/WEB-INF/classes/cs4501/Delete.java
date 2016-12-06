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

public class Delete extends HttpServlet {
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
		int index = Integer.parseInt(req.getParameter("indexDelete"));

		HttpSession session = req.getSession(true);

		ServletContext context = req.getSession().getServletContext();

		readData(context);
		
		ArrayList<String> names = (ArrayList<String>)context.getAttribute("names");
		ArrayList<String> claims = (ArrayList<String>)context.getAttribute("claims");
		ArrayList<String> evidences = (ArrayList<String>)context.getAttribute("evidences");

		ArrayList<Integer> convinces = (ArrayList<Integer>)context.getAttribute("convinces");
		ArrayList<Integer> unsures = (ArrayList<Integer>)context.getAttribute("unsures");
		ArrayList<Integer> disagrees = (ArrayList<Integer>)context.getAttribute("disagrees");

		//remove the items at the index in the context
		names.remove(index);
		claims.remove(index);
		evidences.remove(index);
		convinces.remove(index);
		unsures.remove(index);
		disagrees.remove(index);

		//set the new context
		context.setAttribute("names", names);
		context.setAttribute("claims", claims);
		context.setAttribute("evidences", evidences);
		context.setAttribute("convinces", convinces);
		context.setAttribute("unsures", unsures);
		context.setAttribute("disagrees", disagrees);

		saveData(context);

		res.sendRedirect("form.jsp");
	}
}