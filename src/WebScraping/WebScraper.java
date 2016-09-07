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
    	URL url = new URL("http://www.nitt.edu/");
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
      
      // get all links in page
      Elements links = doc.select("a[href]");
      try (Writer writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream("links.txt"), "utf-8"))) 
      {
              for (Element link : links) 
      {
        // get the value from the href attribute
        writer.write("\nlink: " + link.attr("href") + "\n");
        writer.write("\ntext: " + link.text() + "\n" );
      }
      }
      /*for (Element link : links) {
        // get the value from the href attribute
        System.out.println("\nlink: " + link.attr("href"));
        System.out.println("text: " + link.text());
      } */
    } catch (IOException e) {
    e.printStackTrace();
    }
  }
}