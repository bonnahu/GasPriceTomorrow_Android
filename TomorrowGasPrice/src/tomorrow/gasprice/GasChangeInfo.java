package tomorrow.gasprice;
/**
 * GasChangeInfo class represents a data structure
 * of the change information of gas price.
 * @author lhu
 *
 */
public class GasChangeInfo {
	public static final String CITY = "city";
	public static final String DATE = "date";
	public static final String GASOLINEPRICE = "gasolinePrice";
	public static final String GASOLINEPRICECHANGE = "gasolinePriceChange";
	public static final String DIESELPRICE = "dieselPrice";
	public static final String DIESELPRICECHANGE = "dieselPriceChange";
	private String gasolinePrice;
	private String gasolinePriceChange;
	private String dieselPrice;
	private String dieselPriceChange;
	private String date;
	// Constructor
	public GasChangeInfo(String gasolinePrice, String gasolinePriceChange,
			String dieselPrice, String dieselPriceChange, String date) {
		this.gasolinePrice = gasolinePrice;
		this.gasolinePriceChange = gasolinePriceChange;
		this.dieselPrice = dieselPrice;
		this.dieselPriceChange = dieselPriceChange;
		this.date = date;
	}
	
	public String getGasolinePrice() {
		return this.gasolinePrice;
	}
	
	public String getGasolinePriceChange() {
		return this.gasolinePriceChange;
	}
	
	public String getDieselPrice() {
		return this.dieselPrice;
	}
	
	public String getDieselPriceChange() {
		return this.dieselPriceChange;
	}
	
	public String getDate() {
		return this.date;
	}
}
