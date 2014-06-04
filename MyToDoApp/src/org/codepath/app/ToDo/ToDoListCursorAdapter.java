package org.codepath.app.ToDo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ToDoListCursorAdapter extends CursorAdapter {
	private static final String LOG_TAG = ToDoListCursorAdapter.class.getSimpleName();
	
	private LayoutInflater mInflater;
	private ToDoDbHelper mHelper;
	
	public ToDoListCursorAdapter(Context context, Cursor cursor, ToDoDbHelper helper) {
		super(context, cursor, false);
		mHelper = helper;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView tv = (TextView) view.findViewById(R.id.label);
		tv.setText(cursor.getString(1));
		CheckBox cb = (CheckBox) view.findViewById(R.id.check);
		if (cursor.getInt(2) == 1) {
		    cb.setChecked(true);	
		} else {
			cb.setChecked(false);
		}
	}
	
	@Override
	public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
		final View view = mInflater.inflate(R.layout.todo_list_entry, parent, false); 
		CheckBox cb = (CheckBox) view.findViewById(R.id.check);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
			    Log.d(LOG_TAG, "onCheckedChanged::Checked :" + isChecked);
			    int id = cursor.getInt(0);
			    int checked = isChecked?1:0;
			    ContentValues cv = new ContentValues();
			    cv.put(ToDoDBContracts.ITEM_DONE_COLUMN_NAME, checked);
			    mHelper.update(id, cv);
			}
		});
        return view;
	}

}
