package tomorrow.gasprice;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
/**
 * BaseFeedParser is the base class of 
 * DomFeedParser. It implements the function
 * getInputStream, which takes the url of the
 * rssfeed, set up a connection and get the 
 * inputstream back. It leaves the parsing
 * work to its subclass, e.g., DomFeedParser
 * 
 * @author lhu
 *
 */
public abstract class BaseFeedParser implements FeedParser {
	//names of the XML tags
	static final  String DESCRIPTION = "description";
	static final  String TITLE = "title";
	static final  String ITEM = "item";
	// The URL object to set up the connection
	private final URL feedUrl;
	/**
	 * Constructor with the url as parameter
	 * @param feedUrl
	 */
	protected BaseFeedParser(String feedUrl){
		try {
			this.feedUrl = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * Open a conncection and get the inputStream 
	 * of XML back
	 * @return
	 */
	protected InputStream getInputStream() {
		try {
			URLConnection urlConn =  feedUrl.openConnection();
			return urlConn.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
