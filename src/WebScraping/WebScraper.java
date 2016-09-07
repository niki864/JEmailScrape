package WebScraping;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 
public class WebScraper {
 
  public static void main(String[] args) {
    try {
      // fetch the document over HTTP
    	URL url = new URL("http://www.cse.ust.hk/~quan/"); //INSERT LINK TO SCRAPE EMAIL ID FROM
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
      // Now to find emails not given nicely and tucked away instead 
      Elements paras = doc.select("p");
      try (Writer writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream("hiddenlinks.txt"), "utf-8"))) // writes (or creates if not found) to file links.txt in working directory
      {
              for (Element para : paras) 
      {
        //Write email ids to file line by line
       String stringToSearch = para.text();
       //This pattern matches all ids of the form something at something dot something
       Pattern p = Pattern.compile("([a-z|A-Z|0-9|_]+)\\s?(at|AT|@)\\s?([a-z|A-Z|0-9]+)\\s?(dot|\\.|DOT)\\s?([a-z|A-Z|0-9]+)\\s?(dot|\\.|DOT)?\\s?([a-z|A-Z|0-9]+)?");
       Matcher m = p.matcher(stringToSearch);
       if (m.find())
       {
         // get the three groups we were looking for
         String group1 = m.group(1);
         String group2 = m.group(3);
         String group3 = m.group(5);
         String group4 = m.group(7);
         if(group4.equals(null)||group4.equals(" ")){	 
        	 writer.write("\nEmail: " + group1 +"@"+ group2 + "." + group3 +"\n" );
         }
         else{
        	 writer.write("\nEmail: " + group1 +"@"+ group2 + "." + group3 + "." + group4 +"\n" );
         }
       }
      }
      }
      
    } catch (IOException e) {
    e.printStackTrace();
    }
  }
}