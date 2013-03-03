package tomorrow.gasprice;

import java.util.Properties;
/**
 * This class create a mapping between the
 * name of the cities and the actual code
 * which is used in the url
 * @author lhu
 *
 */
public class CityCodeMatcher {
	private Properties mapCity_Code;
	public CityCodeMatcher(){
		mapCity_Code= new Properties();
		mapCity_Code.put("Toronto", "1");
		mapCity_Code.put("Hamilton", "16");
		mapCity_Code.put("Montreal", "3");
		mapCity_Code.put("Ottawa", "2");
		mapCity_Code.put("Barrie", "20");
		mapCity_Code.put("Kingston", "17");
		mapCity_Code.put("Peterborough", "22");
		mapCity_Code.put("Guelph-Cambridge", "19");
		mapCity_Code.put("Kitchener-Waterloo", "15");
		mapCity_Code.put("Calgary", "10");
		mapCity_Code.put("Vancouver", "13");
		mapCity_Code.put("Winnipeg", "14");
		mapCity_Code.put("Thunder Bay", "18");
		mapCity_Code.put("London", "21");		
	}
	/**
	 * Return the citycode of the input city
	 * @param _city
	 * @return
	 */
	public String getCityCode(String _city){
		return mapCity_Code.getProperty(_city);
	}
}
