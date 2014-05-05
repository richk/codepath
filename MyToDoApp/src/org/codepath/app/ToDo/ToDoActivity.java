package org.codepath.app.ToDo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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

public class ToDoActivity extends Activity {
	private static final String LOG_TAG = "ToDoActivity";
	private static final String TODO_FILE_NAME = "todo.txt";
	private static final int EDIT_ACTIVITY_REQUEST_CODE = 20;
	
	public static final int EDIT_ACTIVITY_RESULT_OK = 21;
	public static final String ITEM_STRING_KEY = "ItemString";
	public static final String ITEM_POS_KEY = "ItemPos";
	
	private List<String> mItems;
	private ArrayAdapter<String> mItemsAdapter;
	private ListView mLv;
	private EditText mEditText;
	private Button mAddButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		mLv = (ListView)findViewById(R.id.listView1);
		readItems();
		mItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mItems);
		mLv.setAdapter(mItemsAdapter);
		mEditText = (EditText)findViewById(R.id.editText1);
		mAddButton = (Button)findViewById(R.id.button1);
		mAddButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addToDoItem(v);
			}
		});
		mLv.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> aView, View item,
					int pos, long id) {
				mItems.remove(pos);
				mItemsAdapter.notifyDataSetInvalidated();
				Log.d(LOG_TAG, "removeItems()::Number of items in ItemsAdapter:" + mItemsAdapter.getCount());
				Log.d(LOG_TAG, "removeItems()::Items in list:" + mItems);
				saveItems();
				return true;
			}
			
		});
		mLv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> aView, View item, int pos,
					long id) {
				Log.d(LOG_TAG, "onItemClick()::ItemPos:" + pos);
				Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
				String itemString = (String) aView.getItemAtPosition(pos);
				Log.d(LOG_TAG, "onItemClick()::ItemString:" + itemString);
				intent.putExtra(ITEM_STRING_KEY, itemString);
				intent.putExtra(ITEM_POS_KEY, pos);
				startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE);
			}
		});
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
	    	    mItems.set(itemPos, itemValue);
	    	    mItemsAdapter.notifyDataSetChanged();
	    	}
	    	Log.d(LOG_TAG, "onClick()::ItemString:" + itemValue);
		    Log.d(LOG_TAG, "onClick()::ItemPos:" + itemPos);
	    }
	    Log.d(LOG_TAG, "onActivityResult()::Number of items in ItemsAdapter:" + mItemsAdapter.getCount());
		Log.d(LOG_TAG, "onActivityResult()::Items in list:" + mItems);
		saveItems();
	}
	
	public void addToDoItem(View v) {
		Editable newItem = mEditText.getText();
		String newItemString = newItem.toString();
		if (newItemString != null && !newItemString.isEmpty()) {
		    mItems.add(newItemString);
		    mItemsAdapter.notifyDataSetChanged();
		}
		mEditText.setText("");
		Log.d(LOG_TAG, "addToDoItems()::Number of items in ItemsAdapter:" + mItemsAdapter.getCount());
		Log.d(LOG_TAG, "addToDoItems()::Items in list:" + mItems);
		saveItems();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do, menu);
		return true;
	}
	
	private void readItems() {
		File appFilesDir = getFilesDir();
		File todoFile = new File(appFilesDir, TODO_FILE_NAME);
		mItems = new ArrayList<String>();
		try {
			mItems = FileUtils.readLines(todoFile);
		} catch (IOException e) {
			Log.e(LOG_TAG, "IOExcepiton", e);
		}
		Log.d(LOG_TAG, "readItems()::Items in list:" + mItems);
	}
	
	private void saveItems() {
		File appFilesDir = getFilesDir();
		File todoFile = new File(appFilesDir, TODO_FILE_NAME);
        try {
			FileUtils.writeLines(todoFile, mItems);
		} catch (IOException e) {
			Log.e(LOG_TAG, "IOException", e);
		}	
	}

}
