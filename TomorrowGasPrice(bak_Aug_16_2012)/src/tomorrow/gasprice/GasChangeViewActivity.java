package tomorrow.gasprice;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
/**
 * This is the Activity to display the 
 * gas change information for each city.
 * We pass data/parameter to from the main Activity to 
 * the GasChangeView Activity by using Bundle
 * @author lhu
 *
 */
public class GasChangeViewActivity extends Activity{
	// TextView controls on the Activity
	private TextView mCityTextView;
	private TextView mTomorrowDateTextView;
	private TextView mTodayDateTextView;
	private TextView mGasPriceTextView;
	private TextView mGasPriceChangeTextView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the activity content from a layout resource, gas_change_view.xml 
	    setContentView(R.layout.gas_change_view);
	    // find all the controls by it id defined in gas_change_view.xml 
	    mCityTextView = (TextView) findViewById(R.id.cityTxtView);
	    mTomorrowDateTextView = (TextView) findViewById(R.id.tomorrowDateTxtView);
	    mGasPriceTextView = (TextView) findViewById(R.id.gasPriceTxtView);
	    mGasPriceChangeTextView = (TextView) findViewById(R.id.gasPriceChangeTxtView);
	    mTodayDateTextView = (TextView) findViewById(R.id.todayDateTxtView);
	    // Get the values of the passed parameter from MainActivity
	    // and set them to the TextView controls to display
	    Bundle extras = getIntent().getExtras();
    	if (extras != null) {
    		mCityTextView.setText(extras.getString(GasChangeInfo.CITY));
    	    mTomorrowDateTextView.setText("Tomorrow: " + extras.getString(GasChangeInfo.DATE));
    		mGasPriceTextView.setText("Price: " + extras.getString(GasChangeInfo.GASOLINEPRICE) + " Cents/Litre");
    		mGasPriceChangeTextView.setText("Change: " + extras.getString(GasChangeInfo.GASOLINEPRICECHANGE)+ " Cents/Litre");
    		mTodayDateTextView.setText("Today: " + extras.getString(GasChangeInfo.DATE));
    	}
	}
}
