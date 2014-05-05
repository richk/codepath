package org.codepath.app.ToDo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends Activity{
	private static final String LOG_TAG = "EditItemActivity";
	
	private EditText mEditText;
	private Button mSaveButton;
	private int mItemPos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		mEditText = (EditText)findViewById(R.id.editText2);
		mSaveButton = (Button) findViewById(R.id.button2);
		mSaveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                Intent resultIntent = new Intent();
                String itemString = mEditText.getText().toString();
                resultIntent.putExtra(ToDoActivity.ITEM_STRING_KEY, itemString);
                resultIntent.putExtra(ToDoActivity.ITEM_POS_KEY, mItemPos);
                setResult(ToDoActivity.EDIT_ACTIVITY_RESULT_OK, resultIntent);
                finish();
			}
		});
		
		String itemString = getIntent().getStringExtra(ToDoActivity.ITEM_STRING_KEY);
		mEditText.setText(itemString);
		mItemPos = getIntent().getIntExtra(ToDoActivity.ITEM_POS_KEY, 0);
	}
}
