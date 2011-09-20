package i2r.sg;

import i2r.sg.db.DBMeterAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class EditMeter extends Activity {
	
	private EditText readET, remarksET;
	private Button confirmBtn;
	private Intent inputIntent;
	private Bundle inputData;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editmeter);
		
		readET = (EditText)findViewById(R.id.et_edit_reading);
		remarksET = (EditText)findViewById(R.id.et_edit_remarks);
		confirmBtn = (Button)findViewById(R.id.bt_edit_button);
		
		inputIntent = this.getIntent();
		inputData = inputIntent.getExtras();
		readET.setText(inputData.getString("reading"));
		remarksET.setText(inputData.getString("remarks"));
		
		confirmBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				DBMeterAdapter dbMeter = new DBMeterAdapter(EditMeter.this);
				dbMeter.open();
				Log.v("EditMeter", "id:" + inputData.getInt("id") + " type:" + inputData.getInt("type") + " reading:" + readET.getText().toString() + " remarks:" + remarksET.getText().toString() + " custId:" + inputData.getInt("custId"));
				dbMeter.UpdateMeter(inputData.getInt("id"), inputData.getInt("type"), Integer.parseInt(readET.getText().toString()), remarksET.getText().toString(), inputData.getInt("custId"));
				dbMeter.close();
				setResult(RESULT_OK, null);
				finish();

			}
			
		});
		
	}
}
