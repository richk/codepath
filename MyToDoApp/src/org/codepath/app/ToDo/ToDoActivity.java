package org.codepath.app.ToDo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.provider.BaseColumns;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ToDoActivity extends Activity {
	private static final String LOG_TAG = "ToDoActivity";
	private static final int EDIT_ACTIVITY_REQUEST_CODE = 20;
	private static final String ITEM_TABLE_PROJECTION = BaseColumns._ID + "," + ToDoDBContracts.ITEM_STRING_COLUMN_NAME;
	
	public static final int EDIT_ACTIVITY_RESULT_OK = 21;
	public static final String ITEM_STRING_KEY = "ItemString";
	public static final String ITEM_POS_KEY = "ItemPos";
	
	private SimpleCursorAdapter mItemsAdapter;
	private ListView mLv;
	private EditText mEditText;
	private Button mAddButton;
	private ToDoDbHelper todoDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		mEditText = (EditText)findViewById(R.id.editText1);
		mEditText.setText(null);
		mAddButton = (Button)findViewById(R.id.button1);
		mAddButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addToDoItem(v);
			}
		});
		mLv = (ListView)findViewById(R.id.listView1);
		todoDbHelper = new ToDoDbHelper(this);
		final Cursor cursor = todoDbHelper.rawQuery("Select "+ ITEM_TABLE_PROJECTION + " from " + ToDoDBContracts.ITEM_TABLE_NAME, null);
		final String[] fromColumns = new String[] {ToDoDBContracts.ITEM_STRING_COLUMN_NAME};
		final int[] toControlIDs = new int[] {android.R.id.text1};
		mItemsAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, fromColumns, toControlIDs, 0);
		mLv.setAdapter(mItemsAdapter);
		Log.d(LOG_TAG, "onCreate()::Number of items in ItemsAdapter:" + mItemsAdapter.getCount());
		mLv.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> aView, View item,
					int pos, long id) {
				Log.d(LOG_TAG, "onItemLongClick()::pos:" + pos + ", id:" + id);
				Cursor cursor = (Cursor)mItemsAdapter.getItem(pos);
				Log.d(LOG_TAG, "onItemLongClick()::cursor position:" + cursor.getPosition());
				Log.d(LOG_TAG, "onItemLongClick()::cursor column:" + cursor.getColumnNames()[0]);
				int itemToRemove = cursor.getInt(0);
				todoDbHelper.delete(itemToRemove);
				cursor.requery();
				mItemsAdapter.notifyDataSetChanged();
				Log.d(LOG_TAG, "removeItems()::Number of items in ItemsAdapter:" + mItemsAdapter.getCount());
				return true;
			}
			
		});
		mLv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> aView, View item, int pos,
					long id) {
				Log.d(LOG_TAG, "onItemClick()::ItemPos:" + pos);
				Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
				Cursor itemCursor = (Cursor) aView.getItemAtPosition(pos);
				Log.d(LOG_TAG, "onItemClick()::ItemString:" + itemCursor.getString(1));
				intent.putExtra(ITEM_STRING_KEY, itemCursor.getString(1));
				intent.putExtra(ITEM_POS_KEY, pos);
				startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
			}
		});
		if (cursor.moveToFirst()) {
			Log.d(LOG_TAG, "onCreate()::id 1=" + cursor.getInt(0));
			if (cursor.moveToNext()) {
				Log.d(LOG_TAG, "onCreate()::id 2=" + cursor.getInt(0));
			}
			if (cursor.moveToNext()) {
				Log.d(LOG_TAG, "onCreate()::id 3=" + cursor.getInt(0));
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(LOG_TAG, "Received result. Request Code:" + requestCode + " . Result Code:" + resultCode);
	    if (resultCode == EDIT_ACTIVITY_RESULT_OK && requestCode == EDIT_ACTIVITY_REQUEST_CODE) {
	    	if (data == null) {
	    		Log.e(LOG_TAG, "No result received");
	    		return;
	    	}
	    	String itemValue = data.getStringExtra("ItemString");
	    	int itemPos = data.getIntExtra("ItemPos", -1);
	    	if (itemPos != -1) {
	    	    Cursor cursor = (Cursor)mItemsAdapter.getItem(itemPos);
	    	    int itemToUpdate = cursor.getInt(0);
				ContentValues cv = new ContentValues();
				cv.put(ToDoDBContracts.ITEM_STRING_COLUMN_NAME, itemValue);
				todoDbHelper.update(itemToUpdate, cv);
				cursor.requery();
	    	    mItemsAdapter.notifyDataSetChanged();
	    	}
	    	Log.d(LOG_TAG, "onClick()::ItemString:" + itemValue);
		    Log.d(LOG_TAG, "onClick()::ItemPos:" + itemPos);
	    }
	    Log.d(LOG_TAG, "onActivityResult()::Number of items in ItemsAdapter:" + mItemsAdapter.getCount());
	}
	
	public void addToDoItem(View v) {
		Editable newItem = mEditText.getText();
		String newItemString = newItem.toString();
		if (newItemString != null && !newItemString.isEmpty()) {
		    ContentValues cv = new ContentValues();
		    cv.put(ToDoDBContracts.ITEM_STRING_COLUMN_NAME, newItemString);
		    cv.put(ToDoDBContracts.ITEM_DATE_COLUMN_NAME, String.valueOf(System.currentTimeMillis()));
		    cv.put(ToDoDBContracts.ITEM_DONE_COLUMN_NAME, 0);
		    todoDbHelper.insert(cv);
		    Cursor cursor = mItemsAdapter.getCursor();
		    cursor.requery();
		    mItemsAdapter.notifyDataSetChanged();
		}
		if (mEditText.length() > 0) {
			mEditText.getText().clear();
		}
		Log.d(LOG_TAG, "addToDoItems()::Number of items in ItemsAdapter:" + mItemsAdapter.getCount());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do, menu);
		return true;
	}
}
