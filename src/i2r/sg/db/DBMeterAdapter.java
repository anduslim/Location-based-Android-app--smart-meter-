
package i2r.sg.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBMeterAdapter 
{ 
    public static final String KEY_METERID= "_meterid";
    public static final String KEY_TYPE = "type";  
    public static final String KEY_READING = "reading";
    public static final String KEY_REMARKS = "remarks";
    public static final String KEY_CUSTOMERID = "customer_id"; 
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "meters.db";
    private static final String DATABASE_TABLE = "meters";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table " + DATABASE_TABLE + " ( " + KEY_METERID + " integer primary key autoincrement, "
        + KEY_TYPE + " integer not null, "
        + KEY_READING + " integer, " 
        + KEY_CUSTOMERID + " integer, "
        + KEY_REMARKS + " integer, "
        + "FOREIGN KEY(" + KEY_CUSTOMERID  + ") REFERENCES " +  DBCustomerAdapter.DATABASE_TABLE + "(" +DBCustomerAdapter.KEY_CUSTOMERID + "));";
        
    private Context context = null;  
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBMeterAdapter(Context ctx) 
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
          	ContentValues initialValues = createContentValues(0, 1, "gas meter", 1);  
          	db.insert(DATABASE_TABLE, null, initialValues);
          	initialValues = createContentValues(1, 2, "water meter", 1); 
          	db.insert(DATABASE_TABLE, null, initialValues);
          	initialValues = createContentValues(2, 3, "electric meter", 1); 
          	db.insert(DATABASE_TABLE, null, initialValues);
          	
          	ContentValues initialValues1 = createContentValues(0, 11, "gas meter", 2);  
          	db.insert(DATABASE_TABLE, null, initialValues1);
          	initialValues = createContentValues(1, 12, "water meter", 2); 
          	db.insert(DATABASE_TABLE, null, initialValues1);
          	initialValues = createContentValues(2, 13, "electric meter", 2); 
          	db.insert(DATABASE_TABLE, null, initialValues1);
          	
          	ContentValues initialValues2 = createContentValues(0, 21, "gas meter", 3);  
          	db.insert(DATABASE_TABLE, null, initialValues2);
          	initialValues = createContentValues(1, 22, "water meter", 3); 
          	db.insert(DATABASE_TABLE, null, initialValues2);
          	initialValues = createContentValues(2, 23, "electric meter", 3); 
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
    
    public long addMeter(int type, int reading, String remarks, int customerid)
    {
    	 ContentValues initialValues = createContentValues(type, reading, remarks, customerid);    
         return db.insert(DATABASE_TABLE, null, initialValues);

    }
    
    public int UpdateMeter(int id, int type, int reading, String remarks, int customerid)
    {
		 ContentValues initialValues = createContentValues(type, reading, remarks, customerid);    
		 return db.update(DATABASE_TABLE, initialValues, KEY_METERID+"=?", new String []{String.valueOf(id)});   
    }
    
	/**
	 * Deletes meter
	 */
	public boolean deleteMeter(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_METERID + "=" + rowId, null) > 0;
	}
	
	public Cursor fetchMeterByCustID(long ID) throws SQLException {
//		Cursor mCursor = db.query(DATABASE_TABLE, new String[] {
//				KEY_METERID, KEY_TYPE, KEY_READING, KEY_REMARKS, KEY_CUSTOMERID  },
//				KEY_CUSTOMERID + "=" + ID, null, null, null, null, null);
		Cursor mCursor = db.rawQuery("SELECT DISTINCT _meterid as _id, type, reading, remarks, customer_id FROM " + DATABASE_TABLE + " WHERE " + KEY_CUSTOMERID + "=?", new String[]{String.valueOf(ID)});
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * Return a Cursor over the list of all meters in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllMeters() {
		return db.query(DATABASE_TABLE, new String[] { KEY_METERID,
				KEY_TYPE, KEY_READING, KEY_REMARKS, KEY_CUSTOMERID }, null, null, null,
				null, null);
	}
	
	private static ContentValues createContentValues(int type, int reading, String remarks, int customerid) {
		ContentValues values = new ContentValues();
		values.put(KEY_TYPE, type);
		values.put(KEY_READING, reading);
		values.put(KEY_REMARKS, remarks);
		values.put(KEY_CUSTOMERID, customerid);
		return values;
	}

}
