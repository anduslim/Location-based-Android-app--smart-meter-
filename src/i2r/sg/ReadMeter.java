package i2r.sg;


import i2r.sg.db.DBCustomerAdapter;
import i2r.sg.db.DBUserAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ReadMeter extends MapActivity  {
	
    private static final double defaultLatitude = Double.parseDouble("1.29909583");
    private static final double defaultLongitude = Double.parseDouble("103.78715148");
	public static final int SEARCH_RADIUS = 500; //metres
	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATE = 1000; // in Milliseconds
	private static final long POINT_RADIUS = 1000; // in Meters
	private static final NumberFormat nf = new DecimalFormat("##.########");
	
	private LocationManager locationManager;
	private MapView mMapView;
	private MapController mMapController;
	
	private TextView txt_lat, txt_long;
	private String provider;
	List<Overlay> mapOverlays;
	//MyLocationOverlay mylocationOverlay;
	MyOwnLocationOverlay myLocationOverlay;
	Location currLocation;
	private boolean isFound;
	
	/**************** LocationListener ****************/
	MyLocationListener locListener = new MyLocationListener();
	
    public class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			txt_lat.setText(String.valueOf(nf.format(location.getLatitude())));
			txt_long.setText(String.valueOf(nf.format(location.getLongitude())));
			GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
			currLocation = location;
	    	displayResults();
//			mylocationOverlay = new MyLocationOL(currLocation);
//			mapOverlays.add(mylocationOverlay);
			mMapController.animateTo(point);
			mMapView.invalidate();
		}

		@Override
		public void onProviderDisabled(String provider) {
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(ReadMeter.this, "Enabled new provider " + provider,
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Toast.makeText(ReadMeter.this, "Disabled provider " + provider,
					Toast.LENGTH_SHORT).show();
		}

    }
    
    class customer {

		private int id;
    	private GeoPoint gPoint;
    	private String name; 
    	
    	public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public GeoPoint getgPoint() {
			return gPoint;
		}

		public void setgPoint(GeoPoint gPoint) {
			this.gPoint = gPoint;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

    	
    	public customer(int myID, String myName, double myLat, double myLng) {
    		this.id = myID;
    		this.name = myName;
    		gPoint = new GeoPoint((int)(myLat * 1E6), (int) (myLng * 1E6));
    	}
    }
    ArrayList <customer> customerList = new ArrayList <customer>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.readmeter);
	        
	        txt_lat = (TextView) findViewById(R.id.tv_lat);
	        txt_long = (TextView) findViewById(R.id.tv_long);
	        mMapView = (MapView) findViewById( R.id.myMapView );
	        mMapView.setClickable( true );
	        mMapView.setStreetView( false );
	        mMapView.setSatellite( false );
	        
	        /* Collect the zoomcontrols and place them */
	        mMapView.setBuiltInZoomControls( true );
	        mMapController = this.mMapView.getController();
	        mMapController.setZoom(14);
	        
	        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        
	        Criteria criteria = new Criteria();
			provider = locationManager.getBestProvider(criteria, false);
			currLocation = locationManager.getLastKnownLocation(provider);
			
	        locationManager.requestLocationUpdates(provider, 
					0, 
					0, 
					locListener
	        );
			
			// Initialize the location fields
			if (currLocation != null) {
				System.out.println("Provider " + provider + " has been selected.");
				int lat = (int) (currLocation.getLatitude());
				int lng = (int) (currLocation.getLongitude());
				txt_lat.setText(String.valueOf(lat));
				txt_long.setText(String.valueOf(lng));
			}
			
			//Log.d( "myLocation", "location:" + currLocation + " lat:" + currLocation.getLatitude() + " long:" + currLocation.getLongitude() );
//			mapOverlays = mMapView.getOverlays();
//			mylocationOverlay = new MyLocationOverlay(this, mMapView);//new MyLocationOL(currLocation);
//			mylocationOverlay.enableMyLocation();
//			mapOverlays.add(mylocationOverlay);
			initLocationOverlay();
			mMapView.invalidate();


    }
    
    void initLocationOverlay() {
    	
    	mMapView.getOverlays().clear();
    	mapOverlays = mMapView.getOverlays();

    	myLocationOverlay = new MyOwnLocationOverlay(ReadMeter.this, mMapView);
    	//myLocationOverlay.setMeters(Integer.parseInt(txtRadius.getText().toString()));
    	myLocationOverlay.enableCompass();
    	myLocationOverlay.enableMyLocation();
    	myLocationOverlay.runOnFirstFix(new Runnable() {
	    	public void run() {
	    		mMapController.animateTo(myLocationOverlay.getMyLocation());
	    	}
    	});
    	mMapView.getOverlays().add(myLocationOverlay);

    	displayResults();
    	
    }
    
    private void displayResults() {
    	
    	double [] myLatLongLimits;
    	
    	isFound = false;
    	
    	mapOverlays = mMapView.getOverlays();
    	int count =0;
    	for (Iterator<Overlay> itr = mapOverlays.iterator(); itr.hasNext();count++)
    	{
    		Overlay temp = itr.next();
    		Log.d("iterator overlay", count + " overlay:" + temp);
    		if (temp != myLocationOverlay)
    		{
    			Log.d("iterator overlay", count + " remove overlay:" + temp);
    			itr.remove();
    		}
    		else
    			Log.d("iterator overlay", count + " mylocationoverlay:" + temp);
    	}

    	Drawable drawable = getResources().getDrawable(R.drawable.home);
    	MeterOverlay itemizedOverlay = new MeterOverlay(drawable, mMapView);

    	Location gpsLocation = new Location("current location");
    	if (currLocation != null)
    		gpsLocation = currLocation;
    	else
    	{
    		double lat, lng;
	    	GeoPoint currentLoc = myLocationOverlay.getMyLocation();
	    	if (currentLoc != null) {
	    		lat = (double) (currentLoc.getLatitudeE6() / 1000000.0);
	    		lng = (double) (currentLoc.getLongitudeE6() / 1000000.0);
	    	}
	    	else
	    	{
	    		lat = defaultLatitude;
	    		lng = defaultLongitude;
	    	}
	    	gpsLocation.setLatitude(lat);
	    	gpsLocation.setLongitude(lng);
    	}
    	
    	//Get box query on customer's lat and long 
    	myLatLongLimits = getBoundingBox(gpsLocation.getLatitude(), gpsLocation.getLongitude(), SEARCH_RADIUS);
    	
		DBCustomerAdapter dbCustomer = new DBCustomerAdapter(ReadMeter.this);
		dbCustomer.open();
		Cursor returnQuery = dbCustomer.getCustomerWithinBox(myLatLongLimits[0], myLatLongLimits[1], myLatLongLimits[2], myLatLongLimits[3]);
		
		if (returnQuery.moveToFirst()) {
			do {
				customerList.add(new customer(returnQuery.getInt(returnQuery.getColumnIndex(DBCustomerAdapter.KEY_CUSTOMERID)),
								returnQuery.getString(returnQuery.getColumnIndex(DBCustomerAdapter.KEY_CUSTOMERNAME)),
								returnQuery.getDouble(returnQuery.getColumnIndex(DBCustomerAdapter.KEY_LATITUDE)),
								returnQuery.getDouble(returnQuery.getColumnIndex(DBCustomerAdapter.KEY_LONGITUDE))
				));
			} while (returnQuery.moveToNext());
		}
		dbCustomer.close();
   
        for(customer cust : customerList) {
	         // Create a location because Location has a distanceTo method that we can
	         // use for buffering. Notice that distanceTo calculate distance in meter

	    	// Get our current gps point and use it to create a location
	    	Location pointLocation = new Location("point");
	    	pointLocation.setLatitude(cust.getgPoint().getLatitudeE6() / 1000000.0);
	    	pointLocation.setLongitude(cust.getgPoint().getLongitudeE6() / 1000000.0);
	    	
	    	// Calculate the distance between current location and point location
	    	if (gpsLocation.distanceTo(pointLocation) < SEARCH_RADIUS) {
		    	isFound = true;
		    	OverlayItem overlayitem = new OverlayItem(cust.getgPoint(), cust.getName(), "ID:" + cust.getId() + " Latitude:" + pointLocation.getLatitude() + " Longitude:" + pointLocation.getLongitude());
		    	itemizedOverlay.addOverlay(overlayitem);
	    	}
        }
    	        // If any location found, draw the placemark
        if(isFound)
        {
        	Log.d("iterator overlay", "add overlay: " + itemizedOverlay + " size:" + mapOverlays.size());
        	mapOverlays.add(itemizedOverlay);
        }
    }
    
    private double[] getBoundingBox(final double pLatitude, final
    		double pLongitude, final int pDistanceInMeters) {

        final double[] boundingBox = new double[4];

        final double latRadian = Math.toRadians(pLatitude);

        final double degLatKm = 110.574235;
        final double degLongKm = 110.572833 * Math.cos(latRadian);
        final double deltaLat = pDistanceInMeters / 1000.0 / degLatKm;
        final double deltaLong = pDistanceInMeters / 1000.0 /degLongKm;

        final double minLat = pLatitude - deltaLat;
        final double minLong = pLongitude - deltaLong;
        final double maxLat = pLatitude + deltaLat;
        final double maxLong = pLongitude + deltaLong;

        boundingBox[0] = minLat;
        boundingBox[1] = minLong;
        boundingBox[2] = maxLat;
        boundingBox[3] = maxLong;

        return boundingBox;
    } 
    
	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(provider, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, locListener);
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(locListener);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
