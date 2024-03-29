package tomorrow.gasprice;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class DomFeedParser extends BaseFeedParser {
	/**
	 * Constructor with url as the parameter
	 * @param feedUrl
	 */
	protected DomFeedParser(String feedUrl) {
		// invoke the constructor of the superclass
		super(feedUrl);
	}
    
	/**
     *  <item>
     *  <guid isPermaLink="false">http://tomorrowsgaspricetoday.com/7879</guid>
     *  <title>Tuesday, April 26, 2011 </title>
     *  <description> Gasoline: 135.9 Cents/Litre :: Change: + 0.6 Cents/Litre || Diesel: 129.9 Cents/Litre :: Change: n/c Cents/Litre || </description>
     *                Gasoline: 130.3 Cents/Litre :: Change: - 0.5 Cents/Litre || Supreme: 145.7 Cents/Litre :: Change: - 0.60000000000002 Cents/Litre ||
     *  <link>http://tomorrowsgaspricetoday.com</link>
     *  </item> 
     *  
     */
	public GasChangeInfo parse() {
		GasChangeInfo gasInfo=null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			//
			Document dom = builder.parse(this.getInputStream());
			Element eleItem = (Element)dom.getElementsByTagName(ITEM).item(0);
    	    Element eleTitle = (Element)eleItem.getElementsByTagName(TITLE).item(0);
    	    Element eleDescription = (Element)eleItem.getElementsByTagName(DESCRIPTION).item(0);
    	    String dateInfo = eleTitle.getFirstChild().getNodeValue(); 
    	    String wholeInfo = eleDescription.getFirstChild().getNodeValue();  
    	
	    	String[] wholeInfo_Arr = StringUtility.split(wholeInfo, "||");
	    	String gas_Info = wholeInfo_Arr[0];
	    	String diesel_Info = wholeInfo_Arr[1];
	    	String[] gasInfo_Arr = StringUtility.split(gas_Info, "::");
	    	String gasInfo_Price = StringUtility.replace(StringUtility.replace(gasInfo_Arr[0], "Gasoline:",""), "Cents/Litre","");
	    	//String gasInfo_Change = gasInfo_Arr[1].replace("Change:","").replace("Cents/Litre","");
	    	String gasInfo_Change = StringUtility.replace(StringUtility.replace(gasInfo_Arr[1], "Change:",""), "Cents/Litre","");
	    	
	    	String[] dieselInfo_Arr = StringUtility.split(diesel_Info, "::");
	    	//String dieselInfo_Price = dieselInfo_Arr[0].replace("Diesel:","").replace("Cents/Litre","");
	    	String dieselInfo_Price = StringUtility.replace(StringUtility.replace(dieselInfo_Arr[0], "Diesel:",""), "Cents/Litre","");
	    	//String dieselInfo_Change = dieselInfo_Arr[1].replace("Change:","").replace("Cents/Litre","");
	    	String dieselInfo_Change = StringUtility.replace(StringUtility.replace(dieselInfo_Arr[1], "Change:",""), "Cents/Litre","");
	    	//errorDialog(dieselInfo_Change);
	    	gasInfo = new GasChangeInfo(gasInfo_Price, gasInfo_Change, 
	    			                      dieselInfo_Price, dieselInfo_Change, dateInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
    	return gasInfo;  
	}
}
