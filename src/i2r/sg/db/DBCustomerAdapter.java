
package i2r.sg.db;

import com.google.android.maps.GeoPoint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBCustomerAdapter 
{ 
    public static final String KEY_CUSTOMERID = "_customerid";
    public static final String KEY_CUSTOMERNAME = "name";
    public static final String KEY_LATITUDE = "latitude";   
    public static final String KEY_LONGITUDE = "longitude"; 
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "customers.db";
    public static final String DATABASE_TABLE = "customers";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table " + DATABASE_TABLE + " (" + KEY_CUSTOMERID + " integer primary key autoincrement, "
        + KEY_CUSTOMERNAME + " text not null, "
        + KEY_LATITUDE + " real not null, "
        + KEY_LONGITUDE + " real not null);";
        
    private Context context = null;  
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBCustomerAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
        
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE);
            //populate points
       	 	ContentValues initialValues = createContentValues("i2r1", 1.300925261, 103.78725708);  
            db.insert(DATABASE_TABLE, null, initialValues);
            
            ContentValues initialValues1 = createContentValues("i2r2", 1.301225261, 103.78785708);
            db.insert(DATABASE_TABLE, null, initialValues1);
            
            ContentValues initialValues2 = createContentValues("i2r3", 1.301292526, 103.78765708);
            db.insert(DATABASE_TABLE, null, initialValues2);
        	
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
        }
    }    
    
    
    public void open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
    }

      
    public void close() 
    {
        DBHelper.close();
    }    
    
    public long AddCustomer(String name, double latitude, double longitude)
    {
    	 ContentValues initialValues = createContentValues(name, latitude, longitude); 
         return db.insert(DATABASE_TABLE, null, initialValues);

    }
    
    public Cursor getCustomerWithinBox(double minLat, double maxLat, double minLong, double maxLong) {
    	Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_LATITUDE + ">? AND " + KEY_LATITUDE + "<? AND " + KEY_LONGITUDE + ">? AND " + KEY_LONGITUDE + "<?", new String[]{String.valueOf(minLat), String.valueOf(maxLat), String.valueOf(minLong), String.valueOf(maxLong)});
    	return mCursor;
    }
    
	private static ContentValues createContentValues(String name, double latitude, double longitude) {
		ContentValues values = new ContentValues();
		values.put(KEY_CUSTOMERNAME, name);
		values.put(KEY_LATITUDE, latitude);
		values.put(KEY_LONGITUDE, longitude);
		return values;
	}

	/**
	 * Return a Cursor over the list of all customers in the database
	 * 
	 * @return Cursor over all customers
	 */
	public Cursor fetchAllCustomers() {
		return db.query(DATABASE_TABLE, new String[] { KEY_CUSTOMERID,
				KEY_CUSTOMERNAME, KEY_LATITUDE, KEY_LONGITUDE }, null, null, null,
				null, null);
	}

}
