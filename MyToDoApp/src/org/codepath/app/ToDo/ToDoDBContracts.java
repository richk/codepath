package org.codepath.app.ToDo;

import android.provider.BaseColumns;

public class ToDoDBContracts {
	public static final String ITEM_STRING_COLUMN_NAME = "item_string";
	public static final String ITEM_DONE_COLUMN_NAME = "item_done";
	public static final String ITEM_DATE_COLUMN_NAME = "item_date";
	
	public static final String ITEM_TABLE_NAME = "item";
	
	private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String AUTO_INCREMENT = "AUTOINCREMENT";
	
	public static final String TODO_CREATE_ITEM_TABLE = "CREATE TABLE " + ITEM_TABLE_NAME + "(" +
			BaseColumns._ID + INTEGER_TYPE +" PRIMARY KEY " + AUTO_INCREMENT + COMMA_SEP +
			ITEM_STRING_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
			ITEM_DATE_COLUMN_NAME + TEXT_TYPE + COMMA_SEP + 
			ITEM_DONE_COLUMN_NAME + INTEGER_TYPE + ")";
	
	public static final String TODO_DELETE_ITEM_TABLE = "DROP TABLE IF EXISTS " + ITEM_TABLE_NAME;
}
