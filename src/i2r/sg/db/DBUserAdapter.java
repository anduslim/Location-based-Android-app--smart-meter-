
package i2r.sg.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBUserAdapter 
{ 
    public static final String KEY_ROWID = "_id";
    public static final String KEY_USERNAME= "username";
    public static final String KEY_PASSWORD = "password";   
    public static final String KEY_ISADMIN = "isAdmin"; 
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "users.db";
    private static final String DATABASE_TABLE = "users";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table users (_id integer primary key autoincrement, "
        + "username text not null, "
        + "password text not null, "
        + "isAdmin boolean not null);";
        
    private Context context = null;  
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBUserAdapter(Context ctx) 
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
            //populate admin user
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_USERNAME, "admin");
            initialValues.put(KEY_PASSWORD, "admin");  
            initialValues.put(KEY_ISADMIN, true);  
            db.insert(DATABASE_TABLE, null, initialValues);
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
    
    public long AddUser(String username, String password, boolean isadmin)
    {
    	 ContentValues initialValues = new ContentValues();
         initialValues.put(KEY_USERNAME, username);
         initialValues.put(KEY_PASSWORD, password); 
         initialValues.put(KEY_ISADMIN, isadmin);      
         return db.insert(DATABASE_TABLE, null, initialValues);

    }

    public boolean Login(String username, String password) throws SQLException 
    {
    	Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE username=? AND password=?", new String[]{username,password});
        if (mCursor != null) {           
            if(mCursor.getCount() > 0)
            {
            	return true;
            }
        }
     return false;
    }

}
