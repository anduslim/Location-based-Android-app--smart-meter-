package i2r.sg;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

class MyLocationOL extends com.google.android.maps.Overlay {

    private static final double defaultLatitude = Double.parseDouble("1.29909583");
    private static final double defaultLongitude = Double.parseDouble("103.78715148");
    private static final float defaultAccuracy = 250f; // or whatever you wish it to be


    Location currentLocation; // this should be already known

    private Paint accuracyPaint;
    private Point center;
    private Point left;
    private Drawable drawable;
    private int width;
    private int height;
    
    public MyLocationOL(Location currLocation) {

    	currentLocation = currLocation;
		Log.d( "myLocation", " lat:" + currentLocation.getLatitude() + " long:" + currentLocation.getLongitude() );
    }

    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
        super.draw(canvas, mapView, shadow);

        Log.d("myLocation", "inside myLocation => draw");
        
        accuracyPaint = new Paint();
        accuracyPaint.setAntiAlias(true);
        accuracyPaint.setStrokeWidth(2.0f);

        drawable = mapView.getContext().getResources().getDrawable(R.drawable.mylocation);
        width = drawable.getIntrinsicWidth();
        height = drawable.getIntrinsicHeight();
        center = new Point();
        left = new Point();
        double latitude;
        double longitude;
        float accuracy;
        Projection projection = mapView.getProjection();

        if(currentLocation == null) {
            latitude = defaultLatitude;
            longitude = defaultLongitude;
            accuracy = defaultAccuracy;
        } else {
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();
            accuracy = currentLocation.getAccuracy();
        }            

        float[] result = new float[1];

        Location.distanceBetween(latitude, longitude, latitude, longitude + 1, result);
        float longitudeLineDistance = result[0];

        GeoPoint leftGeo = new GeoPoint((int)(latitude * 1E6), (int)((longitude - accuracy / longitudeLineDistance) * 1E6));
        projection.toPixels(leftGeo, left);
        GeoPoint currLoc = new GeoPoint((int)(currentLocation.getLatitude() * 1E6), (int) (currentLocation.getLongitude() * 1E6 ));
        projection.toPixels(currLoc, center);
        int radius = center.x - left.x;

        accuracyPaint.setColor(0xff6666ff);
        accuracyPaint.setStyle(Style.STROKE);
        canvas.drawCircle(center.x, center.y, radius, accuracyPaint);

        accuracyPaint.setColor(0x186666ff);
        accuracyPaint.setStyle(Style.FILL);
        canvas.drawCircle(center.x, center.y, radius, accuracyPaint);

        drawable.setBounds(center.x - width / 2, center.y - height / 2, center.x + width / 2, center.y + height / 2);
        drawable.draw(canvas);

        return true;
    }
}