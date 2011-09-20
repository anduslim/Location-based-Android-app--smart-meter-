package i2r.sg;

import i2r.sg.db.DBMeterAdapter;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MeterArrayAdapter extends SimpleCursorAdapter {

        private Cursor c;
        private Context context;

	public MeterArrayAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.c = c;
		this.context = context;
	}

	public View getView(int pos, View inView, ViewGroup parent) {
	       View v = inView;
	       if (v == null) {
	            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = inflater.inflate(R.layout.meterlist_row, null);
	       }
	       ImageView meterImage = (ImageView) v.findViewById(R.id.meterImage);
           TextView meterType = (TextView) v.findViewById(R.id.tv_meterType);
           TextView meterReading = (TextView) v.findViewById(R.id.tv_meterReading);
           TextView meterRemarks = (TextView) v.findViewById(R.id.tv_remarks);
	       this.c.moveToPosition(pos);	
	       
	       Log.v ("MeterArray" , "v: "+ v + " meterType:" + meterType + " meterReading:"+ meterReading);
	       int type = this.c.getInt(this.c.getColumnIndex(DBMeterAdapter.KEY_TYPE));
		   switch (type) {
				case MeterList.GAS_METER_TYPE:
					meterType.setText("GAS METER");
					meterImage.setImageResource(R.drawable.metericon);
				break;
				
				case MeterList.WATER_METER_TYPE:
					meterType.setText("WATER METER");
					meterImage.setImageResource(R.drawable.metericon);
				break;
				
				case MeterList.ELECTRICITY_METER_TYPE:
					meterType.setText("ELECTRICITY METER");
					meterImage.setImageResource(R.drawable.metericon);
				break;								
		   }
	       
		   int readings = this.c.getInt(this.c.getColumnIndex(DBMeterAdapter.KEY_READING));
		   meterReading.setText(String.valueOf(readings));
		   String remarks = this.c.getString(this.c.getColumnIndex(DBMeterAdapter.KEY_REMARKS));
		   meterRemarks.setText(remarks);
		   

	       return(v);
	}

}
