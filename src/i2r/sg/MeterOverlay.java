package i2r.sg;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class MeterOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private Context context;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	
	public MeterOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		this.context = mapView.getContext();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
//    @Override
//    protected boolean onTap(int index) {
//      OverlayItem item = mOverlays.get(index);
//      AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//      dialog.setTitle(item.getTitle());
//      dialog.setMessage(item.getSnippet());
//      dialog.show();
// 
//      return true;
//    }
	
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		Toast.makeText(context, "onBalloonTap for overlay index " + index,
				Toast.LENGTH_LONG).show();
		Intent intent = new Intent(context, MeterList.class);
		
		StringTokenizer tkn = new StringTokenizer(item.getSnippet()," ");
		String token = tkn.nextToken();
		Log.v("StringTokenizer", " tkn:" + token);
		tkn = new StringTokenizer(token,":");
		token = tkn.nextToken();
		Log.v("StringTokenizer", " tkn:" + token);
		intent.putExtra("custID", Integer.parseInt(tkn.nextToken()));
		
		context.startActivity(intent);
		return true;
	}

}
