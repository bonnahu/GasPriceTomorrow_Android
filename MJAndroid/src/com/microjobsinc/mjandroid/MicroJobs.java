package com.microjobsinc.mjandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import com.microjobsinc.mjandroid.MicroJobsDatabase.JobDetailCursor;
import com.microjobsinc.mjandroid.MicroJobsDatabase.JobsCursor;
import com.microjobsinc.mjandroid.MicroJobsDatabase.WorkerCursor;
/**
 * MicroJobs
 * Since we want to display a Map in this appliation, so we
 * extends from a MapActivity
 */
public class MicroJobs extends MapActivity {
    /**
     * Application-wide log tag
     */
	static final String LOG_TAG = "MicroJobs";

	/** 
	 * Database cursor to access user information
	 */
	private WorkerCursor worker;
	
    /**
     * MJJobsOverlay
     */
    private class MJJobsOverlay extends ItemizedOverlay<OverlayItem> {

        /**
         * @param marker the push-pin
         */
        public MJJobsOverlay(Drawable marker) {
            super(marker);
            populate();
        }

        /**
         * @see com.google.android.maps.ItemizedOverlay#size()
         */
        @Override
        public int size() {
        	int size = db.getJobsCount();
        	return size;
        }

        /**
         * @see com.google.android.maps.ItemizedOverlay#createItem(int)
         */
        @Override
        protected OverlayItem createItem(int i) {
        	JobDetailCursor c = db.getJobDetails(i+1);
        	String contactName = c.getColContactName();
        	String description = c.getColDescription();
        	int lat = (int) c.getColLatitude();
        	int lon = (int) c.getColLongitude();
        	return new OverlayItem(new GeoPoint(lat, lon), contactName, description);
        }

        /**
         * React to tap events on Map by showing an appropriate detail activity
         *
         * @see com.google.android.maps.ItemizedOverlay#onTap(com.google.android.maps.GeoPoint, com.google.android.maps.MapView)
         */
        @Override
        public boolean onTap(GeoPoint p, MapView mvMap1) {
            long lat = p.getLatitudeE6();
            long lon = p.getLongitudeE6();
          
            long rowid = -1;
            JobsCursor c = db.getJobs(JobsCursor.SortBy.title);
            for( int i=0; i<c.getCount(); i++){
            	if (Math.abs(c.getColLatitude()-lat)<1000 && Math.abs(c.getColLongitude()-lon)<1000){
            		rowid = c.getColJobsId();
            		break;
            	} else {
            		c.moveToNext();
            	}
            }
            
            if (0 > rowid) { return false; }
            
            Bundle b = new Bundle();
            b.putLong("_id", rowid);
            Intent i = new Intent(MicroJobs.this, MicroJobsDetail.class);
            i.putExtras(b);
            startActivity(i);

            return true;
        }
    }


    MapView mvMap;
    MicroJobsDatabase db;
    MyLocationOverlay mMyLocationOverlay;
    int latitude, longitude;

    /**
     * Called when the activity is first created.
     *
     * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // start trace
        //Debug.startMethodTracing("x");
        Debug.startMethodTracing("/data/data/com.microjobsinc.mjandroid/x.trace");
        setContentView(R.layout.main);

        db = new MicroJobsDatabase(this);

        // Get current position
        // The LocationManager is a special class that 
        // Android instantiates for you, and you can retrieve the
        // instance for your application through the call to
        // getSystemService.
        final Location myLocation
            = getCurrentLocation((LocationManager) getSystemService(Context.LOCATION_SERVICE));

        Spinner spnLocations = (Spinner) findViewById(R.id.spnLocations);
        
        // Connect the Java MapView to the attributes defined for it in
        // main.xml, and
        mvMap = (MapView) findViewById(R.id.mapmain);
        // Get the map controller
        final MapController mc = mvMap.getController();
        // Create a LocationOverlay that will build and draw the Map in our
        // MapView when we want to view a map of our local area.
        mMyLocationOverlay = new MyLocationOverlay(this, mvMap);
        mMyLocationOverlay.enableMyLocation();
        // The first thing we do with mMyLocationOverlay is define
        // a method that Android will call when we receive our first
        // location fix from the location provider
        mMyLocationOverlay.runOnFirstFix(
            new Runnable() {
                public void run() {
                	//move the map to the current location (given by
                	// mMyLocationOverlay.getMyLocation()
                    mc.animateTo(mMyLocationOverlay.getMyLocation());
                    mc.setZoom(16);
                }
            });
        // Identify a marker to use on mMyLocationOverlay to mark
        // available jobs
        Drawable marker = getResources().getDrawable(R.drawable.android_tiny_image);
        marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
        //add the marker overlay to the list of overlays for the MapView mvMap.
        mvMap.getOverlays().add(new MJJobsOverlay(marker));
        // set some initial attributes for mvMap
        // We want users to be able to click(tap) on 
        // a job to display more detail about this job
        mvMap.setClickable(true);
        // This method is inherited from android.view.View
        // it enables the standard map functions(zooming, panning)
        mvMap.setEnabled(true);
        // Setting this flag adds a satellite view
        mvMap.setSatellite(false);
        mvMap.setTraffic(false);
        mvMap.setStreetView(false);
        
        // start out with a general zoom
        mc.setZoom(16);
        mvMap.invalidate();

        // Create a button click listener for the List Jobs button.
        Button btnList = (Button) findViewById(R.id.btnShowList);
        btnList.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MicroJobs.this.getApplication(), MicroJobsList.class);
                startActivity(intent);
            }
        });

        // A list of favorite locations that the Spinner will
        // display and the user can select
        List<String> lsLocations = new ArrayList<String>();
        // Load a HashMap with locations and positions
        final HashMap<String, GeoPoint> hmLocations = new HashMap<String, GeoPoint>();
        hmLocations.put("Current Location", new GeoPoint(latitude, longitude));
        lsLocations.add("Current Location");

        // Add favorite locations from this user's record in workers table
        worker = db.getWorker();
        hmLocations.put(worker.getColLoc1Name(), new GeoPoint((int)worker.getColLoc1Lat(), (int)worker.getColLoc1Long()));
        lsLocations.add(worker.getColLoc1Name());
        hmLocations.put(worker.getColLoc2Name(), new GeoPoint((int)worker.getColLoc2Lat(), (int)worker.getColLoc2Long()));
        lsLocations.add(worker.getColLoc2Name());
        hmLocations.put(worker.getColLoc3Name(), new GeoPoint((int)worker.getColLoc3Lat(), (int)worker.getColLoc3Long()));
        lsLocations.add(worker.getColLoc3Name());
        // Spinner views require an ArrayAdapter to feed them the list, 
        // attaching the list of locations to the ArrayAdapter
        ArrayAdapter<String> aspnLocations
            = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lsLocations);
        // Provide the Spinner with the drop-down layout necessary for the user
        // to display the whole list of locations.
        aspnLocations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLocations.setAdapter(aspnLocations);

        // Setup a callback for the spinner
        spnLocations.setOnItemSelectedListener(
            new OnItemSelectedListener() {
                public void onNothingSelected(AdapterView<?> arg0) { }
                // Enables the appropriate action when the user clicks on an item 
                public void onItemSelected(AdapterView<?> parent, View v, int position, long id)  {
                    TextView vt = (TextView) v;
                    if ("Current Location".equals(vt.getText())) {
                    	mMyLocationOverlay.enableMyLocation();
                    	try {
                    		mc.animateTo(mMyLocationOverlay.getMyLocation());
                    	}
                    	catch (Exception e) {
                    		Log.i("MicroJobs", "Unable to animate map", e);
                    	}
                    	mvMap.invalidate();
                    } else {
                    	mMyLocationOverlay.disableMyLocation();
                        mc.animateTo(hmLocations.get(vt.getText()));
                    }
                    mvMap.invalidate();
                }
            });
    }

    protected GeoPoint setCurrentGeoPoint(){
    	return mMyLocationOverlay.getMyLocation();
    }
    
    /**
     * At that time, Android calls the onPause() routine
     * in the calling Activity so it can prepare itself to go into hibernation. At this point in
     * MicroJobs.java (or just about any MapActivity that uses location updates), we want to
     * turn off location updates.
     * @see com.google.android.maps.MapActivity#onPause()
     */
    @Override
    public void onPause() {
        super.onPause();
        mMyLocationOverlay.disableMyLocation();
    }

    // stop tracing when application ends
    @Override
    public void onDestroy() {
        super.onDestroy();
        Debug.stopMethodTracing();
    }

    /**
     * @see com.google.android.maps.MapActivity#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        mMyLocationOverlay.enableMyLocation();
    }

    /**
     * Setup menus for this page
     *
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean supRetVal = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, 0, Menu.NONE, getString(R.string.map_menu_zoom_in));
        menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.map_menu_zoom_out));
        menu.add(Menu.NONE, 2, Menu.NONE, getString(R.string.map_menu_set_satellite));
        menu.add(Menu.NONE, 3, Menu.NONE, getString(R.string.map_menu_set_map));
        menu.add(Menu.NONE, 4, Menu.NONE, getString(R.string.map_menu_set_traffic));
        menu.add(Menu.NONE, 5, Menu.NONE, getString(R.string.map_menu_show_list));
        return supRetVal;
    }

    /**
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                // Zoom in
                zoomIn();
                return true;
            case 1:
                // Zoom out
                zoomOut();
                return true;
            case 2:
                // Toggle satellite views
                mvMap.setSatellite(!mvMap.isSatellite());
                return true;
            case 3:
                // Toggle street views
                mvMap.setStreetView(!mvMap.isStreetView());
                return true;
            case 4:
                // Toggle traffic views
                mvMap.setTraffic(!mvMap.isTraffic());
                return true;
            case 5:
                // Show the job list activity
                startActivity(new Intent(MicroJobs.this, MicroJobsList.class));
                return true;
        }
        return false;
    }

    /**
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP: // zoom in
                zoomIn();
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN: // zoom out
                zoomOut();
                return true;
            case KeyEvent.KEYCODE_BACK: // go back (meaning exit the app)
                finish();
                return true;
            default:
                return false;
        }
    }

    /**
     * Required method to indicate whether we display routes
     */
    @Override
    protected boolean isRouteDisplayed() { return false; }

    /**
     * Zoom in on the map
     */
    private void zoomIn() {
        mvMap.getController().setZoom(mvMap.getZoomLevel() + 1);
    }

    /**
     * Zoom out on the map, but not past level 10
     */
    private void zoomOut() {
        int zoom = mvMap.getZoomLevel() - 1;
        if (zoom < 5) { zoom = 5; }
        mvMap.getController().setZoom(zoom);
    }

    /**
     * @return the current location
     */
    private Location getCurrentLocation(LocationManager lm) {
         Location l = lm.getLastKnownLocation("gps");
        if (null != l) { return l; }

        // getLastKnownLocation returns null if loc provider is not enabled
        l = new Location("gps");
        l.setLatitude(42.352299);
        l.setLatitude(-71.063979);

        return l;
    }

}
