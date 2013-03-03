package tomorrow.gasprice;

import tomorrow.gasprice.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * The main activity of the application.
 * Note, the main activity is specified in AndroidManifest.xml with:
 * <intent-filter>
 *   <action android:name="android.intent.action.MAIN" />
 *   <category android:name="android.intent.category.LAUNCHER" />
 * </intent-filter>
 * @author lhu
 */
public class MainActivity extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity content from a layout resource. 
        // The resource will be inflated, adding all top-level 
        // views to the activity.
        setContentView(R.layout.main);
        final CityCodeMatcher _myCityCodeMatcher =  new CityCodeMatcher();
        //In Android you can create a XML file (strings.xml) in res/values that contains data
        /*
        <string-array name="cities_array">
        <item cityCode = "1">Toronto</item>
        <item>Hamilton</item>
        <item>London</item>
        <item>Ottawa</item>
        <item>Montreal</item>
        <item>Vancouver</item>
        <item>Barrie</item>
        </string-array>
        */
        String[] arrCities = getResources().getStringArray(R.array.cities_array);
        // Finds the listview that was identified by the id attribute from the XML that was processed in onCreate(Bundle).
        ListView lvCity = (ListView) findViewById(R.id.mylist);
        // First paramenter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the View to which the data is written
        // Forth - the Array of data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, android.R.id.text1, arrCities);
        // Assign adapter to ListView
        lvCity.setAdapter(adapter);
        
        lvCity.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
        	        int position, long id) {
        	
        		//Pass parameters between two activities using Intent
        		GasChangeInfo _gasChangeInfo = getGasPriceChangeInfo( _myCityCodeMatcher.getCityCode(((TextView) view).getText().toString()));
        		Intent myIntent = new Intent(view.getContext(),GasChangeViewActivity.class);
        		myIntent.putExtra(GasChangeInfo.CITY, ((TextView) view).getText());
        		myIntent.putExtra(GasChangeInfo.DATE, _gasChangeInfo.getDate());
        		myIntent.putExtra(GasChangeInfo.GASOLINEPRICE, _gasChangeInfo.getGasolinePrice());
        		myIntent.putExtra(GasChangeInfo.GASOLINEPRICECHANGE, _gasChangeInfo.getGasolinePriceChange());
        		myIntent.putExtra(GasChangeInfo.DIESELPRICE, _gasChangeInfo.getDieselPrice());
        		myIntent.putExtra(GasChangeInfo.DIESELPRICECHANGE, _gasChangeInfo.getDieselPriceChange());
        	      
        		 // When clicked, show a toast with the TextView text
        	     //Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
        	     //    Toast.LENGTH_SHORT).show();
        		 //Toast.makeText(getApplicationContext(),  getGasPriceInfo("http://tomorrowsgaspricetoday.com/rssfeed.php?city=1").getGasolinePrice(),
      	         //Toast.LENGTH_SHORT).show();
        		// open another activity to show the gas change info
        		startActivity(myIntent);
        	}
        });	      
    }
    
    private GasChangeInfo getGasPriceChangeInfo(String cityCode) {
    	String feedUrl = getString(R.string.feedUrl)+cityCode;
    	DomFeedParser domFeedParser = new DomFeedParser(feedUrl);
    	return domFeedParser.parse();
    	
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasprice_menu, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        	case R.id.find_gas_station:
        	findGasStation();
            return true;
	        case R.id.show_gas_price:
	        	showGasPrice();
	            return true;
	        case R.id.help:
	            //showHelp();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }
    
    private void findGasStation(){
    	Intent myIntent = new Intent(MainActivity.this,MapViewActivity.class);
    	startActivity(myIntent);
    }
    
    private void showGasPrice(){
    	new AlertDialog.Builder(this)
        .setTitle("Show Gas")
        .setMessage("This function will be provided soon!").show();
    }
    
    private void showHelp(){
    	
    }
}