package i2r.sg;

import i2r.sg.db.DBCustomerAdapter;
import i2r.sg.db.DBMeterAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;


public class MeterList extends ListActivity {
	
	public static final int GAS_METER_TYPE = 0;
	public static final int WATER_METER_TYPE = 1;
	public static final int ELECTRICITY_METER_TYPE = 2;
	
	private Bundle custInfo;
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		


	      
	      //Cursor cur = managedQuery(android.provider.Browser.BOOKMARKS_URI, 
	    	//	  projection, null, null, null);
		custInfo = this.getIntent().getExtras();
        
		Log.v("MeterList", "custID:" + custInfo.getInt("custID"));
		updateActivityList();

		registerForContextMenu(getListView());
	}
	
	void updateActivityList() {
		String[] displayFields = new String[] {DBMeterAdapter.KEY_TYPE,
				  DBMeterAdapter.KEY_READING, 
				  DBMeterAdapter.KEY_REMARKS};
		int[] displayViews = new int[] { R.id.tv_meterType,
		          R.id.tv_meterReading,
		          R.id.tv_remarks};
		DBMeterAdapter dbMeter = new DBMeterAdapter(MeterList.this);
		dbMeter.open();
		Cursor returnQuery = dbMeter.fetchMeterByCustID(custInfo.getInt("custID"));
		dbMeter.close();
			
		setListAdapter(new MeterArrayAdapter(this, 
		        R.layout.meterlist_row, returnQuery, 
		        displayFields, displayViews
		));
	}
	
	// ListView and view (row) on which was clicked, position and
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Cursor text = (Cursor) l.getItemAtPosition(position);
		int meterid = text.getInt(text.getColumnIndex(text.getColumnName(0)));
		int metertype = text.getInt(text.getColumnIndex(DBMeterAdapter.KEY_TYPE));
		String reading = text.getString(text.getColumnIndex(DBMeterAdapter.KEY_READING));
		String remarks = text.getString(text.getColumnIndex(DBMeterAdapter.KEY_REMARKS));
		int custId = text.getInt(text.getColumnIndex(DBMeterAdapter.KEY_CUSTOMERID));
		Log.v("MeterList", "slection:" + meterid + " " + reading + " " +remarks);
		
		Intent i = new Intent(this, EditMeter.class);
		Bundle data = new Bundle();
		data.putInt("id", meterid);
		data.putInt("type", metertype);
		data.putString("reading", reading);
		data.putString("remarks", remarks);
		data.putInt("custId", custId);
		i.putExtras(data);
		Toast.makeText(this, meterid + " " + reading + " " + remarks  + " " + custId , Toast.LENGTH_LONG).show();
		// Activity returns an result if called with startActivityForResult
		
		startActivityForResult(i, 1);
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 1, 0, "edit");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateActivityList();
	}
}
