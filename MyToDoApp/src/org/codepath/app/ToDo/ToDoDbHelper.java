package org.codepath.app.ToDo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ToDoDbHelper extends SQLiteOpenHelper{
	private static final String LOG_TAG = ToDoDbHelper.class.getSimpleName();
	private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;
    
    private SQLiteDatabase mDb;
    
	public ToDoDbHelper(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(LOG_TAG, "onCreate()");
		db.execSQL(ToDoDBContracts.TODO_CREATE_ITEM_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(ToDoDBContracts.TODO_DELETE_ITEM_TABLE);
		onCreate(db);
	}
	
	private void initDb() {
		if (mDb == null) {
			mDb = getWritableDatabase();
		}
	}
	
	public long insert(ContentValues contentValues) {
		Log.d(LOG_TAG, "insert()");
	    initDb(); 
	    long id = mDb.insert(ToDoDBContracts.ITEM_TABLE_NAME, null, contentValues);
	    return id;
	}
	
	public void delete(int id) {
		Log.d(LOG_TAG, "delete()::id="+id);
		initDb();
		String whereClause = "_id=?";
		String whereArgs[] = new String[]{String.valueOf(id)};
		mDb.delete(ToDoDBContracts.ITEM_TABLE_NAME, whereClause, whereArgs);
	}
	
	public void update(int id, ContentValues contentValues) {
		Log.d(LOG_TAG, "update()::id="+id);
	    initDb();
	    String whereClause = "_id=?";
		String whereArgs[] = new String[]{String.valueOf(id)};
	    mDb.update(ToDoDBContracts.ITEM_TABLE_NAME, contentValues, whereClause, whereArgs);
	}
	
	public Cursor rawQuery(String sql, String[] whereArgs) {
		initDb();	
		return mDb.rawQuery(sql, whereArgs);
	}
	
	public void closeDatabase() {
		mDb.close();
		mDb = null;
	}
}
