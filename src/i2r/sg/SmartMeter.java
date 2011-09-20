package i2r.sg;


import i2r.sg.db.DBUserAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SmartMeter extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
				
		final EditText txtUserName = (EditText)findViewById(R.id.txtUsername);
		final EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
		Button btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String username = txtUserName.getText().toString();
				String password = txtPassword.getText().toString();
				try{
					if(username.length() > 0 && password.length() >0)
					{
						DBUserAdapter dbUser = new DBUserAdapter(SmartMeter.this);
						dbUser.open();
						
						if(dbUser.Login(username, password))
						{
							Toast.makeText(SmartMeter.this,"Successfully Logged In", Toast.LENGTH_LONG).show();
							Intent intent = new Intent(SmartMeter.this, MeterSelection.class);
							//intent.setClassName("i2r.sg", "i2r.sg.MeterSelection");
							Bundle user = new Bundle();
							user.putString("username", username);
							user.putString("password", password);
							intent.putExtras(user);
							
							startActivity(intent);
						}else{
							Toast.makeText(SmartMeter.this,"Invalid Username/Password", Toast.LENGTH_LONG).show();
						}
						dbUser.close();
					}
					
				}catch(Exception e)
				{
					Toast.makeText(SmartMeter.this,e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
			
		});
    }
    
}