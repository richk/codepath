package org.codepath.app.ToDo;

import android.content.Context;
import android.database.Cursor;
import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;

public class ToDoListCursorAdapter extends SimpleCursorAdapter implements Filterable {
	public ToDoListCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to, 0);
		// TODO Auto-generated constructor stub
	}
	private Context mContext;
	private int layout;

}
