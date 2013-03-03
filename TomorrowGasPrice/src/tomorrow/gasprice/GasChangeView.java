package tomorrow.gasprice;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GasChangeView extends Activity{
	private TextView mCityTextView;
	private TextView mDateTextView;
	private TextView mGasPriceTextView;
	private TextView mGasPriceChangeTextView;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.gas_change_view);
	    mCityTextView = (TextView) findViewById(R.id.cityTxtView);
	    mDateTextView = (TextView) findViewById(R.id.dateTxtView);
	    mGasPriceTextView = (TextView) findViewById(R.id.gasPriceTxtView);
	    mGasPriceChangeTextView = (TextView) findViewById(R.id.gasPriceChangeTxtView);
	    Bundle extras = getIntent().getExtras();
    	if (extras != null) {
    		mCityTextView.setText(extras.getString(GasChangeInfo.CITY));
    	    mDateTextView.setText(extras.getString(GasChangeInfo.DATE));
    		mGasPriceTextView.setText(extras.getString(GasChangeInfo.GASOLINEPRICE));
    		mGasPriceChangeTextView.setText(extras.getString(GasChangeInfo.GASOLINEPRICECHANGE));
    	}
	}
}
