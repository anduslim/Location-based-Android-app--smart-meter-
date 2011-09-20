package i2r.sg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MeterSelection extends Activity {
	
	final static int READ_METER_CODE = 0;
	final static int MANAGE_LOAD_CODE = 1;
	final static int REPORT_CODE = 2;
	
	private Button readMeterBtn, manageLoadsBtn, reportBtn;
	private String userId, userpw;
	private Bundle userInfo;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        
	        userInfo = this.getIntent().getExtras();
	        setUserId(userInfo.getString("username"));
	        setUserpw(userInfo.getString("password"));
	        
	        readMeterBtn = (Button)findViewById(R.id.readMeterButton);
	        manageLoadsBtn = (Button)findViewById(R.id.manageLoadButton);
	        reportBtn = (Button)findViewById(R.id.reportButton);
	        
	        readMeterBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClassName("i2r.sg", "i2r.sg.ReadMeter");
					startActivityForResult(intent, READ_METER_CODE);
				}
			});
	        
	        manageLoadsBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClassName("i2r.sg", "i2r.sg.ManageLoads");
					startActivityForResult(intent, MANAGE_LOAD_CODE);
				}
			});
	        
	        reportBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClassName("i2r.sg", "i2r.sg.Report");
					startActivityForResult(intent, REPORT_CODE);
				}
			});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(0, 0, 0, "Admin");
    	menu.add(0, 1, 1, "Settings");

      return true;
    }
    
    // Called when menu item is selected //
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
      
      switch(item.getItemId()){
      
      case 1:
        // Launch Prefs activity
        Intent i = new Intent(this, Prefs.class);
        i.putExtras(userInfo);
        startActivity(i);
        break;
        
      }    
      return true;
    }

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userpw the userpw to set
	 */
	public void setUserpw(String userpw) {
		this.userpw = userpw;
	}

	/**
	 * @return the userpw
	 */
	public String getUserpw() {
		return userpw;
	}
}
