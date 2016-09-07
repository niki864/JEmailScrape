package WebScraping;

import java.io.*;
import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 
public class WebScraper {
 
  public static void main(String[] args) {
    try {
      // fetch the document over HTTP
    	URL url = new URL("http://wagner.nyu.edu/faculty/directory"); //INSERT LINK TO SCRAPE EMAIL ID FROM
    	HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
    	String line = null;
    	StringBuilder tmp = new StringBuilder();
    	BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
    	while ((line = in.readLine()) != null) {
    	  tmp.append(line);
    	}
    	 
    	Document doc = Jsoup.parse(tmp.toString());
      
      // get the page title
      String title = doc.title();
      System.out.println("title: " + title);
      
      // get all email links in page
      Elements links = doc.select("a[href~=(mailto:)[a-z|A-Z|0-9|.|@]+]"); //simple regex matching for email ids
      try (Writer writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream("links.txt"), "utf-8"))) // writes (or creates if not found) to file links.txt in working directory
      {
              for (Element link : links) 
      {
        //Write email ids to file line by line
        writer.write("\nEmail: " + link.text() + "\n" );
      }
      }
    } catch (IOException e) {
    e.printStackTrace();
    }
  }
}