package com.chat.app.firebasechat;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class SelectUser extends ActionBarActivity implements OnClickListener {

	Spinner spinner;
	int[] id;
	List<String> list;
	ListView list1;
	ArrayAdapter<String> dataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listuser);
		id = new int[3];
		list1 = (ListView) findViewById(R.id.list1);
		list = new ArrayList<String>();
		dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		id[0] = 1;
		id[1] = 2;
		id[2] = 3;

		list.add("mantra");
		list.add("marrily");
		list.add("touchkin");
		dataAdapter.notifyDataSetChanged();

		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		list1.setAdapter(dataAdapter);
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String yob = parent.getAdapter().getItem(position).toString();
				Intent intent = new Intent(SelectUser.this, MainActivity.class);
				intent.putExtra("id", position);
				intent.putExtra("group", yob);
				startActivity(intent);
				Log.d("yob", yob+" "+position);
			}
		});

//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//
//				String yob = parent.getSelectedItem().toString();
//				Intent intent = new Intent(SelectUser.this, MainActivity.class);
//				intent.putExtra("id", position);
//				intent.putExtra("group", yob);
//				startActivity(intent);
//				Log.d("yob", yob+" "+position);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//
//			}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
